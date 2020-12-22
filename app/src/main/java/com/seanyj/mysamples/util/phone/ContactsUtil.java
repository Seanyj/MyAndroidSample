package com.seanyj.mysamples.util.phone;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog.Calls;
import android.provider.Contacts;
import android.provider.ContactsContract;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ContactsUtil {

    public interface OnResult {
        void onGetContacts(List<ContactEntity> list);

        void onGetCalls(List<CallEntity> list);

        void onGetSMSes(List<SmsEntity> list);
    }

    public static List<CallEntity> getCallListSync(Context context) {
        List<CallEntity> list = getCalls(context);
        return list;
    }

    public static List<ContactEntity> getContactListSync(Context context, boolean includeSim) {
        List<ContactEntity> list = new ArrayList<>();
        List<ContactEntity> contactEntityList = getContacts(context);

        if (contactEntityList != null) {
            list.addAll(contactEntityList);
        }

        if (includeSim) {
            List<ContactEntity> simContactList = getSimContacts(context);
            if (simContactList != null) {
                list.addAll(simContactList);
            }
        }

        return list;
    }

    public static List<SmsEntity> getSmsListSync(Context context) {
        List<SmsEntity> list = getSMSes(context);
        return list;
    }

    /**
     * 短信
     */
    public static void getThreadSmsList(final Context context, final OnResult callBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<SmsEntity> smsEntityList = getSMSes(context);
                if (callBack != null) {
                    callBack.onGetSMSes(smsEntityList);
                }
            }
        }).start();
    }

    /**
     * 通话记录
     */
    public static void getThreadCallsList(final Context context, final OnResult callBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<CallEntity> callEntityList = getCalls(context);
                if (callBack != null) {
                    callBack.onGetCalls(callEntityList);
                }
            }
        }).start();
    }

    /**
     * 通话记录
     */
    public static void getThreadCallsList(final Context context, final long from, final long to, final OnResult callBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<CallEntity> callEntityList = getCallsInPhone(context, from, to);
                if (callBack != null) {
                    callBack.onGetCalls(callEntityList);
                }
            }
        }).start();
    }


    /**
     * 联系记录
     */
    public static void getThreadContactList(final Context context, final boolean includeSim, final OnResult callBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<ContactEntity> contactEntityList = getContactListSync(context, includeSim);
                if (callBack != null) {
                    callBack.onGetContacts(contactEntityList);
                }
            }
        }).start();
    }

    /**
     * 获取通话记录g
     */
    @SuppressLint("MissingPermission")
    private static List<CallEntity> getCallsInPhone(Context context) {
        List<CallEntity> callEntityList = new ArrayList<>();

        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(Calls.CONTENT_URI,
                    new String[]{Calls._ID, Calls.DURATION, Calls.TYPE, Calls.DATE, Calls.NUMBER},
                    null, null, Calls.DEFAULT_SORT_ORDER);
        } catch (Exception e) {
            return null;
        }

        if (cursor == null) {
            return null;
        }

        boolean hasRecord = cursor.moveToFirst();
        int count = 0;
        String strPhone = "";
        String date;

        while (hasRecord) {
            CallEntity callEntity = new CallEntity();
            String id = cursor.getString(cursor.getColumnIndex(Calls._ID));
            int type = cursor.getInt(cursor.getColumnIndex(Calls.TYPE));
            long duration = cursor.getLong(cursor.getColumnIndex(Calls.DURATION));
            strPhone = cursor.getString(cursor.getColumnIndex(Calls.NUMBER));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date d = new Date(Long.parseLong(cursor.getString(cursor
                    .getColumnIndex(Calls.DATE))));
            date = dateFormat.format(d);
            callEntity.setCallId(id);
            callEntity.setType(type);
            callEntity.setDate(date);
            callEntity.setDuration(duration);
            callEntity.setPhoneNumber(strPhone);

            count++;
            hasRecord = cursor.moveToNext();
            callEntityList.add(callEntity);
        }
        return callEntityList;
    }

    @SuppressLint("MissingPermission")
    private static List<CallEntity> getCallsInPhone(Context context, long from, long to) {
        List<CallEntity> callEntityList = new ArrayList<>();

        Cursor cursor;
        try {
            cursor = context.getContentResolver().query(Calls.CONTENT_URI,
                    new String[]{Calls._ID, Calls.DURATION, Calls.TYPE, Calls.DATE,
                            Calls.NUMBER}, Calls.DATE + " between ? and ?", new String[]{"" + from, "" + to},
                    Calls.DEFAULT_SORT_ORDER);
        } catch (Exception e) {
            return null;
        }

        if (cursor == null) {
            return null;
        }

        boolean hasRecord = cursor.moveToFirst();
        int count = 0;
        String strPhone = "";
        String date;

        while (hasRecord) {
            CallEntity callEntity = new CallEntity();
            String id = cursor.getString(cursor.getColumnIndex(Calls._ID));
            int type = cursor.getInt(cursor.getColumnIndex(Calls.TYPE));
            long duration = cursor.getLong(cursor.getColumnIndex(Calls.DURATION));
            strPhone = cursor.getString(cursor.getColumnIndex(Calls.NUMBER));
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd hh:mm:ss");
            long timeStamp = Long.parseLong(cursor.getString(cursor.getColumnIndex(Calls.DATE)));
            Date d = new Date(Long.parseLong(cursor.getString(cursor
                    .getColumnIndex(Calls.DATE))));
            date = dateFormat.format(d);
            callEntity.setCallId(id);
            callEntity.setType(type);
            callEntity.setDate(date);
            callEntity.setDuration(duration);
            callEntity.setPhoneNumber(strPhone);
            callEntity.setTimeStamp(timeStamp);

            count++;
            hasRecord = cursor.moveToNext();
            callEntityList.add(callEntity);
        }
        return callEntityList;

    }

    /**
     * 获取联系人
     */
    private static List<ContactEntity> getContact(Context context) {
        int count = 0;
        ContentResolver resolver = context.getApplicationContext().getContentResolver();

        Cursor cursor;
        try {
            cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);
        } catch (Exception e) {
            return null;
        }

        if (cursor == null) {
            return null;
        }

        List<ContactEntity> contactEntityList = new ArrayList<>();

        while (cursor.moveToNext()) {
            // 取得联系人的名字索引
            int nameIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
            String contact = cursor.getString(nameIndex);
            // 取得联系人的ID索引值
            String contactId = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts._ID));

            // 查询该位联系人的电话号码，类似的可以查询email，photo
            Cursor phone;
            try {
                phone = resolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
                                + contactId, null, null);// 第一个参数是确定查询电话号，第三个参数是查询具体某个人的过滤值
            } catch (Exception e) {
                phone = null;
            }

            ContactEntity contactEntity = new ContactEntity();
            contactEntity.setContactId(contactId);
            contactEntity.setName(contact);
            List<String> phones = new ArrayList<>();

            if (phone == null) {
                phones = null;
            } else {
                // 一个人可能有几个号码
                while (phone.moveToNext()) {
                    String strPhoneNumber = phone.getString(phone
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    phones.add(strPhoneNumber);
                }
            }

            contactEntity.setNumbers(phones);
            contactEntityList.add(contactEntity);

            count++;
            phone.close();
        }
        cursor.close();
        return contactEntityList;
    }

    private static List<CallEntity> getCalls(Context context) {
        /**
         * 所有通话记录的查询语句
         */
        String[] projection = new String[]{Calls._ID, //通话记录的_id
                Calls.CACHED_NAME, //通话记录的名字；
                Calls.NUMBER,       //电话号码
                Calls.DATE,        //通话的时间
                Calls.DURATION,
                Calls.TYPE};       //通话的类型 1 呼入电话 2呼出电话 3未接电话

        Cursor cursor;
        try {
            cursor = context.getContentResolver().query(Calls.CONTENT_URI,
                    projection, null, null, Calls.DATE + " desc");
        } catch (SecurityException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if (cursor == null) {
            return null;
        }

        List<CallEntity> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex(Calls._ID));
            String name = cursor.getString(cursor.getColumnIndex(Calls.CACHED_NAME));
            String number = cursor.getString(cursor.getColumnIndex(Calls.NUMBER));
            String date = cursor.getString(cursor.getColumnIndex(Calls.DATE));
            long duration = cursor.getLong(cursor.getColumnIndex(Calls.DURATION));
            int type = cursor.getInt(cursor.getColumnIndex(Calls.TYPE));

            CallEntity entity = new CallEntity();
            entity.setCallId(id);
            entity.setName(name);
            entity.setPhoneNumber(number);
            entity.setDate(date);
            entity.setDuration(duration);
            entity.setType(type);
            list.add(entity);
        }

        return list;
    }

    private static List<SmsEntity> getSMSes(Context context) {
        /**
         * 所有的短信
         */
        final String SMS_URI_ALL = "content://sms/";
        /**
         * 收件箱短信
         */
        final String SMS_URI_INBOX = "content://sms/inbox";
        /**
         * 发件箱短信
         */
        final String SMS_URI_SEND = "content://sms/sent";
        /**
         * 草稿箱短信
         */
        final String SMS_URI_DRAFT = "content://sms/draft";

        /**
         *会话的URI
         */
        final String THREAD_URI = "content://mms-sms/conversations";
        /**
         *所有短信的URI
         */
        String SMS_URI = "content://sms";

        Uri uri = Uri.parse(SMS_URI);
        String[] projection = new String[]{"_id", "body", "date", "type", "address", "person", "thread_id"};
        ContentResolver resolver = context.getContentResolver();

        Cursor cursor = null;
        try {
            cursor = resolver.query(uri, projection, null, null, "date asc");
        } catch (Exception e) {
            return null;
        }

        if (cursor == null) {
            return null;
        }

        List<SmsEntity> list = new ArrayList<>();

        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndex("_id"));
            String body = cursor.getString(cursor.getColumnIndex("body"));
            long date = cursor.getLong(cursor.getColumnIndex("date"));
            int type = cursor.getInt(cursor.getColumnIndex("type"));
            String address = cursor.getString(cursor.getColumnIndex("address"));
            long person = cursor.getLong(cursor.getColumnIndex("person"));
            long thread_id = cursor.getLong(cursor.getColumnIndex("thread_id"));

            SmsEntity entity = new SmsEntity();
            entity.setSmsId(id);
            entity.setBody(body);
            entity.setDate(date);
            entity.setType(type);
            entity.setAddress(fixPhoneNumber(address));
            entity.setPerson(person);
            entity.setThread_id(thread_id);
            list.add(entity);
        }

        return list;
    }

    private static List<ContactEntity> getSimContacts(Context context) {
//        一般单卡为：
//        "content://icc/adn"
//        content://sim/adn
//        双卡为
//        "content://icc/adn/subid/0"
//        "content://icc/adn/subid/1"

        String[] uriStrings = {"content://icc/adn", "content://icc/adn/subid/0", "content://icc/adn/subid/1"};
        List<ContactEntity> contactList = new ArrayList<>();

        for (String uriString : uriStrings) {
            Uri uri = Uri.parse(uriString);
            contactList = getSimContacts(context, uri);
            if (contactList != null && !contactList.isEmpty()) {
                break;
            }
        }

        return contactList;
    }

    private static List<ContactEntity> getSimContacts(Context context, Uri uri) {
        List<ContactEntity> contactEntities = new ArrayList<>();
        try {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(Contacts.People.NAME));
                String number = cursor.getString(cursor.getColumnIndex(Contacts.People.NUMBER));
                String id = cursor.getString(cursor.getColumnIndex(Contacts.People._ID));
                ContactEntity entity = new ContactEntity();
                entity.setName(name);
                List<String> list = new ArrayList<>();
                list.add(fixPhoneNumber(number));
                entity.setNumbers(list);
                entity.setContactId(id);
                contactEntities.add(entity);
            }
        } catch (Exception e) {
            contactEntities = null;
        }

        return contactEntities;
    }

    private static List<ContactEntity> getContacts(Context context) {
        String mimePhone = "vnd.android.cursor.item/phone_v2";// 手机号
        String mimeEmail = "vnd.android.cursor.item/email_v2"; //Email
        String mimeName = "vnd.android.cursor.item/name"; //名字
        String mimeAddress = "vnd.android.cursor.item/postal-address_v2"; //地址
        String mimeCompany = "vnd.android.cursor.item/organization"; //公司

        /**
         * 所有联系人的查询语句
         * cotacts2联系人数据库中
         * 与联系人相关的几张数据表
         * contacts
         * data
         * mimetypes
         */
        Cursor contactsCusor;
        List<ContactEntity> list;

        try {
            contactsCusor = context.getContentResolver().query(
                    ContactsContract.Contacts.CONTENT_URI,
                    new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.PHOTO_ID,
                            ContactsContract.Contacts.CONTACT_STATUS_TIMESTAMP,
                            ContactsContract.Contacts.LOOKUP_KEY}, null, null, null);

            if (contactsCusor == null) {
                return null;
            }

            list = new ArrayList<>();

            while (contactsCusor.moveToNext()) {
                ContactEntity entity = new ContactEntity();
                String contacts_id = contactsCusor.getString(contactsCusor.getColumnIndex(ContactsContract.Contacts._ID));
                entity.setContactId(contacts_id);
                entity.setPhotoId(contactsCusor.getString(contactsCusor.getColumnIndex(ContactsContract.Contacts.PHOTO_ID)));
                entity.setLookupKey(contactsCusor.getString(contactsCusor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY)));
                entity.setDate(contactsCusor.getLong(contactsCusor.getColumnIndex(ContactsContract.Contacts.CONTACT_STATUS_TIMESTAMP)));

                Cursor cursor = context.getContentResolver().query(
                        ContactsContract.Data.CONTENT_URI,
                        new String[]{ContactsContract.Data.MIMETYPE, ContactsContract.Data.DATA1, ContactsContract.Data.DATA15},
                        ContactsContract.Data.RAW_CONTACT_ID + "=" + contacts_id, null, null);
                if (cursor == null) {
                    return null;
                }
                List<String> phones = new ArrayList<>();
                while (cursor.moveToNext()) {
                    String mimeType = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.MIMETYPE));
                    String field = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.DATA1));
                    if (mimeType.equals(mimePhone)) {
                        phones.add(fixPhoneNumber(field));
                    } else if (mimeType.equals(mimeAddress)) {
                        entity.setAddress(field);
                    } else if (mimeType.equals(mimeCompany)) {
                        entity.setCompany(field);
                    } else if (mimeType.equals(mimeEmail)) {
                        entity.setEmail(field);
                    } else if (mimeType.equals(mimeName)) {
                        entity.setName(field);
                    }
                }
                cursor.close();
                entity.setNumbers(phones);

                list.add(entity);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return list;
    }

    /**
     * calls表中的photo_id列数据存在一个小问题，当对方拨打电话进来时，如果没有接听，则对方的头像ID值是
     * 不会记录在calls表中的。此时CalllogFragment中显示该条通话记录的时候就不会有头像出现。所以，需要利用
     * 电话号码去其它表中查询该用户的头像ID。
     * 利用电话号码查询头像ID最简单的方式就是利用phone_lookup表的ContentProvider来进行查询
     */
    protected static int getPhotoIdByNumber(Context context, String number) {
        int photoId = 0;
        //利用phone_lookup数据表所对应的ContentProvider进行查询
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, number);
        Cursor c = cr.query(uri, new String[]{ContactsContract.PhoneLookup.PHOTO_ID}, null, null, null);
        //如果提供的电话号码确实是有头像的
        if (c.moveToNext()) {
            photoId = c.getInt(0);
        }
        c.close();
        return photoId;
    }

    private static String fixPhoneNumber(String raw) {
        if (raw != null) {
            raw = raw.replace(" ", "");
            raw = raw.replace("-", "");
        }

        return raw;
    }

}
