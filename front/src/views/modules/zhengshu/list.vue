<template>
  <div class="main-content">
    <div v-if="showFlag">
      <el-form :inline="true" :model="searchForm" class="form-content">
        <el-row class="slt">
          <el-form-item label="竞赛名称">
            <el-input v-model="searchForm.jingsaimingcheng" placeholder="竞赛名称" clearable prefix-icon="el-icon-search"></el-input>
          </el-form-item>
          <el-form-item label="学生姓名">
            <el-input v-model="searchForm.xueshengxingming" placeholder="学生姓名" clearable></el-input>
          </el-form-item>
          <el-form-item label="证书编号">
            <el-input v-model="searchForm.zhengshubianhao" placeholder="证书编号" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="search()">查询</el-button>
            <el-button icon="el-icon-refresh" @click="resetSearch()">重置</el-button>
          </el-form-item>
        </el-row>
      </el-form>

      <div class="table-content">
        <el-table :data="dataList" border stripe v-loading="dataListLoading" style="width:100%">
          <el-table-column type="index" label="序号" width="60" align="center"></el-table-column>
          <el-table-column prop="zhengshubianhao" label="证书编号" width="160" align="center"></el-table-column>
          <el-table-column prop="jingsaimingcheng" label="竞赛名称" min-width="160" show-overflow-tooltip></el-table-column>
          <el-table-column prop="jingsaijib" label="竞赛级别" width="90" align="center">
            <template slot-scope="scope">
              <el-tag size="mini" :type="jibType(scope.row.jingsaijib)">{{ scope.row.jingsaijib }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="xueshengxingming" label="获奖者" width="100" align="center"></el-table-column>
          <el-table-column prop="xueyuanmingcheng" label="学院" min-width="120" show-overflow-tooltip></el-table-column>
          <el-table-column prop="jiangxiangdengji" label="获奖等级" width="100" align="center">
            <template slot-scope="scope">
              <el-tag :type="dengjiType(scope.row.jiangxiangdengji)" size="mini">{{ scope.row.jiangxiangdengji }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="jiaoshixingming" label="指导教师" width="100" align="center"></el-table-column>
          <el-table-column prop="huojiangriqi" label="获奖日期" width="110" align="center"></el-table-column>
          <el-table-column prop="shenqingshijian" label="生成时间" width="160" align="center"></el-table-column>
          <el-table-column label="操作" width="200" align="center" fixed="right">
            <template slot-scope="scope">
              <el-button type="primary" size="mini" icon="el-icon-view" @click="viewCert(scope.row)">查看</el-button>
              <el-button type="success" size="mini" icon="el-icon-printer" @click="printCert(scope.row)">打印/下载</el-button>
              <el-button v-if="isAuth('zhengshu','删除')" type="danger" size="mini" icon="el-icon-delete" @click="deleteHandler(scope.row.id)">删除</el-button>
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

    <!-- 证书预览弹窗 -->
    <el-dialog :visible.sync="certDialogVisible" title="电子证书预览" width="680px" center>
      <div class="cert-wrap" id="cert-print-area" v-if="currentCert">
        <div class="cert-border">
          <div class="cert-title">荣 誉 证 书</div>
          <div class="cert-subtitle">{{ currentCert.jingsaimingcheng }}</div>
          <div class="cert-body">
            <p>兹证明 <strong>{{ currentCert.xueshengxingming }}</strong>（学号：{{ currentCert.xuehao }}）</p>
            <p>来自 <strong>{{ currentCert.xueyuanmingcheng }}</strong></p>
            <p>在本次竞赛中荣获</p>
            <div class="cert-award">{{ currentCert.jiangxiangmingcheng }}</div>
            <div class="cert-level">（{{ currentCert.jiangxiangdengji }}）</div>
            <p class="cert-teacher">指导教师：{{ currentCert.jiaoshixingming }}</p>
          </div>
          <div class="cert-footer">
            <div class="cert-no">证书编号：{{ currentCert.zhengshubianhao }}</div>
            <div class="cert-date">{{ currentCert.huojiangriqi }}</div>
          </div>
        </div>
      </div>
      <div slot="footer">
        <el-button type="primary" icon="el-icon-printer" @click="doPrint()">打印证书</el-button>
        <el-button @click="certDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      searchForm: {},
      dataList: [], pageIndex: 1, pageSize: 10, totalPage: 0,
      dataListLoading: false,
      showFlag: true,
      certDialogVisible: false,
      currentCert: null
    }
  },
  created() { this.getDataList() },
  methods: {
    jibType(jib) {
      return { '国际级': 'danger', '国家级': 'warning', '省级': 'primary', '校级': 'info' }[jib] || 'info'
    },
    dengjiType(dengji) {
      return { '一等奖': 'danger', '特等奖': 'danger', '二等奖': 'warning', '三等奖': 'primary', '优秀奖': 'info' }[dengji] || 'info'
    },
    search() { this.pageIndex = 1; this.getDataList() },
    resetSearch() { this.searchForm = {}; this.search() },
    getDataList() {
      this.dataListLoading = true
      let params = { page: this.pageIndex, limit: this.pageSize, sort: 'id' }
      if (this.searchForm.jingsaimingcheng) params['jingsaimingcheng'] = '%' + this.searchForm.jingsaimingcheng + '%'
      if (this.searchForm.xueshengxingming) params['xueshengxingming'] = '%' + this.searchForm.xueshengxingming + '%'
      if (this.searchForm.zhengshubianhao) params['zhengshubianhao'] = '%' + this.searchForm.zhengshubianhao + '%'
      this.$http({ url: 'zhengshu/page', method: 'get', params }).then(({ data }) => {
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
    viewCert(row) { this.currentCert = row; this.certDialogVisible = true },
    printCert(row) {
      this.currentCert = row
      this.certDialogVisible = true
      // 等待对话框动画完成后再打印（v-if + el-dialog 需要两个tick + 动画时间）
      setTimeout(() => { this.doPrint() }, 400)
    },
    doPrint() {
      const el = document.getElementById('cert-print-area')
      if (!el) return
      const w = window.open('', '_blank')
      w.document.write(`
        <html><head><title>电子证书</title>
        <style>
          body { margin: 0; display: flex; justify-content: center; align-items: center; min-height: 100vh; font-family: "仿宋", serif; }
          .cert-wrap { width: 600px; padding: 40px; }
          .cert-border { border: 8px double #b8860b; border-radius: 8px; padding: 40px 50px; text-align: center; background: linear-gradient(135deg, #fffdf0, #fff8dc); box-shadow: 0 0 30px rgba(184,134,11,0.3); }
          .cert-title { font-size: 42px; font-weight: bold; color: #8b0000; letter-spacing: 8px; margin-bottom: 10px; }
          .cert-subtitle { font-size: 18px; color: #555; margin-bottom: 30px; }
          .cert-body p { font-size: 16px; line-height: 2.5; color: #333; }
          .cert-award { font-size: 32px; font-weight: bold; color: #8b0000; margin: 20px 0 10px; letter-spacing: 4px; }
          .cert-level { font-size: 18px; color: #b8860b; margin-bottom: 20px; }
          .cert-teacher { color: #666; font-size: 15px; }
          .cert-footer { display: flex; justify-content: space-between; margin-top: 30px; font-size: 14px; color: #888; }
        </style></head><body>${el.innerHTML}</body></html>
      `)
      w.document.close()
      w.print()
    },
    deleteHandler(id) {
      this.$confirm('确定删除该证书？', '提示', { type: 'warning' }).then(() => {
        this.$http({ url: 'zhengshu/delete', method: 'post', data: [id] }).then(({ data }) => {
          if (data && data.code === 0) { this.$message.success('删除成功'); this.search() }
          else { this.$message.error(data.msg || '删除失败') }
        }).catch(() => { this.$message.error('网络异常，删除失败') })
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.slt { margin: 0 !important; display: flex; flex-wrap: wrap; }
.cert-wrap { width: 100%; padding: 20px; }
.cert-border {
  border: 8px double #b8860b;
  border-radius: 8px;
  padding: 40px 50px;
  text-align: center;
  background: linear-gradient(135deg, #fffdf0, #fff8dc);
  box-shadow: 0 0 20px rgba(184,134,11,0.2);
}
.cert-title { font-size: 38px; font-weight: bold; color: #8b0000; letter-spacing: 8px; margin-bottom: 8px; font-family: "仿宋", serif; }
.cert-subtitle { font-size: 16px; color: #666; margin-bottom: 24px; }
.cert-body p { font-size: 15px; line-height: 2.5; color: #333; margin: 0; }
.cert-award { font-size: 28px; font-weight: bold; color: #8b0000; margin: 16px 0 8px; letter-spacing: 4px; }
.cert-level { font-size: 16px; color: #b8860b; margin-bottom: 16px; }
.cert-teacher { color: #666; }
.cert-footer { display: flex; justify-content: space-between; margin-top: 24px; font-size: 13px; color: #999; }
</style>
