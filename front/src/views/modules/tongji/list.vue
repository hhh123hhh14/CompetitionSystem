<template>
  <div class="tongji-wrap">
    <el-row :gutter="16" style="margin-bottom:20px">
      <el-col :span="6" v-for="item in statCards" :key="item.label">
        <div class="stat-card" :style="{background: item.bg}">
          <div class="stat-icon"><i :class="item.icon"></i></div>
          <div class="stat-info">
            <div class="stat-num">{{ item.value }}</div>
            <div class="stat-label">{{ item.label }}</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="16">
      <el-col :span="12">
        <el-card shadow="hover">
          <div slot="header"><span>按学院获奖分布</span></div>
          <div ref="chartXueyuan" style="height:300px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <div slot="header"><span>按竞赛级别分布</span></div>
          <div ref="chartJib" style="height:300px"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" style="margin-top:16px">
      <el-col :span="12">
        <el-card shadow="hover">
          <div slot="header"><span>按奖项等级分布</span></div>
          <div ref="chartDengji" style="height:300px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <div slot="header"><span>报名审核状态统计</span></div>
          <div ref="chartShenhe" style="height:300px"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" style="margin-top:16px">
      <el-col :span="24">
        <el-card shadow="hover">
          <div slot="header"><span>获奖公示榜（近期获奖）</span></div>
          <el-table :data="recentHuojiang" stripe border style="width:100%">
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
      </el-col>
    </el-row>
  </div>
</template>

<script>
import echarts from 'echarts'
export default {
  data() {
    return {
      statCards: [
        { label: '参赛总人数', value: 0, icon: 'el-icon-user', bg: 'linear-gradient(135deg,#667eea,#764ba2)' },
        { label: '获奖总数', value: 0, icon: 'el-icon-medal', bg: 'linear-gradient(135deg,#f093fb,#f5576c)' },
        { label: '竞赛总数', value: 0, icon: 'el-icon-trophy', bg: 'linear-gradient(135deg,#4facfe,#00f2fe)' },
        { label: '国家级获奖', value: 0, icon: 'el-icon-star-on', bg: 'linear-gradient(135deg,#43e97b,#38f9d7)' }
      ],
      recentHuojiang: []
    }
  },
  mounted() {
    this.loadStats()
    this.loadCharts()
    this.loadRecentHuojiang()
  },
  methods: {
    jibType(jib) {
      return { '国际级': 'danger', '国家级': 'warning', '省级': 'primary', '校级': 'info' }[jib] || 'info'
    },
    dengjiType(dengji) {
      return { '一等奖': 'danger', '特等奖': 'danger', '二等奖': 'warning', '三等奖': 'primary', '优秀奖': 'info' }[dengji] || 'info'
    },
    loadStats() {
      // 竞赛总数
      this.$http({ url: 'jingsaixinxi/page', method: 'get', params: { page: 1, limit: 1 } }).then(({ data }) => {
        if (data?.code === 0) this.statCards[2].value = data.data.total
      })
      // 获奖总数 & 国家级
      this.$http({ url: 'huojiang/page', method: 'get', params: { page: 1, limit: 1 } }).then(({ data }) => {
        if (data?.code === 0) this.statCards[1].value = data.data.total
      })
      this.$http({ url: 'huojiang/tongji/jib', method: 'get' }).then(({ data }) => {
        if (data?.code === 0) {
          const gj = (data.data || []).find(i => i.name === '国家级')
          if (gj) this.statCards[3].value = gj.value
        }
      })
      // 参赛人数
      this.$http({ url: 'jingsaibaoming/page', method: 'get', params: { page: 1, limit: 1 } }).then(({ data }) => {
        if (data?.code === 0) this.statCards[0].value = data.data.total
      })
    },
    loadRecentHuojiang() {
      this.$http({ url: 'huojiang/page', method: 'get', params: { page: 1, limit: 10, sort: 'huojiangriqi', order: 'desc' } }).then(({ data }) => {
        if (data?.code === 0) this.recentHuojiang = data.data.list || []
      })
    },
    loadCharts() {
      this.$http({ url: 'huojiang/tongji/xueyuan', method: 'get' }).then(({ data }) => {
        if (data?.code === 0) this.renderBarChart(this.$refs.chartXueyuan, data.data, '各学院获奖数量')
      })
      this.$http({ url: 'huojiang/tongji/jib', method: 'get' }).then(({ data }) => {
        if (data?.code === 0) this.renderPieChart(this.$refs.chartJib, data.data, '竞赛级别分布')
      })
      this.$http({ url: 'huojiang/tongji/dengji', method: 'get' }).then(({ data }) => {
        if (data?.code === 0) this.renderPieChart(this.$refs.chartDengji, data.data, '奖项等级分布')
      })
      // 审核状态统计
      Promise.all([
        this.$http({ url: 'jingsaibaoming/page', method: 'get', params: { page: 1, limit: 1, sfsh: '已通过' } }),
        this.$http({ url: 'jingsaibaoming/page', method: 'get', params: { page: 1, limit: 1, sfsh: '待审核' } }),
        this.$http({ url: 'jingsaibaoming/page', method: 'get', params: { page: 1, limit: 1, sfsh: '已驳回' } })
      ]).then(([r1, r2, r3]) => {
        const shenheData = [
          { name: '已通过', value: r1.data?.data?.total || 0 },
          { name: '待审核', value: r2.data?.data?.total || 0 },
          { name: '已驳回', value: r3.data?.data?.total || 0 }
        ]
        this.renderPieChart(this.$refs.chartShenhe, shenheData, '报名审核状态')
      })
    },
    renderBarChart(el, data, title) {
      if (!el || !data?.length) return
      const chart = echarts.init(el)
      chart.setOption({
        title: { text: title, left: 'center', textStyle: { fontSize: 14 } },
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: data.map(d => d.name), axisLabel: { rotate: 30 } },
        yAxis: { type: 'value' },
        series: [{
          data: data.map(d => d.value),
          type: 'bar',
          itemStyle: { color: { type: 'linear', x: 0, y: 0, x2: 0, y2: 1, colorStops: [{ offset: 0, color: '#667eea' }, { offset: 1, color: '#764ba2' }] } },
          barMaxWidth: 50
        }],
        grid: { left: '3%', right: '4%', bottom: '15%', containLabel: true }
      })
    },
    renderPieChart(el, data, title) {
      if (!el || !data?.length) return
      const chart = echarts.init(el)
      const colors = ['#667eea', '#f5576c', '#4facfe', '#43e97b', '#f093fb', '#ff9a44']
      chart.setOption({
        title: { text: title, left: 'center', textStyle: { fontSize: 14 } },
        tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
        legend: { bottom: '5%', left: 'center' },
        series: [{
          type: 'pie',
          radius: ['30%', '60%'],
          data: data.map((d, i) => ({ ...d, itemStyle: { color: colors[i % colors.length] } })),
          label: { formatter: '{b}\n{c}个' }
        }]
      })
    }
  }
}
</script>

<style scoped>
.tongji-wrap { padding: 16px; }
.stat-card {
  border-radius: 12px; padding: 20px; color: #fff;
  display: flex; align-items: center; gap: 16px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.12); margin-bottom: 0;
}
.stat-icon { font-size: 40px; opacity: 0.9; }
.stat-num { font-size: 28px; font-weight: bold; }
.stat-label { font-size: 13px; opacity: 0.85; margin-top: 2px; }
</style>
