package com.justCopyBt.slide;

import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 淘宝selenium滑块操作公共类
 * @date 2019/07/05
 */

public class TaobaoSeleniumSlideHelpUtils {


    private static Random random = new Random();

    protected static final Logger logger = LoggerFactory.getLogger(TaobaoSeleniumSlideHelpUtils.class);

    private static String loginJs = "";

    static {
        try {
            loginJs = FileUtils.readFileByClassPath("taobao.js");
        } catch (Exception ex) {
            logger.info("load taobao login js error", ex);
        }
    }

    /**
     * 淘宝的需要滑动的距离，是固定的
     */
    private static int SLIDE_DISTANCE = 256;

    private static List<TaoBaoResource> clickResourceList = new ArrayList<TaoBaoResource>() {
        {
            add(TaoBaoResourceUtils.CLICK_LOGIN_ADLINK_DIV);
            add(TaoBaoResourceUtils.CLICK_LOGIN_FORM);
            add(TaoBaoResourceUtils.CLICK_LOGIN_NAME_LABEL);
            add(TaoBaoResourceUtils.CLICK_LOGIN_NEWBG_DIV);
            add(TaoBaoResourceUtils.CLICK_PASSWORD_LABEL);
        }
    };

    private static final String SLIDE_BY_JS = "slide_by_js";
    private static final String SLIDE_BY_DRIVER = "slide_by_driver";

    public static Map<Integer, String> slideByJsTrailMap = new HashMap<Integer, String>() {{
        put(9, "[[1,0,399],[3,-1,6],[4,-1,7],[3,-1,7],[5,-1,7],[6,-1,7],[5,-1,7],[6,-1,6],[6,0,7],[5,0,6],[3,0,7],[3,0,5],[6,0,8],[3,0,6],[4,0,7],[3,0,8],[4,0,7],[4,1,7],[5,0,7],[5,1,6],[7,0,7],[7,1,8],[9,0,6],[8,1,4],[8,1,7],[8,0,7],[8,1,7],[7,1,7],[5,1,7],[5,0,7],[4,0,7],[3,1,7],[2,0,7],[2,0,7],[2,0,5],[3,1,7],[3,0,7],[2,0,7],[3,0,7],[3,0,7],[4,0,7],[5,0,7],[4,0,7],[4,0,6],[6,-1,7],[5,-1,7],[4,-1,5],[6,-1,7],[4,-1,7],[3,-1,7],[4,-1,7],[3,0,7],[2,0,7],[2,-1,7],[2,0,7],[2,0,7],[3,0,8],[3,0,4],[2,0,7],[3,0,7],[3,0,7],[3,0,7],[3,-1,7],[3,-1,7],[5,-1,7],[3,0,7],[3,-1,7],[3,-1,7],[3,0,5],[3,-1,6],[2,0,9],[1,0,13],[1,0,125],[0,1,7],[1,0,7]]");
    }};

    /**
     * 滑动比例
     */
    private static Map<String, Integer> slideTypeMap = new HashMap<String, Integer>() {{
        put(SLIDE_BY_JS, 100);
        put(SLIDE_BY_DRIVER, 0);
    }};

    /**
     * 随机获取滑动方式
     *
     * @return
     */
    public static String randomSlideType() {
        Integer randomResult = random.nextInt(100);
        String slideType = "";
        Integer currentPercent = 0;
        for (Map.Entry<String, Integer> entry : slideTypeMap.entrySet()) {
            currentPercent += entry.getValue();
            if (randomResult < currentPercent) {
                slideType = entry.getKey();
                break;
            }
        }
        return slideType;
    }

    public static SlideTrailRecord getSlideTrailRecord(){
        SlideTrailRecord slideTrailRecord = new SlideTrailRecord();
        Integer currentIndex = randomSlideTrailIndex();
        slideTrailRecord.setId(currentIndex);
        slideTrailRecord.setSlideTrail(slideByJsTrailMap.get(currentIndex));
        return slideTrailRecord;
    }

