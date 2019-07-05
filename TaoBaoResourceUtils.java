package com.justCopyBt.slide;
/**
 * 淘宝selenium滑块元素操作辅助类
 * @date 2019/07/05
 */
public class TaoBaoResourceUtils {

    //随机乱点的元素
    /**
     * 登录FORM
     */
    public static final  TaoBaoResource CLICK_LOGIN_FORM = TaoBaoResource.Builder.Builder().setId("J_Form").setWaitSeconds(1).build();
    /**
     * 登录名的label
     */
    public static final  TaoBaoResource CLICK_LOGIN_NAME_LABEL = TaoBaoResource.Builder.Builder().setCssSelector(".username-field>label").setWaitSeconds(1).build();
    /**
     * 密码的label
     */
    public static final  TaoBaoResource CLICK_PASSWORD_LABEL = TaoBaoResource.Builder.Builder().setCssSelector(".pwd-field>label").setWaitSeconds(1).build();
    /**
     * 登录页的图片
     */
    public static final  TaoBaoResource CLICK_LOGIN_ADLINK_DIV = TaoBaoResource.Builder.Builder().setClassName("login-adlink").setWaitSeconds(1).build();
    /**
     * 登录页的背景
     */
    public static final  TaoBaoResource CLICK_LOGIN_NEWBG_DIV = TaoBaoResource.Builder.Builder().setClassName("login-newbg").setWaitSeconds(1).build();

    /**
     * 验证通过
     */
    public static final  TaoBaoResource LOGIN_SLIDE_NEED = TaoBaoResource.Builder.Builder().setClassName("nc-lang-cnt").setExpectDesc("请按住滑块").setWaitSeconds(5).build();

    /**
     * 滑动块儿按钮
     */
    public static final  TaoBaoResource LOGIN_SLIDE_BUTTON = TaoBaoResource.Builder.Builder().setId("nc_1_n1z").setWaitSeconds(15).build();

    /**
     * 验证通过
     */
    public static final  TaoBaoResource LOGIN_SLIDE_SUCCESS = TaoBaoResource.Builder.Builder().setId("nocaptcha").setExpectDesc("验证通过").setWaitSeconds(5).build();
    /**
     * 验证失败
     */
    public static final  TaoBaoResource LOGIN_SLIDE_FAIL = TaoBaoResource.Builder.Builder().setId("nocaptcha").setExpectDesc("出错了").setWaitSeconds(5).build();

}
