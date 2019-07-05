package com.justCopyBt.slide;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.sl.usermodel.Slide;

import javax.persistence.*;
import javax.ws.rs.FormParam;

/**
 * 滑动轨迹
 * @date 2019/07/05
 */
@Entity
@Table(name = "slide_trail_record")
public class SlideTrailRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @FormParam("id")
    private Integer id;
    /**
     * 所属网站
     */
    @FormParam("site")
    @Column(name = "site")
    private String site;

    /**
     * 滑动距离
     */
    @Column(name = "distance")
    @FormParam("distance")
    private Integer distance;

    /**
     * 滑动轨迹
     */
    @FormParam("slide_trail")
    @Column(name = "slide_trail")
    private String slideTrail;

    /**
     * 轨迹状态
     */
    @FormParam("status")
    @Column(name = "status" ,columnDefinition="INT default 1")
    private Integer status = 1;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public String getSlideTrail() {
        return slideTrail;
    }

    public void setSlideTrail(String slideTrail) {
        this.slideTrail = slideTrail;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 从其他的轨迹合并信息到本轨迹
     * @param slideTrailRecord
     */
    public void mergeSlideTrailInfo(SlideTrailRecord slideTrailRecord){
        if(StringUtils.isNotBlank(slideTrailRecord.getSite())) {
            this.setSite(slideTrailRecord.getSite());
        }
        if(slideTrailRecord.getDistance()!=null && slideTrailRecord.getDistance()>0) {
            this.setDistance(slideTrailRecord.getDistance());
        }
        if(StringUtils.isNotBlank(slideTrailRecord.getSlideTrail())) {
            this.setSlideTrail(slideTrailRecord.getSlideTrail());
        }
        if(slideTrailRecord.getStatus() !=null) {
            this.setStatus(slideTrailRecord.getStatus());
        }
    }
}
