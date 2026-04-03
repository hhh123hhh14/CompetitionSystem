<template>
  <div class="addEdit-block">
    <el-card>
      <div slot="header">
        <span>{{ type === 'info' ? '竞赛详情' : (ruleForm.id ? '编辑竞赛' : '发布竞赛') }}</span>
        <el-button style="float:right" type="text" @click="singleClose()">返回列表</el-button>
      </div>

      <el-form class="detail-form-content" ref="ruleForm" :model="ruleForm" :rules="rules" label-width="120px" :disabled="type === 'info'">
        <el-divider content-position="left">基本信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="竞赛名称" prop="jingsaimingcheng">
              <el-input v-model="ruleForm.jingsaimingcheng" placeholder="竞赛名称" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="竞赛类型" prop="jingsaileixing">
              <el-input v-model="ruleForm.jingsaileixing" placeholder="如：数学建模、程序设计等" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="竞赛级别" prop="jingsaijib">
              <el-select v-model="ruleForm.jingsaijib" placeholder="请选择竞赛级别" style="width:100%">
                <el-option label="校级" value="校级"></el-option>
                <el-option label="省级" value="省级"></el-option>
                <el-option label="国家级" value="国家级"></el-option>
                <el-option label="国际级" value="国际级"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="竞赛地点" prop="jingsaididian">
              <el-input v-model="ruleForm.jingsaididian" placeholder="竞赛地点" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="竞赛状态" prop="zhuangtai">
              <el-select v-model="ruleForm.zhuangtai" placeholder="请选择" style="width:100%">
                <el-option label="报名中" value="报名中"></el-option>
                <el-option label="进行中" value="进行中"></el-option>
                <el-option label="已结束" value="已结束"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="模式" prop="moshi">
              <el-select v-model="ruleForm.moshi" placeholder="收费模式" style="width:100%">
                <el-option v-for="(item,index) in moshiOptions" :key="index" :label="item" :value="item"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="收费标准">
              <el-input v-model="ruleForm.shoufeibiaozhun" placeholder="如：免费 或 100元/队" clearable></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">时间设置</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="报名开始时间">
              <el-date-picker v-model="ruleForm.baomingkaishi" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="报名开始时间" style="width:100%"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报名截止时间">
              <el-date-picker v-model="ruleForm.baomingjiezhi" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="报名截止时间" style="width:100%"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="竞赛时间">
              <el-date-picker v-model="ruleForm.jingsaishijian" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="竞赛时间" style="width:100%"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="提交截止时间">
              <el-date-picker v-model="ruleForm.tijiaojiezhi" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="作品提交截止时间" style="width:100%"></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">竞赛内容</el-divider>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="参赛要求">
              <el-input v-model="ruleForm.cansaiyaoqiu" type="textarea" :rows="3" placeholder="参赛资格要求、人员限制等"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="竞赛规则">
              <el-input v-model="ruleForm.jingsaiguize" type="textarea" :rows="4" placeholder="竞赛规则、评分标准等详细说明"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="奖项设置">
              <el-input v-model="ruleForm.jiangxiangshezhi" type="textarea" :rows="3" placeholder="如：一等奖1名，奖金5000元；二等奖3名，奖金3000元..."></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="竞赛奖励">
              <el-input v-model="ruleForm.jingsaijiangli" type="textarea" :rows="3" placeholder="奖励说明（奖金、荣誉证书等）"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">封面与发布者</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="工号">
              <el-input v-model="ruleForm.gonghao" placeholder="教师工号" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="教师姓名">
              <el-input v-model="ruleForm.jiaoshixingming" placeholder="教师姓名" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="封面图片">
              <el-upload
                :action="uploadUrl"
                :headers="uploadHeaders"
                list-type="picture-card"
                :on-success="handleUploadSuccess"
                :on-remove="handleRemove"
                :file-list="fileList"
                :limit="1"
                :disabled="type === 'info'">
                <i class="el-icon-plus"></i>
              </el-upload>
              <div v-if="ruleForm.fengmiantupian && type === 'info'" style="margin-top:8px">
                <img :src="ruleForm.fengmiantupian" style="width:120px;height:120px;object-fit:cover;border-radius:4px" />
              </div>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- ===== 动态报名字段配置 ===== -->
        <el-divider content-position="left">
          <i class="el-icon-setting" style="margin-right:4px"></i>
          报名信息配置
        </el-divider>
        <el-row v-if="type !== 'info'">
          <el-col :span="24">
            <el-form-item label-width="0">
              <field-designer ref="fieldDesigner" v-model="customFields"></field-designer>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-else>
          <el-col :span="24">
            <div v-if="customFields.length === 0" style="color:#909399;font-size:13px;padding:8px 0">
              该竞赛未配置自定义报名字段
            </div>
            <div v-else>
              <el-tag v-for="f in customFields" :key="f.fieldKey" style="margin:4px">
                {{ f.fieldLabel }}（{{ typeNameMap[f.fieldType] || f.fieldType }}）
                <span v-if="f.required === '是'" style="color:#f56c6c">*</span>
              </el-tag>
            </div>
          </el-col>
        </el-row>

        <el-form-item v-if="type !== 'info'" style="text-align:center;margin-top:24px">
          <el-button type="primary" icon="el-icon-check" @click="submitForm('ruleForm')">保存发布</el-button>
          <el-button icon="el-icon-close" @click="singleClose()">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import FieldDesigner from './FieldDesigner'
