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
 * 获奖管理实体类
 */
@TableName("huojiang")
public class HuojiangEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    public HuojiangEntity() {}

    public HuojiangEntity(T t) {
        try {
            BeanUtils.copyProperties(this, t);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @TableId
    private Long id;

    /** 关联竞赛信息ID */
    private Long jingsaixinxiid;

    /** 竞赛名称 */
    private String jingsaimingcheng;

    /** 竞赛类型 */
    private String jingsaileixing;

    /** 竞赛级别 */
    private String jingsaijib;

    /** 获奖学号（队长/个人） */
    private String xuehao;

    /** 获奖学生姓名 */
    private String xueshengxingming;

    /** 学院名称 */
    private String xueyuanmingcheng;

    /** 团队名称 */
    private String tuanduimingcheng;

    /** 团队成员 */
    private String tuanduichengyuan;

    /** 奖项名称 */
    private String jiangxiangmingcheng;

    /** 奖项等级 */
    private String jiangxiangdengji;

    /** 指导教师工号 */
    private String gonghao;

    /** 指导教师姓名 */
    private String jiaoshixingming;

    /** 获奖日期 */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    @DateTimeFormat
    private Date huojiangriqi;

    /** 证书图片路径 */
    private String zhengshu;

    /** 备注 */
    private String beizhu;

    /** 是否公示（是/否） */
    private String sfgs;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date addtime;

    public Date getAddtime() { return addtime; }
    public void setAddtime(Date addtime) { this.addtime = addtime; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getJingsaixinxiid() { return jingsaixinxiid; }
    public void setJingsaixinxiid(Long jingsaixinxiid) { this.jingsaixinxiid = jingsaixinxiid; }

    public String getJingsaimingcheng() { return jingsaimingcheng; }
    public void setJingsaimingcheng(String jingsaimingcheng) { this.jingsaimingcheng = jingsaimingcheng; }

    public String getJingsaileixing() { return jingsaileixing; }
    public void setJingsaileixing(String jingsaileixing) { this.jingsaileixing = jingsaileixing; }

    public String getJingsaijib() { return jingsaijib; }
    public void setJingsaijib(String jingsaijib) { this.jingsaijib = jingsaijib; }

    public String getXuehao() { return xuehao; }
    public void setXuehao(String xuehao) { this.xuehao = xuehao; }

    public String getXueshengxingming() { return xueshengxingming; }
    public void setXueshengxingming(String xueshengxingming) { this.xueshengxingming = xueshengxingming; }

    public String getXueyuanmingcheng() { return xueyuanmingcheng; }
    public void setXueyuanmingcheng(String xueyuanmingcheng) { this.xueyuanmingcheng = xueyuanmingcheng; }

    public String getTuanduimingcheng() { return tuanduimingcheng; }
    public void setTuanduimingcheng(String tuanduimingcheng) { this.tuanduimingcheng = tuanduimingcheng; }

    public String getTuanduichengyuan() { return tuanduichengyuan; }
    public void setTuanduichengyuan(String tuanduichengyuan) { this.tuanduichengyuan = tuanduichengyuan; }

    public String getJiangxiangmingcheng() { return jiangxiangmingcheng; }
    public void setJiangxiangmingcheng(String jiangxiangmingcheng) { this.jiangxiangmingcheng = jiangxiangmingcheng; }

    public String getJiangxiangdengji() { return jiangxiangdengji; }
    public void setJiangxiangdengji(String jiangxiangdengji) { this.jiangxiangdengji = jiangxiangdengji; }

    public String getGonghao() { return gonghao; }
    public void setGonghao(String gonghao) { this.gonghao = gonghao; }

    public String getJiaoshixingming() { return jiaoshixingming; }
    public void setJiaoshixingming(String jiaoshixingming) { this.jiaoshixingming = jiaoshixingming; }

    public Date getHuojiangriqi() { return huojiangriqi; }
    public void setHuojiangriqi(Date huojiangriqi) { this.huojiangriqi = huojiangriqi; }

    public String getZhengshu() { return zhengshu; }
    public void setZhengshu(String zhengshu) { this.zhengshu = zhengshu; }

    public String getBeizhu() { return beizhu; }
    public void setBeizhu(String beizhu) { this.beizhu = beizhu; }

    public String getSfgs() { return sfgs; }
    public void setSfgs(String sfgs) { this.sfgs = sfgs; }
}
