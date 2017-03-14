package com.umarbhutta.xlightcompanion.okHttp.requests;

import android.content.Context;

import com.umarbhutta.xlightcompanion.Tools.Logger;
import com.umarbhutta.xlightcompanion.Tools.UserUtils;
import com.umarbhutta.xlightcompanion.okHttp.HttpUtils;
import com.umarbhutta.xlightcompanion.okHttp.NetConfig;
import com.umarbhutta.xlightcompanion.okHttp.model.DeviceInfoResult;
import com.umarbhutta.xlightcompanion.okHttp.model.LoginResult;

/**
 * Created by guangbinw on 2017/3/14.
 * 设备规则列表接口
 */
public class RequestDeviceRulesInfo implements HttpUtils.OnHttpRequestCallBack {
    private OnRequestFirstPageInfoCallback mOnRequestFirstPageInfoCallback;

    public static RequestDeviceRulesInfo getInstance() {
        return new RequestDeviceRulesInfo();
    }

    /**
     * 获取规则列表
     *
     * @param context
     */
    public void getRules(Context context, OnRequestFirstPageInfoCallback mOnRequestFirstPageInfoCallback) {
        this.mOnRequestFirstPageInfoCallback = mOnRequestFirstPageInfoCallback;
        LoginResult info = UserUtils.getUserInfo(context);
        HttpUtils.getInstance().getRequestInfo(NetConfig.URL_DEVICE_RULES_LIST + info.getAccess_token() + "&userId=" + info.getId(), DeviceInfoResult.class, this);
    }

    @Override
    public void onHttpRequestSuccess(Object result) {
        DeviceInfoResult info = (DeviceInfoResult) result;
        Logger.i("deviceInfo = " + info);
        if (null != mOnRequestFirstPageInfoCallback) {
            mOnRequestFirstPageInfoCallback.onRequestFirstPageInfoSuccess(info.data);
        }
    }

    @Override
    public void onHttpRequestFail(int code, String errMsg) {
        if (null != mOnRequestFirstPageInfoCallback) {
            mOnRequestFirstPageInfoCallback.onRequestFirstPageInfoFail(code, errMsg);
        }
    }

    public interface OnRequestFirstPageInfoCallback {
        void onRequestFirstPageInfoSuccess(DeviceInfoResult mDeviceInfoResult);

        void onRequestFirstPageInfoFail(int code, String errMsg);
    }


}
