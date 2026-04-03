package com.controller;

import com.annotation.IgnoreAuth;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.JingsaiFieldEntity;
import com.service.JingsaiFieldService;
import com.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 竞赛动态报名字段配置 Controller
 *
 * 核心接口：
 *   GET  /jingsaiField/byCompetition/{jingsaixinxiid}  — 获取某竞赛的字段列表（学生端用）
 *   GET  /jingsaiField/list/{jingsaixinxiid}            — 管理员/教师查看字段列表
 *   POST /jingsaiField/save                             — 新增字段
 *   POST /jingsaiField/update                           — 修改字段
 *   POST /jingsaiField/delete                           — 删除字段（批量）
 *   POST /jingsaiField/batchSave                        — 批量保存（替换某竞赛全部字段）
 */
@RestController
@RequestMapping("/jingsaiField")
public class JingsaiFieldController {

    @Autowired
    private JingsaiFieldService jingsaiFieldService;

    /**
     * 按竞赛ID获取字段列表（对学生开放，免鉴权）
     */
    @IgnoreAuth
    @RequestMapping("/byCompetition/{jingsaixinxiid}")
    public R byCompetition(@PathVariable("jingsaixinxiid") Long jingsaixinxiid) {
        EntityWrapper<JingsaiFieldEntity> ew = new EntityWrapper<>();
        ew.eq("jingsaixinxiid", jingsaixinxiid)
          .orderBy("sort_order", true);
        List<JingsaiFieldEntity> list = jingsaiFieldService.selectList(ew);
        return R.ok().put("data", list);
    }

    /**
     * 管理端：查看某竞赛的字段列表（需登录）
     */
    @RequestMapping("/list/{jingsaixinxiid}")
    public R list(@PathVariable("jingsaixinxiid") Long jingsaixinxiid) {
        EntityWrapper<JingsaiFieldEntity> ew = new EntityWrapper<>();
        ew.eq("jingsaixinxiid", jingsaixinxiid)
          .orderBy("sort_order", true);
        List<JingsaiFieldEntity> list = jingsaiFieldService.selectList(ew);
        return R.ok().put("data", list);
    }

    /**
     * 新增单个字段
     */
    @RequestMapping("/save")
    public R save(@RequestBody JingsaiFieldEntity field) {
        field.setId(new Date().getTime() + new Double(Math.floor(Math.random() * 1000)).longValue());
        if (field.getSortOrder() == null) field.setSortOrder(0);
        if (field.getRequired() == null) field.setRequired("否");
        jingsaiFieldService.insert(field);
        return R.ok().put("data", field);
    }

    /**
     * 修改字段
     */
    @RequestMapping("/update")
    public R update(@RequestBody JingsaiFieldEntity field) {
        jingsaiFieldService.updateById(field);
        return R.ok();
    }

    /**
     * 删除字段（批量）
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        jingsaiFieldService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    /**
     * 批量保存：先删除该竞赛所有字段，再批量插入新字段
     * 前端在保存竞赛时整体提交字段列表使用
     */
    @RequestMapping("/batchSave")
    public R batchSave(@RequestParam("jingsaixinxiid") Long jingsaixinxiid,
                       @RequestBody List<JingsaiFieldEntity> fields) {
        // 删除旧字段
        EntityWrapper<JingsaiFieldEntity> ew = new EntityWrapper<>();
        ew.eq("jingsaixinxiid", jingsaixinxiid);
        jingsaiFieldService.delete(ew);

        // 插入新字段
        for (int i = 0; i < fields.size(); i++) {
            JingsaiFieldEntity f = fields.get(i);
            f.setId(new Date().getTime() + new Double(Math.floor(Math.random() * 1000)).longValue() + i);
            f.setJingsaixinxiid(jingsaixinxiid);
            f.setSortOrder(i);
            if (f.getRequired() == null) f.setRequired("否");
            jingsaiFieldService.insert(f);
        }
        return R.ok();
    }
}
