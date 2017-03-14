package com.umarbhutta.xlightcompanion.okHttp.requests;

import android.content.Context;

import com.umarbhutta.xlightcompanion.Tools.UserUtils;
import com.umarbhutta.xlightcompanion.okHttp.HttpUtils;
import com.umarbhutta.xlightcompanion.okHttp.NetConfig;
import com.umarbhutta.xlightcompanion.okHttp.requests.imp.CommentRequstCallback;

/**
 * Created by guangbinw on 2017/3/14.
 * 解绑设备接口
 */
public class RequestUnBindDevice implements HttpUtils.OnHttpRequestCallBack {

    private Context context;
    private CommentRequstCallback mCommentRequstCallback;

    public static RequestUnBindDevice getInstance() {
        return new RequestUnBindDevice();
    }

    /**
     */
    public void unBindDevice(Context context, String deviceId, CommentRequstCallback mCommentRequstCallback) {
        this.context = context;
        this.mCommentRequstCallback = mCommentRequstCallback;
        if (UserUtils.isLogin(context))
            HttpUtils.getInstance().putRequestInfo(NetConfig.URL_UNBIND_DEVICE + deviceId + "/unbind?access_token=" + UserUtils.getUserInfo(context).getAccess_token(),
                    null, null, this);
    }


    @Override
    public void onHttpRequestSuccess(Object result) {
        if (null != mCommentRequstCallback) {
            mCommentRequstCallback.onCommentRequstCallbackSuccess();
        }
    }

    @Override
    public void onHttpRequestFail(int code, String errMsg) {
        if (null != mCommentRequstCallback) {
            mCommentRequstCallback.onCommentRequstCallbackFail(code, errMsg);
        }
    }


}
