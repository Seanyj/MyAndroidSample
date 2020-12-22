package com.seanyj.mysamples.util.phone;

import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;

public class PhoneUtil {
    public static void sendMsg(Context context, String number, String message) {
        String SENT = "sms_sent";
        String DELIVERED = "sms_delivered";

        PendingIntent sentPI = PendingIntent.getActivity(context, 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getActivity(context, 0, new Intent(DELIVERED), 0);

        SmsManager smsm = SmsManager.getDefault();
        smsm.sendTextMessage(number, null, message, sentPI, deliveredPI);
        insertSms(context, number, message);
    }

    public static void insertSms(Context context, String address, String body){
        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.parse("content://sms/");
        ContentValues values = new ContentValues();
        values.put("address", address);
        values.put("type", "2");
        values.put("body", body);
        values.put("date",System.currentTimeMillis());
        resolver.insert(uri, values);
    }
}
