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
 * 作品提交实体类
 */
@TableName("zuopintijiao")
public class ZuopintijiaoEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    public ZuopintijiaoEntity() {}

    public ZuopintijiaoEntity(T t) {
        try {
            BeanUtils.copyProperties(this, t);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @TableId
    private Long id;

    /** 学号 */
    private String xuehao;

    /** 学生姓名 */
    private String xueshengxingming;

    /** 竞赛名称 */
    private String jingsaimingcheng;

    /** 关联竞赛信息ID */
    private Long jingsaixinxiid;

    /** 关联报名ID */
    private Long baomingid;

    /** 作品名称 */
    private String zuopinmingcheng;

    /** 作品描述 */
    private String zuopinmiaoshu;

    /** 附件文件（多个逗号分隔） */
    private String fujianwenjian;

    /** 提交时间 */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date tijiaoshijian;

    /** 提交状态（已提交/已修改） */
    private String zhuangtai;

    /** 版本号 */
    private Integer banben;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date addtime;

    public Date getAddtime() { return addtime; }
    public void setAddtime(Date addtime) { this.addtime = addtime; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getXuehao() { return xuehao; }
    public void setXuehao(String xuehao) { this.xuehao = xuehao; }

    public String getXueshengxingming() { return xueshengxingming; }
    public void setXueshengxingming(String xueshengxingming) { this.xueshengxingming = xueshengxingming; }

    public String getJingsaimingcheng() { return jingsaimingcheng; }
    public void setJingsaimingcheng(String jingsaimingcheng) { this.jingsaimingcheng = jingsaimingcheng; }

    public Long getJingsaixinxiid() { return jingsaixinxiid; }
    public void setJingsaixinxiid(Long jingsaixinxiid) { this.jingsaixinxiid = jingsaixinxiid; }

    public Long getBaomingid() { return baomingid; }
    public void setBaomingid(Long baomingid) { this.baomingid = baomingid; }

    public String getZuopinmingcheng() { return zuopinmingcheng; }
    public void setZuopinmingcheng(String zuopinmingcheng) { this.zuopinmingcheng = zuopinmingcheng; }

    public String getZuopinmiaoshu() { return zuopinmiaoshu; }
    public void setZuopinmiaoshu(String zuopinmiaoshu) { this.zuopinmiaoshu = zuopinmiaoshu; }

    public String getFujianwenjian() { return fujianwenjian; }
    public void setFujianwenjian(String fujianwenjian) { this.fujianwenjian = fujianwenjian; }

    public Date getTijiaoshijian() { return tijiaoshijian; }
    public void setTijiaoshijian(Date tijiaoshijian) { this.tijiaoshijian = tijiaoshijian; }

    public String getZhuangtai() { return zhuangtai; }
    public void setZhuangtai(String zhuangtai) { this.zhuangtai = zhuangtai; }

    public Integer getBanben() { return banben; }
    public void setBanben(Integer banben) { this.banben = banben; }
}
