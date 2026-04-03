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
 * 竞赛报名
 * 数据库通用操作实体类
 */
@TableName("jingsaibaoming")
public class JingsaibaomingEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    public JingsaibaomingEntity() {}

    public JingsaibaomingEntity(T t) {
        try {
            BeanUtils.copyProperties(this, t);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @TableId
    private Long id;

    /** 工号（指导教师） */
    private String gonghao;

    /** 教师姓名 */
    private String jiaoshixingming;

    /** 竞赛名称 */
    private String jingsaimingcheng;

    /** 竞赛类型 */
    private String jingsaileixing;

    /** 参赛类型（个人/团队） */
    private String cansaileixing;

    /** 参赛人员 */
    private String cansairenyuan;

    /** 参赛作品（附件路径） */
    private String cansaizuopin;

    /** 参赛宣言 */
    private String cansaixuanyan;

    /** 申请日期 */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    @DateTimeFormat
    private Date shenqingriqi;

    /** 学号（队长/个人） */
    private String xuehao;

    /** 学生姓名 */
    private String xueshengxingming;

    /** 是否审核（待审核/已通过/已驳回） */
    private String sfsh;

    /** 审核回复 */
    private String shhf;

    /** 是否支付 */
    private String ispay;

    // ===== 新增扩展字段 =====

    /** 学院名称 */
    private String xueyuanmingcheng;

    /** 年级 */
    private String nianji;

    /** 联系电话 */
    private String shouji;

    /** 团队名称 */
    private String tuanduimingcheng;

    /** 团队成员详情（JSON格式） */
    private String tuanduichengyuan;

    /** 关联竞赛信息ID */
    private Long jingsaixinxiid;

    /**
     * 动态扩展字段数据（JSON对象字符串）
     * 例如：{"xueyuan":"计算机学院","tezhang":"编程能力突出"}
     * 按照 jingsai_fields 表中配置的 fieldKey 存储
     */
    private String extFields;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date addtime;

    public Date getAddtime() { return addtime; }
    public void setAddtime(Date addtime) { this.addtime = addtime; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getGonghao() { return gonghao; }
    public void setGonghao(String gonghao) { this.gonghao = gonghao; }

    public String getJiaoshixingming() { return jiaoshixingming; }
    public void setJiaoshixingming(String jiaoshixingming) { this.jiaoshixingming = jiaoshixingming; }

    public String getJingsaimingcheng() { return jingsaimingcheng; }
    public void setJingsaimingcheng(String jingsaimingcheng) { this.jingsaimingcheng = jingsaimingcheng; }

    public String getJingsaileixing() { return jingsaileixing; }
    public void setJingsaileixing(String jingsaileixing) { this.jingsaileixing = jingsaileixing; }

    public String getCansaileixing() { return cansaileixing; }
    public void setCansaileixing(String cansaileixing) { this.cansaileixing = cansaileixing; }

    public String getCansairenyuan() { return cansairenyuan; }
    public void setCansairenyuan(String cansairenyuan) { this.cansairenyuan = cansairenyuan; }

    public String getCansaizuopin() { return cansaizuopin; }
    public void setCansaizuopin(String cansaizuopin) { this.cansaizuopin = cansaizuopin; }

    public String getCansaixuanyan() { return cansaixuanyan; }
    public void setCansaixuanyan(String cansaixuanyan) { this.cansaixuanyan = cansaixuanyan; }

    public Date getShenqingriqi() { return shenqingriqi; }
    public void setShenqingriqi(Date shenqingriqi) { this.shenqingriqi = shenqingriqi; }

    public String getXuehao() { return xuehao; }
    public void setXuehao(String xuehao) { this.xuehao = xuehao; }

    public String getXueshengxingming() { return xueshengxingming; }
    public void setXueshengxingming(String xueshengxingming) { this.xueshengxingming = xueshengxingming; }

    public String getSfsh() { return sfsh; }
    public void setSfsh(String sfsh) { this.sfsh = sfsh; }

    public String getShhf() { return shhf; }
    public void setShhf(String shhf) { this.shhf = shhf; }

    public String getIspay() { return ispay; }
    public void setIspay(String ispay) { this.ispay = ispay; }

    public String getXueyuanmingcheng() { return xueyuanmingcheng; }
    public void setXueyuanmingcheng(String xueyuanmingcheng) { this.xueyuanmingcheng = xueyuanmingcheng; }

    public String getNianji() { return nianji; }
    public void setNianji(String nianji) { this.nianji = nianji; }

    public String getShouji() { return shouji; }
    public void setShouji(String shouji) { this.shouji = shouji; }

    public String getTuanduimingcheng() { return tuanduimingcheng; }
    public void setTuanduimingcheng(String tuanduimingcheng) { this.tuanduimingcheng = tuanduimingcheng; }

    public String getTuanduichengyuan() { return tuanduichengyuan; }
    public void setTuanduichengyuan(String tuanduichengyuan) { this.tuanduichengyuan = tuanduichengyuan; }

    public Long getJingsaixinxiid() { return jingsaixinxiid; }
    public void setJingsaixinxiid(Long jingsaixinxiid) { this.jingsaixinxiid = jingsaixinxiid; }

    public String getExtFields() { return extFields; }
    public void setExtFields(String extFields) { this.extFields = extFields; }
}
