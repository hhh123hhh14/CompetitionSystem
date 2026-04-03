import Vue from 'vue';
import VueRouter from 'vue-router'
Vue.use(VueRouter);

import Index from '@/views/index'
import Home from '@/views/home'
import Login from '@/views/login'
import NotFound from '@/views/404'
import UpdatePassword from '@/views/update-password'
import pay from '@/views/pay'
import register from '@/views/register'
import center from '@/views/center'

// ===== 原有模块 =====
import jingsaixinxi from '@/views/modules/jingsaixinxi/list'
import xuesheng from '@/views/modules/xuesheng/list'
import jingsaibaoming from '@/views/modules/jingsaibaoming/list'
import jiaoshi from '@/views/modules/jiaoshi/list'
import zuopindafen from '@/views/modules/zuopindafen/list'
import banjileixing from '@/views/modules/banjileixing/list'

// ===== 新增模块 =====
import huojiang from '@/views/modules/huojiang/list'
import zuopintijiao from '@/views/modules/zuopintijiao/list'
import zhengshu from '@/views/modules/zhengshu/list'
import tongji from '@/views/modules/tongji/list'

const routes = [{
    path: '/index',
    name: '首页',
    component: Index,
    children: [{
      path: '/',
      name: '首页',
      component: Home,
      meta: {icon:'dashboard', title:'center'}
    }, {
      path: '/updatePassword',
      name: '修改密码',
      component: UpdatePassword,
      meta: {icon:'', title:'updatePassword'}
    }, {
      path: '/pay',
      name: '支付',
      component: pay,
      meta: {icon:'', title:'pay'}
    }, {
      path: '/center',
      name: '个人信息',
      component: center,
      meta: {icon:'', title:'center'}
    },
    // ===== 赛前：信息发布 + 报名管理 =====
    {
      path: '/jingsaixinxi',
      name: '竞赛信息',
      component: jingsaixinxi
    },
    {
      path: '/jingsaibaoming',
      name: '竞赛报名',
      component: jingsaibaoming
    },
    {
      path: '/jiaoshi',
      name: '教师管理',
      component: jiaoshi
    },
    {
      path: '/xuesheng',
      name: '学生管理',
      component: xuesheng
    },
    {
      path: '/banjileixing',
      name: '班级类型',
      component: banjileixing
    },
    // ===== 赛中：作品提交 + 评审管理 =====
    {
      path: '/zuopintijiao',
      name: '作品提交',
      component: zuopintijiao
    },
    {
      path: '/zuopindafen',
      name: '作品评审',
      component: zuopindafen
    },
    // ===== 赛后：成绩 + 证书 + 统计 =====
    {
      path: '/huojiang',
      name: '获奖管理',
      component: huojiang
    },
    {
      path: '/zhengshu',
      name: '证书管理',
      component: zhengshu
    },
    {
      path: '/tongji',
      name: '统计分析',
      component: tongji
    }
    ]
  },
  {
    path: '/login',
    name: 'login',
    component: Login,
    meta: {icon:'', title:'login'}
  },
  {
    path: '/register',
    name: 'register',
    component: register,
    meta: {icon:'', title:'register'}
  },
  {
    path: '/',
    name: '首页',
    redirect: '/index'
  },
  {
    path: '*',
    component: NotFound
  }
]

const router = new VueRouter({
  mode: 'hash',
  routes
})

export default router;
