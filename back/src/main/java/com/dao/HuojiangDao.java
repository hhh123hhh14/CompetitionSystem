package com.dao;

import com.entity.HuojiangEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.Wrapper;
import org.apache.ibatis.annotations.Param;

/**
 * 获奖管理 DAO
 */
public interface HuojiangDao extends BaseMapper<HuojiangEntity> {

    List<HuojiangEntity> selectListView(@Param("ew") Wrapper<HuojiangEntity> wrapper);

    List<HuojiangEntity> selectListView(Pagination page, @Param("ew") Wrapper<HuojiangEntity> wrapper);

    HuojiangEntity selectView(@Param("ew") Wrapper<HuojiangEntity> wrapper);

    // 按学院统计获奖数量
    List<Map<String, Object>> countByXueyuan();

    // 按竞赛级别统计
    List<Map<String, Object>> countByJib();

    // 按奖项等级统计
    List<Map<String, Object>> countByDengji();
}
