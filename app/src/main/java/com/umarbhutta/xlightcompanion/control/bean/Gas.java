package com.umarbhutta.xlightcompanion.control.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/24.
 */

public class Gas implements Serializable {
    public String name;
    public int value;

    @Override
    public String toString() {
        return "Gas{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