export default {
  components: { FieldDesigner },
  props: ['parent'],
  data() {
    return {
      type: 'else',
      ro: {},
      ruleForm: { zhuangtai: '报名中', moshi: '免费', jingsaijib: '国家级' },
      rules: {
        jingsaimingcheng: [{ required: true, message: '请输入竞赛名称', trigger: 'blur' }],
        jingsaijib: [{ required: true, message: '请选择竞赛级别', trigger: 'change' }]
      },
      moshiOptions: ['付费', '免费'],
      fileList: [],
      customFields: [],  // 动态字段列表
      typeNameMap: {
        text: '文本', number: '数字', textarea: '多行文本',
        radio: '单选', checkbox: '多选', select: '下拉', date: '日期'
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
      this.ruleForm = { zhuangtai: '报名中', moshi: '免费', jingsaijib: '国家级' }
      this.fileList = []
      this.customFields = []
      if (id) {
        this.$http({ url: `jingsaixinxi/info/${id}`, method: 'get' }).then(({ data }) => {
          if (data && data.code === 0) {
            this.ruleForm = data.data
            if (this.ruleForm.fengmiantupian) {
              this.fileList = [{ name: '封面', url: this.ruleForm.fengmiantupian }]
            }
            // 加载该竞赛的动态字段配置
            this.loadCustomFields(id)
          }
        })
      }
    },
    loadCustomFields(jingsaixinxiid) {
      this.$http({ url: `jingsaiField/list/${jingsaixinxiid}`, method: 'get' }).then(({ data }) => {
        if (data && data.code === 0) {
          this.customFields = data.data || []
        }
      })
    },
    handleUploadSuccess(res) {
      if (res.code === 0) this.ruleForm.fengmiantupian = res.data
    },
    handleRemove() { this.ruleForm.fengmiantupian = '' },
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (!valid) return
        const url = this.ruleForm.id ? 'jingsaixinxi/update' : 'jingsaixinxi/save'
        this.$http({ url, method: 'post', data: this.ruleForm }).then(({ data }) => {
          if (data && data.code === 0) {
            const savedId = this.ruleForm.id || data.data
            // 保存动态字段
            this.saveCustomFields(this.ruleForm.id || savedId, data)
          } else { this.$message.error(data.msg) }
        })
      })
    },
    saveCustomFields(jingsaixinxiid, prevData) {
      // 从 FieldDesigner 获取字段列表
      const fields = this.$refs.fieldDesigner ? this.$refs.fieldDesigner.getFields() : this.customFields
      if (!jingsaixinxiid) {
        // 没有ID（新建后后端未返回ID），直接提示成功
        this.$message.success('保存成功')
        this.singleClose()
        this.parent.getDataList()
        return
      }
      this.$http({
        url: `jingsaiField/batchSave?jingsaixinxiid=${jingsaixinxiid}`,
        method: 'post',
        data: fields
      }).then(({ data }) => {
        this.$message.success('保存成功')
        this.singleClose()
        this.parent.getDataList()
      }).catch(() => {
        // 字段保存失败不影响竞赛信息
        this.$message.success('竞赛信息已保存（字段配置保存失败，请重新编辑）')
        this.singleClose()
        this.parent.getDataList()
      })
    },
    singleClose() {
      this.parent.showFlag = true
      this.parent.addOrUpdateFlag = false
    }
  }
}
</script>

<style scoped>
.addEdit-block { padding: 16px; }
</style>
