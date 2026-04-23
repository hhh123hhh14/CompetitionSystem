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

    <!-- 通知弹窗：报名即将截止 + 竞赛信息更新 -->
    <el-dialog :visible.sync="noticeVisible" title="" width="680px" top="8vh" :close-on-click-modal="false" custom-class="notice-dialog">
      <div class="notice-header">
        <i class="el-icon-bell notice-bell"></i>
        <span>系统通知</span>
      </div>
      <!-- 报名即将截止 -->
      <div v-if="deadlineList.length" class="notice-section">
        <div class="notice-section-title">
          <i class="el-icon-alarm-clock" style="color:#e6a23c"></i> 报名即将截止
        </div>
        <div v-for="item in deadlineList" :key="'dl-'+item.id" class="notice-item deadline-item" @click="goDetail(item)">
          <div class="notice-item-left">
            <div class="notice-item-name">{{ item.jingsaimingcheng }}</div>
            <div class="notice-item-meta">
              <el-tag size="mini" :type="jibType(item.jingsaijib)" v-if="item.jingsaijib" effect="plain">{{ item.jingsaijib }}</el-tag>
              <span v-if="item.jingsaileixing" class="meta-text">{{ item.jingsaileixing }}</span>
              <span v-if="item.jiaoshixingming" class="meta-text">负责人：{{ item.jiaoshixingming }}</span>
            </div>
          </div>
          <div class="notice-item-right">
            <div class="countdown" :class="getCountdownClass(item)">
              <i class="el-icon-time"></i> {{ getCountdownText(item) }}
            </div>
            <div class="deadline-date">截止：{{ formatDate(item.baomingjiezhi) }}</div>
          </div>
        </div>
      </div>
      <!-- 最近更新的竞赛 -->
      <div v-if="recentUpdateList.length" class="notice-section">
        <div class="notice-section-title">
          <i class="el-icon-refresh" style="color:#409eff"></i> 最近更新的竞赛
        </div>
        <div v-for="item in recentUpdateList" :key="'up-'+item.id" class="notice-item update-item" @click="goDetail(item)">
          <div class="notice-item-left">
            <div class="notice-item-name">{{ item.jingsaimingcheng }}</div>
            <div class="notice-item-meta">
              <el-tag size="mini" :type="jibType(item.jingsaijib)" v-if="item.jingsaijib" effect="plain">{{ item.jingsaijib }}</el-tag>
              <el-tag size="mini" v-if="item.zhuangtai" :type="zhuangtaiType(item.zhuangtai)" effect="plain">{{ item.zhuangtai }}</el-tag>
              <span v-if="item.jingsaileixing" class="meta-text">{{ item.jingsaileixing }}</span>
            </div>
          </div>
          <div class="notice-item-right">
            <div class="update-time">
              <i class="el-icon-time"></i> {{ formatDateTime(item.addtime) }}
            </div>
            <el-tag size="mini" type="success" effect="dark">NEW</el-tag>
          </div>
        </div>
      </div>
      <!-- 教师：待审核提醒 -->
      <div v-if="pendingReviewCount > 0" class="notice-section">
        <div class="notice-section-title">
          <i class="el-icon-warning" style="color:#e6a23c"></i> 待处理事项
        </div>
        <div class="notice-item deadline-item" @click="noticeVisible = false; $router.push('/jingsaibaoming')">
          <div class="notice-item-left">
            <div class="notice-item-name">您有 {{ pendingReviewCount }} 条报名申请待审核</div>
            <div class="notice-item-meta">
              <span class="meta-text">请及时处理，避免影响学生参赛</span>
            </div>
          </div>
          <div class="notice-item-right">
            <el-button type="warning" size="mini" icon="el-icon-s-check">前往审核</el-button>
          </div>
        </div>
      </div>
      <!-- 学生：审核结果通知 -->
      <div v-if="reviewResultList.length" class="notice-section">
        <div class="notice-section-title">
          <i class="el-icon-s-check" style="color:#67c23a"></i> 报名审核结果
        </div>
        <div v-for="item in reviewResultList" :key="'rv-'+item.id" class="notice-item"
             :class="item.sfsh === '已通过' ? 'update-item' : 'deadline-item'"
             @click="noticeVisible = false; $router.push('/jingsaibaoming')">
          <div class="notice-item-left">
            <div class="notice-item-name">{{ item.jingsaimingcheng }}</div>
            <div class="notice-item-meta">
              <el-tag size="mini" :type="item.sfsh === '已通过' ? 'success' : 'danger'" effect="plain">{{ item.sfsh }}</el-tag>
              <span v-if="item.shhf" class="meta-text">回复：{{ item.shhf }}</span>
            </div>
          </div>
          <div class="notice-item-right">
            <el-tag size="mini" :type="item.sfsh === '已通过' ? 'success' : 'danger'" effect="dark">
              {{ item.sfsh === '已通过' ? '通过' : '驳回' }}
            </el-tag>
          </div>
        </div>
      </div>
      <!-- 学生：获奖通知 -->
      <div v-if="awardList.length" class="notice-section">
        <div class="notice-section-title">
          <i class="el-icon-trophy" style="color:#f5a623"></i> 获奖通知
        </div>
        <div v-for="item in awardList" :key="'aw-'+item.id" class="notice-item update-item"
             @click="noticeVisible = false; $router.push('/huojiang')">
          <div class="notice-item-left">
            <div class="notice-item-name">{{ item.jingsaimingcheng }}</div>
            <div class="notice-item-meta">
              <el-tag size="mini" :type="dengjiType(item.jiangxiangdengji)" effect="plain">{{ item.jiangxiangdengji }}</el-tag>
              <el-tag size="mini" :type="jibType(item.jingsaijib)" v-if="item.jingsaijib" effect="plain">{{ item.jingsaijib }}</el-tag>
              <span v-if="item.jiangxiangmingcheng" class="meta-text">{{ item.jiangxiangmingcheng }}</span>
            </div>
          </div>
          <div class="notice-item-right">
            <el-tag size="mini" type="warning" effect="dark">
              <i class="el-icon-trophy"></i> 恭喜获奖
            </el-tag>
          </div>
        </div>
      </div>
      <!-- 无通知 -->
      <div v-if="!deadlineList.length && !recentUpdateList.length && !reviewResultList.length && !awardList.length && pendingReviewCount === 0" class="notice-empty">
        <i class="el-icon-circle-check" style="font-size:48px;color:#67c23a"></i>
        <p>暂无新通知，一切正常！</p>
      </div>
      <div slot="footer">
        <el-button type="primary" @click="noticeVisible = false" icon="el-icon-check">我知道了</el-button>
        <el-button v-if="deadlineList.length" type="warning" @click="noticeVisible = false; $router.push('/jingsaixinxi')" icon="el-icon-s-promotion">前往报名</el-button>
        <el-button v-if="pendingReviewCount > 0" type="warning" @click="noticeVisible = false; $router.push('/jingsaibaoming')" icon="el-icon-s-check">前往审核</el-button>
      </div>
    </el-dialog>
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
      recentHuojiang: [],
      noticeVisible: false,
      deadlineList: [],
      recentUpdateList: [],
      // 角色相关通知
      reviewResultList: [],   // 学生：审核结果通知
      awardList: [],          // 学生：获奖通知
      pendingReviewCount: 0   // 教师：待审核数量
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
        if (data && data.code === 0) this.statCards[0].value = data.data.total
      })
      // 报名人数
      this.$http({ url: 'jingsaibaoming/page', method: 'get', params: { page: 1, limit: 1 } }).then(({ data }) => {
        if (data && data.code === 0) this.statCards[1].value = data.data.total
      })
      // 获奖数量
      this.$http({ url: 'huojiang/page', method: 'get', params: { page: 1, limit: 1 } }).then(({ data }) => {
        if (data && data.code === 0) this.statCards[2].value = data.data.total
      })
      // 证书数量
      this.$http({ url: 'zhengshu/page', method: 'get', params: { page: 1, limit: 1 } }).then(({ data }) => {
        if (data && data.code === 0) this.statCards[3].value = data.data.total
      })
      // 近期获奖
      this.$http({ url: 'huojiang/page', method: 'get', params: { page: 1, limit: 8, sort: 'id', order: 'desc' } }).then(({ data }) => {
        if (data && data.code === 0) this.recentHuojiang = data.data.list || []
      })
      // 图表
      this.$http({ url: 'huojiang/tongji/xueyuan', method: 'get' }).then(({ data }) => {
        if (data && data.code === 0) this.renderBar(data.data)
      })
      this.$http({ url: 'huojiang/tongji/dengji', method: 'get' }).then(({ data }) => {
        if (data && data.code === 0) this.renderPie(data.data)
      })
      // 加载通知
      this.loadNotices()
      this.loadRoleNotices()
    },
    loadNotices() {
      const now = new Date()
      const todayStr = this.toDateStr(now)
      // 7天后
      const future = new Date(now.getTime() + 7 * 24 * 60 * 60 * 1000)
      const futureStr = this.toDateStr(future)
      // 查询报名截止时间在今天~7天后的竞赛（即将截止）
      this.$http({
        url: 'jingsaixinxi/list', method: 'get',
        params: { page: 1, limit: 20, baomingjiezhi_start: todayStr, baomingjiezhi_end: futureStr, sort: 'baomingjiezhi', order: 'asc' }
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.deadlineList = (data.data.list || []).filter(item => item.baomingjiezhi)
        }
        this.checkShowNotice()
      }).catch(() => { this.checkShowNotice() })
      // 查询最近3天内新增/更新的竞赛
      const past3 = new Date(now.getTime() - 3 * 24 * 60 * 60 * 1000)
      const past3Str = this.toDateStr(past3)
      this.$http({
        url: 'jingsaixinxi/list', method: 'get',
        params: { page: 1, limit: 10, addtime_start: past3Str, addtime_end: futureStr, sort: 'addtime', order: 'desc' }
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.recentUpdateList = data.data.list || []
        }
        this.checkShowNotice()
      }).catch(() => { this.checkShowNotice() })
    },
    loadRoleNotices() {
      const role = this.$storage.get('role')
      if (role === '学生') {
        // 查询审核结果：已通过或已驳回的报名记录
        this.$http({
          url: 'jingsaibaoming/page', method: 'get',
          params: { page: 1, limit: 10, sort: 'id', order: 'desc' }
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.reviewResultList = (data.data.list || []).filter(
              item => item.sfsh === '已通过' || item.sfsh === '已驳回'
            ).slice(0, 5)
          }
          this.checkShowNotice()
        }).catch(() => { this.checkShowNotice() })
        // 查询获奖通知
        this.$http({
          url: 'huojiang/page', method: 'get',
          params: { page: 1, limit: 5, sort: 'id', order: 'desc' }
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.awardList = data.data.list || []
          }
          this.checkShowNotice()
        }).catch(() => { this.checkShowNotice() })
      } else if (role === '教师') {
        // 查询待审核的报名数量
        this.$http({
          url: 'jingsaibaoming/page', method: 'get',
          params: { page: 1, limit: 1, sfsh: '待审核' }
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.pendingReviewCount = data.data.total || 0
          }
          this.checkShowNotice()
        }).catch(() => { this.checkShowNotice() })
      }
    },
    checkShowNotice() {
      if (this.deadlineList.length > 0 || this.recentUpdateList.length > 0
        || this.reviewResultList.length > 0 || this.awardList.length > 0
        || this.pendingReviewCount > 0) {
        this.noticeVisible = true
      }
    },
    toDateStr(d) {
      const y = d.getFullYear()
      const m = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      return `${y}-${m}-${day}`
    },
    formatDate(val) {
      if (!val) return '-'
      return val.substring(0, 10)
    },
    formatDateTime(val) {
      if (!val) return '-'
      return val.substring(0, 16)
    },
    getCountdownText(item) {
      if (!item.baomingjiezhi) return ''
      const end = new Date(item.baomingjiezhi.replace(/-/g, '/'))
      const now = new Date()
      const diff = end.getTime() - now.getTime()
      if (diff <= 0) return '已截止'
      const days = Math.floor(diff / (24 * 60 * 60 * 1000))
      const hours = Math.floor((diff % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000))
      if (days > 0) return `剩余 ${days} 天 ${hours} 小时`
      return `剩余 ${hours} 小时`
    },
    getCountdownClass(item) {
      if (!item.baomingjiezhi) return ''
      const end = new Date(item.baomingjiezhi.replace(/-/g, '/'))
      const diff = end.getTime() - new Date().getTime()
      const days = diff / (24 * 60 * 60 * 1000)
      if (days <= 1) return 'urgent'
      if (days <= 3) return 'warning'
      return 'normal'
    },
    zhuangtaiType(val) {
      return { '报名中': 'success', '进行中': 'primary', '已结束': 'info' }[val] || 'info'
    },
    goDetail(item) {
      this.noticeVisible = false
      this.$router.push('/jingsaixinxi')
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

/* 通知弹窗 */
.notice-header {
  text-align: center;
  margin: -10px 0 20px;
  font-size: 22px;
  font-weight: 700;
  color: #303133;
}
.notice-bell {
  font-size: 28px;
  color: #e6a23c;
  margin-right: 8px;
  animation: bellRing 2s ease-in-out infinite;
}
@keyframes bellRing {
  0%, 100% { transform: rotate(0); }
  10% { transform: rotate(15deg); }
  20% { transform: rotate(-12deg); }
  30% { transform: rotate(8deg); }
  40% { transform: rotate(-5deg); }
  50% { transform: rotate(0); }
}
.notice-section {
  margin-bottom: 20px;
}
.notice-section-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 12px;
  padding-bottom: 8px;
  border-bottom: 2px solid #ebeef5;
}
.notice-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 14px 16px;
  margin-bottom: 8px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s;
}
.deadline-item {
  background: linear-gradient(135deg, #fff9ef, #fff3e0);
  border: 1px solid #ffecd2;
}
.deadline-item:hover {
  background: linear-gradient(135deg, #fff3e0, #ffe8cc);
  box-shadow: 0 2px 12px rgba(230,162,60,0.15);
}
.update-item {
  background: linear-gradient(135deg, #f0f8ff, #e8f4fd);
  border: 1px solid #d9ecff;
}
.update-item:hover {
  background: linear-gradient(135deg, #e8f4fd, #d9ecff);
  box-shadow: 0 2px 12px rgba(64,158,255,0.15);
}
.notice-item-left { flex: 1; }
.notice-item-name {
  font-size: 15px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 4px;
}
.notice-item-meta {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
}
.meta-text {
  font-size: 12px;
  color: #909399;
}
.notice-item-right {
  text-align: right;
  flex-shrink: 0;
  margin-left: 16px;
}
.countdown {
  font-size: 14px;
  font-weight: 600;
  margin-bottom: 4px;
}
.countdown.urgent { color: #f56c6c; }
.countdown.warning { color: #e6a23c; }
.countdown.normal { color: #67c23a; }
.deadline-date {
  font-size: 12px;
  color: #909399;
}
.update-time {
  font-size: 13px;
  color: #606266;
  margin-bottom: 4px;
}
.notice-empty {
  text-align: center;
  padding: 40px 0;
  color: #909399;
}
.notice-empty p {
  margin-top: 12px;
  font-size: 15px;
}
</style>
