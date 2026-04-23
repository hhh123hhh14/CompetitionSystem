package com.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.utils.PageUtils;
import com.utils.Query;

import com.dao.ZhengshuDao;
import com.entity.ZhengshuEntity;
import com.service.ZhengshuService;

@Service("zhengshuService")
public class ZhengshuServiceImpl extends ServiceImpl<ZhengshuDao, ZhengshuEntity> implements ZhengshuService {

    @Override
    public PageUtils queryPage(Map<String, Object> params, Wrapper<ZhengshuEntity> wrapper) {
        Page<ZhengshuEntity> page = new Query<ZhengshuEntity>(params).getPage();
        page.setRecords(baseMapper.selectListView(page, wrapper));
        return new PageUtils(page);
    }

    @Override
    public List<ZhengshuEntity> selectListView(Wrapper<ZhengshuEntity> wrapper) {
        return baseMapper.selectListView(wrapper);
    }

    @Override
    public ZhengshuEntity selectView(Wrapper<ZhengshuEntity> wrapper) {
        return baseMapper.selectView(wrapper);
    }
}
