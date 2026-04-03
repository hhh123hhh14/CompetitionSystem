package com.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.HuojiangEntity;
import java.util.List;
import java.util.Map;

public interface HuojiangService extends IService<HuojiangEntity> {
    PageUtils queryPage(Map<String, Object> params, Wrapper<HuojiangEntity> wrapper);
    List<HuojiangEntity> selectListView(Wrapper<HuojiangEntity> wrapper);
    HuojiangEntity selectView(Wrapper<HuojiangEntity> wrapper);
    List<Map<String, Object>> countByXueyuan();
    List<Map<String, Object>> countByJib();
    List<Map<String, Object>> countByDengji();
}
