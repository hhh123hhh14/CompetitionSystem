<template>
  <div class="detail-wrap">
    <el-card class="card-header">
      <div slot="header">
        <span>{{ type === 'info' ? '获奖详情' : (form.id ? '编辑获奖' : '新增获奖') }}</span>
        <el-button style="float:right" type="text" @click="back()">返回列表</el-button>
      </div>

      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px" :disabled="type === 'info'">
        <el-divider content-position="left">基本信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="竞赛名称" prop="jingsaimingcheng">
              <el-input v-model="form.jingsaimingcheng" placeholder="请输入竞赛名称"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="竞赛类型" prop="jingsaileixing">
              <el-input v-model="form.jingsaileixing" placeholder="请输入竞赛类型"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="竞赛级别" prop="jingsaijib">
              <el-select v-model="form.jingsaijib" placeholder="请选择" style="width:100%">
                <el-option label="校级" value="校级"></el-option>
                <el-option label="省级" value="省级"></el-option>
                <el-option label="国家级" value="国家级"></el-option>
                <el-option label="国际级" value="国际级"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="获奖日期" prop="huojiangriqi">
              <el-date-picker v-model="form.huojiangriqi" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width:100%"></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">获奖信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="获奖学号" prop="xuehao">
              <el-input v-model="form.xuehao" placeholder="队长/个人学号"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="获奖学生" prop="xueshengxingming">
              <el-input v-model="form.xueshengxingming" placeholder="学生姓名"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学院名称" prop="xueyuanmingcheng">
              <el-input v-model="form.xueyuanmingcheng" placeholder="所在学院"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="团队名称">
              <el-input v-model="form.tuanduimingcheng" placeholder="个人参赛可为空"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="团队成员">
              <el-input v-model="form.tuanduichengyuan" type="textarea" :rows="2" placeholder="团队所有成员姓名（逗号分隔）"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">奖项信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="奖项名称" prop="jiangxiangmingcheng">
              <el-input v-model="form.jiangxiangmingcheng" placeholder="如：全国一等奖"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="奖项等级" prop="jiangxiangdengji">
              <el-select v-model="form.jiangxiangdengji" placeholder="请选择" style="width:100%">
                <el-option label="特等奖" value="特等奖"></el-option>
                <el-option label="一等奖" value="一等奖"></el-option>
                <el-option label="二等奖" value="二等奖"></el-option>
                <el-option label="三等奖" value="三等奖"></el-option>
                <el-option label="优秀奖" value="优秀奖"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="指导教师工号">
              <el-input v-model="form.gonghao" placeholder="指导教师工号"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="指导教师姓名">
              <el-input v-model="form.jiaoshixingming" placeholder="指导教师姓名"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否公示">
              <el-radio-group v-model="form.sfgs">
                <el-radio label="是">是（前台展示）</el-radio>
                <el-radio label="否">否（暂不公示）</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input v-model="form.beizhu" type="textarea" :rows="3" placeholder="备注信息"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item v-if="type !== 'info'" style="text-align:center;margin-top:20px">
          <el-button type="primary" icon="el-icon-check" @click="submit()">保存</el-button>
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
      form: { sfgs: '否' },
      rules: {
        jingsaimingcheng: [{ required: true, message: '请输入竞赛名称', trigger: 'blur' }],
        xueshengxingming: [{ required: true, message: '请输入学生姓名', trigger: 'blur' }],
        jiangxiangmingcheng: [{ required: true, message: '请输入奖项名称', trigger: 'blur' }],
        jiangxiangdengji: [{ required: true, message: '请选择奖项等级', trigger: 'change' }],
        jingsaijib: [{ required: true, message: '请选择竞赛级别', trigger: 'change' }]
      }
    }
  },
  methods: {
    init(id, type) {
      this.type = type || 'else'
      this.form = { sfgs: '否' }
      if (id) {
        this.$http({ url: `huojiang/info/${id}`, method: 'get' }).then(({ data }) => {
          if (data && data.code === 0) this.form = data.data
        })
      }
    },
    submit() {
      this.$refs.formRef.validate(valid => {
        if (!valid) return
        const url = this.form.id ? 'huojiang/update' : 'huojiang/save'
        this.$http({ url, method: 'post', data: this.form }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message.success('保存成功')
            this.back()
            this.parent.getDataList()
          } else {
            this.$message.error(data.msg)
          }
        })
      })
    },
    back() {
      this.parent.showFlag = true
      this.parent.addOrUpdateFlag = false
    }
  }
}
</script>

<style scoped>
.detail-wrap { padding: 16px; }
.card-header { margin-bottom: 16px; }
</style>
