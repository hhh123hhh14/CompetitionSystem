package com.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.utils.PageUtils;
import com.utils.Query;

import com.dao.HuojiangDao;
import com.entity.HuojiangEntity;
import com.service.HuojiangService;

@Service("huojiangService")
public class HuojiangServiceImpl extends ServiceImpl<HuojiangDao, HuojiangEntity> implements HuojiangService {

    @Override
    public PageUtils queryPage(Map<String, Object> params, Wrapper<HuojiangEntity> wrapper) {
        Page<HuojiangEntity> page = new Query<HuojiangEntity>(params).getPage();
        page.setRecords(baseMapper.selectListView(wrapper));
        return new PageUtils(page);
    }

    @Override
    public List<HuojiangEntity> selectListView(Wrapper<HuojiangEntity> wrapper) {
        return baseMapper.selectListView(wrapper);
    }

    @Override
    public HuojiangEntity selectView(Wrapper<HuojiangEntity> wrapper) {
        return baseMapper.selectView(wrapper);
    }

    @Override
    public List<Map<String, Object>> countByXueyuan() {
        return baseMapper.countByXueyuan();
    }

    @Override
    public List<Map<String, Object>> countByJib() {
        return baseMapper.countByJib();
    }

    @Override
    public List<Map<String, Object>> countByDengji() {
        return baseMapper.countByDengji();
    }
}
