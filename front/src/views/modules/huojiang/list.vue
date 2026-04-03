<template>
  <div class="main-content">
    <div v-if="showFlag">
      <el-form :inline="true" :model="searchForm" class="form-content">
        <el-row :gutter="20" class="slt">
          <el-form-item label="竞赛名称">
            <el-input v-model="searchForm.jingsaimingcheng" placeholder="竞赛名称" clearable prefix-icon="el-icon-search"></el-input>
          </el-form-item>
          <el-form-item label="获奖学生">
            <el-input v-model="searchForm.xueshengxingming" placeholder="学生姓名" clearable prefix-icon="el-icon-user"></el-input>
          </el-form-item>
          <el-form-item label="学院">
            <el-input v-model="searchForm.xueyuanmingcheng" placeholder="学院名称" clearable></el-input>
          </el-form-item>
          <el-form-item label="奖项等级">
            <el-select v-model="searchForm.jiangxiangdengji" placeholder="全部" clearable style="width:130px">
              <el-option label="一等奖" value="一等奖"></el-option>
              <el-option label="二等奖" value="二等奖"></el-option>
              <el-option label="三等奖" value="三等奖"></el-option>
              <el-option label="优秀奖" value="优秀奖"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="竞赛级别">
            <el-select v-model="searchForm.jingsaijib" placeholder="全部" clearable style="width:130px">
              <el-option label="校级" value="校级"></el-option>
              <el-option label="省级" value="省级"></el-option>
              <el-option label="国家级" value="国家级"></el-option>
              <el-option label="国际级" value="国际级"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="search()">查询</el-button>
            <el-button icon="el-icon-refresh" @click="resetSearch()">重置</el-button>
          </el-form-item>
        </el-row>
        <el-row class="ad">
          <el-form-item>
            <el-button v-if="isAuth('huojiang','新增')" type="success" icon="el-icon-plus" @click="addOrUpdateHandler()">新增获奖</el-button>
            <el-button v-if="isAuth('huojiang','删除')" type="danger" icon="el-icon-delete" :disabled="dataListSelections.length <= 0" @click="deleteHandler()">批量删除</el-button>
            <el-button type="warning" icon="el-icon-data-analysis" @click="$router.push('/tongji')">获奖统计</el-button>
          </el-form-item>
        </el-row>
      </el-form>

      <!-- 统计卡片 -->
      <el-row :gutter="16" style="margin-bottom:16px">
        <el-col :span="6">
          <div class="stat-card" style="background:linear-gradient(135deg,#667eea,#764ba2)">
            <div class="stat-num">{{ totalPage }}</div>
            <div class="stat-label">获奖总数</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card" style="background:linear-gradient(135deg,#f093fb,#f5576c)">
            <div class="stat-num">{{ guojiaji }}</div>
            <div class="stat-label">国家级获奖</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card" style="background:linear-gradient(135deg,#4facfe,#00f2fe)">
            <div class="stat-num">{{ shengji }}</div>
            <div class="stat-label">省级获奖</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card" style="background:linear-gradient(135deg,#43e97b,#38f9d7)">
            <div class="stat-num">{{ yidengjang }}</div>
            <div class="stat-label">一等奖数量</div>
          </div>
        </el-col>
      </el-row>

      <div class="table-content">
        <el-table
          :data="dataList"
          border
          stripe
          v-loading="dataListLoading"
          @selection-change="selectionChangeHandler"
          style="width:100%">
          <el-table-column type="selection" width="50" align="center"></el-table-column>
          <el-table-column type="index" label="序号" width="60" align="center"></el-table-column>
          <el-table-column prop="jingsaimingcheng" label="竞赛名称" min-width="160" show-overflow-tooltip></el-table-column>
          <el-table-column prop="jingsaijib" label="级别" width="90" align="center">
            <template slot-scope="scope">
              <el-tag :type="jibType(scope.row.jingsaijib)" size="mini">{{ scope.row.jingsaijib }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="xueshengxingming" label="获奖学生" width="100" align="center"></el-table-column>
          <el-table-column prop="xueyuanmingcheng" label="学院" min-width="120" show-overflow-tooltip></el-table-column>
          <el-table-column prop="tuanduimingcheng" label="团队名称" min-width="100" show-overflow-tooltip></el-table-column>
          <el-table-column prop="jiangxiangmingcheng" label="奖项名称" min-width="120" show-overflow-tooltip></el-table-column>
          <el-table-column prop="jiangxiangdengji" label="获奖等级" width="100" align="center">
            <template slot-scope="scope">
              <el-tag :type="dengjiType(scope.row.jiangxiangdengji)" size="mini">{{ scope.row.jiangxiangdengji }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="jiaoshixingming" label="指导教师" width="100" align="center"></el-table-column>
          <el-table-column prop="huojiangriqi" label="获奖日期" width="110" align="center"></el-table-column>
          <el-table-column prop="sfgs" label="公示状态" width="90" align="center">
            <template slot-scope="scope">
              <el-tag :type="scope.row.sfgs === '是' ? 'success' : 'info'" size="mini">
                {{ scope.row.sfgs === '是' ? '已公示' : '未公示' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="260" align="center" fixed="right">
            <template slot-scope="scope">
              <el-button type="success" size="mini" icon="el-icon-view" @click="addOrUpdateHandler(scope.row.id,'info')">详情</el-button>
              <el-button v-if="isAuth('huojiang','修改')" type="primary" size="mini" icon="el-icon-edit" @click="addOrUpdateHandler(scope.row.id)">修改</el-button>
              <el-button type="warning" size="mini" icon="el-icon-medal" @click="generateCert(scope.row)">生成证书</el-button>
              <el-button v-if="isAuth('huojiang','公示')" type="info" size="mini" @click="toggleGongshi(scope.row)">
                {{ scope.row.sfgs === '是' ? '取消公示' : '公示' }}
              </el-button>
              <el-button v-if="isAuth('huojiang','删除')" type="danger" size="mini" icon="el-icon-delete" @click="deleteHandler(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          @size-change="sizeChangeHandle"
          @current-change="currentChangeHandle"
          :current-page="pageIndex"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pageSize"
          :total="totalPage"
          layout="total, sizes, prev, pager, next, jumper"
          style="margin-top:16px;text-align:right">
        </el-pagination>
      </div>
    </div>
    <add-or-update v-if="addOrUpdateFlag" :parent="this" ref="addOrUpdate"></add-or-update>
  </div>
</template>

<script>
import AddOrUpdate from './add-or-update'
export default {
  data() {
    return {
      searchForm: {},
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      guojiaji: 0,
      shengji: 0,
      yidengjang: 0,
      dataListLoading: false,
      dataListSelections: [],
      showFlag: true,
      addOrUpdateFlag: false
    }
  },
  components: { AddOrUpdate },
  created() {
    this.getDataList()
    this.getStats()
  },
  methods: {
    jibType(jib) {
      const map = { '国际级': 'danger', '国家级': 'warning', '省级': 'primary', '校级': 'info' }
      return map[jib] || 'info'
    },
    dengjiType(dengji) {
      const map = { '一等奖': 'danger', '二等奖': 'warning', '三等奖': 'primary', '优秀奖': 'info' }
      return map[dengji] || 'info'
    },
    search() { this.pageIndex = 1; this.getDataList() },
    resetSearch() { this.searchForm = {}; this.search() },
    getStats() {
      this.$http({ url: 'huojiang/tongji/jib', method: 'get' }).then(({ data }) => {
        if (data && data.code === 0) {
          const list = data.data || []
          list.forEach(item => {
            if (item.name === '国家级') this.guojiaji = item.value
            if (item.name === '省级') this.shengji = item.value
          })
        }
      }).catch(() => {})
      this.$http({ url: 'huojiang/tongji/dengji', method: 'get' }).then(({ data }) => {
        if (data && data.code === 0) {
          const list = data.data || []
          const item = list.find(i => i.name === '一等奖')
          if (item) this.yidengjang = item.value
        }
      }).catch(() => {})
    },
    getDataList() {
      this.dataListLoading = true
      let params = { page: this.pageIndex, limit: this.pageSize, sort: 'id' }
      if (this.searchForm.jingsaimingcheng) params['jingsaimingcheng'] = '%' + this.searchForm.jingsaimingcheng + '%'
      if (this.searchForm.xueshengxingming) params['xueshengxingming'] = '%' + this.searchForm.xueshengxingming + '%'
      if (this.searchForm.xueyuanmingcheng) params['xueyuanmingcheng'] = '%' + this.searchForm.xueyuanmingcheng + '%'
      if (this.searchForm.jiangxiangdengji) params['jiangxiangdengji'] = this.searchForm.jiangxiangdengji
      if (this.searchForm.jingsaijib) params['jingsaijib'] = this.searchForm.jingsaijib
      this.$http({ url: 'huojiang/page', method: 'get', params }).then(({ data }) => {
        if (data && data.code === 0) {
          this.dataList = data.data.list || []
          this.totalPage = data.data.total || 0
        } else {
          this.dataList = []; this.totalPage = 0
        }
        this.dataListLoading = false
      }).catch(() => {
        this.dataList = []; this.totalPage = 0
        this.dataListLoading = false
      })
    },
    sizeChangeHandle(val) { this.pageSize = val; this.pageIndex = 1; this.getDataList() },
    currentChangeHandle(val) { this.pageIndex = val; this.getDataList() },
    selectionChangeHandler(val) { this.dataListSelections = val },
    addOrUpdateHandler(id, type) {
      this.showFlag = false; this.addOrUpdateFlag = true
      this.$nextTick(() => { this.$refs.addOrUpdate.init(id, type || 'else') })
    },
    toggleGongshi(row) {
      const newVal = row.sfgs === '是' ? '否' : '是'
      this.$http({ url: `huojiang/gongshi/${row.id}?sfgs=${newVal}`, method: 'get' }).then(({ data }) => {
        if (data && data.code === 0) {
          this.$message.success(newVal === '是' ? '已公示' : '已取消公示')
          this.getDataList()
        } else {
          this.$message.error(data.msg || '操作失败')
        }
      }).catch(() => { this.$message.error('网络异常') })
    },
    generateCert(row) {
      this.$confirm(`确认为 [${row.xueshengxingming}] 生成电子证书？`, '提示', {
        confirmButtonText: '确定', cancelButtonText: '取消', type: 'info'
      }).then(() => {
        this.$http({ url: `zhengshu/generate/${row.id}`, method: 'get' }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message.success('证书生成成功，请前往证书管理查看')
          } else {
            this.$message.error(data.msg || '生成失败')
          }
        }).catch(() => { this.$message.error('网络异常，生成失败') })
      }).catch(() => {})
    },
    deleteHandler(id) {
      const ids = id ? [Number(id)] : this.dataListSelections.map(item => Number(item.id))
      this.$confirm(`确定进行[${id ? '删除' : '批量删除'}]操作？`, '提示', {
        confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
      }).then(() => {
        this.$http({ url: 'huojiang/delete', method: 'post', data: ids }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message({ message: '操作成功', type: 'success', duration: 1500, onClose: () => this.search() })
          } else {
            this.$message.error(data.msg || '删除失败')
          }
        }).catch(() => { this.$message.error('网络异常，删除失败') })
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.stat-card {
  border-radius: 12px;
  padding: 20px 24px;
  color: #fff;
  text-align: center;
  box-shadow: 0 4px 15px rgba(0,0,0,0.1);
}
.stat-num { font-size: 32px; font-weight: bold; }
.stat-label { font-size: 14px; margin-top: 4px; opacity: 0.9; }
.slt { margin: 0 !important; display: flex; flex-wrap: wrap; }
.ad { margin: 8px 0 !important; display: flex; }
</style>
