package com.umarbhutta.xlightcompanion.bindDevice;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.Tools.NetworkUtils;
import com.umarbhutta.xlightcompanion.Tools.ToastUtil;
import com.umarbhutta.xlightcompanion.adapter.WifiListAdapter;
import com.umarbhutta.xlightcompanion.okHttp.model.AddDeviceResult;
import com.umarbhutta.xlightcompanion.okHttp.requests.RequestAddDevice;
import com.umarbhutta.xlightcompanion.settings.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/4.
 * 绑定设备
 */

public class BindDeviceFirstActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private LinearLayout llBack;
    private TextView btnSure;
    private EditText ssidEdit;
    private EditText wifiPwdEdit;
    private ListView listView;
    private ImageView deleteIcon;
    private WifiListAdapter adapter;
    private List<ScanResult> listb = new ArrayList<ScanResult>();
    private ScanResult curWifi = null;
    private final int WIFI_PERMISSION_REQ_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_device_first);
        getSupportActionBar().hide();
        llBack = (LinearLayout) findViewById(R.id.ll_back);
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSure = (TextView) findViewById(R.id.tvEditSure);
        btnSure.setVisibility(View.GONE);
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("连接Wifi");

        ssidEdit = (EditText) findViewById(R.id.ssid);
        wifiPwdEdit = (EditText) findViewById(R.id.wifi_pwd);
        findViewById(R.id.rightIcon).setOnClickListener(this);
        deleteIcon = (ImageView) findViewById(R.id.pwdrightIcon);
        deleteIcon.setOnClickListener(this);
        findViewById(R.id.btn_connect_wifi).setOnClickListener(this);

        listView = (ListView) findViewById(R.id.wifi_list);
        listView.setOnItemClickListener(this);
        adapter = new WifiListAdapter(this.getApplicationContext(), listb);
        listView.setAdapter(adapter);

        checkPublishPermission();
//        if (checkPublishPermission()) {
        getCurWifiInfo();
//        }
    }


    private void checkPublishPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission_group.LOCATION) != PackageManager.PERMISSION_GRANTED) {
// 获取wifi连接需要定位权限,没有获取权限
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_WIFI_STATE,
            }, WIFI_PERMISSION_REQ_CODE);
            return;
        }


//        if (Build.VERSION.SDK_INT >= 23) {
//            List<String> permissions = new ArrayList<>();
//            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE)) {
//                permissions.add(Manifest.permission.ACCESS_WIFI_STATE);
//            }
//
//            if (permissions.size() != 0) {
//                ActivityCompat.requestPermissions(this,
//                        (String[]) permissions.toArray(new String[0]),
//                        WIFI_PERMISSION_REQ_CODE);
//                return false;
//            }
//        }
//        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case WIFI_PERMISSION_REQ_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {// 允许
                    getCurWifiInfo();
                } else { // 不允许
                    ToastUtil.showToast(this, "您拒绝了获取wifi列表权限");
                }
                break;
        }
    }

    private void getCurWifiInfo() {
        if (!isWifiAvailable()) {
            ToastUtil.showToast(this, "请打开WiFi");
            return;
        }

        getWifiList();
    }


    /**
     * wifi是否可用
     *
     * @return
     */
    private boolean isWifiAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected() && networkInfo
                .getType() == ConnectivityManager.TYPE_WIFI);
    }

    /**
     * wifi列表
     */
    private void getWifiList() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        listb.addAll(wifiManager.getScanResults());
        adapter.notifyDataSetChanged();
        if (null != listb && listb.size() > 0) {
            curWifi = listb.get(0);
            ssidEdit.setText(curWifi.SSID);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rightIcon: //下箭头
                listView.setVisibility((View.VISIBLE == listView.getVisibility()) ? View.GONE : View.VISIBLE);
                break;
            case R.id.pwdrightIcon: //清除密码按钮
                wifiPwdEdit.setText("");
                break;
            case R.id.btn_connect_wifi: //连接wifi
                bindDevice();
                break;
            default:
                listView.setVisibility(View.GONE);
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        listView.setVisibility(View.GONE);
        curWifi = listb.get(position);
        ssidEdit.setText(curWifi.SSID);
    }


    /**
     * 绑定设备
     * TODO 需要先把调用SDK的接口，再调用服务器的接口，SDK接口目前先跳过
     */
    private void bindDevice() {
        if (!NetworkUtils.isNetworkAvaliable(this)) {
            ToastUtil.showToast(this, R.string.net_error);
            return;
        }


        RequestAddDevice.getInstance().addDevice(this, "测试设备", 0, 1, 0, new RequestAddDevice.OnAddDeviceCallBack() {
            @Override
            public void mOnAddDeviceCallBackFail(int code, final String errMsg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(BindDeviceFirstActivity.this, "设备连接失败" + errMsg);
                    }
                });
            }

            @Override
            public void mOnAddDeviceCallBackSuccess(AddDeviceResult mAddDeviceResult) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setResult(100);
                        ToastUtil.showToast(BindDeviceFirstActivity.this, "设备连接成功");
                        BindDeviceFirstActivity.this.finish();
                    }
                });

            }
        });
    }


}
