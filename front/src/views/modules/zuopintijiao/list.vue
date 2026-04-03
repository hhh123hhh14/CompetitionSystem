<template>
  <div class="main-content">
    <div v-if="showFlag">
      <el-form :inline="true" :model="searchForm" class="form-content">
        <el-row :gutter="20" class="slt">
          <el-form-item label="竞赛名称">
            <el-input v-model="searchForm.jingsaimingcheng" placeholder="竞赛名称" clearable prefix-icon="el-icon-search"></el-input>
          </el-form-item>
          <el-form-item label="作品名称">
            <el-input v-model="searchForm.zuopinmingcheng" placeholder="作品名称" clearable></el-input>
          </el-form-item>
          <el-form-item label="提交状态">
            <el-select v-model="searchForm.zhuangtai" placeholder="全部" clearable style="width:120px">
              <el-option label="已提交" value="已提交"></el-option>
              <el-option label="已修改" value="已修改"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="search()">查询</el-button>
            <el-button icon="el-icon-refresh" @click="resetSearch()">重置</el-button>
          </el-form-item>
        </el-row>
        <el-row class="ad">
          <el-form-item>
            <el-button type="success" icon="el-icon-upload" @click="addOrUpdateHandler()">提交作品</el-button>
            <el-button type="danger" icon="el-icon-delete" :disabled="dataListSelections.length <= 0" @click="deleteHandler()">批量删除</el-button>
          </el-form-item>
        </el-row>
      </el-form>

      <div class="table-content">
        <el-table :data="dataList" border stripe v-loading="dataListLoading" @selection-change="selectionChangeHandler" style="width:100%">
          <el-table-column type="selection" width="50" align="center"></el-table-column>
          <el-table-column type="index" label="序号" width="60" align="center"></el-table-column>
          <el-table-column prop="jingsaimingcheng" label="竞赛名称" min-width="160" show-overflow-tooltip></el-table-column>
          <el-table-column prop="xueshengxingming" label="提交人" width="100" align="center"></el-table-column>
          <el-table-column prop="zuopinmingcheng" label="作品名称" min-width="150" show-overflow-tooltip></el-table-column>
          <el-table-column prop="banben" label="版本" width="70" align="center">
            <template slot-scope="scope">
              <el-tag type="primary" size="mini">V{{ scope.row.banben }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="zhuangtai" label="状态" width="90" align="center">
            <template slot-scope="scope">
              <el-tag :type="scope.row.zhuangtai === '已提交' ? 'success' : 'warning'" size="mini">{{ scope.row.zhuangtai }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="tijiaoshijian" label="提交时间" width="160" align="center"></el-table-column>
          <el-table-column label="附件" width="100" align="center">
            <template slot-scope="scope">
              <el-button v-if="scope.row.fujianwenjian" type="text" icon="el-icon-download" @click="downloadFile(scope.row.fujianwenjian)">下载</el-button>
              <span v-else style="color:#ccc">无附件</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" align="center" fixed="right">
            <template slot-scope="scope">
              <el-button type="success" size="mini" icon="el-icon-view" @click="addOrUpdateHandler(scope.row.id, 'info')">详情</el-button>
              <el-button type="warning" size="mini" icon="el-icon-edit" @click="addOrUpdateHandler(scope.row.id, 'resubmit')">重新提交</el-button>
              <el-button type="danger" size="mini" icon="el-icon-delete" @click="deleteHandler(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          @size-change="sizeChangeHandle"
          @current-change="currentChangeHandle"
          :current-page="pageIndex"
          :page-sizes="[10, 20, 50]"
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
      dataList: [], pageIndex: 1, pageSize: 10, totalPage: 0,
      dataListLoading: false, dataListSelections: [],
      showFlag: true, addOrUpdateFlag: false
    }
  },
  components: { AddOrUpdate },
  created() { this.getDataList() },
  methods: {
    search() { this.pageIndex = 1; this.getDataList() },
    resetSearch() { this.searchForm = {}; this.search() },
    getDataList() {
      this.dataListLoading = true
      let params = { page: this.pageIndex, limit: this.pageSize, sort: 'id' }
      if (this.searchForm.jingsaimingcheng) params['jingsaimingcheng'] = '%' + this.searchForm.jingsaimingcheng + '%'
      if (this.searchForm.zuopinmingcheng) params['zuopinmingcheng'] = '%' + this.searchForm.zuopinmingcheng + '%'
      if (this.searchForm.zhuangtai) params['zhuangtai'] = this.searchForm.zhuangtai
      this.$http({ url: 'zuopintijiao/page', method: 'get', params }).then(({ data }) => {
        if (data && data.code === 0) {
          this.dataList = data.data.list || []; this.totalPage = data.data.total || 0
        } else { this.dataList = []; this.totalPage = 0 }
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
    downloadFile(url) { window.open(url) },
    deleteHandler(id) {
      const ids = id ? [Number(id)] : this.dataListSelections.map(i => Number(i.id))
      this.$confirm('确定删除？', '提示', { type: 'warning' }).then(() => {
        this.$http({ url: 'zuopintijiao/delete', method: 'post', data: ids }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message.success('删除成功')
            this.search()
          } else { this.$message.error(data.msg || '删除失败') }
        }).catch(() => { this.$message.error('网络异常，删除失败') })
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.slt { margin: 0 !important; display: flex; flex-wrap: wrap; }
.ad { margin: 8px 0 !important; display: flex; }
</style>
