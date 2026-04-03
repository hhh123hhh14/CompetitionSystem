package com.dao;

import com.entity.ZhengshuEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import com.baomidou.mybatisplus.mapper.Wrapper;
import org.apache.ibatis.annotations.Param;

/**
 * 电子证书 DAO
 */
public interface ZhengshuDao extends BaseMapper<ZhengshuEntity> {

    List<ZhengshuEntity> selectListView(Wrapper<ZhengshuEntity> wrapper);

    ZhengshuEntity selectView(@Param("ew") Wrapper<ZhengshuEntity> wrapper);
}
