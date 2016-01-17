package com.gg.app.mobilesafe2.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gg.app.mobilesafe2.R;
import com.gg.app.mobilesafe2.bean.BlackNumberInfo;
import com.gg.app.mobilesafe2.db.dao.BlackNumberDao;
import com.gg.app.mobilesafe2.utils.DialogUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

@ContentView(R.layout.activity_call_safe)
public class CallSafeActivity extends BaseActivity {
    @ViewInject(R.id.lv_callSafe_contacts)
    private RecyclerView recyclerView;
    @ViewInject(R.id.linear_call_safe_loading)
    private LinearLayout layout;
    private List<BlackNumberInfo> startList;
    private recycleAdapter adapter;
    private int currentPager = -1;
    private BlackNumberDao dao;
    private ProgressBar progressBar;
    private TextView textView;
    private int lastVisibleItemPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dao = new BlackNumberDao(CallSafeActivity.this);

        layout.setVisibility(View.VISIBLE);

        new Thread() {
            @Override
            public void run() {
                currentPager += 1;
                startList = dao.queryPager(currentPager, 15);
                handler.sendEmptyMessage(1);
            }
        }.start();

    }

    @Event(value = R.id.btn_call_safe_edit)
    private void onClick(View v) {
        addBlackNumber();
    }

    private void addBlackNumber() {
        View view = View.inflate(this, R.layout.dialog_call_safe_add, null);

        final AlertDialog addDialog = DialogUtils.showAlertDialog(CallSafeActivity.this, null, null, view, null, null, null);

        //没办法，这里怎么注解还没有去看
        final EditText et = (EditText) view.findViewById(R.id.et_dialog_call_safe_add);
        final CheckBox phone = (CheckBox) view.findViewById(R.id.cb_dialog_call_safe_phone);
        final CheckBox sms = (CheckBox) view.findViewById(R.id.cb_dialog_call_safe_sms);
        Button ensure = (Button) view.findViewById(R.id.btn_dialog_call_safe_ensure);
        Button cancel = (Button) view.findViewById(R.id.btn_dialog_call_safe_cancel);

        ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = et.getText().toString().trim();

                if (TextUtils.isEmpty(number)) {
                    et.setError("请输入拦截号码");
                    return;
                }

                String mode;
                if (phone.isChecked() && sms.isChecked()) {
                    mode = "1";
                } else if (phone.isChecked()) {
                    mode = "2";
                } else if (sms.isChecked()) {
                    mode = "3";
                } else {
                    CallSafeActivity.this.showToast("请勾选拦截模式");
                    return;
                }

                if (!dao.insert(number, mode)) {
                    return;
                }

                BlackNumberInfo info = new BlackNumberInfo();
                info.setNumber(number);
                info.setMode(mode);

                //添加到list里的第一条
                startList.add(0, info);
                adapter.notifyDataSetChanged();

                addDialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDialog.dismiss();
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    startLoadOver();
                    break;
                case 2:
                    progressBar.setVisibility(View.INVISIBLE);
                    textView.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                    break;
                case 3:
                    progressBar.setVisibility(View.INVISIBLE);
                    textView.setVisibility(View.VISIBLE);
                    CallSafeActivity.this.showSnaker(recyclerView, "数据全部加载完毕", "", null);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    //页面加载完成
    private void startLoadOver() {
        layout.setVisibility(View.INVISIBLE);

        //这里一般是耗时操作,或者是setList
        adapter = new recycleAdapter();

        //recycle使用layoutManager来管理item的布局的
        final LinearLayoutManager layoutManager = new LinearLayoutManager(CallSafeActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        //上拉刷新，不用点击footView也能刷新，看清是add
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                // findLastVisibleItemPosition()会提前加载
                lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition + 1 == adapter.getItemCount()) {
                    addList();
                }
            }
        });
    }

    /**
     * adapter
     */
    private class recycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int TYPE_ITEM = 0;
        private static final int TYPE_FOOTER = 1;

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return startList.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position + 1 == getItemCount()) {
                return TYPE_FOOTER;
            } else {
                return TYPE_ITEM;
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            if (viewType == TYPE_ITEM) {
                View view = LayoutInflater.from(CallSafeActivity.this).inflate(R.layout.item_call_safe, parent, false);
                // 下面这两行组合也可以
                // View view = LayoutInflater.from(CallSafeActivity.this).inflate(R.layout.item_call_safe, null);
                // view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                return new ItemViewHolder(view);

            } else if (viewType == TYPE_FOOTER) {
                View view = LayoutInflater.from(CallSafeActivity.this).inflate(R.layout.item_call_safe_foot, parent, false);
                return new FooterViewHolder(view);
            }

            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ItemViewHolder) {
                ((ItemViewHolder) holder).number.setText(String.valueOf(startList.get(position).getNumber()));

                String getMode = startList.get(position).getMode();

                switch (getMode) {
                    case "1":
                        ((ItemViewHolder) holder).mode.setText("电话+短信拦截");
                        break;
                    case "2":
                        ((ItemViewHolder) holder).mode.setText("电话拦截");
                        break;
                    case "3":
                        ((ItemViewHolder) holder).mode.setText("短信拦截");
                        break;
                }
            }
        }

        class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView number;
            TextView mode;
            Button delete;

            //把实例化封装到构造函数里
            public ItemViewHolder(View itemView) {
                super(itemView);
                number = (TextView) itemView.findViewById(R.id.tv_item_call_safe_number);
                mode = (TextView) itemView.findViewById(R.id.tv_item_call_safe_mode);
                delete = (Button) itemView.findViewById(R.id.btn_item_call_safe_delete);

                delete.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                //创建快捷方式
                Intent intent = new Intent();

                intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");

                intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "快捷方式");

                intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));

                Intent doWhatIntent = new Intent();
                doWhatIntent.setAction(Intent.ACTION_CALL);
                doWhatIntent.setData(Uri.parse("tel:" + number.getText().toString()));
                intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, doWhatIntent);

                CallSafeActivity.this.sendBroadcast(intent);

                CallSafeActivity.this.showToast("快捷方式创建成功");
            }
        }

        class FooterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public FooterViewHolder(View footView) {
                super(footView);
                textView = (TextView) footView.findViewById(R.id.tv_item_call_safe_foot);
                progressBar = (ProgressBar) footView.findViewById(R.id.pb_item_call_safe_foot);

                footView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                addList();
            }
        }
    }

    private void addList() {
        textView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        new Thread() {
            @Override
            public void run() {
                currentPager += 1;
                List<BlackNumberInfo> newList = dao.queryPager(currentPager, 15);

                if (newList.size() != 0) {
                    startList.addAll(newList);
                    handler.sendEmptyMessage(2);

                } else {
                    handler.sendEmptyMessage(3);
                }
            }
        }.start();
    }

}
