package com.dao;

import com.entity.ZhengshuEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import java.util.List;
import com.baomidou.mybatisplus.mapper.Wrapper;
import org.apache.ibatis.annotations.Param;

/**
 * 电子证书 DAO
 */
public interface ZhengshuDao extends BaseMapper<ZhengshuEntity> {

    List<ZhengshuEntity> selectListView(@Param("ew") Wrapper<ZhengshuEntity> wrapper);

    List<ZhengshuEntity> selectListView(Pagination page, @Param("ew") Wrapper<ZhengshuEntity> wrapper);

    ZhengshuEntity selectView(@Param("ew") Wrapper<ZhengshuEntity> wrapper);
}