    /**
     * 获取轨迹的index
     *
     * @return
     */
    public static Integer randomSlideTrailIndex() {
        List<Integer> trailIndexList = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : TaobaoSeleniumSlideHelpUtils.slideByJsTrailMap.entrySet()) {
            trailIndexList.add(entry.getKey());
        }
        return trailIndexList.get(random.nextInt(trailIndexList.size()));
    }

    /**
     * 随机乱移动鼠标
     *
     * @param remoteWebDriver
     */
    public static void randomClickElement(RemoteWebDriver remoteWebDriver) {
        TaoBaoResource taoBaoResource = clickResourceList.get(random.nextInt(clickResourceList.size()));
        try {
            Actions actions = new Actions(remoteWebDriver);
            WebElement webElement = TaoBaoSeleniumUtils.waitElement(remoteWebDriver, taoBaoResource);
            actions.moveToElement(webElement).perform();
            actions.release();
        } catch (Exception ex) {
            logger.info("随机移动鼠标到元素异常, resource:{}", taoBaoResource.toString(), ex);
        }
    }

    /**
     * 滑动验证
     *
     * @param remoteWebDriver
     * @return
     */
    public static boolean slideCheck(RemoteWebDriver remoteWebDriver, String slideType, String slideTrail) {
        logger.info("开始滑动, slide_type:{}", slideType);
        long startTime = System.currentTimeMillis();
        remoteWebDriver.executeScript("window.scrollTo(document.body.scrollWidth, 0)");
        if (SLIDE_BY_JS.equals(slideType)) {
            logger.info("开始通过JS滑动, slide_trail:{}", slideTrail);
            TaobaoSeleniumSlideHelpUtils.slideByJs(remoteWebDriver, slideTrail);
        } else {
            TaobaoSeleniumSlideHelpUtils.slideByDriver(remoteWebDriver, slideTrail);
        }

        logger.info("执行滑动轨迹完成, slide_type:{}, time_span:{}", slideType, (System.currentTimeMillis() - startTime));
        boolean slideResult = false;
        logger.info("开始检测滑动结果, slide_type:{}", slideType);
        TaoBaoResource sliderSuccess = TaoBaoSeleniumUtils.getExistElement(remoteWebDriver, TaoBaoResourceUtils.LOGIN_SLIDE_SUCCESS.getWaitSeconds(),
                //滑动成功
                TaoBaoResourceUtils.LOGIN_SLIDE_SUCCESS,
                //滑动失败
                TaoBaoResourceUtils.LOGIN_SLIDE_FAIL
        );
        if (sliderSuccess != null && sliderSuccess.equals(TaoBaoResourceUtils.LOGIN_SLIDE_SUCCESS)) {
            slideResult = true;
        }
        String logTips = slideResult ? "成功" : "失败";
        logger.info("滑动验证{}, slide_type:{}", logTips, slideType);
        return slideResult;
    }

    /**
     * 根据Js执行滑动
     *
     * @param remoteWebDriver
     */
    private static void slideByJs(RemoteWebDriver remoteWebDriver, String slideTrailStr) {
        String slideJs = loginJs.replace("MOVE_LIST_FORMAT", slideTrailStr);
        remoteWebDriver.executeScript(slideJs); // 执行登陆js
        CommonUtil.sleep(800);
    }

    private static void slideByDriver(RemoteWebDriver remoteWebDriver, String slideTrail) {
        Actions actions = new Actions(remoteWebDriver);
        WebElement sliderButton = TaoBaoSeleniumUtils.waitElementUntil(remoteWebDriver, TaoBaoResource.LOGIN_SLIDE_BUTTON);
        actions.moveToElement(sliderButton).perform();
        actions.clickAndHold().perform();
        //生成滑动轨迹
        List<Object[]> slideMoveObjects;
        if (SLIDE_BY_DRIVER.equals(slideTrail)) {
            slideMoveObjects = TaobaoSeleniumSlideHelpUtils.getMoveDistance(SLIDE_DISTANCE);
        } else {
            slideMoveObjects = TaobaoSeleniumSlideHelpUtils.getMoveDistance(SLIDE_DISTANCE);
        }
        logger.info("生成滑动轨迹成功 开始执行滑动轨迹, slide_type:slide_by_driver, slide:{}", JSONArray.fromObject(slideMoveObjects).toString());
        //执行滑动操作
        TaobaoSeleniumSlideHelpUtils.slide(actions, slideMoveObjects);
        actions.release();
    }

    /**
     * 获取提交的滑动轨迹
     * 1、按住
     * 2、一口气直接拖到底
     * 3、拖回来一些
     *
     * @param slideDistance
     * @return
     */
    private static List<Object[]> getMoveDistance(Integer slideDistance) {
        List<Object[]> moveDistanceList = new ArrayList<>();
        //第一次移动坐标点
        int moveX = 0, moveY = 0, sleepTime = 120;
        //按住
        moveDistanceList.add(createMove(moveX, moveY, sleepTime));

        //拖到底，还需要拖出去一些
        moveX = slideDistance + random.nextInt(5) + 2;
        moveY = random.nextInt(10) + 2;
        sleepTime = random.nextInt(30) + 50;
        moveDistanceList.add(createMove(moveX, moveY, sleepTime));

        //拖回来一些， 这个一些可以在30-300 之间随机
        moveX = 20 + random.nextInt(slideDistance);
        moveY = 5 - random.nextInt(10);
        sleepTime = random.nextInt(100) + 150;
        moveDistanceList.add(createMove(-moveX, moveY, sleepTime));
        return moveDistanceList;
    }

    /**
     * 进行滑动操作
     *
     * @param actions
     */
    private static void slide(Actions actions, List<Object[]> slideMoveObjects) {
        for (Object[] moveObjects : slideMoveObjects) {
            Integer moveX = Integer.parseInt(String.valueOf(moveObjects[0]));
            Integer moveY = Integer.parseInt(String.valueOf(moveObjects[1]));
            Long sleepTime = Long.parseLong(String.valueOf(moveObjects[2]));
            logger.info("{}, 执行滑动, point:[{},{}], sleep_time:{}", moveX, moveY, sleepTime);
            actions.moveByOffset(moveX, moveY).perform();
            CommonUtil.sleep(sleepTime);
        }
    }

    /**
     * 判断是否需要进行滑块验证
     * String styleStr = sliderGround.getAttribute("style");
     * if (styleStr.contains("block")) {
     * haveSlideCheck = true;
     * }
     *
     * @param remoteWebDriver
     * @return
     */
    public static boolean haveSlideCheck(RemoteWebDriver remoteWebDriver) {
        boolean haveSlideCheck = false;
        WebElement sliderGround = TaoBaoSeleniumUtils.waitElementUntil(remoteWebDriver, TaoBaoResourceUtils.LOGIN_SLIDE_NEED);
        if (sliderGround != null) {
            String slideText = sliderGround.getText();
            if (StringUtils.isNotBlank(slideText) && slideText.contains(TaoBaoResourceUtils.LOGIN_SLIDE_NEED.getExpectDesc())) {
                haveSlideCheck = true;
            }
        }
        return haveSlideCheck;
    }

    /**
     * 创建移动距离对象
     * @param X
     * @param Y
     * @param time
     * @return
     */
    public static Object[] createMove(int X,int Y,long time){
        return new Object[]{X,Y,time};
    }
}
