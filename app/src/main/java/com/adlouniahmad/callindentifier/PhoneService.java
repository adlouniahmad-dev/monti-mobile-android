package com.adlouniahmad.callindentifier;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class PhoneService extends Service {

    private static final String TAG = PhoneService.class.getSimpleName();
    private PhoneReceiver phoneReceiver = new PhoneReceiver();

    public PhoneService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter("android.intent.action.PHONE_STATE");
        registerReceiver(phoneReceiver, filter);
        IntentFilter customFilter = new IntentFilter(PhoneReceiver.NOTIFY_PHONE_RINGING);
        LocalBroadcastManager.getInstance(this).registerReceiver(mLocalReceiver, customFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(phoneReceiver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void getPersonDetailsFromPhoneNumber(String phoneNumber) {
        String url = "http://192.168.1.70:8080/api/v1/persons/search/m?mobile=" + phoneNumber;
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    String notes = response.getString("notes");
                    Toast.makeText(PhoneService.this, notes, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, response.toString());
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PhoneService.this, "Person Not found", Toast.LENGTH_LONG).show();
                VolleyLog.d(TAG, error.getMessage());
            }
        });
        queue.add(jsonObjectRequest);
    }

    private BroadcastReceiver mLocalReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String number = intent.getStringExtra(PhoneReceiver.EXTRA_PHONE_NUMBER);
            Log.d(TAG, "Number : " + number);
            if (number != null && !number.equals("")) {
                getPersonDetailsFromPhoneNumber(number);
            }
        }
    };


}
