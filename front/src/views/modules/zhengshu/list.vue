<template>
  <div class="main-content">
    <!-- 搜索栏 -->
    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="竞赛名称">
          <el-input v-model="searchForm.jingsaimingcheng" placeholder="竞赛名称" clearable prefix-icon="el-icon-search" style="width:180px"></el-input>
        </el-form-item>
        <el-form-item label="学生姓名">
          <el-input v-model="searchForm.xueshengxingming" placeholder="学生姓名" clearable prefix-icon="el-icon-user" style="width:140px"></el-input>
        </el-form-item>
        <el-form-item label="证书编号">
          <el-input v-model="searchForm.zhengshubianhao" placeholder="证书编号" clearable style="width:160px"></el-input>
        </el-form-item>
        <el-form-item label="获奖等级">
          <el-select v-model="searchForm.jiangxiangdengji" placeholder="全部" clearable style="width:110px">
            <el-option label="特等奖" value="特等奖"></el-option>
            <el-option label="一等奖" value="一等奖"></el-option>
            <el-option label="二等奖" value="二等奖"></el-option>
            <el-option label="三等奖" value="三等奖"></el-option>
            <el-option label="优秀奖" value="优秀奖"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="search()">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetSearch()">重置</el-button>
        </el-form-item>
      </el-form>
      <div class="action-row">
        <el-tag type="info" style="margin-right:8px">
          <i class="el-icon-info"></i> 可在此管理电子证书，支持上传证书原件、预览及下载
        </el-tag>
      </div>
    </el-card>

    <!-- 数据表格 -->
    <el-card shadow="never" style="margin-top:16px">
      <el-table
        :data="dataList"
        border
        stripe
        v-loading="dataListLoading"
        element-loading-text="数据加载中..."
        style="width:100%"
        :header-cell-style="{background:'#f5f7fa',color:'#606266',fontWeight:'600'}"
      >
        <el-table-column type="index" label="序号" width="60" align="center"></el-table-column>
        <el-table-column prop="zhengshubianhao" label="证书编号" width="180" align="center">
          <template slot-scope="scope">
            <span style="font-family:monospace;font-size:12px;color:#409eff">{{ scope.row.zhengshubianhao }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="jingsaimingcheng" label="竞赛名称" min-width="160" show-overflow-tooltip></el-table-column>
        <el-table-column prop="jingsaijib" label="竞赛级别" width="90" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" :type="jibType(scope.row.jingsaijib)" effect="plain">{{ scope.row.jingsaijib }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="xueshengxingming" label="获奖者" width="100" align="center"></el-table-column>
        <el-table-column prop="xuehao" label="学号" width="110" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="xueyuanmingcheng" label="学院" min-width="120" show-overflow-tooltip></el-table-column>
        <el-table-column prop="jiangxiangdengji" label="获奖等级" width="90" align="center">
          <template slot-scope="scope">
            <el-tag :type="dengjiType(scope.row.jiangxiangdengji)" size="mini">{{ scope.row.jiangxiangdengji }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="jiaoshixingming" label="指导教师" width="100" align="center"></el-table-column>
        <el-table-column prop="huojiangriqi" label="获奖日期" width="110" align="center"></el-table-column>
        <el-table-column label="证书文件" width="100" align="center">
          <template slot-scope="scope">
            <el-tooltip :content="scope.row.zhengshuluqing ? '已上传：' + scope.row.zhengshuluqing : '暂未上传证书原件'" placement="top">
              <el-tag :type="scope.row.zhengshuluqing ? 'success' : 'info'" size="mini" style="cursor:pointer"
                      @click="scope.row.zhengshuluqing && viewCert(scope.row)">
                <i :class="scope.row.zhengshuluqing ? 'el-icon-document-checked' : 'el-icon-document'"></i>
                {{ scope.row.zhengshuluqing ? '已上传' : '未上传' }}
              </el-tag>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column prop="shenqingshijian" label="生成时间" width="160" align="center">
          <template slot-scope="scope">
            <span style="font-size:12px">{{ scope.row.shenqingshijian }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="300" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button type="primary" size="mini" icon="el-icon-view" @click="viewCert(scope.row)">预览</el-button>
            <el-button type="success" size="mini" icon="el-icon-download" @click="downloadCert(scope.row)">下载</el-button>
            <!-- 上传证书原件 -->
            <el-upload
              class="inline-upload"
              :action="getUploadUrl(scope.row.id)"
              :headers="uploadHeaders"
              :show-file-list="false"
              :on-success="(res) => handleUploadSuccess(res, scope.row)"
              :on-error="handleUploadError"
              :before-upload="beforeUpload"
              accept=".jpg,.jpeg,.png,.pdf"
            >
              <el-button size="mini" type="warning" icon="el-icon-upload2">上传</el-button>
            </el-upload>
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
        style="margin-top:16px;text-align:right"
      ></el-pagination>
    </el-card>

    <!-- 证书预览弹窗 -->
    <el-dialog :visible.sync="certDialogVisible" title="电子证书" width="760px" center :close-on-click-modal="false">
      <div v-if="currentCert" class="cert-dialog-body">
        <!-- 已上传文件预览区 -->
        <div v-if="currentCert.zhengshuluqing" class="file-preview-section">
          <el-alert title="已上传证书原件" type="success" :closable="false" show-icon style="margin-bottom:16px"></el-alert>
          <div v-if="isImage(currentCert.zhengshuluqing)" class="img-preview-wrap">
            <img :src="fileUrl(currentCert.zhengshuluqing)"
                 style="max-width:100%;max-height:400px;border-radius:6px;box-shadow:0 2px 16px rgba(0,0,0,0.15)" />
          </div>
          <div v-else class="pdf-preview-wrap">
            <i class="el-icon-document" style="font-size:64px;color:#409eff;display:block;text-align:center"></i>
            <p style="text-align:center;color:#606266;margin:12px 0">PDF证书文件，请点击下载后查看</p>
            <div style="text-align:center">
              <el-button type="primary" icon="el-icon-download" @click="downloadCert(currentCert)">下载PDF证书</el-button>
            </div>
          </div>
          <el-divider content-position="center">证书信息</el-divider>
        </div>

        <!-- 电子证书展示 -->
        <div class="cert-wrap" id="cert-print-area">
          <div class="cert-outer">
            <div class="cert-inner">
              <!-- 顶部装饰线 -->
              <div class="cert-top-decor">
                <div class="cert-corner tl"></div>
                <div class="cert-corner tr"></div>
              </div>
              <!-- 国徽/奖杯图标 -->
              <div class="cert-emblem">
                <div class="cert-emblem-ring">
                  <div class="cert-emblem-icon">&#9733;</div>
                </div>
              </div>
              <!-- 标题 -->
              <div class="cert-title">荣 誉 证 书</div>
              <div class="cert-title-en">CERTIFICATE OF HONOR</div>
              <div class="cert-divider"><span></span></div>
              <!-- 正文 -->
              <div class="cert-body">
                <p class="cert-text">兹证明</p>
                <p class="cert-name">{{ currentCert.xueshengxingming }}</p>
                <p class="cert-text cert-text-sub">学号：{{ currentCert.xuehao }}　　学院：{{ currentCert.xueyuanmingcheng }}</p>
                <p class="cert-text" style="margin-top:20px">在 <strong>{{ currentCert.jingsaimingcheng }}</strong>
                  <span v-if="currentCert.jingsaijib">（{{ currentCert.jingsaijib }}）</span> 中表现优异</p>
                <p class="cert-text">荣获</p>
                <p class="cert-award-name">{{ currentCert.jiangxiangmingcheng }}</p>
                <p class="cert-award-level">{{ currentCert.jiangxiangdengji }}</p>
                <p class="cert-text cert-teacher" v-if="currentCert.jiaoshixingming">
                  指导教师：{{ currentCert.jiaoshixingming }}
                </p>
              </div>
              <!-- 落款 -->
              <div class="cert-sign">
                <div class="cert-sign-left">
                  <p class="cert-text-sm">特发此证，以资鼓励</p>
                </div>
                <div class="cert-sign-right">
                  <p class="cert-text-sm">颁发日期：{{ currentCert.huojiangriqi }}</p>
                </div>
              </div>
              <div class="cert-divider-bottom"><span></span></div>
              <!-- 底部编号 -->
              <div class="cert-bottom">
                <span class="cert-no">证书编号：{{ currentCert.zhengshubianhao }}</span>
              </div>
              <!-- 底部装饰 -->
              <div class="cert-bottom-decor">
                <div class="cert-corner bl"></div>
                <div class="cert-corner br"></div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" icon="el-icon-printer" @click="doPrint()">打印证书</el-button>
        <el-button v-if="currentCert && currentCert.zhengshuluqing" type="success" icon="el-icon-download" @click="downloadCert(currentCert)">下载文件</el-button>
        <el-button @click="certDialogVisible = false" icon="el-icon-close">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import storage from '@/utils/storage'
export default {
  data() {
    return {
      searchForm: {},
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      certDialogVisible: false,
      currentCert: null,
      uploadUrl: '/springbootrd362/zhengshu/uploadFile/'
    }
  },
  computed: {
    uploadHeaders() {
      return { Token: storage.get('Token') }
    }
  },
  created() { this.getDataList() },
  methods: {
    getUploadUrl(id) {
      return '/springbootrd362/zhengshu/uploadFile/' + id
    },
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
    search() { this.pageIndex = 1; this.getDataList() },
    resetSearch() { this.searchForm = {}; this.search() },
    getDataList() {
      this.dataListLoading = true
      let params = { page: this.pageIndex, limit: this.pageSize, sort: 'id', order: 'desc' }
      if (this.searchForm.jingsaimingcheng) params['jingsaimingcheng'] = '%' + this.searchForm.jingsaimingcheng + '%'
      if (this.searchForm.xueshengxingming) params['xueshengxingming'] = '%' + this.searchForm.xueshengxingming + '%'
      if (this.searchForm.zhengshubianhao) params['zhengshubianhao'] = '%' + this.searchForm.zhengshubianhao + '%'
      if (this.searchForm.jiangxiangdengji) params['jiangxiangdengji'] = this.searchForm.jiangxiangdengji
      this.$http({ url: 'zhengshu/page', method: 'get', params }).then(({ data }) => {
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
    viewCert(row) {
      this.currentCert = row
      this.certDialogVisible = true
    },
    downloadCert(row) {
      if (row.zhengshuluqing) {
        // 有上传文件，直接下载
        const link = document.createElement('a')
        link.href = this.fileUrl(row.zhengshuluqing)
        const ext = row.zhengshuluqing.includes('.') ? row.zhengshuluqing.split('.').pop() : 'pdf'
        link.download = (row.zhengshubianhao || 'certificate') + '.' + ext
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
      } else {
        // 无文件，打开打印预览
        this.currentCert = row
        this.certDialogVisible = true
        this.$nextTick(() => { setTimeout(() => this.doPrint(), 400) })
      }
    },
    beforeUpload(file) {
      const allowed = ['image/jpeg', 'image/png', 'image/gif', 'application/pdf']
      if (!allowed.includes(file.type)) {
        this.$message.error('只支持 JPG/PNG/PDF 格式文件！')
        return false
      }
      if (file.size / 1024 / 1024 > 10) {
        this.$message.error('文件大小不能超过 10MB！')
        return false
      }
      return true
    },
    handleUploadSuccess(res, row) {
      if (res && res.code === 0 && res.file) {
        this.$message.success('证书文件上传成功！')
        // 同步更新本地列表数据，无需刷新（后端已经更新了数据库）
        const idx = this.dataList.findIndex(item => item.id === row.id)
        if (idx !== -1) this.$set(this.dataList[idx], 'zhengshuluqing', res.file)
      } else {
        this.$message.error('上传失败：' + (res && res.msg || '服务器错误'))
      }
    },
    handleUploadError() {
      this.$message.error('文件上传失败，请检查网络或文件格式后重试')
    },
    doPrint() {
      const el = document.getElementById('cert-print-area')
      if (!el) { this.$message.warning('证书内容未加载'); return }
      const w = window.open('', '_blank')
      w.document.write(`
        <html><head><title>电子证书 - ${this.currentCert && this.currentCert.zhengshubianhao || ''}</title>
        <style>
          * { box-sizing: border-box; margin: 0; padding: 0; }
          body { display: flex; justify-content: center; align-items: center; min-height: 100vh; background: #f5f5f5; }
          .cert-wrap { width: 700px; padding: 20px; }
          .cert-outer {
            background: linear-gradient(145deg, #7b1f1f 0%, #a0522d 25%, #8b0000 50%, #a0522d 75%, #7b1f1f 100%);
            padding: 18px; border-radius: 4px;
            box-shadow: 0 8px 32px rgba(139,0,0,0.25);
          }
          .cert-inner {
            background: linear-gradient(170deg, #fffef5, #fdf8e8, #fef9e7, #fffdf0);
            border: 2px solid #c9a84c; padding: 48px 60px 36px; position: relative; min-height: 560px;
          }
          .cert-corner { position: absolute; width: 50px; height: 50px; }
          .cert-corner::before, .cert-corner::after { content: ''; position: absolute; background: #c9a84c; }
          .cert-corner.tl { top: 10px; left: 10px; }
          .cert-corner.tl::before { top:0;left:0;width:30px;height:2px; }
          .cert-corner.tl::after { top:0;left:0;width:2px;height:30px; }
          .cert-corner.tr { top: 10px; right: 10px; }
          .cert-corner.tr::before { top:0;right:0;width:30px;height:2px; }
          .cert-corner.tr::after { top:0;right:0;width:2px;height:30px; }
          .cert-corner.bl { bottom: 10px; left: 10px; }
          .cert-corner.bl::before { bottom:0;left:0;width:30px;height:2px; }
          .cert-corner.bl::after { bottom:0;left:0;width:2px;height:30px; }
          .cert-corner.br { bottom: 10px; right: 10px; }
          .cert-corner.br::before { bottom:0;right:0;width:30px;height:2px; }
          .cert-corner.br::after { bottom:0;right:0;width:2px;height:30px; }
          .cert-emblem { text-align:center; margin-bottom:12px; }
          .cert-emblem-ring {
            display:inline-flex; align-items:center; justify-content:center;
            width:64px; height:64px; border:3px solid #c9a84c; border-radius:50%;
            background:radial-gradient(circle,#fdf6d0,#f5e6a3);
            box-shadow: 0 0 0 4px rgba(201,168,76,0.15);
          }
          .cert-emblem-icon { font-size:30px; color:#b8860b; }
          .cert-title {
            text-align:center; font-size:42px; font-weight:bold; color:#8b0000;
            letter-spacing:14px; margin-bottom:2px;
            font-family:"STZhongsong","华文中宋","SimSun",serif;
          }
          .cert-title-en {
            text-align:center; font-size:13px; color:#c9a84c; letter-spacing:5px;
            font-weight:600; margin-bottom:16px;
          }
          .cert-divider, .cert-divider-bottom {
            text-align:center; margin:0 auto 22px; max-width:400px; height:6px; position:relative;
          }
          .cert-divider span, .cert-divider-bottom span {
            display:block; height:1px; background:linear-gradient(90deg,transparent,#c9a84c,transparent);
            position:absolute; top:2px; left:0; right:0;
          }
          .cert-divider::before, .cert-divider::after,
          .cert-divider-bottom::before, .cert-divider-bottom::after {
            content:''; display:block; height:1px; background:linear-gradient(90deg,transparent,#c9a84c,transparent);
          }
          .cert-divider::after, .cert-divider-bottom::after { margin-top:4px; }
          .cert-divider-bottom { margin-top:28px; margin-bottom:16px; }
          .cert-body { text-align:center; padding:0 24px; }
          .cert-text { font-size:16px; color:#4a4a4a; line-height:2.2; font-family:"FangSong","仿宋",serif; }
          .cert-text-sub { font-size:14px; color:#888; margin-bottom:8px; }
          .cert-name {
            font-size:34px; font-weight:bold; color:#333; letter-spacing:8px; margin:8px 0 6px;
            font-family:"STKaiti","华文楷体","KaiTi",serif;
            border-bottom:2px solid #c9a84c; display:inline-block; padding:0 28px 4px;
          }
          .cert-award-name {
            font-size:28px; font-weight:bold; color:#8b0000; letter-spacing:4px; margin:14px 0 8px;
            font-family:"STZhongsong","华文中宋","SimSun",serif;
          }
          .cert-award-level {
            display:inline-block; font-size:17px; color:#b8860b; border:2px solid #c9a84c;
            border-radius:4px; padding:5px 28px; letter-spacing:4px; font-weight:600;
            background:linear-gradient(to bottom,#fef9e7,#fdf3d0); margin-bottom:12px;
          }
          .cert-teacher { margin-top:16px; color:#666; }
          .cert-sign { display:flex; justify-content:space-between; padding:0 24px; margin-top:24px; }
          .cert-text-sm { font-size:14px; color:#666; font-family:"FangSong","仿宋",serif; }
          .cert-bottom { text-align:center; margin-bottom:6px; }
          .cert-no { font-size:12px; color:#aaa; letter-spacing:1px; font-family:monospace; }
          .cert-top-decor, .cert-bottom-decor { display:contents; }
          @media print {
            body { background:white; }
            .cert-outer { box-shadow:none; }
          }
        </style></head>
        <body>${el.outerHTML}</body></html>
      `)
      w.document.close()
      setTimeout(() => { w.focus(); w.print() }, 300)
    },
    deleteHandler(id) {
      this.$confirm('确认删除该证书记录？删除后无法恢复！', '警告', {
        confirmButtonText: '确认删除', cancelButtonText: '取消', type: 'warning'
      }).then(() => {
        this.$http({ url: 'zhengshu/delete', method: 'post', data: [id] }).then(({ data }) => {
          if (data && data.code === 0) { this.$message.success('删除成功'); this.search() }
          else { this.$message.error(data && data.msg || '删除失败') }
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
.inline-upload { display: inline-block; margin: 0 3px; }

/* 弹窗 */
.cert-dialog-body { max-height: 70vh; overflow-y: auto; padding: 0 8px; }
.img-preview-wrap { text-align: center; margin-bottom: 16px; }
.pdf-preview-wrap { padding: 24px; background: #f5f7fa; border-radius: 8px; margin-bottom: 16px; }

/* 电子证书样式 */
.cert-wrap { width: 100%; padding: 16px 8px; }
.cert-outer {
  background: linear-gradient(145deg, #7b1f1f 0%, #a0522d 25%, #8b0000 50%, #a0522d 75%, #7b1f1f 100%);
  padding: 16px;
  border-radius: 4px;
  box-shadow: 0 8px 32px rgba(139,0,0,0.25), 0 2px 8px rgba(0,0,0,0.15);
}
.cert-inner {
  background: linear-gradient(170deg, #fffef5 0%, #fdf8e8 30%, #fef9e7 60%, #fffdf0 100%);
  border: 2px solid #c9a84c;
  padding: 40px 50px 30px;
  position: relative;
  min-height: 500px;
}
/* 四角装饰 */
.cert-corner {
  position: absolute;
  width: 50px;
  height: 50px;
}
.cert-corner::before, .cert-corner::after {
  content: '';
  position: absolute;
  background: #c9a84c;
}
.cert-corner.tl { top: 8px; left: 8px; }
.cert-corner.tl::before { top: 0; left: 0; width: 30px; height: 2px; }
.cert-corner.tl::after { top: 0; left: 0; width: 2px; height: 30px; }
.cert-corner.tr { top: 8px; right: 8px; }
.cert-corner.tr::before { top: 0; right: 0; width: 30px; height: 2px; }
.cert-corner.tr::after { top: 0; right: 0; width: 2px; height: 30px; }
.cert-corner.bl { bottom: 8px; left: 8px; }
.cert-corner.bl::before { bottom: 0; left: 0; width: 30px; height: 2px; }
.cert-corner.bl::after { bottom: 0; left: 0; width: 2px; height: 30px; }
.cert-corner.br { bottom: 8px; right: 8px; }
.cert-corner.br::before { bottom: 0; right: 0; width: 30px; height: 2px; }
.cert-corner.br::after { bottom: 0; right: 0; width: 2px; height: 30px; }
/* 奖章图标 */
.cert-emblem { text-align: center; margin-bottom: 10px; }
.cert-emblem-ring {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 60px;
  height: 60px;
  border: 3px solid #c9a84c;
  border-radius: 50%;
  background: radial-gradient(circle, #fdf6d0, #f5e6a3);
  box-shadow: 0 0 0 4px rgba(201,168,76,0.15), 0 2px 10px rgba(201,168,76,0.2);
}
.cert-emblem-icon {
  font-size: 28px;
  color: #b8860b;
  line-height: 1;
}
/* 标题 */
.cert-title {
  text-align: center;
  font-size: 36px;
  font-weight: bold;
  color: #8b0000;
  letter-spacing: 12px;
  margin-bottom: 2px;
  font-family: "STZhongsong", "华文中宋", "SimSun", serif;
}
.cert-title-en {
  text-align: center;
  font-size: 12px;
  color: #c9a84c;
  letter-spacing: 4px;
  font-weight: 600;
  margin-bottom: 14px;
}
/* 分割线 */
.cert-divider, .cert-divider-bottom {
  text-align: center;
  margin: 0 auto 20px;
  max-width: 380px;
  height: 6px;
  position: relative;
}
.cert-divider span, .cert-divider-bottom span {
  display: block;
  height: 1px;
  background: linear-gradient(90deg, transparent, #c9a84c, transparent);
  position: absolute;
  top: 2px;
  left: 0;
  right: 0;
}
.cert-divider::before, .cert-divider::after,
.cert-divider-bottom::before, .cert-divider-bottom::after {
  content: '';
  display: block;
  height: 1px;
  background: linear-gradient(90deg, transparent, #c9a84c, transparent);
}
.cert-divider::after, .cert-divider-bottom::after { margin-top: 4px; }
.cert-divider-bottom { margin-top: 24px; margin-bottom: 14px; }
/* 正文 */
.cert-body { text-align: center; padding: 0 20px; }
.cert-text {
  font-size: 15px;
  color: #4a4a4a;
  line-height: 2;
  font-family: "FangSong", "仿宋", serif;
  margin: 0;
}
.cert-text-sub {
  font-size: 13px;
  color: #888;
  margin-bottom: 6px;
}
.cert-name {
  font-size: 30px;
  font-weight: bold;
  color: #333;
  letter-spacing: 6px;
  margin: 6px 0 4px;
  font-family: "STKaiti", "华文楷体", "KaiTi", serif;
  border-bottom: 2px solid #c9a84c;
  display: inline-block;
  padding: 0 24px 4px;
}
.cert-award-name {
  font-size: 26px;
  font-weight: bold;
  color: #8b0000;
  letter-spacing: 4px;
  margin: 12px 0 6px;
  font-family: "STZhongsong", "华文中宋", "SimSun", serif;
}
.cert-award-level {
  display: inline-block;
  font-size: 16px;
  color: #b8860b;
  border: 2px solid #c9a84c;
  border-radius: 4px;
  padding: 4px 24px;
  letter-spacing: 4px;
  font-weight: 600;
  background: linear-gradient(to bottom, #fef9e7, #fdf3d0);
  margin-bottom: 10px;
}
.cert-teacher {
  margin-top: 14px;
  color: #666;
}
/* 落款 */
.cert-sign {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  padding: 0 20px;
  margin-top: 20px;
}
.cert-text-sm {
  font-size: 13px;
  color: #666;
  font-family: "FangSong", "仿宋", serif;
  margin: 0;
}
/* 底部编号 */
.cert-bottom {
  text-align: center;
  margin-bottom: 4px;
}
.cert-no {
  font-size: 11px;
  color: #aaa;
  letter-spacing: 1px;
  font-family: monospace;
}

.dialog-footer .el-button { margin: 0 6px; }
</style>
