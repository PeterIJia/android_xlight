package com.umarbhutta.xlightcompanion.control;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.SDK.xltDevice;
import com.umarbhutta.xlightcompanion.Tools.DataReceiver;
import com.umarbhutta.xlightcompanion.Tools.Logger;
import com.umarbhutta.xlightcompanion.Tools.ToastUtil;
import com.umarbhutta.xlightcompanion.control.adapter.ControlResultListAdapter;
import com.umarbhutta.xlightcompanion.control.adapter.DeviceRulesListAdapter;
import com.umarbhutta.xlightcompanion.main.MainActivity;
import com.umarbhutta.xlightcompanion.okHttp.model.DeviceGetRules;
import com.umarbhutta.xlightcompanion.okHttp.model.DeviceInfoResult;
import com.umarbhutta.xlightcompanion.okHttp.requests.RequestDeleteRuleDevice;
import com.umarbhutta.xlightcompanion.okHttp.requests.RequestDeviceRulesInfo;
import com.umarbhutta.xlightcompanion.okHttp.requests.imp.CommentRequstCallback;

/**
 * Created by Umar Bhutta.
 * 新的规则页面
 */
public class ControlRuleFragment extends Fragment {
    private static final String TAG = ControlRuleFragment.class.getSimpleName();

    private Handler m_handlerGlance;

    private class MyStatusRuleReceiver extends DataReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
//            roomTemp.setText(MainActivity.m_mainDevice.m_Data.m_RoomTemp + "\u00B0");
//            roomHumidity.setText(MainActivity.m_mainDevice.m_Data.m_RoomHumidity + "\u0025");
        }
    }

    @Override
    public void onDestroyView() {
        rulesRecyclerView.setAdapter(null);
        MainActivity.m_mainDevice.removeDataEventHandler(m_handlerGlance);
        if (MainActivity.m_mainDevice.getEnableEventBroadcast()) {
            getContext().unregisterReceiver(m_DataReceiver);
        }
        super.onDestroyView();
    }

    private ListView rulesRecyclerView;
    private final MyStatusRuleReceiver m_DataReceiver = new MyStatusRuleReceiver();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_control_rule, container, false);
        rulesRecyclerView = (ListView) view.findViewById(R.id.rulesRecyclerView);

        rulesRecyclerView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position,final long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("删除规则提示");
                builder.setMessage("确定删除此规则吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RequestDeleteRuleDevice.getInstance().deleteRule(getActivity(),mDeviceInfoResult.rows.get(position).id+"", new CommentRequstCallback() {
                            @Override
                            public void onCommentRequstCallbackSuccess() {
                                if(devicesListAdapter!=null){
                                    devicesListAdapter.notifyDataSetChanged();
                                }
                                ToastUtil.showToast(getActivity(),"删除成功"+position);
                            }

                            @Override
                            public void onCommentRequstCallbackFail(int code, String errMsg) {
                                ToastUtil.showToast(getActivity(),"删除失败"+errMsg);
                            }
                        });
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
                return true;
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getControlRuleList();
    }

    public DeviceGetRules mDeviceInfoResult;

    private void getControlRuleList() {
        RequestDeviceRulesInfo.getInstance().getRules(getActivity(), new RequestDeviceRulesInfo.OnRequestFirstPageInfoCallback() {

            @Override
            public void onRequestFirstPageInfoSuccess(final DeviceGetRules deviceInfoResult) {
                try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDeviceInfoResult = deviceInfoResult;
                            Logger.e(TAG, mDeviceInfoResult.toString());
                            if (mDeviceInfoResult.code == 0) {
                                initList();
                            } else if (mDeviceInfoResult.code == 1) {
//                                ToastUtil.showToast(getActivity(), "数据为空");
                            } else {
                                ToastUtil.showToast(getActivity(), mDeviceInfoResult.msg + "");
                            }

                        }
                    });
                } catch (NullPointerException e) {
                    Log.e(TAG, "Exception caught: " + e);
                }
            }

            @Override
            public void onRequestFirstPageInfoFail(int code, final String errMsg) {
                if(getActivity()!=null){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showToast(getActivity(), errMsg);
                        }
                    });
                }
            }
        });
    }
private  DeviceRulesListAdapter devicesListAdapter;
    private void initList() {
        devicesListAdapter = new DeviceRulesListAdapter(getContext(), mDeviceInfoResult);
        rulesRecyclerView.setAdapter(devicesListAdapter);
        if(devicesListAdapter!=null){
            devicesListAdapter.notifyDataSetChanged();
        }
    }
}
