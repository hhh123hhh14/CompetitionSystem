package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

/**
 * 竞赛报名字段配置
 * 每条记录对应某个竞赛（jingsaixinxiid）中的一个自定义字段
 */
@TableName("jingsai_fields")
public class JingsaiFieldEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    public JingsaiFieldEntity() {}

    public JingsaiFieldEntity(T t) {
        try {
            BeanUtils.copyProperties(this, t);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /** 主键 */
    @TableId
    private Long id;

    /** 关联竞赛ID */
    private Long jingsaixinxiid;

    /**
     * 字段类型：
     * text       文本框
     * number     数字框
     * radio      单选按钮
     * checkbox   多选框
     * select     下拉选择
     * date       日期选择
     * textarea   多行文本
     */
    private String fieldType;

    /** 字段标签（展示名称），如"学院名称"、"特长说明" */
    private String fieldLabel;

    /** 字段KEY（英文，用于数据存储），如"xueyuan"、"tezhang" */
    private String fieldKey;

    /** 是否必填：是/否 */
    private String required;

    /** 默认值 */
    private String defaultValue;

    /** 选项列表（JSON数组字符串，针对radio/checkbox/select），如 ["选项A","选项B"] */
    private String fieldOptions;

    /** 占位提示文字 */
    private String placeholder;

    /** 排序号（越小越靠前） */
    private Integer sortOrder;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date addtime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getJingsaixinxiid() { return jingsaixinxiid; }
    public void setJingsaixinxiid(Long jingsaixinxiid) { this.jingsaixinxiid = jingsaixinxiid; }

    public String getFieldType() { return fieldType; }
    public void setFieldType(String fieldType) { this.fieldType = fieldType; }

    public String getFieldLabel() { return fieldLabel; }
    public void setFieldLabel(String fieldLabel) { this.fieldLabel = fieldLabel; }

    public String getFieldKey() { return fieldKey; }
    public void setFieldKey(String fieldKey) { this.fieldKey = fieldKey; }

    public String getRequired() { return required; }
    public void setRequired(String required) { this.required = required; }

    public String getDefaultValue() { return defaultValue; }
    public void setDefaultValue(String defaultValue) { this.defaultValue = defaultValue; }

    public String getFieldOptions() { return fieldOptions; }
    public void setFieldOptions(String fieldOptions) { this.fieldOptions = fieldOptions; }

    public String getPlaceholder() { return placeholder; }
    public void setPlaceholder(String placeholder) { this.placeholder = placeholder; }

    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }

    public Date getAddtime() { return addtime; }
    public void setAddtime(Date addtime) { this.addtime = addtime; }
}
