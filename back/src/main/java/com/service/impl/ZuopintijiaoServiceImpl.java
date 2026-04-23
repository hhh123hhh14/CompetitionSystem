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

import com.dao.ZuopintijiaoDao;
import com.entity.ZuopintijiaoEntity;
import com.service.ZuopintijiaoService;

@Service("zuopintijiaoService")
public class ZuopintijiaoServiceImpl extends ServiceImpl<ZuopintijiaoDao, ZuopintijiaoEntity> implements ZuopintijiaoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params, Wrapper<ZuopintijiaoEntity> wrapper) {
        Page<ZuopintijiaoEntity> page = new Query<ZuopintijiaoEntity>(params).getPage();
        page.setRecords(baseMapper.selectListView(page, wrapper));
        return new PageUtils(page);
    }

    @Override
    public List<ZuopintijiaoEntity> selectListView(Wrapper<ZuopintijiaoEntity> wrapper) {
        return baseMapper.selectListView(wrapper);
    }

    @Override
    public ZuopintijiaoEntity selectView(Wrapper<ZuopintijiaoEntity> wrapper) {
        return baseMapper.selectView(wrapper);
    }
}
