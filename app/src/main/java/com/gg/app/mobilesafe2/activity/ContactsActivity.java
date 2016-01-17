package com.gg.app.mobilesafe2.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.gg.app.mobilesafe2.R;
import com.gg.app.mobilesafe2.utils.ContactsUtils;

import java.util.List;
import java.util.Map;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.view.annotation.Event;

@ContentView(R.layout.activity_contacts)
public class ContactsActivity extends BaseActivity {
    @ViewInject(R.id.lv_contacts_phone)
    private ListView listView;
    private List<Map<String, String>> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        data = ContactsUtils.readContact(this);

        SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.item_contact,
                new String[]{"name", "phone"}, new int[]{R.id.tv_contact_name, R.id.tv_contact_phone});

        listView.setAdapter(adapter);
    }

    @Event(value = R.id.lv_contacts_phone, type = AdapterView.OnItemClickListener.class)
    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        String phone = data.get(position).get("phone");
        intent.putExtra("phone", phone);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
    }
}
