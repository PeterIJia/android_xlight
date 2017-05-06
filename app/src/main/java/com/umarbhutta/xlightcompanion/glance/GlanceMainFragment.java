package com.umarbhutta.xlightcompanion.glance;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.SDK.CloudAccount;
import com.umarbhutta.xlightcompanion.SDK.xltDevice;
import com.umarbhutta.xlightcompanion.Tools.DataReceiver;
import com.umarbhutta.xlightcompanion.Tools.Logger;
import com.umarbhutta.xlightcompanion.Tools.NetworkUtils;
import com.umarbhutta.xlightcompanion.Tools.SharedPreferencesUtils;
import com.umarbhutta.xlightcompanion.Tools.ToastUtil;
import com.umarbhutta.xlightcompanion.Tools.UserUtils;
import com.umarbhutta.xlightcompanion.bindDevice.BindDeviceFirstActivity;
import com.umarbhutta.xlightcompanion.control.adapter.DevicesMainListAdapter;
import com.umarbhutta.xlightcompanion.deviceList.DeviceListActivity;
import com.umarbhutta.xlightcompanion.location.BaiduMapUtils;
import com.umarbhutta.xlightcompanion.main.SimpleDividerMainItemDecoration;
import com.umarbhutta.xlightcompanion.main.SlidingMenuMainActivity;
import com.umarbhutta.xlightcompanion.okHttp.model.DeviceInfoResult;
import com.umarbhutta.xlightcompanion.okHttp.model.Devicenodes;
import com.umarbhutta.xlightcompanion.okHttp.model.Rows;
import com.umarbhutta.xlightcompanion.okHttp.requests.RequestFirstPageInfo;
import com.umarbhutta.xlightcompanion.okHttp.requests.RequestUnBindDevice;
import com.umarbhutta.xlightcompanion.okHttp.requests.imp.CommentRequstCallback;
import com.umarbhutta.xlightcompanion.views.ProgressDialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Umar Bhutta.
 */
public class GlanceMainFragment extends Fragment implements View.OnClickListener {
    private ImageButton fab;
    TextView txtLocation, outsideTemp, degreeSymbol, roomTemp, roomHumidity, outsideHumidity, apparentTemp;
    ImageView imgWeather;
    private ImageButton home_menu, home_setting;

    private static final String TAG = GlanceMainFragment.class.getSimpleName();
    private RecyclerView devicesRecyclerView;
    WeatherDetails mWeatherDetails;

    private Handler m_handlerGlance;

    private Bitmap icoDefault, icoClearDay, icoClearNight, icoRain, icoSnow, icoSleet, icoWind, icoFog;
    private Bitmap icoCloudy, icoPartlyCloudyDay, icoPartlyCloudyNight;
    private static int ICON_WIDTH = 70;
    private static int ICON_HEIGHT = 75;

    /**
     * 设备列表
     */
    public static List<Rows> deviceList = new ArrayList<Rows>();
    public static List<Devicenodes> devicenodes = new ArrayList<Devicenodes>();
    private DevicesMainListAdapter devicesListAdapter;
    private TextView default_text;
    private TextView save_money;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_setting:
                // 跳转到选择的主设备列表页面
                onFabPressed(DeviceListActivity.class);
                break;
            case R.id.home_menu:
                switchFragment();
                break;
        }
    }

    // the meat of switching the above fragment
    private void switchFragment() {
        if (getActivity() == null)
            return;

        if (getActivity() instanceof SlidingMenuMainActivity) {
            SlidingMenuMainActivity ra = (SlidingMenuMainActivity) getActivity();
            ra.toggle();
        }
    }

    private void onFabPressed(Class activity) {
        if (getActivity() == null)
            return;

        if (getActivity() instanceof SlidingMenuMainActivity) {
            SlidingMenuMainActivity ra = (SlidingMenuMainActivity) getActivity();
            ra.onActivityPressed(activity);
        }
    }

    private class MyDataReceiver extends DataReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            roomTemp.setText(SlidingMenuMainActivity.m_mainDevice.m_Data.m_RoomTemp + "℃");
            roomHumidity.setText(SlidingMenuMainActivity.m_mainDevice.m_Data.m_RoomHumidity + "\u0025");
