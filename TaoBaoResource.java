package com.justCopyBt.slide;
import net.sf.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 淘宝SELENIUM资源类，方便处理
 * @date 2019/07/05
 */
public class TaoBaoResource {

    private String id;

    private String name;

    private String className;

    private String cssSelector;

    private String xpath;

    private String text;

    private String url;

    private Integer waitSeconds = 10;

    private String expectDesc;

    private Boolean textBlank;

    public Boolean getTextBlank() {
        return textBlank;
    }

    public void setTextBlank(Boolean textBlank) {
        this.textBlank = textBlank;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCssSelector() {
        return cssSelector;
    }

    public void setCssSelector(String cssSelector) {
        this.cssSelector = cssSelector;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getWaitSeconds() {
        return waitSeconds;
    }

    public void setWaitSeconds(Integer waitSeconds) {
        this.waitSeconds = waitSeconds;
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getExpectDesc() {
        return expectDesc;
    }

    public void setExpectDesc(String expectDesc) {
        this.expectDesc = expectDesc;
    }





    @Override
    public String toString(){
        Map<String,String> strMap = new HashMap<>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            String methodName = field.getName();
            try {
                Method method = this.getClass().getMethod("get" + methodName.substring(0, 1).toUpperCase() + methodName.substring(1, methodName.length()));
                Object value = method.invoke(this);
                if(value!=null){
                    strMap.put(methodName,String.valueOf(value));
                }
            }catch (Exception ex){

            }
        }
        return JSONObject.fromObject(strMap).toString();
    }

    public static final class Builder {

        private String id;

        private String className;

        private String name;

        private String url;

        private String cssSelector;

        private String xpath;

        private String text;

        private Integer waitSeconds = 10;

        private String expectDesc;

        private Boolean textBlank;

        private Builder() {
        }

        public static Builder Builder() {
            return new Builder();
        }

        /**
         * css id
         *
         * @param id
         * @return
         */
        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        /**
         * css name
         *
         * @param name
         * @return
         */
        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        /**
         * current url
         *
         * @param url
         * @return
         */
        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        /**
         * class_name
         *
         * @param className
         * @return
         */
        public Builder setClassName(String className) {
            this.className = className;
            return this;
        }

        /**
         * css_selector
         *
         * @param cssSelector
         * @return
         */
        public Builder setCssSelector(String cssSelector) {
            this.cssSelector = cssSelector;
            return this;
        }

        /**
         * xpath
         *
         * @param xpath
         * @return
         */
        public Builder setXpath(String xpath) {
            this.xpath = xpath;
            return this;
        }

        /**
         * text
         *
         * @param text
         * @return
         */
        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        /**
         * 等待时间
         *
         * @param waitSeconds
         * @return
         */
        public Builder setWaitSeconds(int waitSeconds) {
            this.waitSeconds = waitSeconds;
            return this;
        }

        /**
         * 设置期望的内容
         *
         * @param expectDesc
         * @return
         */
        public Builder setExpectDesc(String expectDesc) {
            this.expectDesc = expectDesc;
            return this;
        }

        public Builder setTextBlank(Boolean textBlank){
            this.textBlank = textBlank;
            return this;
        }

        public TaoBaoResource build() {
            TaoBaoResource taoBaoResource = new TaoBaoResource();
            taoBaoResource.setId(this.id);
            taoBaoResource.setName(this.name);
            taoBaoResource.setClassName(this.className);
            taoBaoResource.setCssSelector(this.cssSelector);
            taoBaoResource.setXpath(this.xpath);
            taoBaoResource.setText(this.text);
            taoBaoResource.setWaitSeconds(this.waitSeconds);
            taoBaoResource.setUrl(this.url);
            taoBaoResource.setExpectDesc(this.expectDesc);
            taoBaoResource.setTextBlank(this.textBlank);
            return taoBaoResource;
        }
    }


}
