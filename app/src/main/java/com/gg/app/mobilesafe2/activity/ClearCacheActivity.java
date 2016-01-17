package com.gg.app.mobilesafe2.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gg.app.mobilesafe2.R;

import org.xutils.view.annotation.ContentView;

/**
 * aidl 不会配置，暂且跳过
 */
@ContentView(R.layout.activity_clear_cache)
public class ClearCacheActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ClearCacheActivity.this).inflate(R.layout.item_clear_cache, parent, false);

            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }

        private class ItemViewHolder extends RecyclerView.ViewHolder {
            private ImageView icon;
            private TextView name;
            private TextView cache;
            private Button clear;

            public ItemViewHolder(View itemView) {
                super(itemView);
                icon = (ImageView) findViewById(R.id.iv_item_clear_icon);
                name = (TextView) findViewById(R.id.tv_item_clear_name);
                cache = (TextView) findViewById(R.id.tv_item_clear_cache);
                clear = (Button) findViewById(R.id.btn_item_clear_clear);
            }
        }
    }

}
