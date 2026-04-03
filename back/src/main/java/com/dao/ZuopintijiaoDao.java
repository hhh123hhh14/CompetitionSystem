package com.dao;

import com.entity.ZuopintijiaoEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import com.baomidou.mybatisplus.mapper.Wrapper;
import org.apache.ibatis.annotations.Param;

/**
 * 作品提交 DAO
 */
public interface ZuopintijiaoDao extends BaseMapper<ZuopintijiaoEntity> {

    List<ZuopintijiaoEntity> selectListView(Wrapper<ZuopintijiaoEntity> wrapper);

    ZuopintijiaoEntity selectView(@Param("ew") Wrapper<ZuopintijiaoEntity> wrapper);
}