//            roomBrightness.setText(SlidingMenuMainActivity.m_mainDevice.m_Data.m_RoomBrightness + "\u0025");
        }
    }

    private final MyDataReceiver m_DataReceiver = new MyDataReceiver();

    @Override
    public void onDestroyView() {
        devicesRecyclerView.setAdapter(null);
        SlidingMenuMainActivity.m_mainDevice.removeDataEventHandler(m_handlerGlance);
        if (SlidingMenuMainActivity.m_mainDevice.getEnableEventBroadcast()) {
            getContext().unregisterReceiver(m_DataReceiver);
        }
        super.onDestroyView();
    }

    private Dialog mSelectDialog = null;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_glance, container, false);
        if (SlidingMenuMainActivity.m_mainDevice == null) {
            SlidingMenuMainActivity.m_mainDevice = new xltDevice();
            SlidingMenuMainActivity.m_mainDevice.Init(getActivity());
        }

        //        hide nav bar
        fab = (ImageButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到绑定设备页面
                Intent intent = new Intent(getContext(), BindDeviceFirstActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        default_text = (TextView) view.findViewById(R.id.default_text);

        txtLocation = (TextView) view.findViewById(R.id.location);
        outsideTemp = (TextView) view.findViewById(R.id.outsideTemp);
        degreeSymbol = (TextView) view.findViewById(R.id.degreeSymbol);
        outsideHumidity = (TextView) view.findViewById(R.id.valLocalHumidity);
        apparentTemp = (TextView) view.findViewById(R.id.valApparentTemp);
        roomTemp = (TextView) view.findViewById(R.id.valRoomTemp);
        roomTemp.setText(SlidingMenuMainActivity.m_mainDevice.m_Data.m_RoomTemp + "℃");
        roomHumidity = (TextView) view.findViewById(R.id.valRoomHumidity);
        roomHumidity.setText(SlidingMenuMainActivity.m_mainDevice.m_Data.m_RoomHumidity + "\u0025");
        imgWeather = (ImageView) view.findViewById(R.id.weatherIcon);
        home_menu = (ImageButton) view.findViewById(R.id.home_menu);
        home_menu.setOnClickListener(this);
        home_setting = (ImageButton) view.findViewById(R.id.home_setting);
        home_setting.setOnClickListener(this);
        save_money = (TextView) view.findViewById(R.id.save_money);

        Resources res = getResources();
        Bitmap weatherIcons = decodeResource(res, R.drawable.weather_icons_1, 420, 600);
        icoDefault = Bitmap.createBitmap(weatherIcons, 0, 0, ICON_WIDTH, ICON_HEIGHT);
        icoClearDay = Bitmap.createBitmap(weatherIcons, ICON_WIDTH, 0, ICON_WIDTH, ICON_HEIGHT);
        icoClearNight = Bitmap.createBitmap(weatherIcons, ICON_WIDTH * 2, 0, ICON_WIDTH, ICON_HEIGHT);
        icoRain = Bitmap.createBitmap(weatherIcons, ICON_WIDTH * 5, ICON_HEIGHT * 2, ICON_WIDTH, ICON_HEIGHT);
        icoSnow = Bitmap.createBitmap(weatherIcons, ICON_WIDTH * 4, ICON_HEIGHT * 3, ICON_WIDTH, ICON_HEIGHT);
        icoSleet = Bitmap.createBitmap(weatherIcons, ICON_WIDTH * 5, ICON_HEIGHT * 3, ICON_WIDTH, ICON_HEIGHT);
        icoWind = Bitmap.createBitmap(weatherIcons, 0, ICON_HEIGHT * 3, ICON_WIDTH, ICON_HEIGHT);
        icoFog = Bitmap.createBitmap(weatherIcons, 0, ICON_HEIGHT * 2, ICON_WIDTH, ICON_HEIGHT);
        icoCloudy = Bitmap.createBitmap(weatherIcons, ICON_WIDTH, ICON_HEIGHT * 5, ICON_WIDTH, ICON_HEIGHT);
        icoPartlyCloudyDay = Bitmap.createBitmap(weatherIcons, ICON_WIDTH, ICON_HEIGHT, ICON_WIDTH, ICON_HEIGHT);
        icoPartlyCloudyNight = Bitmap.createBitmap(weatherIcons, ICON_WIDTH * 2, ICON_HEIGHT, ICON_WIDTH, ICON_HEIGHT);

        if (SlidingMenuMainActivity.m_mainDevice.getEnableEventBroadcast()) {
            IntentFilter intentFilter = new IntentFilter(xltDevice.bciSensorData);
            intentFilter.setPriority(3);
            getContext().registerReceiver(m_DataReceiver, intentFilter);
        }

        if (SlidingMenuMainActivity.m_mainDevice.getEnableEventSendMessage()) {
            m_handlerGlance = new Handler() {
                public void handleMessage(Message msg) {
                    int intValue = msg.getData().getInt("DHTt", -255);
                    if (intValue != -255) {
                        roomTemp.setText(intValue + "℃");
                    }
                    intValue = msg.getData().getInt("DHTh", -255);
                    if (intValue != -255) {
                        roomHumidity.setText(intValue + "\u0025");
                    }
                    intValue = msg.getData().getInt("ALS", -255);
                    if (intValue != -255) {
//                        roomBrightness.setText(intValue + "\u0025");
                    }
                }
            };
            SlidingMenuMainActivity.m_mainDevice.addDataEventHandler(m_handlerGlance);
        }

        //setup recycler view
        devicesRecyclerView = (RecyclerView) view.findViewById(R.id.devicesRecyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        devicesRecyclerView.setLayoutManager(layoutManager);
        devicesRecyclerView.addItemDecoration(new SimpleDividerMainItemDecoration(getActivity()));

        getTitleInfo();

        return view;
    }

    /**
     * 获取title信息
     */
    private void getTitleInfo() {
        String forecastUrl = "https://api.forecast.io/forecast/" + CloudAccount.DarkSky_apiKey + "/" + mLatitude + "," + mLongitude;

        if (NetworkUtils.isNetworkAvaliable(getActivity())) {
            OkHttpClient client = new OkHttpClient();
            //build request
            Request request = new Request.Builder()
                    .url(forecastUrl)
                    .build();
            //put request in call object to use for returning data
            Call call = client.newCall(request);
            //make async call
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    try {
                        String jsonData = response.body().string();
                        if (response.isSuccessful()) {
                            mWeatherDetails = getWeatherDetails(jsonData);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });
                        } else {
                            Toast.makeText(getActivity(), "There was an error retrieving weather data.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException | JSONException | NullPointerException e) {
                        Logger.i("Exception caught: " + e);
                    }
                }
            });
        } else {
            //if network isn't available
            Toast.makeText(getActivity(), "Please connect to the network before continuing.", Toast.LENGTH_SHORT).show();
        }
    }

    private View.OnClickListener btnlistener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_choose_add_wifi_device: // 扫描设备添加
                    Toast.makeText(getActivity(), getString(R.string.scan_device_add), Toast.LENGTH_SHORT).show();
                    break;
                //扫描二维码添加
                case R.id.btn_choose_scan_add_device:
                    Toast.makeText(getActivity(), getString(R.string.scan_qr_add), Toast.LENGTH_SHORT).show();
                    break;
                // 输入口令添加
                case R.id.btn_choose_add_line_device:
                    Toast.makeText(getActivity(), getString(R.string.input_order_add), Toast.LENGTH_SHORT).show();
                    break;
                // 取消
                case R.id.btn_cancel:
                    if (mSelectDialog != null) {
                        mSelectDialog.dismiss();
                    }
                    break;
            }
        }
    };

    private void updateDisplay() {
        imgWeather.setVisibility(View.VISIBLE);
        imgWeather.setImageResource(R.drawable.cloud);
//        txtLocation.setText(mWeatherDetails.getLocation());
//        imgWeather.setImageBitmap(getWeatherIcon(mWeatherDetails.getIcon()));
        outsideTemp.setText(" " + mWeatherDetails.getTemp("celsius"));
        degreeSymbol.setText("℃");
        outsideHumidity.setText(mWeatherDetails.getmHumidity() + "\u0025");
        apparentTemp.setText(mWeatherDetails.getApparentTemp("celsius") + "℃");

        roomTemp.setText(SlidingMenuMainActivity.m_mainDevice.m_Data.m_RoomTemp + "℃");
        roomHumidity.setText(SlidingMenuMainActivity.m_mainDevice.m_Data.m_RoomHumidity + "\u0025");
    }

    private WeatherDetails getWeatherDetails(String jsonData) throws JSONException {
        WeatherDetails weatherDetails = new WeatherDetails();

        //make JSONObject for all JSON
        JSONObject forecast = new JSONObject(jsonData);

        //JSONObject for nested JSONObject inside 'forecast' for current weather details
        JSONObject currently = forecast.getJSONObject("currently");

        weatherDetails.setTemp(currently.getDouble("temperature"));
        weatherDetails.setIcon(currently.getString("icon"));
        weatherDetails.setApparentTemp(currently.getDouble("apparentTemperature"));
        weatherDetails.setHumidity((int) (currently.getDouble("humidity") * 100 + 0.5));

        return weatherDetails;
    }

    private Bitmap decodeResource(Resources resources, final int id, final int newWidth, final int newHeight) {
        TypedValue value = new TypedValue();
        resources.openRawResource(id, value);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inTargetDensity = value.density;
        Bitmap loadBmp = BitmapFactory.decodeResource(resources, id, opts);

        int width = loadBmp.getWidth();
        int height = loadBmp.getHeight();

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap newBmp = Bitmap.createBitmap(loadBmp, 0, 0, width, height, matrix, true);
        return newBmp;
    }

    public Bitmap getWeatherIcon(final String iconName) {
        if (iconName.equalsIgnoreCase("clear-day")) {
            return icoClearDay;
        } else if (iconName.equalsIgnoreCase("clear-night")) {
            return icoClearNight;
        } else if (iconName.equalsIgnoreCase("rain")) {
            return icoRain;
        } else if (iconName.equalsIgnoreCase("snow")) {
            return icoSnow;
        } else if (iconName.equalsIgnoreCase("sleet")) {
            return icoSleet;
        } else if (iconName.equalsIgnoreCase("wind")) {
            return icoWind;
        } else if (iconName.equalsIgnoreCase("fog")) {
            return icoFog;
        } else if (iconName.equalsIgnoreCase("cloudy")) {
            return icoCloudy;
        } else if (iconName.equalsIgnoreCase("partly-cloudy-day")) {
            return icoPartlyCloudyDay;
        } else if (iconName.equalsIgnoreCase("partly-cloudy-night")) {
            return icoPartlyCloudyNight;
        } else {
            return icoDefault;
        }
    }

    public void getBaseInfos() {
        if (!NetworkUtils.isNetworkAvaliable(getActivity())) {
            ToastUtil.showToast(getActivity(), R.string.net_error);

            List<Rows> devices = (List<Rows>) SharedPreferencesUtils.getObject(getActivity(), SharedPreferencesUtils.KEY_DEVICE_LIST, null);
            if (null != devices && devices.size() > 0) {
                deviceList.clear();
                deviceList.addAll(devices);
            }
            if (devicesListAdapter != null) {
                devicesListAdapter.notifyDataSetChanged();
            }
            addDeviceMapsSDK(deviceList);
            return;
        }

        if (!UserUtils.isLogin(getActivity())) {
            return;
        }
        final ProgressDialog progressDialog = ProgressDialogUtils.showProgressDialog(getContext(), getString(R.string.loading));
        if (progressDialog != null) {
            progressDialog.show();
        }
        RequestFirstPageInfo.getInstance(getActivity()).getBaseInfo(new RequestFirstPageInfo.OnRequestFirstPageInfoCallback() {
            @Override
            public void onRequestFirstPageInfoSuccess(final DeviceInfoResult mDeviceInfoResult) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        List<Rows> devices = mDeviceInfoResult.rows;
                        Logger.i("mDeviceInfoResult = " + devices.toString());
                        deviceList.clear();
                        deviceList.addAll(devices);
                        if (devicesListAdapter != null) {
                            devicesListAdapter.notifyDataSetChanged();
                        }
                        addDeviceMapsSDK(deviceList);
                    }
                });
            }

            @Override
            public void onRequestFirstPageInfoFail(int code, String errMsg) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initLoacation();
        getBaseInfos();
    }

    public void addDeviceMapsSDK(List<Rows> deviceList) {
        if (null != deviceList && deviceList.size() > 0) {
            Logger.e(TAG, "deviceList=" + deviceList.toString());

            default_text.setVisibility(View.GONE);
            SharedPreferencesUtils.putObject(getActivity(), SharedPreferencesUtils.KEY_DEVICE_LIST, deviceList);
            if (SlidingMenuMainActivity.xltDeviceMaps != null) {
                SlidingMenuMainActivity.xltDeviceMaps.clear();
            }
            for (int i = 0; i < deviceList.size(); i++) {
                // Initialize SmartDevice SDK
                xltDevice m_XltDevice = new xltDevice();
                m_XltDevice.Init(getActivity());
                if (deviceList.get(i).devicenodes != null) {
                    for (int lv_idx = 0; lv_idx < deviceList.get(i).devicenodes.size(); lv_idx++) {
                        m_XltDevice.addNodeToDeviceList(deviceList.get(i).devicenodes.get(lv_idx).nodeno, xltDevice.DEFAULT_DEVICE_TYPE, deviceList.get(i).devicenodes.get(lv_idx).devicenodename);
                        deviceList.get(i).devicenodes.get(lv_idx).coreid = deviceList.get(i).coreid;
                    }
                }
                if (deviceList.get(i).coreid != null) {
                    // Connect to Controller
                    boolean isControlConnect = m_XltDevice.Connect(deviceList.get(i).coreid);
                    Logger.e(TAG, "isControlConnect=" + isControlConnect);

                    SlidingMenuMainActivity.xltDeviceMaps.put(deviceList.get(i).coreid, m_XltDevice);
                }
                if (deviceList.get(i).maindevice == 1) {//主设备
                    SlidingMenuMainActivity.m_mainDevice = m_XltDevice;
                }
            }
            devicenodes.clear();
            for (int i = 0; i < deviceList.size(); i++) {
                if (deviceList.get(i).devicenodes != null) {
                    devicenodes.addAll(deviceList.get(i).devicenodes);
                }
            }
            devicesListAdapter = new DevicesMainListAdapter(getContext(), devicenodes);
            devicesRecyclerView.setAdapter(devicesListAdapter);
            devicesListAdapter.setOnSwitchStateChangeListener(new DevicesMainListAdapter.OnSwitchStateChangeListener() {
                @Override
                public void onLongClick(int position) {
                    showDeleteSceneDialog(position);
                }

                @Override
                public void onSwitchChange(int position, boolean checked) {

                }
            });
            devicesListAdapter.notifyDataSetChanged();
            if (null != deviceList && deviceList.size() > 0) {
                default_text.setVisibility(View.GONE);
            } else {
                default_text.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 设备开关,调用SDK
     *
     * @param deviceInfo
     */
    private void switchLight(boolean checked, Rows deviceInfo) {

        //TODO 测试sdk 这里的id 需要确定一下。 deviceList.get(mPositon).id 这里的id代表什么意思。
        SlidingMenuMainActivity.m_mainDevice.setDeviceID(deviceInfo.id);
        SlidingMenuMainActivity.m_mainDevice.PowerSwitch(checked ? xltDevice.STATE_ON : xltDevice.STATE_OFF);

//        String param = "{\"ison\":" + (checked ? 1 : 0) + "}";
//
//        HttpUtils.getInstance().putRequestInfo(NetConfig.URL_LAMP_SWITCH + deviceInfo.id + "/onoff?access_token=" + UserUtils.getUserInfo(getActivity()).getAccess_token(), param, null, new HttpUtils.OnHttpRequestCallBack() {
//            @Override
//            public void onHttpRequestSuccess(Object result) {
//                Logger.i("开关结果 = " + result.toString());
//            }
//
//            @Override
//            public void onHttpRequestFail(int code, String errMsg) {
//                Logger.i("开关结果失败 = " + errMsg);
//            }
//        });
    }


    /**
     * 弹出解绑设备确认框
     */
    private void showDeleteSceneDialog(final int position) {
        AlertDialog mAlertDialog = new AlertDialog.Builder(getActivity()).setTitle(getString(R.string.unbind_device_tap))
                .setMessage(getString(R.string.sure_unbind_device))
                .setPositiveButton(getString(R.string.queding), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        unBindDevice(position);
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();

        Button btn1 = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        btn1.setTextColor(getResources().getColor(R.color.colorPrimary));
        Button btn2 = mAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        btn2.setTextColor(getResources().getColor(R.color.colorPrimary));

    }

    /**
     * 解绑设备
     *
     * @param position
     */
    private void unBindDevice(final int position) {
        if (!NetworkUtils.isNetworkAvaliable(getActivity())) {
            ToastUtil.showToast(getActivity(), R.string.net_error);
            return;
        }

        ((SlidingMenuMainActivity) getActivity()).showProgressDialog(getString(R.string.setting));

        RequestUnBindDevice.getInstance().unBindDevice(getActivity(), "" + devicenodes.get(position).id, new CommentRequstCallback() {
            @Override
            public void onCommentRequstCallbackSuccess() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((SlidingMenuMainActivity) getActivity()).cancelProgressDialog();
                        ToastUtil.showToast(getActivity(), getString(R.string.unbind_sucess));
                        updateUnbindList(position);
                    }
                });
            }

            @Override
            public void onCommentRequstCallbackFail(int code, final String errMsg) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((SlidingMenuMainActivity) getActivity()).cancelProgressDialog();
                        ToastUtil.showToast(getActivity(), getString(R.string.unbind_fail) + errMsg);
                    }
                });
            }
        });
    }

    /**
     * 解绑后更新页面
     *
     * @param position
     */
    private void updateUnbindList(int position) {
        devicenodes.remove(position);
        devicesListAdapter.notifyDataSetChanged();
    }

    /**
     * TODO  变更住设备，每次变更主设备后需要再次同步一下
     */
    public void changeMainDeviceInfo() {
    }


    /**
     * 位置信息
     */
    public static String city = "";
    public static String country = "";
    public static double mLongitude = -80.5204;
    public static double mLatitude = 43.4643;
    private LocationClient mLocationClient;

    /**
     * 初始化百度地图
     */
    private void initLoacation() {
        //声明LocationClient类
        mLocationClient = new LocationClient(getContext().getApplicationContext());
        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                    country = location.getCountry();
                    city = location.getCity();
                    mLongitude = location.getLongitude();
                    mLatitude = location.getLatitude();

                    Logger.i("country = " + country + ", city = " + city);
                    if (!TextUtils.isEmpty(country) || !TextUtils.isEmpty(city)) {
                        mLocationClient.stop();
                    }
                    updateLocationInfo();

                }
            }

            @Override
            public void onConnectHotSpotMessage(String s, int i) {

            }
        });    //注册监听函数
        mLocationClient.setLocOption(BaiduMapUtils.getLocationClientOption());
        mLocationClient.start();
    }

    private void updateLocationInfo() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(city)) {
                    txtLocation.setText(city);
                } else {
                    if (country == null) {
                        country = "";
                    }
                    txtLocation.setText("" + country);
                }
                getTitleInfo();
            }
        });
    }

}
