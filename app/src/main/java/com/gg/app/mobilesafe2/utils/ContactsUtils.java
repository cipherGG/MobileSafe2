package com.gg.app.mobilesafe2.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactsUtils {

    public static List<Map<String, String>> readContact(Context context) {
        // 首先，从raw_contact表中读取联系人的id("contact_id")
        List<Map<String, String>> list = new ArrayList<>();

        Uri rawContactsUri = Uri.parse("content://com.android.contacts/raw_contacts");
        Cursor rawContactsCursor = context.getContentResolver().query(rawContactsUri,
                new String[]{"contact_id"}, null, null, null);

        if (rawContactsCursor == null) {
            return null;
        }

        while (rawContactsCursor.moveToNext()) {
            // 其次，根据contact_id从data表中查询出相对应的电话号码和联系人
            String contactId = rawContactsCursor.getString(0);

            if (TextUtils.isEmpty(contactId)) {
                continue;
            }

            Uri dataUri = Uri.parse("content://com.android.contacts/data");
            Cursor dataCursor = context.getContentResolver().query(dataUri,
                    new String[]{"data1", "mimetype"}, "contact_id=?", new String[]{contactId}, null);

            if (dataCursor == null) {
                continue;
            }

            Map<String, String> map = new HashMap<>();

            while (dataCursor.moveToNext()) {
                String data1 = dataCursor.getString(0);
                String mimetype = dataCursor.getString(1);

                // 然后，根据mimeType来区分联系人和电话号
                if ("vnd.android.cursor.item/name".equals(mimetype)) {
                    map.put("name", data1);
                } else if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
                    map.put("phone", data1);
                }
            }
            list.add(map);
            dataCursor.close();
        }
        rawContactsCursor.close();
        return list;
    }

}
