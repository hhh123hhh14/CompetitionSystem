package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;

/**
 * 电子证书实体类
 */
@TableName("zhengshu")
public class ZhengshuEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    public ZhengshuEntity() {}

    public ZhengshuEntity(T t) {
        try {
            BeanUtils.copyProperties(this, t);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @TableId
    private Long id;

    /** 关联获奖ID */
    private Long huojiangid;

    /** 竞赛名称 */
    private String jingsaimingcheng;

    /** 竞赛级别 */
    private String jingsaijib;

    /** 学号 */
    private String xuehao;

    /** 姓名 */
    private String xueshengxingming;

    /** 学院 */
    private String xueyuanmingcheng;

    /** 奖项名称 */
    private String jiangxiangmingcheng;

    /** 获奖等级 */
    private String jiangxiangdengji;

    /** 指导教师 */
    private String jiaoshixingming;

    /** 获奖日期 */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    @DateTimeFormat
    private Date huojiangriqi;

    /** 证书编号 */
    private String zhengshubianhao;

    /** 证书文件路径 */
    private String zhengshuluqing;

    /** 申请生成时间 */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date shenqingshijian;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date addtime;

    public Date getAddtime() { return addtime; }
    public void setAddtime(Date addtime) { this.addtime = addtime; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getHuojiangid() { return huojiangid; }
    public void setHuojiangid(Long huojiangid) { this.huojiangid = huojiangid; }

    public String getJingsaimingcheng() { return jingsaimingcheng; }
    public void setJingsaimingcheng(String jingsaimingcheng) { this.jingsaimingcheng = jingsaimingcheng; }

    public String getJingsaijib() { return jingsaijib; }
    public void setJingsaijib(String jingsaijib) { this.jingsaijib = jingsaijib; }

    public String getXuehao() { return xuehao; }
    public void setXuehao(String xuehao) { this.xuehao = xuehao; }

    public String getXueshengxingming() { return xueshengxingming; }
    public void setXueshengxingming(String xueshengxingming) { this.xueshengxingming = xueshengxingming; }

    public String getXueyuanmingcheng() { return xueyuanmingcheng; }
    public void setXueyuanmingcheng(String xueyuanmingcheng) { this.xueyuanmingcheng = xueyuanmingcheng; }

    public String getJiangxiangmingcheng() { return jiangxiangmingcheng; }
    public void setJiangxiangmingcheng(String jiangxiangmingcheng) { this.jiangxiangmingcheng = jiangxiangmingcheng; }

    public String getJiangxiangdengji() { return jiangxiangdengji; }
    public void setJiangxiangdengji(String jiangxiangdengji) { this.jiangxiangdengji = jiangxiangdengji; }

    public String getJiaoshixingming() { return jiaoshixingming; }
    public void setJiaoshixingming(String jiaoshixingming) { this.jiaoshixingming = jiaoshixingming; }

    public Date getHuojiangriqi() { return huojiangriqi; }
    public void setHuojiangriqi(Date huojiangriqi) { this.huojiangriqi = huojiangriqi; }

    public String getZhengshubianhao() { return zhengshubianhao; }
    public void setZhengshubianhao(String zhengshubianhao) { this.zhengshubianhao = zhengshubianhao; }

    public String getZhengshuluqing() { return zhengshuluqing; }
    public void setZhengshuluqing(String zhengshuluqing) { this.zhengshuluqing = zhengshuluqing; }

    public Date getShenqingshijian() { return shenqingshijian; }
    public void setShenqingshijian(Date shenqingshijian) { this.shenqingshijian = shenqingshijian; }
}
