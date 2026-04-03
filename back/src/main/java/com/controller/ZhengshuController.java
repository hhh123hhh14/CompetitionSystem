package com.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.annotation.IgnoreAuth;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.ZhengshuEntity;
import com.entity.HuojiangEntity;
import com.service.ZhengshuService;
import com.service.HuojiangService;
import com.utils.MPUtil;
import com.utils.PageUtils;
import com.utils.R;

/**
 * 电子证书 Controller
 */
@RestController
@RequestMapping("/zhengshu")
public class ZhengshuController {

    @Autowired
    private ZhengshuService zhengshuService;

    @Autowired
    private HuojiangService huojiangService;

    /** 后端分页列表 */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, ZhengshuEntity zhengshu,
                  HttpServletRequest request) {
        String tableName = request.getSession().getAttribute("tableName").toString();
        if (tableName.equals("xuesheng")) {
            zhengshu.setXuehao((String) request.getSession().getAttribute("username"));
        }
        EntityWrapper<ZhengshuEntity> ew = new EntityWrapper<>();
        PageUtils page = zhengshuService.queryPage(params,
                MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, zhengshu), params), params));
        return R.ok().put("data", page);
    }

    /** 前端我的证书列表 */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, ZhengshuEntity zhengshu,
                  HttpServletRequest request) {
        EntityWrapper<ZhengshuEntity> ew = new EntityWrapper<>();
        PageUtils page = zhengshuService.queryPage(params,
                MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, zhengshu), params), params));
        return R.ok().put("data", page);
    }

    /** 详情 */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        ZhengshuEntity e = zhengshuService.selectById(id);
        return R.ok().put("data", e);
    }

    /** 根据获奖记录生成证书 */
    @RequestMapping("/generate/{huojiangId}")
    public R generate(@PathVariable("huojiangId") Long huojiangId, HttpServletRequest request) {
        HuojiangEntity huojiang = huojiangService.selectById(huojiangId);
        if (huojiang == null) return R.error("获奖记录不存在");

        // 检查是否已经生成过
        EntityWrapper<ZhengshuEntity> ew = new EntityWrapper<>();
        ew.eq("huojiangid", huojiangId)
          .eq("xuehao", huojiang.getXuehao());
        ZhengshuEntity existing = zhengshuService.selectOne(ew);
        if (existing != null) {
            return R.ok("证书已存在").put("data", existing);
        }

        // 生成证书编号
        String bianhao = "CERT-" + new Date().getYear() + 1900 + "-"
                + String.format("%06d", (int)(Math.random() * 999999));

        ZhengshuEntity zhengshu = new ZhengshuEntity();
        zhengshu.setId(new Date().getTime() + new Double(Math.floor(Math.random() * 1000)).longValue());
        zhengshu.setHuojiangid(huojiangId);
        zhengshu.setJingsaimingcheng(huojiang.getJingsaimingcheng());
        zhengshu.setJingsaijib(huojiang.getJingsaijib());
        zhengshu.setXuehao(huojiang.getXuehao());
        zhengshu.setXueshengxingming(huojiang.getXueshengxingming());
        zhengshu.setXueyuanmingcheng(huojiang.getXueyuanmingcheng());
        zhengshu.setJiangxiangmingcheng(huojiang.getJiangxiangmingcheng());
        zhengshu.setJiangxiangdengji(huojiang.getJiangxiangdengji());
        zhengshu.setJiaoshixingming(huojiang.getJiaoshixingming());
        zhengshu.setHuojiangriqi(huojiang.getHuojiangriqi());
        zhengshu.setZhengshubianhao(bianhao);
        zhengshu.setShenqingshijian(new Date());
        zhengshuService.insert(zhengshu);
        return R.ok("证书生成成功").put("data", zhengshu);
    }

    /** 保存 */
    @RequestMapping("/save")
    public R save(@RequestBody ZhengshuEntity zhengshu, HttpServletRequest request) {
        zhengshu.setId(new Date().getTime() + new Double(Math.floor(Math.random() * 1000)).longValue());
        if (zhengshu.getZhengshubianhao() == null || zhengshu.getZhengshubianhao().isEmpty()) {
            zhengshu.setZhengshubianhao("CERT-" + System.currentTimeMillis());
        }
        zhengshu.setShenqingshijian(new Date());
        zhengshuService.insert(zhengshu);
        return R.ok();
    }

    /** 修改 */
    @RequestMapping("/update")
    public R update(@RequestBody ZhengshuEntity zhengshu) {
        zhengshuService.updateById(zhengshu);
        return R.ok();
    }

    /** 删除 */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        zhengshuService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
}
