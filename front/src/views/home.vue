<template>
  <div class="content">
    <div class="welcome-header">
      <div class="welcome-title">
        <i class="el-icon-trophy" style="color:#f5a623;font-size:36px;margin-right:12px"></i>
        欢迎使用 大学生竞赛管理系统
      </div>
      <div class="welcome-sub">College Student Competition Management System</div>
    </div>

    <!-- 统计概览卡片 -->
    <el-row :gutter="16" style="margin:24px 0 16px">
      <el-col :span="6" v-for="item in statCards" :key="item.label">
        <div class="stat-card" :style="{background: item.bg}" @click="$router.push(item.path)">
          <div class="stat-icon-wrap"><i :class="item.icon"></i></div>
          <div class="stat-info">
            <div class="stat-num">{{ item.value }}</div>
            <div class="stat-label">{{ item.label }}</div>
          </div>
          <div class="stat-arrow"><i class="el-icon-arrow-right"></i></div>
        </div>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="16" style="margin-bottom:16px">
      <el-col :span="14">
        <el-card shadow="hover">
          <div slot="header" style="font-weight:bold">
            <i class="el-icon-data-analysis" style="color:#667eea;margin-right:6px"></i>
            各学院获奖数量
          </div>
          <div ref="chartBar" style="height:260px"></div>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card shadow="hover">
          <div slot="header" style="font-weight:bold">
            <i class="el-icon-pie-chart" style="color:#f5576c;margin-right:6px"></i>
            获奖等级分布
          </div>
          <div ref="chartPie" style="height:260px"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 近期获奖公示 -->
    <el-card shadow="hover">
      <div slot="header" style="font-weight:bold">
        <i class="el-icon-medal" style="color:#f5a623;margin-right:6px"></i>
        近期获奖公示
        <el-button style="float:right;padding:3px 0" type="text" @click="$router.push('/huojiang')">查看更多 →</el-button>
      </div>
      <el-table :data="recentHuojiang" stripe style="width:100%">
        <el-table-column type="index" label="排名" width="60" align="center"></el-table-column>
        <el-table-column prop="xueshengxingming" label="获奖学生" width="100" align="center"></el-table-column>
        <el-table-column prop="xueyuanmingcheng" label="学院" min-width="120" show-overflow-tooltip></el-table-column>
        <el-table-column prop="jingsaimingcheng" label="竞赛名称" min-width="160" show-overflow-tooltip></el-table-column>
        <el-table-column prop="jingsaijib" label="级别" width="80" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" :type="jibType(scope.row.jingsaijib)">{{ scope.row.jingsaijib }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="jiangxiangdengji" label="获奖等级" width="100" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" :type="dengjiType(scope.row.jiangxiangdengji)">{{ scope.row.jiangxiangdengji }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="jiaoshixingming" label="指导教师" width="100" align="center"></el-table-column>
        <el-table-column prop="huojiangriqi" label="获奖日期" width="110" align="center"></el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import router from '@/router/router-static'
import echarts from 'echarts'

export default {
  data() {
    return {
      statCards: [
        { label: '竞赛总数', value: 0, icon: 'el-icon-trophy', bg: 'linear-gradient(135deg,#667eea,#764ba2)', path: '/jingsaixinxi' },
        { label: '报名人数', value: 0, icon: 'el-icon-user', bg: 'linear-gradient(135deg,#f093fb,#f5576c)', path: '/jingsaibaoming' },
        { label: '获奖数量', value: 0, icon: 'el-icon-medal', bg: 'linear-gradient(135deg,#4facfe,#00f2fe)', path: '/huojiang' },
        { label: '证书数量', value: 0, icon: 'el-icon-postcard', bg: 'linear-gradient(135deg,#43e97b,#38f9d7)', path: '/zhengshu' }
      ],
      recentHuojiang: []
    }
  },
  mounted() {
    this.init()
  },
  methods: {
    init() {
      if (this.$storage.get('Token')) {
        this.$http({ url: `${this.$storage.get('sessionTable')}/session`, method: 'get' }).then(({ data }) => {
          if (data && data.code != 0) { router.push({ name: 'login' }) }
          else { this.loadDashboard() }
        })
      } else {
        router.push({ name: 'login' })
      }
    },
    loadDashboard() {
      // 竞赛总数
      this.$http({ url: 'jingsaixinxi/page', method: 'get', params: { page: 1, limit: 1 } }).then(({ data }) => {
        if (data?.code === 0) this.statCards[0].value = data.data.total
      })
      // 报名人数
      this.$http({ url: 'jingsaibaoming/page', method: 'get', params: { page: 1, limit: 1 } }).then(({ data }) => {
        if (data?.code === 0) this.statCards[1].value = data.data.total
      })
      // 获奖数量
      this.$http({ url: 'huojiang/page', method: 'get', params: { page: 1, limit: 1 } }).then(({ data }) => {
        if (data?.code === 0) this.statCards[2].value = data.data.total
      })
      // 证书数量
      this.$http({ url: 'zhengshu/page', method: 'get', params: { page: 1, limit: 1 } }).then(({ data }) => {
        if (data?.code === 0) this.statCards[3].value = data.data.total
      })
      // 近期获奖
      this.$http({ url: 'huojiang/page', method: 'get', params: { page: 1, limit: 8, sort: 'id', order: 'desc' } }).then(({ data }) => {
        if (data?.code === 0) this.recentHuojiang = data.data.list || []
      })
      // 图表
      this.$http({ url: 'huojiang/tongji/xueyuan', method: 'get' }).then(({ data }) => {
        if (data?.code === 0) this.renderBar(data.data)
      })
      this.$http({ url: 'huojiang/tongji/dengji', method: 'get' }).then(({ data }) => {
        if (data?.code === 0) this.renderPie(data.data)
      })
    },
    renderBar(data) {
      if (!this.$refs.chartBar || !data?.length) return
      const chart = echarts.init(this.$refs.chartBar)
      chart.setOption({
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: data.map(d => d.name), axisLabel: { rotate: 20 } },
        yAxis: { type: 'value' },
        series: [{
          data: data.map(d => d.value),
          type: 'bar',
          itemStyle: { color: { type: 'linear', x: 0, y: 0, x2: 0, y2: 1, colorStops: [{ offset: 0, color: '#667eea' }, { offset: 1, color: '#764ba2' }] } },
          barMaxWidth: 60, label: { show: true, position: 'top' }
        }],
        grid: { left: '3%', right: '4%', bottom: '15%', containLabel: true }
      })
    },
    renderPie(data) {
      if (!this.$refs.chartPie || !data?.length) return
      const chart = echarts.init(this.$refs.chartPie)
      const colors = ['#f5576c', '#f5a623', '#4facfe', '#43e97b', '#667eea']
      chart.setOption({
        tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
        legend: { bottom: '2%', left: 'center' },
        series: [{
          type: 'pie', radius: ['35%', '65%'],
          data: data.map((d, i) => ({ ...d, itemStyle: { color: colors[i % colors.length] } })),
          label: { formatter: '{b}\n{c}个' }
        }]
      })
    },
    jibType(jib) {
      return { '国际级': 'danger', '国家级': 'warning', '省级': 'primary', '校级': 'info' }[jib] || 'info'
    },
    dengjiType(dengji) {
      return { '一等奖': 'danger', '特等奖': 'danger', '二等奖': 'warning', '三等奖': 'primary', '优秀奖': 'info' }[dengji] || 'info'
    }
  }
}
</script>

<style scoped>
.content { padding: 20px; min-height: 100%; }

.welcome-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  padding: 30px 40px;
  color: #fff;
  margin-bottom: 8px;
}
.welcome-title { font-size: 24px; font-weight: bold; display: flex; align-items: center; }
.welcome-sub { font-size: 14px; opacity: 0.8; margin-top: 8px; padding-left: 48px; }

.stat-card {
  border-radius: 12px; padding: 20px 16px;
  color: #fff; display: flex; align-items: center;
  box-shadow: 0 4px 20px rgba(0,0,0,0.12);
  cursor: pointer; transition: transform 0.2s, box-shadow 0.2s;
  margin-bottom: 0;
}
.stat-card:hover { transform: translateY(-3px); box-shadow: 0 8px 25px rgba(0,0,0,0.2); }
.stat-icon-wrap { font-size: 36px; margin-right: 16px; opacity: 0.9; }
.stat-info { flex: 1; }
.stat-num { font-size: 28px; font-weight: bold; }
.stat-label { font-size: 13px; opacity: 0.85; margin-top: 2px; }
.stat-arrow { font-size: 18px; opacity: 0.6; }
</style>
