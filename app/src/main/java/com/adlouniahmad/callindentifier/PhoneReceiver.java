package com.adlouniahmad.callindentifier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class PhoneReceiver extends BroadcastReceiver {

    private static final String TAG = PhoneReceiver.class.getSimpleName();
    public static final String NOTIFY_PHONE_RINGING = "NOTIFY_PHONE_RINGING";
    public static final String EXTRA_PHONE_NUMBER = "EXTRA_PHONE_NUMBER";

    Intent localIntent = new Intent(NOTIFY_PHONE_RINGING);
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        receiveNumber();
        LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);
    }

    public void receiveNumber() {
        TelephonyManager telephony = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        telephony.listen(new PhoneStateListener() {

            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);
                if (state == TelephonyManager.CALL_STATE_RINGING) {
                    localIntent.putExtra(EXTRA_PHONE_NUMBER, incomingNumber);
                }
            }
        }, PhoneStateListener.LISTEN_CALL_STATE);
    }
}

