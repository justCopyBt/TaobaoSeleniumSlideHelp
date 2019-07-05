package com.justCopyBt.slide;

import java.io.Serializable;

public class ErrorCode implements Serializable {
    private static final long serialVersionUID = 1263178717333346398L;

    /***** 状态码 *****/
    private int code;

    /***** 异常信息 *****/
    private String msg;

    public ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static final ErrorCode TAOBAO_SELENIUM_FAILED = new ErrorCode(100000, "初始化失败，请重新登录");//初始化driver失败

    public static final ErrorCode TAOBAO_LOGIN_SLIDE_FAIL = new ErrorCode(100002, "尝试登录失败，请稍候重试");

}