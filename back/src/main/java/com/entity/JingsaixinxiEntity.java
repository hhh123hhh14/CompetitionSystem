package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.lang.reflect.InvocationTargetException;
import java.io.Serializable;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.beanutils.BeanUtils;

/**
 * 竞赛信息
 * 数据库通用操作实体类（普通增删改查）
 */
@TableName("jingsaixinxi")
public class JingsaixinxiEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    public JingsaixinxiEntity() {}

    public JingsaixinxiEntity(T t) {
        try {
            BeanUtils.copyProperties(this, t);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /** 主键id */
    @TableId
    private Long id;

    /** 竞赛名称 */
    private String jingsaimingcheng;

    /** 竞赛类型 */
    private String jingsaileixing;

    /** 竞赛地点 */
    private String jingsaididian;

    /** 竞赛规则 */
    private String jingsaiguize;

    /** 竞赛奖励 */
    private String jingsaijiangli;

    /** 竞赛时间 */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date jingsaishijian;

    /** 模式（付费/免费） */
    private String moshi;

    /** 封面图片 */
    private String fengmiantupian;

    /** 工号 */
    private String gonghao;

    /** 教师姓名 */
    private String jiaoshixingming;

    // ===== 新增扩展字段 =====

    /** 竞赛级别（校级/省级/国家级/国际级） */
    private String jingsaijib;

    /** 报名开始时间 */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date baomingkaishi;

    /** 报名截止时间 */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date baomingjiezhi;

    /** 作品提交截止时间 */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date tijiaojiezhi;

    /** 收费标准 */
    private String shoufeibiaozhun;

    /** 参赛要求 */
    private String cansaiyaoqiu;

    /** 奖项设置 */
    private String jiangxiangshezhi;

    /** 竞赛状态（报名中/进行中/已结束） */
    private String zhuangtai;

    /** 收款码图片 */
    private String shoukuanma;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date addtime;

    public Date getAddtime() { return addtime; }
    public void setAddtime(Date addtime) { this.addtime = addtime; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public void setJingsaimingcheng(String jingsaimingcheng) { this.jingsaimingcheng = jingsaimingcheng; }
    public String getJingsaimingcheng() { return jingsaimingcheng; }

    public void setJingsaileixing(String jingsaileixing) { this.jingsaileixing = jingsaileixing; }
    public String getJingsaileixing() { return jingsaileixing; }

    public void setJingsaididian(String jingsaididian) { this.jingsaididian = jingsaididian; }
    public String getJingsaididian() { return jingsaididian; }

    public void setJingsaiguize(String jingsaiguize) { this.jingsaiguize = jingsaiguize; }
    public String getJingsaiguize() { return jingsaiguize; }

    public void setJingsaijiangli(String jingsaijiangli) { this.jingsaijiangli = jingsaijiangli; }
    public String getJingsaijiangli() { return jingsaijiangli; }

    public void setJingsaishijian(Date jingsaishijian) { this.jingsaishijian = jingsaishijian; }
    public Date getJingsaishijian() { return jingsaishijian; }

    public void setMoshi(String moshi) { this.moshi = moshi; }
    public String getMoshi() { return moshi; }

    public void setFengmiantupian(String fengmiantupian) { this.fengmiantupian = fengmiantupian; }
    public String getFengmiantupian() { return fengmiantupian; }

    public void setGonghao(String gonghao) { this.gonghao = gonghao; }
    public String getGonghao() { return gonghao; }

    public void setJiaoshixingming(String jiaoshixingming) { this.jiaoshixingming = jiaoshixingming; }
    public String getJiaoshixingming() { return jiaoshixingming; }

    public String getJingsaijib() { return jingsaijib; }
    public void setJingsaijib(String jingsaijib) { this.jingsaijib = jingsaijib; }

    public Date getBaomingkaishi() { return baomingkaishi; }
    public void setBaomingkaishi(Date baomingkaishi) { this.baomingkaishi = baomingkaishi; }

    public Date getBaomingjiezhi() { return baomingjiezhi; }
    public void setBaomingjiezhi(Date baomingjiezhi) { this.baomingjiezhi = baomingjiezhi; }

    public Date getTijiaojiezhi() { return tijiaojiezhi; }
    public void setTijiaojiezhi(Date tijiaojiezhi) { this.tijiaojiezhi = tijiaojiezhi; }

    public String getShoufeibiaozhun() { return shoufeibiaozhun; }
    public void setShoufeibiaozhun(String shoufeibiaozhun) { this.shoufeibiaozhun = shoufeibiaozhun; }

    public String getCansaiyaoqiu() { return cansaiyaoqiu; }
    public void setCansaiyaoqiu(String cansaiyaoqiu) { this.cansaiyaoqiu = cansaiyaoqiu; }

    public String getJiangxiangshezhi() { return jiangxiangshezhi; }
    public void setJiangxiangshezhi(String jiangxiangshezhi) { this.jiangxiangshezhi = jiangxiangshezhi; }

    public String getZhuangtai() { return zhuangtai; }
    public void setZhuangtai(String zhuangtai) { this.zhuangtai = zhuangtai; }

    public String getShoukuanma() { return shoukuanma; }
    public void setShoukuanma(String shoukuanma) { this.shoukuanma = shoukuanma; }
}
