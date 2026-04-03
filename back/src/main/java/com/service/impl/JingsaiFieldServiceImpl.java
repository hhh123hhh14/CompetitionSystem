package com.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dao.JingsaiFieldDao;
import com.entity.JingsaiFieldEntity;
import com.service.JingsaiFieldService;
import org.springframework.stereotype.Service;

/**
 * 竞赛动态字段配置 ServiceImpl
 */
@Service("jingsaiFieldService")
public class JingsaiFieldServiceImpl extends ServiceImpl<JingsaiFieldDao, JingsaiFieldEntity>
        implements JingsaiFieldService {
}
