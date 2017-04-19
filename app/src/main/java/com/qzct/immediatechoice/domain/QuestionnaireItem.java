package com.qzct.immediatechoice.domain;

/**
 * Created by tsh2 on 2017/4/19.
 */

public class QuestionnaireItem {
    String title;
    String a;
    String b;
    String c;
    String d;

    public String getTitle() {
        return title;
    }

    public String getA() {
        return a;
    }

    public String getB() {
        return b;
    }

    public String getC() {
        return c;
    }

    public String getD() {
        return d;
    }

    public QuestionnaireItem(String title, String a, String b, String c, String d) {
        this.title = title;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }
}
