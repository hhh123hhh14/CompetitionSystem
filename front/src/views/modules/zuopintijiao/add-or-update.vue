<template>
  <div class="detail-wrap">
    <el-card>
      <div slot="header">
        <span>{{ type === 'info' ? '作品详情' : (type === 'resubmit' ? '重新提交作品' : '提交作品') }}</span>
        <el-button style="float:right" type="text" @click="back()">返回列表</el-button>
      </div>

      <!-- 截止时间提示 -->
      <el-alert v-if="isExpired" title="提交截止时间已过，不可提交或修改！" type="error" show-icon :closable="false" style="margin-bottom:16px"></el-alert>
      <el-alert v-else-if="deadlineText" :title="'作品提交截止时间：' + deadlineText" type="warning" show-icon :closable="false" style="margin-bottom:16px"></el-alert>

      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px" :disabled="type === 'info' || isExpired">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="竞赛名称" prop="jingsaimingcheng">
              <el-select v-model="form.jingsaimingcheng" placeholder="选择参赛竞赛" style="width:100%" @change="onCompChange" filterable>
                <el-option v-for="item in myBaomingList" :key="item.id" :label="item.jingsaimingcheng" :value="item.jingsaimingcheng"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="提交人" prop="xueshengxingming">
              <el-input v-model="form.xueshengxingming" placeholder="提交人姓名"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="作品名称" prop="zuopinmingcheng">
              <el-input v-model="form.zuopinmingcheng" placeholder="请输入作品名称"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="作品描述" prop="zuopinmiaoshu">
              <el-input v-model="form.zuopinmiaoshu" type="textarea" :rows="5" placeholder="请详细描述作品内容、特色、创新点等..."></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="附件上传">
              <el-upload
                class="upload-demo"
                :action="uploadUrl"
                :on-success="handleUploadSuccess"
                :on-remove="handleRemove"
                :file-list="fileList"
                :headers="uploadHeaders"
                multiple
                :limit="5">
                <el-button type="primary" icon="el-icon-upload2">点击上传</el-button>
                <div slot="tip" class="el-upload__tip">支持PDF、ZIP、DOC等格式，单文件不超过10MB，最多5个文件</div>
              </el-upload>
              <div v-if="type === 'info' && form.fujianwenjian">
                <el-tag v-for="(f,i) in form.fujianwenjian.split(',')" :key="i" style="margin:4px">
                  <a :href="f" target="_blank">{{ getFileName(f) }}</a>
                </el-tag>
              </div>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item v-if="type !== 'info' && !isExpired" style="text-align:center;margin-top:20px">
          <el-button type="primary" icon="el-icon-upload" @click="submit()">{{ type === 'resubmit' ? '重新提交' : '提交作品' }}</el-button>
          <el-button icon="el-icon-close" @click="back()">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
export default {
  props: ['parent'],
  data() {
    return {
      type: 'else',
      form: {},
      isExpired: false,
      deadlineText: '',
      myBaomingList: [],
      fileList: [],
      uploadedFiles: [],
      rules: {
        jingsaimingcheng: [{ required: true, message: '请选择竞赛', trigger: 'change' }],
        zuopinmingcheng: [{ required: true, message: '请输入作品名称', trigger: 'blur' }],
        zuopinmiaoshu: [{ required: true, message: '请填写作品描述', trigger: 'blur' }]
      }
    }
  },
  computed: {
    uploadUrl() { return `/springbootrd362/file/upload` },
    uploadHeaders() { return { Token: this.$storage.get('Token') } }
  },
  methods: {
    init(id, type) {
      this.type = type || 'else'
      this.form = {}
      this.fileList = []
      this.uploadedFiles = []
      this.isExpired = false
      this.deadlineText = ''
      this.loadMyBaoming()
      if (id) {
        this.$http({ url: `zuopintijiao/info/${id}`, method: 'get' }).then(({ data }) => {
          if (data && data.code === 0) {
            this.form = data.data
            if (this.form.fujianwenjian) {
              this.uploadedFiles = this.form.fujianwenjian.split(',')
              this.fileList = this.uploadedFiles.map((f, i) => ({
                name: this.getFileName(f), url: f, uid: i
              }))
            }
          }
        })
      } else {
        // 自动填写当前用户信息
        const userInfo = this.$storage.get('userInfo')
        if (userInfo) {
          this.form.xuehao = userInfo.username
          this.form.xueshengxingming = userInfo.xueshengxingming || ''
        }
      }
    },
    loadMyBaoming() {
      this.$http({ url: 'jingsaibaoming/page', method: 'get', params: { page: 1, limit: 100, sfsh: '已通过' } }).then(({ data }) => {
        if (data && data.code === 0) this.myBaomingList = data.data.list || []
      })
    },
    onCompChange(name) {
      const item = this.myBaomingList.find(b => b.jingsaimingcheng === name)
      if (item) {
        this.form.jingsaixinxiid = item.jingsaixinxiid
        this.form.baomingid = item.id
        // 检查截止时间
        this.$http({ url: `jingsaixinxi/info/${item.jingsaixinxiid}`, method: 'get' }).then(({ data }) => {
          if (data && data.code === 0 && data.data.tijiaojiezhi) {
            const deadline = new Date(data.data.tijiaojiezhi)
            this.deadlineText = data.data.tijiaojiezhi
            this.isExpired = new Date() > deadline
          }
        })
      }
    },
    handleUploadSuccess(res) {
      if (res.code === 0) {
        this.uploadedFiles.push(res.data)
        this.form.fujianwenjian = this.uploadedFiles.join(',')
      }
    },
    handleRemove(file) {
      const url = file.url || (file.response && file.response.data)
      this.uploadedFiles = this.uploadedFiles.filter(f => f !== url)
      this.form.fujianwenjian = this.uploadedFiles.join(',')
    },
    getFileName(url) {
      if (!url) return '文件'
      return url.split('/').pop() || '附件'
    },
    submit() {
      this.$refs.formRef.validate(valid => {
        if (!valid) return
        const isResubmit = this.form.id && this.type === 'resubmit'
        this.$confirm(
          isResubmit
            ? `确认重新提交作品【${this.form.zuopinmingcheng}】？原版本将被覆盖。`
            : `确认提交作品【${this.form.zuopinmingcheng}】至竞赛【${this.form.jingsaimingcheng}】？`,
          '提交确认',
          { confirmButtonText: '确认提交', cancelButtonText: '取消', type: 'info', iconClass: 'el-icon-upload' }
        ).then(() => {
          const url = isResubmit ? 'zuopintijiao/update' : 'zuopintijiao/save'
          this.$http({ url, method: 'post', data: this.form }).then(({ data }) => {
            if (data && data.code === 0) {
              this.$notify({
                title: isResubmit ? '重新提交成功' : '作品提交成功',
                message: isResubmit
                  ? `作品【${this.form.zuopinmingcheng}】已重新提交，请等待评审`
                  : `作品【${this.form.zuopinmingcheng}】已成功提交至【${this.form.jingsaimingcheng}】`,
                type: 'success',
                duration: 4000
              })
              this.back()
              this.parent.getDataList()
            } else { this.$message.error(data.msg) }
          })
        }).catch(() => {})
      })
    },
    back() { this.parent.showFlag = true; this.parent.addOrUpdateFlag = false }
  }
}
</script>

<style scoped>
.detail-wrap { padding: 16px; }
</style>
