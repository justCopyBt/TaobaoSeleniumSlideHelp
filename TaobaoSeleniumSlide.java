package com.justCopyBt.slide;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 淘宝selenium滑块入口
 * @date 2019/07/05
 */

public class TaobaoSeleniumSlide {

    private static String LOG_PREFIX = "TAOBAO SELENIUM PC ACCOUNT LOGIN SLIDE";

    //不需要滑块
    private static final ErrorCode TAOBAO_LOGIN_NOT_NEED_SLIDE = new ErrorCode(200001, "不需要滑块");
    //滑动成功
    private static final ErrorCode TAOBAO_LOGIN_SLIDE_SUCCESS = new ErrorCode(200002, "滑动成功");


    private static final Logger logger = LoggerFactory.getLogger(TaobaoSeleniumSlide.class);


    /**
     * 检测是否存在滑动验证码并且提交
     *
     * @param remoteWebDriver
     * @return
     */
    private ErrorCode checkHaveSlideAndSubmit(RemoteWebDriver remoteWebDriver) {

        //先随机获取一下滑动验证使用的方式
        String slideType = TaobaoSeleniumSlideHelpUtils.randomSlideType();
        SlideTrailRecord slideTrailRecord = TaobaoSeleniumSlideHelpUtils.getSlideTrailRecord();
        if (slideTrailRecord == null) {
            logger.info("{}, 查询滑动验证轨迹出错", LOG_PREFIX);
            return ErrorCode.TAOBAO_SELENIUM_FAILED;
        }
        //判断是否出现了滑动验证，并且需要进行滑动通过，如果不通过，直接提示失败
        ErrorCode returnErrorCode = TAOBAO_LOGIN_NOT_NEED_SLIDE;
        if (TaobaoSeleniumSlideHelpUtils.haveSlideCheck(remoteWebDriver)) {
            //随机乱移动鼠标
            TaobaoSeleniumSlideHelpUtils.randomClickElement(remoteWebDriver);
            boolean slideResult = TaobaoSeleniumSlideHelpUtils.slideCheck(remoteWebDriver, slideType, slideTrailRecord.getSlideTrail());
            //滑动失败！！！
            returnErrorCode = slideResult ? TAOBAO_LOGIN_SLIDE_SUCCESS : ErrorCode.TAOBAO_LOGIN_SLIDE_FAIL;
        }
        return returnErrorCode;
    }

}
