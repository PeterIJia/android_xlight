package com.umarbhutta.xlightcompanion.okHttp.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by guangbinw on 2017/3/17.
 * 创建规则
 */

public class Rules implements Serializable{
    public String rulename;
    public int relationtype;
    public int type;
    public int status;
    public int userId;
    public List<Rulecondition> rulecondition;
    public List<Ruleresult> ruleresult;

    @Override
    public String toString() {
        return "Rules{" +
                "rulename='" + rulename + '\'' +
                ", relationtype=" + relationtype +
                ", type=" + type +
                ", status=" + status +
                ", userId=" + userId +
                ", rulecondition=" + rulecondition +
                ", ruleresult=" + ruleresult +
                '}';
    }
}
