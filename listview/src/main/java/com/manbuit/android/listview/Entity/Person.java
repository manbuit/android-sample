package com.manbuit.android.listview.Entity;

import java.util.Date;

/**
 * Created by MB on 2015/10/2.
 */
public class Person {
    String name;
    Date birthDay;

    public Person(String name, Date birthDay) {
        this.name = name;
        this.birthDay = birthDay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    @Override
    public String toString() {
        return String.format("姓名：%s，出生日期：%tF",this.name,this.birthDay);
    }
}
