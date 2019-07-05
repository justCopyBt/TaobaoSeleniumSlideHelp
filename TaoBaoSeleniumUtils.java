package com.justCopyBt.slide;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 淘宝selenium通用帮助类
 * 执行监测元素是否存在、点击、输入、滑动等操作
 * @date 2019/07/05
 */
public class TaoBaoSeleniumUtils {

    /**
     * 日志记录
     */
    private static final Logger logger = LoggerFactory.getLogger(TaoBaoSeleniumUtils.class);
    /**
     * 1秒的毫秒数
     */
    private static Integer MILLISECEND = 1000;

    /**
     * 获取UA使用的随机
     */
    private static Random random = new Random();

    /**
     * 输入参数
     * 默认逐个输入
     *
     * @param action      当前活动页面
     * @param inputValue  要输入的值
     * @param sleepMillis 休眠时间，会随机休眠
     */
    public static void sendKeys(Actions action, String inputValue, int sleepMillis) {
        for (int i = 0; i < inputValue.length(); i++) {
            action.sendKeys(String.valueOf(inputValue.charAt(i))).perform();
            Util.sleep(random.nextInt(sleepMillis));
        }
    }

    /**
     * 等待某个存在的元素出现
     *
     * @param driver
     * @param taoBaoResources
     * @return
     */
    public static TaoBaoResource getExistElement(RemoteWebDriver driver, Integer waitSeconds, TaoBaoResource... taoBaoResources) {
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < waitSeconds * MILLISECEND) {
            //挨个监测是否存在
            for (TaoBaoResource taoBaoResource : taoBaoResources) {
                //通过url监测
                try {
                    if (StringUtils.isNotBlank(taoBaoResource.getUrl())) {
                        if (driver.getCurrentUrl().startsWith(taoBaoResource.getUrl())) {
                            return taoBaoResource;
                        }
                        continue;
                    }
                    List<WebElement> webElementsLists = getWebElements(driver, taoBaoResource);
                    //如果存在就直接返回
                    if (webElementsLists != null && webElementsLists.size() > 0) {
                        //判断元素的text是否为空
                        if (taoBaoResource.getTextBlank() != null) {
                            for (WebElement webElement : webElementsLists) {
                                if (StringUtils.isBlank(webElement.getText()) == taoBaoResource.getTextBlank()) {
                                    return taoBaoResource;
                                }
                            }
                            continue;
                        }
                        // 判断getExpectDesc是否为空，如果为空则continue
                        if (StringUtils.isNotBlank(taoBaoResource.getExpectDesc())) {
                            for (WebElement webElement : webElementsLists) {
                                if (webElement.getText().contains(taoBaoResource.getExpectDesc())) {
                                    return taoBaoResource;
                                }
                            }
                            continue;
                        }
                        return taoBaoResource;
                    }
                } catch (Exception ex) {
                    logger.warn("getExistElement exception,element:{},message:{}", taoBaoResource.toString(), ex.getMessage(), ex);
                }
            }
            CommonUtil.sleep(200);
        }
        return null;
    }

    /**
     * 等待元素出现并执行点击操作
     *
     * @param action
     * @param driver
     * @param taoBaoResource
     * @return
     */
    public static WebElement waitElementClick(Actions action, RemoteWebDriver driver, TaoBaoResource taoBaoResource) {
        WebElement webElement = waitElement(driver, taoBaoResource);
        if (null != webElement) {
            action.moveToElement(webElement).perform();
            action.click(webElement).perform();
        }
        return webElement;
    }

    /**
     * 等待元素出现并执行点击操作
     *
     * @param driver
     * @param taoBaoResource
     * @return
     */
    public static WebElement waitElementClick(RemoteWebDriver driver, TaoBaoResource taoBaoResource) {
        return waitElementClick(driver, taoBaoResource, taoBaoResource.getWaitSeconds());
    }

    /**
     * 等待元素出现并执行点击操作
     *
     * @param driver
     * @param taoBaoResource
     * @return
     */
    public static WebElement waitElementClick(RemoteWebDriver driver, TaoBaoResource taoBaoResource, Integer waitSeconds) {
        WebElement webElement = waitElement(driver, taoBaoResource, waitSeconds);
        if (null != webElement) {
            webElement.click();
        }
        return webElement;
    }

    /**
     * driver在超时时间内等待某元素加载出现
     *
     * @param driver
     * @return 找到则返回元素节点，未找到则抛出相关异常
     */
    public static WebElement waitElement(RemoteWebDriver driver, TaoBaoResource taobaoResource) {
        return waitElement(driver, taobaoResource, taobaoResource.getWaitSeconds());
    }

    /**
     * driver在超时时间内等待某元素加载出现
     *
     * @param driver
     * @return 找到则返回元素节点，未找到则抛出相关异常
     */
    public static WebElement waitElement(RemoteWebDriver driver, TaoBaoResource taobaoResource, Integer waitSeconds) {
        List<WebElement> webElementsLists = waitElements(driver, taobaoResource, waitSeconds);
        if (webElementsLists != null && webElementsLists.size() > 0) {
            return webElementsLists.get(0);
        }
        return null;
    }

    /**
     * driver在超时时间内等待某元素加载出现
     *
     * @param driver
     * @return 找到则返回元素节点，未找到则抛出相关异常
     */
    public static WebElement waitElementUntilEnableClick(RemoteWebDriver driver, TaoBaoResource taobaoResource) {
        WebElement webElement = waitElementUntil(driver, taobaoResource);
        if (webElement != null) {
            if (!webElement.isEnabled()) {
                CommonUtil.sleep(3000);
                webElement = waitElementUntilEnableClick(driver, taobaoResource);
            } else {
                webElement.click();
            }
        }
        return webElement;
    }

    /**
     * driver在超时时间内等待某元素加载出现
     *
     * @param driver
     * @return 找到则返回元素节点，未找到则抛出相关异常
     */
    public static WebElement waitElementUntilClick(RemoteWebDriver driver, TaoBaoResource taobaoResource) {
        WebElement webElement = waitElementUntil(driver, taobaoResource);
        if (webElement != null) {
            webElement.click();
        }
        return webElement;
    }

    /**
     * driver在超时时间内等待某元素加载出现
     *
     * @param driver
     * @return 找到则返回元素节点，未找到则抛出相关异常
     */
    public static WebElement waitElementUntilClick(Actions actions, RemoteWebDriver driver, TaoBaoResource taobaoResource) {
        WebElement webElement = waitElementUntil(driver, taobaoResource);
        if (webElement != null) {
            actions.moveToElement(webElement).perform();
            actions.click(webElement).perform();
        }
        return webElement;
    }

    /**
     * driver在超时时间内等待某元素加载出现
     *
     * @param driver
     * @return 找到则返回元素节点，未找到则抛出相关异常
     */
    public static WebElement waitElementUntil(RemoteWebDriver driver, TaoBaoResource taobaoResource) {
        return waitElementUntil(driver, taobaoResource, taobaoResource.getWaitSeconds());
    }

    /**
     * driver在超时时间内等待某元素加载出现
     *
     * @param driver
     * @return 找到则返回元素节点，未找到则抛出相关异常
     */
    public static WebElement waitElementUntil(RemoteWebDriver driver, TaoBaoResource taobaoResource, int waitSeconds) {
        try {
            By by = getBy(taobaoResource);
            if (by != null) {
                new WebDriverWait(driver, waitSeconds).until(
                        ExpectedConditions.presenceOfElementLocated(by));
                return driver.findElement(by);
            }
        } catch (Exception ex) {
        }
        return null;
    }

    /**
     * driver在超时时间内等待某元素加载出现
     *
     * @param driver
     * @return 找到则返回元素节点，未找到则抛出相关异常
     */
    public static List<WebElement> waitElements(RemoteWebDriver driver, TaoBaoResource taobaoResource) {
        return waitElements(driver, taobaoResource, taobaoResource.getWaitSeconds());
    }

    /**
     * driver在超时时间内等待某元素加载出现
     *
     * @param driver
     * @return 找到则返回元素节点，未找到则抛出相关异常
     */
    public static List<WebElement> waitElements(RemoteWebDriver driver, TaoBaoResource taobaoResource, Integer waitSeconds) {
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < waitSeconds * MILLISECEND) {
            List<WebElement> webElementsLists = getWebElements(driver, taobaoResource);
            if (webElementsLists != null && webElementsLists.size() > 0) {
                return webElementsLists;
            }
            CommonUtil.sleep(200);
        }
        return null;
    }

    /**
     * 根据选择方式获取元素
     *
     * @param driver
     * @param taobaoResource
     * @return
     */
    public static List<WebElement> getWebElements(RemoteWebDriver driver, TaoBaoResource taobaoResource) {
        try {
            By by = getBy(taobaoResource);
            if (by != null) {
                return driver.findElements(by);
            }
        } catch (Exception ex) {
        }
        return null;
    }

    /**
     * 获取BY
     *
     * @param taobaoResource
     * @return
     */
    public static By getBy(TaoBaoResource taobaoResource) {
        By by = null;
        if (StringUtils.isNotBlank(taobaoResource.getId())) {
            by = By.id(taobaoResource.getId());
        } else if (StringUtils.isNotBlank(taobaoResource.getName())) {
            by = By.name(taobaoResource.getName());
        } else if (StringUtils.isNotBlank(taobaoResource.getClassName())) {
            by = By.className(taobaoResource.getClassName());
        } else if (StringUtils.isNotBlank(taobaoResource.getXpath())) {
            by = By.xpath(taobaoResource.getXpath());
        } else if (StringUtils.isNotBlank(taobaoResource.getCssSelector())) {
            by = By.cssSelector(taobaoResource.getCssSelector());
        }
        return by;
    }

    /***
     * 等待页面加载完成
     * @param driver
     * @param times 次数
     * @return
     */
    public static boolean waitForDocumentReady(final RemoteWebDriver driver, int times) {
        Object o;
        for (int i = 1; i <= times; i++) {
            o = driver.executeScript("return window.document.readyState");
            if ("complete".equals(o)) {
                return true;
            }
            Util.sleep(1000);
        }
        return false;
    }

    /**
     * 后退
     *
     * @param remoteWebDriver
     */
    public static boolean back(RemoteWebDriver remoteWebDriver) {
        try {
            remoteWebDriver.navigate().back();
            return true;
        } catch (Exception ex) {
            logger.warn("没有监测兼容的错误提示:{}", ex.getMessage(), ex);
        }
        return false;
    }

    /**
     * 保存截图和当前源码
     *
     * @param remoteWebDriver
     * @return
     */
    public static String takeScreenShotAndSaveHBase(RemoteWebDriver remoteWebDriver) {
        return takeScreenShotAndSaveHBase(remoteWebDriver, true);
    }

    /**
     * 保存截图和当前源码
     *
     * @param remoteWebDriver
     * @return
     */
    public static String takeScreenShotAndSaveHBase(RemoteWebDriver remoteWebDriver, boolean isSaveScreen) {
        List<String> hbaseKeyList = new ArrayList<>();
        try {
            //保存源码
            String pageSource = remoteWebDriver.getPageSource();
            if(isSaveScreen) {
                //保存截图
                byte[] bytes = remoteWebDriver.getScreenshotAs(OutputType.BYTES);
                String base64 = Base64Util.encode(bytes);
            }
        } catch (Exception ex) {
            logger.warn("保存当前页面源码和截屏异常, message:{}", ex.getMessage());
        }
    }
}