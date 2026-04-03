package com.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.ZuopintijiaoEntity;
import java.util.List;
import java.util.Map;

public interface ZuopintijiaoService extends IService<ZuopintijiaoEntity> {
    PageUtils queryPage(Map<String, Object> params, Wrapper<ZuopintijiaoEntity> wrapper);
    List<ZuopintijiaoEntity> selectListView(Wrapper<ZuopintijiaoEntity> wrapper);
    ZuopintijiaoEntity selectView(Wrapper<ZuopintijiaoEntity> wrapper);
}
