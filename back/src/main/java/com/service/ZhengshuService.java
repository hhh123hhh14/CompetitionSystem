package com.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.ZhengshuEntity;
import java.util.List;
import java.util.Map;

public interface ZhengshuService extends IService<ZhengshuEntity> {
    PageUtils queryPage(Map<String, Object> params, Wrapper<ZhengshuEntity> wrapper);
    List<ZhengshuEntity> selectListView(Wrapper<ZhengshuEntity> wrapper);
    ZhengshuEntity selectView(Wrapper<ZhengshuEntity> wrapper);
}
