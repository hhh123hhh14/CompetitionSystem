package com.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.annotation.IgnoreAuth;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.entity.HuojiangEntity;
import com.service.HuojiangService;
import com.utils.MPUtil;
import com.utils.PageUtils;
import com.utils.R;

/**
 * 获奖管理 Controller
 */
@RestController
@RequestMapping("/huojiang")
public class HuojiangController {

    @Autowired
    private HuojiangService huojiangService;

    /** 后端分页列表 */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, HuojiangEntity huojiang,
                  HttpServletRequest request) {
        String tableName = request.getSession().getAttribute("tableName").toString();
        if (tableName.equals("jiaoshi")) {
            huojiang.setGonghao((String) request.getSession().getAttribute("username"));
        }
        if (tableName.equals("xuesheng")) {
            huojiang.setXuehao((String) request.getSession().getAttribute("username"));
        }
        EntityWrapper<HuojiangEntity> ew = new EntityWrapper<>();
        PageUtils page = huojiangService.queryPage(params,
                MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, huojiang), params), params));
        return R.ok().put("data", page);
    }

    /** 前端公示列表（免鉴权） */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, HuojiangEntity huojiang,
                  HttpServletRequest request) {
        EntityWrapper<HuojiangEntity> ew = new EntityWrapper<>();
        ew.eq("sfgs", "是"); // 只展示已公示的
        PageUtils page = huojiangService.queryPage(params,
                MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, huojiang), params), params));
        return R.ok().put("data", page);
    }

    /** 所有获奖列表（不分页） */
    @RequestMapping("/lists")
    public R lists(HuojiangEntity huojiang) {
        EntityWrapper<HuojiangEntity> ew = new EntityWrapper<>();
        ew.allEq(MPUtil.allEQMapPre(huojiang, "huojiang"));
        return R.ok().put("data", huojiangService.selectListView(ew));
    }

    /** 后端详情 */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        HuojiangEntity huojiang = huojiangService.selectById(id);
        return R.ok().put("data", huojiang);
    }

    /** 保存（新增） */
    @RequestMapping("/save")
    public R save(@RequestBody HuojiangEntity huojiang, HttpServletRequest request) {
        huojiang.setId(new Date().getTime() + new Double(Math.floor(Math.random() * 1000)).longValue());
        huojiangService.insert(huojiang);
        return R.ok();
    }

    /** 修改 */
    @RequestMapping("/update")
    public R update(@RequestBody HuojiangEntity huojiang, HttpServletRequest request) {
        huojiangService.updateById(huojiang);
        return R.ok();
    }

    /** 删除 */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        huojiangService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    /** 公示/取消公示 */
    @RequestMapping("/gongshi/{id}")
    public R gongshi(@PathVariable("id") Long id, @RequestParam String sfgs) {
        HuojiangEntity huojiang = huojiangService.selectById(id);
        if (huojiang == null) return R.error("获奖记录不存在");
        huojiang.setSfgs(sfgs);
        huojiangService.updateById(huojiang);
        return R.ok("操作成功");
    }

    /** 按学院统计获奖数量 */
    @IgnoreAuth
    @RequestMapping("/tongji/xueyuan")
    public R tongjiXueyuan() {
        List<Map<String, Object>> list = huojiangService.countByXueyuan();
        return R.ok().put("data", list);
    }

    /** 按竞赛级别统计 */
    @IgnoreAuth
    @RequestMapping("/tongji/jib")
    public R tongjiJib() {
        List<Map<String, Object>> list = huojiangService.countByJib();
        return R.ok().put("data", list);
    }

    /** 按奖项等级统计 */
    @IgnoreAuth
    @RequestMapping("/tongji/dengji")
    public R tongjiDengji() {
        List<Map<String, Object>> list = huojiangService.countByDengji();
        return R.ok().put("data", list);
    }
}
