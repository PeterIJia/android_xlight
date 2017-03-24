package com.umarbhutta.xlightcompanion.okHttp;

/**
 * Created by guangbinw on 2017/3/12.
 */

public class NetConfig {

    /**
     * 正式版开关
     */
    public static final boolean isDebug = true;

    /**
     * 数据解析错误
     */
    public static final int ERROR_PARSE = -100;

    /**
     * 网络异常
     */
    public static final int ERROR_NET_ERROT = -101;


    public static final String ERROR_PARSE_MSG = "数据解析异常";

    /**
     * 服务器地址
     */
    public static final String SERVER_ADDRESS = "http://123.207.166.211:8080";

    /**
     * 登录
     */
    public static final String URL_LOGIN = "http://123.207.166.211:8080/users/login";
    /**
     * 注册
     */
    public static final String URL_REGISTER = "http://123.207.166.211:8080/users";


    /**
     * 首页基本信息
     */
    public static final String URL_FIRST_PAGE_INFO = "http://123.207.166.211:8080/devices/?access_token=";
    /**
     * 修改密码
     */
    public static final String URL_MODIFY_PWD = "http://123.207.166.211:8080/users/2/resetpassword?access_token=";
    /**
     * 解绑设备
     */
    public static final String URL_UNBIND_DEVICE = "http://123.207.166.211:8080/devices/";
    /**
     * 设置主设备
     */
    public static final String URL_SET_MAIN_DEVICE = "http://123.207.166.211:8080/devices/";
    /**
     * 设备详细信息
     */
    public static final String URL_DEVICE_DETAIL_INFO = "http://123.207.166.211:8080/devices/";
    /**
     * 设备规则列表
     */
    public static final String URL_DEVICE_RULES_LIST = "http://123.207.166.211:8080/rules/?access_token=";

    /**
     * 删除规则
     */
    public static final String URL_DELETE_RULE = "http://123.207.166.211:8080/rules/";

    /**
     * 启用、禁用规则
     */
    public static final String URL_RULE_SWITCH = "http://123.207.166.211:8080/rules/";
    /**
     * 场景列表
     */
    public static final String URL_SCENE_LIST = "http://123.207.166.211:8080/scenarios/?access_token=";
    /**
     * 场景详细
     */
    public static final String URL_SCENE_DETAIL = "http://123.207.166.211:8080/scenarios/";
    /**
     * 添加场景
     */
    public static final String URL_ADD_SCENE = "http://123.207.166.211:8080/scenarios?access_token=";
    /**
     * 删除场景
     */
    public static final String URL_DELETE_SCENE = "http://123.207.166.211:8080/scenarios/";
    /**
     * 添加设备
     */
    public static final String URL_ADD_DEVICE = "http://123.207.166.211:8080/devices/?access_token=";
    /**
     * 忘记密码--发送验证码
     */
    public static final String URL_SEND_VERIFICATION_CODE = "http://123.207.166.211:8080/users/sendverificationcode";
    /**
     * 重置密码
     */
    public static final String URL_RESET_PWD = "http://123.207.166.211:8080/users/updatepassword";
    /**
     * 创建规则
     */
    public static final String URL_CREATE_RULES = "http://123.207.166.211:8080/rules?access_token=";
    /**
     * 修改个人信息
     */
    public static final String URL_MODIFY_USER_INFO = "http://123.207.166.211:8080/users/";
    /**
     * 上传照片
     */
    public static final String URL_UPLOAD_IMG = "http://123.207.166.211:8080/users/uploadimg?access_token=";
    /**
     * 注册协议帮助的url
     */
    public static final String URL_GET_REGISTER_URL = "http://123.207.166.211:8080/users/regagreement";
    /**
     * 获取帮助的url
     */
    public static final String URL_GET_HELP_URL = "http://123.207.166.211:8080/users/help";
    /**
     * 报表url
     */
    public static final String URL_GET_REPORT_FORM = "http://123.207.166.211:8080/users/report?access_token=";
    /**
     * 灯开关接口
     */
    public static final String URL_LAMP_SWITCH = "http://123.207.166.211:8080/devices/";
    /**
     * 启动条件列表及详细信息
     */
    public static final String URL_RULES_RULECONDITIONS = "http://123.207.166.211:8080/rules/ruleconditions";
    /**
     * 编辑场景
     */
    public static final String URL_EDIT_SCENE = "http://123.207.166.211:8080/scenarios/";
}
