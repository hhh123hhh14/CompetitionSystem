<template>
  <div class="main-content">
    <!-- 列表视图 -->
    <div v-if="showFlag">
      <!-- 搜索栏 -->
      <el-card shadow="never" class="search-card">
        <el-form :inline="true" :model="searchForm">
          <el-form-item label="竞赛名称">
            <el-input v-model="searchForm.jingsaimingcheng" placeholder="竞赛名称" clearable prefix-icon="el-icon-search" style="width:180px"></el-input>
          </el-form-item>
          <el-form-item label="获奖学生">
            <el-input v-model="searchForm.xueshengxingming" placeholder="学生姓名" clearable prefix-icon="el-icon-user" style="width:140px"></el-input>
          </el-form-item>
          <el-form-item label="学院">
            <el-input v-model="searchForm.xueyuanmingcheng" placeholder="学院名称" clearable style="width:140px"></el-input>
          </el-form-item>
          <el-form-item label="奖项等级">
            <el-select v-model="searchForm.jiangxiangdengji" placeholder="全部" clearable style="width:110px">
              <el-option label="特等奖" value="特等奖"></el-option>
              <el-option label="一等奖" value="一等奖"></el-option>
              <el-option label="二等奖" value="二等奖"></el-option>
              <el-option label="三等奖" value="三等奖"></el-option>
              <el-option label="优秀奖" value="优秀奖"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="竞赛级别">
            <el-select v-model="searchForm.jingsaijib" placeholder="全部" clearable style="width:110px">
              <el-option label="校级" value="校级"></el-option>
              <el-option label="省级" value="省级"></el-option>
              <el-option label="国家级" value="国家级"></el-option>
              <el-option label="国际级" value="国际级"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="获奖年份">
            <el-select v-model="searchForm.nianfen" placeholder="全部" clearable style="width:110px">
              <el-option v-for="y in yearList" :key="y" :label="y + '年'" :value="y"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="search()">查询</el-button>
            <el-button icon="el-icon-refresh" @click="resetSearch()">重置</el-button>
          </el-form-item>
        </el-form>
        <!-- 操作按钮行 -->
        <div class="action-row">
          <el-button v-if="isAuth('huojiang','新增')" type="success" icon="el-icon-plus" @click="addOrUpdateHandler()">新增获奖</el-button>
          <el-button v-if="isAuth('huojiang','删除')" type="danger" icon="el-icon-delete"
                     :disabled="dataListSelections.length <= 0" @click="deleteHandler()">批量删除</el-button>
          <el-button type="warning" icon="el-icon-data-analysis" @click="$router.push('/tongji')">获奖统计</el-button>
        </div>
      </el-card>

      <!-- 统计卡片 -->
      <el-row :gutter="16" style="margin:16px 0">
        <el-col :span="6">
          <div class="stat-card" style="background:linear-gradient(135deg,#667eea,#764ba2)">
            <div class="stat-icon"><i class="el-icon-trophy"></i></div>
            <div class="stat-body">
              <div class="stat-num">{{ totalPage }}</div>
              <div class="stat-label">获奖总数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card" style="background:linear-gradient(135deg,#f093fb,#f5576c)">
            <div class="stat-icon"><i class="el-icon-flag"></i></div>
            <div class="stat-body">
              <div class="stat-num">{{ guojiaji }}</div>
              <div class="stat-label">国家级获奖</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card" style="background:linear-gradient(135deg,#4facfe,#00f2fe)">
            <div class="stat-icon"><i class="el-icon-location"></i></div>
            <div class="stat-body">
              <div class="stat-num">{{ shengji }}</div>
              <div class="stat-label">省级获奖</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card" style="background:linear-gradient(135deg,#43e97b,#38f9d7)">
            <div class="stat-icon"><i class="el-icon-medal"></i></div>
            <div class="stat-body">
              <div class="stat-num">{{ yidengjang }}</div>
              <div class="stat-label">一等奖数量</div>
            </div>
          </div>
        </el-col>
      </el-row>

      <!-- 数据表格 -->
      <el-card shadow="never">
        <el-table
          :data="dataList"
          border
          stripe
          v-loading="dataListLoading"
          element-loading-text="数据加载中..."
          @selection-change="selectionChangeHandler"
          style="width:100%"
          :header-cell-style="{background:'#f5f7fa',color:'#606266',fontWeight:'600'}"
        >
          <el-table-column type="selection" width="50" align="center"></el-table-column>
          <el-table-column type="index" label="序号" width="60" align="center"></el-table-column>
          <el-table-column prop="jingsaimingcheng" label="竞赛名称" min-width="160" show-overflow-tooltip></el-table-column>
          <el-table-column prop="jingsaijib" label="级别" width="90" align="center">
            <template slot-scope="scope">
              <el-tag :type="jibType(scope.row.jingsaijib)" size="mini" effect="plain">{{ scope.row.jingsaijib }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="xueshengxingming" label="获奖学生" width="100" align="center"></el-table-column>
          <el-table-column prop="xuehao" label="学号" width="110" align="center" show-overflow-tooltip></el-table-column>
          <el-table-column prop="xueyuanmingcheng" label="学院" min-width="120" show-overflow-tooltip></el-table-column>
          <el-table-column prop="jiangxiangmingcheng" label="奖项名称" min-width="120" show-overflow-tooltip></el-table-column>
          <el-table-column prop="jiangxiangdengji" label="获奖等级" width="90" align="center">
            <template slot-scope="scope">
              <el-tag :type="dengjiType(scope.row.jiangxiangdengji)" size="mini">{{ scope.row.jiangxiangdengji }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="jiaoshixingming" label="指导教师" width="100" align="center"></el-table-column>
          <el-table-column prop="huojiangriqi" label="获奖日期" width="110" align="center"></el-table-column>
          <el-table-column label="证书" width="70" align="center">
            <template slot-scope="scope">
              <el-tooltip v-if="scope.row.zhengshu" content="已有证书图片" placement="top">
                <i class="el-icon-postcard" style="color:#67c23a;font-size:18px;cursor:pointer"
                   @click="previewCert(scope.row)"></i>
              </el-tooltip>
              <i v-else class="el-icon-postcard" style="color:#dcdfe6;font-size:18px"></i>
            </template>
          </el-table-column>
          <el-table-column prop="sfgs" label="公示状态" width="90" align="center">
            <template slot-scope="scope">
              <el-tag :type="scope.row.sfgs === '是' ? 'success' : 'info'" size="mini">
                {{ scope.row.sfgs === '是' ? '已公示' : '未公示' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="280" align="center" fixed="right">
            <template slot-scope="scope">
              <el-button type="info" size="mini" icon="el-icon-view" @click="addOrUpdateHandler(scope.row.id,'info')">详情</el-button>
              <el-button v-if="isAuth('huojiang','修改')" type="primary" size="mini" icon="el-icon-edit" @click="addOrUpdateHandler(scope.row.id)">修改</el-button>
              <el-button type="warning" size="mini" icon="el-icon-medal" @click="generateCert(scope.row)">生成证书</el-button>
              <el-dropdown v-if="isAuth('huojiang','删除') || isAuth('huojiang','公示')" size="mini" style="margin-left:4px" @command="handleCommand($event, scope.row)">
                <el-button type="danger" size="mini">
                  更多<i class="el-icon-arrow-down el-icon--right"></i>
                </el-button>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item v-if="isAuth('huojiang','公示')" command="toggle">
                    <i :class="scope.row.sfgs === '是' ? 'el-icon-close' : 'el-icon-check'"></i>
                    {{ scope.row.sfgs === '是' ? '取消公示' : '设为公示' }}
                  </el-dropdown-item>
                  <el-dropdown-item v-if="isAuth('huojiang','删除')" command="delete" style="color:#f56c6c">
                    <i class="el-icon-delete"></i> 删除
                  </el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
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
          style="margin-top:16px;text-align:right"
        ></el-pagination>
      </el-card>
    </div>

    <!-- 新增/编辑/详情视图 -->
    <add-or-update v-if="addOrUpdateFlag" :parent="this" ref="addOrUpdate"></add-or-update>

    <!-- 证书图片预览弹窗 -->
    <el-dialog :visible.sync="certPreviewVisible" title="证书图片预览" width="500px" center>
      <div style="text-align:center" v-if="previewCertRow">
        <img v-if="isImage(previewCertRow.zhengshu)" :src="fileUrl(previewCertRow.zhengshu)"
             style="max-width:100%;max-height:70vh;border-radius:4px;box-shadow:0 2px 12px rgba(0,0,0,0.2)" />
        <div v-else style="padding:40px 0">
          <i class="el-icon-document" style="font-size:60px;color:#409eff"></i>
          <p style="color:#606266;margin-top:12px">PDF文件，请点击下载查看</p>
          <el-button type="primary" icon="el-icon-download" style="margin-top:8px"
                     @click="downloadFile(previewCertRow.zhengshu)">下载查看</el-button>
        </div>
      </div>
    </el-dialog>
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
      addOrUpdateFlag: false,
      certPreviewVisible: false,
      previewCertRow: null
    }
  },
  computed: {
    yearList() {
      const currentYear = new Date().getFullYear()
      const years = []
      for (let y = currentYear; y >= currentYear - 10; y--) {
        years.push(y)
      }
      return years
    }
  },
  components: { AddOrUpdate },
  created() {
    this.getDataList()
    this.getStats()
  },
  methods: {
    jibType(jib) {
      return { '国际级': 'danger', '国家级': 'warning', '省级': 'primary', '校级': 'info' }[jib] || 'info'
    },
    dengjiType(dengji) {
      return { '特等奖': 'danger', '一等奖': 'danger', '二等奖': 'warning', '三等奖': 'primary', '优秀奖': 'info' }[dengji] || 'info'
    },
    isImage(fileName) {
      if (!fileName) return false
      return /\.(jpg|jpeg|png|gif|bmp|webp)$/i.test(fileName)
    },
    fileUrl(fileName) {
      if (!fileName) return ''
      if (fileName.startsWith('http')) return fileName
      return '/springbootrd362/file/download?fileName=' + fileName
    },
    downloadFile(fileName) {
      const link = document.createElement('a')
      link.href = this.fileUrl(fileName)
      link.download = fileName
      link.click()
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
      let params = { page: this.pageIndex, limit: this.pageSize, sort: 'id', order: 'desc' }
      if (this.searchForm.jingsaimingcheng) params['jingsaimingcheng'] = '%' + this.searchForm.jingsaimingcheng + '%'
      if (this.searchForm.xueshengxingming) params['xueshengxingming'] = '%' + this.searchForm.xueshengxingming + '%'
      if (this.searchForm.xueyuanmingcheng) params['xueyuanmingcheng'] = '%' + this.searchForm.xueyuanmingcheng + '%'
      if (this.searchForm.jiangxiangdengji) params['jiangxiangdengji'] = this.searchForm.jiangxiangdengji
      if (this.searchForm.jingsaijib) params['jingsaijib'] = this.searchForm.jingsaijib
      if (this.searchForm.nianfen) {
        params['huojiangriqi_start'] = this.searchForm.nianfen + '-01-01'
        params['huojiangriqi_end'] = this.searchForm.nianfen + '-12-31'
      }
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
    handleCommand(cmd, row) {
      if (cmd === 'toggle') this.toggleGongshi(row)
      else if (cmd === 'delete') this.deleteHandler(row.id)
    },
    toggleGongshi(row) {
      const newVal = row.sfgs === '是' ? '否' : '是'
      this.$confirm(`确认${newVal === '是' ? '公示' : '取消公示'}该获奖记录？`, '提示', {
        confirmButtonText: '确定', cancelButtonText: '取消', type: 'info'
      }).then(() => {
        this.$http({ url: `huojiang/gongshi/${row.id}?sfgs=${newVal}`, method: 'get' }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message.success(newVal === '是' ? '已设为公示' : '已取消公示')
            this.getDataList()
          } else {
            this.$message.error(data && data.msg || '操作失败')
          }
        }).catch(() => { this.$message.error('网络异常') })
      }).catch(() => {})
    },
    previewCert(row) {
      this.previewCertRow = row
      this.certPreviewVisible = true
    },
    generateCert(row) {
      this.$confirm(`确认为 [${row.xueshengxingming}] 的获奖记录生成电子证书？`, '生成电子证书', {
        confirmButtonText: '确定', cancelButtonText: '取消', type: 'info'
      }).then(() => {
        this.$http({ url: `zhengshu/generate/${row.id}`, method: 'get' }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message.success('证书生成成功！请前往【证书管理】查看')
          } else {
            this.$message.error(data && data.msg || '生成失败')
          }
        }).catch(() => { this.$message.error('网络异常，生成失败') })
      }).catch(() => {})
    },
    deleteHandler(id) {
      const ids = id ? [Number(id)] : this.dataListSelections.map(item => Number(item.id))
      this.$confirm(`确认删除选中的 ${ids.length} 条获奖记录？`, '警告', {
        confirmButtonText: '确认删除', cancelButtonText: '取消', type: 'warning'
      }).then(() => {
        this.$http({ url: 'huojiang/delete', method: 'post', data: ids }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message.success('删除成功')
            this.search()
          } else {
            this.$message.error(data && data.msg || '删除失败')
          }
        }).catch(() => { this.$message.error('网络异常，删除失败') })
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.search-card { margin-bottom: 0; }
.action-row {
  padding-top: 10px;
  border-top: 1px dashed #ebeef5;
  margin-top: 10px;
}

/* 统计卡片 */
.stat-card {
  border-radius: 12px;
  padding: 18px 20px;
  color: #fff;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.1);
  transition: transform 0.2s;
}
.stat-card:hover { transform: translateY(-2px); }
.stat-icon { font-size: 36px; opacity: 0.85; }
.stat-num { font-size: 30px; font-weight: bold; line-height: 1; }
.stat-label { font-size: 13px; margin-top: 4px; opacity: 0.9; }
</style>
