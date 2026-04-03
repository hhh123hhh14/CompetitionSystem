package com.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.annotation.IgnoreAuth;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.ZuopintijiaoEntity;
import com.service.ZuopintijiaoService;
import com.utils.MPUtil;
import com.utils.PageUtils;
import com.utils.R;

/**
 * 作品提交 Controller
 */
@RestController
@RequestMapping("/zuopintijiao")
public class ZuopintijiaoController {

    @Autowired
    private ZuopintijiaoService zuopintijiaoService;

    /** 后端分页列表 */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, ZuopintijiaoEntity zuopintijiao,
                  HttpServletRequest request) {
        String tableName = request.getSession().getAttribute("tableName").toString();
        if (tableName.equals("xuesheng")) {
            zuopintijiao.setXuehao((String) request.getSession().getAttribute("username"));
        }
        EntityWrapper<ZuopintijiaoEntity> ew = new EntityWrapper<>();
        PageUtils page = zuopintijiaoService.queryPage(params,
                MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, zuopintijiao), params), params));
        return R.ok().put("data", page);
    }

    /** 前端列表 */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, ZuopintijiaoEntity zuopintijiao,
                  HttpServletRequest request) {
        EntityWrapper<ZuopintijiaoEntity> ew = new EntityWrapper<>();
        PageUtils page = zuopintijiaoService.queryPage(params,
                MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, zuopintijiao), params), params));
        return R.ok().put("data", page);
    }

    /** 详情 */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        ZuopintijiaoEntity e = zuopintijiaoService.selectById(id);
        return R.ok().put("data", e);
    }

    /** 提交作品（新增或重新提交） */
    @RequestMapping("/save")
    public R save(@RequestBody ZuopintijiaoEntity zuopintijiao, HttpServletRequest request) {
        zuopintijiao.setId(new Date().getTime() + new Double(Math.floor(Math.random() * 1000)).longValue());
        zuopintijiao.setTijiaoshijian(new Date());
        if (zuopintijiao.getBanben() == null) zuopintijiao.setBanben(1);
        zuopintijiao.setZhuangtai("已提交");
        zuopintijiaoService.insert(zuopintijiao);
        return R.ok();
    }

    /** 修改（重新提交）——版本号+1 */
    @RequestMapping("/update")
    public R update(@RequestBody ZuopintijiaoEntity zuopintijiao, HttpServletRequest request) {
        ZuopintijiaoEntity old = zuopintijiaoService.selectById(zuopintijiao.getId());
        if (old != null) {
            zuopintijiao.setBanben((old.getBanben() == null ? 1 : old.getBanben()) + 1);
        }
        zuopintijiao.setTijiaoshijian(new Date());
        zuopintijiao.setZhuangtai("已修改");
        zuopintijiaoService.updateById(zuopintijiao);
        return R.ok();
    }

    /** 删除 */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        zuopintijiaoService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
}
