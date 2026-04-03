<template>
  <div class="addEdit-block">
    <el-card>
      <div slot="header">
        <span>{{ type === 'info' ? '报名详情' : (type === 'cross' ? '竞赛报名' : (ruleForm.id ? '编辑报名' : '新增报名')) }}</span>
        <el-button style="float:right" type="text" @click="singleClose()">返回</el-button>
      </div>

      <el-form class="detail-form-content" ref="ruleForm" :model="ruleForm" :rules="rules" label-width="120px" :disabled="type === 'info'">

        <!-- 审核状态展示 -->
        <el-alert v-if="ruleForm.sfsh === '已驳回'" :title="'审核未通过：' + (ruleForm.shhf || '请修改后重新提交')" type="error" show-icon :closable="false" style="margin-bottom:16px"></el-alert>
        <el-alert v-else-if="ruleForm.sfsh === '已通过'" title="恭喜！报名审核已通过" type="success" show-icon :closable="false" style="margin-bottom:16px"></el-alert>
        <el-alert v-else-if="ruleForm.sfsh === '待审核'" title="报名信息已提交，等待审核中..." type="warning" show-icon :closable="false" style="margin-bottom:16px"></el-alert>

        <el-divider content-position="left">指导教师信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="教师工号" prop="gonghao">
              <el-input v-model="ruleForm.gonghao" placeholder="指导教师工号" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="教师姓名" prop="jiaoshixingming">
              <el-input v-model="ruleForm.jiaoshixingming" placeholder="指导教师姓名" clearable></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">竞赛信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="竞赛名称" prop="jingsaimingcheng">
              <el-input v-model="ruleForm.jingsaimingcheng" placeholder="竞赛名称" :readonly="type === 'cross'" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="竞赛类型" prop="jingsaileixing">
              <el-input v-model="ruleForm.jingsaileixing" placeholder="竞赛类型" :readonly="type === 'cross'" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="参赛类型" prop="cansaileixing">
              <el-select v-model="ruleForm.cansaileixing" placeholder="请选择" style="width:100%">
                <el-option label="个人" value="个人"></el-option>
                <el-option label="团队" value="团队"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="申请日期">
              <el-date-picker v-model="ruleForm.shenqingriqi" type="date" value-format="yyyy-MM-dd" placeholder="申请日期" style="width:100%"></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">参赛学生信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="学号（队长）" prop="xuehao">
              <el-input v-model="ruleForm.xuehao" placeholder="参赛学号/队长学号" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学生姓名" prop="xueshengxingming">
              <el-input v-model="ruleForm.xueshengxingming" placeholder="学生姓名" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学院名称">
              <el-input v-model="ruleForm.xueyuanmingcheng" placeholder="所在学院" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="年级">
              <el-select v-model="ruleForm.nianji" placeholder="请选择年级" style="width:100%">
                <el-option label="大一" value="大一"></el-option>
                <el-option label="大二" value="大二"></el-option>
                <el-option label="大三" value="大三"></el-option>
                <el-option label="大四" value="大四"></el-option>
                <el-option label="研究生" value="研究生"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话">
              <el-input v-model="ruleForm.shouji" placeholder="手机号码" clearable></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 团队报名时显示 -->
        <div v-if="ruleForm.cansaileixing === '团队'">
          <el-divider content-position="left">团队信息</el-divider>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="团队名称">
                <el-input v-model="ruleForm.tuanduimingcheng" placeholder="请输入团队名称" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="参赛人员">
                <el-input v-model="ruleForm.cansairenyuan" placeholder="所有团队成员姓名（逗号分隔）" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="成员详情">
                <el-input v-model="ruleForm.tuanduichengyuan" type="textarea" :rows="3" placeholder="团队成员详细信息（学号、姓名、学院等）"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </div>
        <div v-else>
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="参赛人员">
                <el-input v-model="ruleForm.cansairenyuan" placeholder="参赛人员姓名" clearable></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <el-divider content-position="left">参赛宣言</el-divider>
        <el-row>
          <el-col :span="24">
            <el-form-item label="参赛宣言">
              <el-input v-model="ruleForm.cansaixuanyan" type="textarea" :rows="4" placeholder="请填写参赛宣言或参赛动机..."></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- ===== 动态自定义字段（由竞赛管理员配置） ===== -->
        <template v-if="customFields.length > 0">
          <el-divider content-position="left">
            <i class="el-icon-edit-outline" style="margin-right:4px"></i>
            竞赛附加信息
            <span style="font-size:12px;color:#909399;margin-left:8px">（该竞赛要求填写以下信息）</span>
          </el-divider>
          <el-row :gutter="20">
            <el-col :span="12" v-for="field in customFields" :key="field.fieldKey">
              <el-form-item
                :label="field.fieldLabel"
                :prop="'ext_' + field.fieldKey"
                :rules="field.required === '是' ? [{required: true, message: '请填写' + field.fieldLabel, trigger: 'blur'}] : []">

                <!-- 单行文本 -->
                <el-input v-if="field.fieldType === 'text'"
                          v-model="extFieldsData[field.fieldKey]"
                          :placeholder="field.placeholder || '请输入' + field.fieldLabel"
                          clearable></el-input>

                <!-- 数字 -->
                <el-input v-else-if="field.fieldType === 'number'"
                          v-model.number="extFieldsData[field.fieldKey]"
                          type="number"
                          :placeholder="field.placeholder || '请输入数字'"
                          clearable></el-input>

                <!-- 多行文本 -->
                <el-input v-else-if="field.fieldType === 'textarea'"
                          v-model="extFieldsData[field.fieldKey]"
                          type="textarea" :rows="3"
                          :placeholder="field.placeholder || '请输入' + field.fieldLabel"></el-input>

                <!-- 单选 -->
                <el-radio-group v-else-if="field.fieldType === 'radio'"
                                v-model="extFieldsData[field.fieldKey]">
                  <el-radio v-for="opt in parseOptions(field.fieldOptions)" :key="opt" :label="opt">{{ opt }}</el-radio>
                </el-radio-group>

                <!-- 多选 -->
                <el-checkbox-group v-else-if="field.fieldType === 'checkbox'"
                                   v-model="extFieldsData[field.fieldKey]">
                  <el-checkbox v-for="opt in parseOptions(field.fieldOptions)" :key="opt" :label="opt">{{ opt }}</el-checkbox>
                </el-checkbox-group>

                <!-- 下拉 -->
                <el-select v-else-if="field.fieldType === 'select'"
                           v-model="extFieldsData[field.fieldKey]"
                           :placeholder="field.placeholder || '请选择' + field.fieldLabel"
                           style="width:100%">
                  <el-option v-for="opt in parseOptions(field.fieldOptions)" :key="opt" :label="opt" :value="opt"></el-option>
                </el-select>

                <!-- 日期 -->
                <el-date-picker v-else-if="field.fieldType === 'date'"
                                v-model="extFieldsData[field.fieldKey]"
                                type="date" value-format="yyyy-MM-dd"
                                :placeholder="field.placeholder || '请选择日期'"
                                style="width:100%"></el-date-picker>

              </el-form-item>
            </el-col>
          </el-row>
        </template>

        <!-- 审核区域（管理员/教师可见） -->
        <template v-if="type === 'info' && canAudit">
          <el-divider content-position="left">审核操作</el-divider>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="审核结果">
                <el-radio-group v-model="auditForm.sfsh">
                  <el-radio label="已通过">通过</el-radio>
                  <el-radio label="已驳回">驳回</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="审核回复">
                <el-input v-model="auditForm.shhf" type="textarea" :rows="3" placeholder="审核意见（驳回时必填）"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item>
                <el-button type="primary" icon="el-icon-check" @click="doAudit()">提交审核</el-button>
              </el-form-item>
            </el-col>
          </el-row>
        </template>

        <el-form-item v-if="type !== 'info'" style="text-align:center;margin-top:24px">
          <el-button type="primary" icon="el-icon-check" @click="submitForm()">提交报名</el-button>
          <el-button icon="el-icon-close" @click="singleClose()">取消</el-button>
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
      ro: {},
      ruleForm: { cansaileixing: '个人', sfsh: '待审核' },
      auditForm: { sfsh: '已通过', shhf: '' },
      canAudit: false,
      customFields: [],      // 竞赛配置的动态字段定义
      extFieldsData: {},     // 动态字段的值（key->value）
      rules: {
        jingsaimingcheng: [{ required: true, message: '请输入竞赛名称', trigger: 'blur' }],
        xuehao: [{ required: true, message: '请输入学号', trigger: 'blur' }],
        xueshengxingming: [{ required: true, message: '请输入学生姓名', trigger: 'blur' }],
        cansaileixing: [{ required: true, message: '请选择参赛类型', trigger: 'change' }]
      }
    }
  },
  methods: {
    init(id, type) {
      this.type = type || 'else'
      this.ruleForm = { cansaileixing: '个人', sfsh: '待审核' }
      this.auditForm = { sfsh: '已通过', shhf: '' }
      this.customFields = []
      this.extFieldsData = {}
      const role = this.$storage.get('role')
      this.canAudit = role === '管理员' || role === '教师'

      if (id && type !== 'cross') {
        this.$http({ url: `jingsaibaoming/info/${id}`, method: 'get' }).then(({ data }) => {
          if (data && data.code === 0) {
            this.ruleForm = data.data
            this.auditForm.sfsh = data.data.sfsh || '已通过'
            this.auditForm.shhf = data.data.shhf || ''
            // 解析已存储的动态字段数据
            if (data.data.extFields) {
              try { this.extFieldsData = JSON.parse(data.data.extFields) } catch (e) { this.extFieldsData = {} }
            }
            // 加载字段定义
            if (data.data.jingsaixinxiid) {
              this.loadCustomFields(data.data.jingsaixinxiid)
            }
          }
        })
      } else if (type === 'cross') {
        const crossObj = this.$storage.get('crossObj')
        if (crossObj) {
          this.ruleForm.jingsaimingcheng = crossObj.jingsaimingcheng
          this.ruleForm.jingsaileixing = crossObj.jingsaileixing
          this.ruleForm.jingsaixinxiid = crossObj.id
          // 加载该竞赛的动态字段
          this.loadCustomFields(crossObj.id)
        }
        const userInfo = this.$storage.get('userInfo')
        if (userInfo) {
          this.ruleForm.xuehao = userInfo.username
          this.ruleForm.xueshengxingming = userInfo.xueshengxingming || ''
          this.ruleForm.xueyuanmingcheng = userInfo.xueyuanmingcheng || ''
          this.ruleForm.shouji = userInfo.shouji || ''
        }
        this.ruleForm.shenqingriqi = new Date().toISOString().split('T')[0]
      }
    },

    loadCustomFields(jingsaixinxiid) {
      this.$http({ url: `jingsaiField/byCompetition/${jingsaixinxiid}`, method: 'get' }).then(({ data }) => {
        if (data && data.code === 0) {
          this.customFields = data.data || []
          // 初始化多选字段为数组
          this.customFields.forEach(f => {
            if (f.fieldType === 'checkbox' && !this.extFieldsData[f.fieldKey]) {
              this.$set(this.extFieldsData, f.fieldKey, [])
            }
          })
        }
      })
    },

    parseOptions(str) {
      if (!str) return []
      try {
        const arr = JSON.parse(str)
        return Array.isArray(arr) ? arr : []
      } catch {
        return str.split(/[,，]/).map(s => s.trim()).filter(Boolean)
      }
    },

    submitForm() {
      this.$refs.ruleForm.validate(valid => {
        if (!valid) return
        // 将动态字段数据序列化存入 extFields
        this.ruleForm.sfsh = '待审核'
        this.ruleForm.extFields = JSON.stringify(this.extFieldsData)
        const url = this.ruleForm.id ? 'jingsaibaoming/update' : 'jingsaibaoming/add'
        this.$http({ url, method: 'post', data: this.ruleForm }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message.success('报名成功，请等待审核')
            this.singleClose()
            if (this.parent && this.parent.getDataList) this.parent.getDataList()
          } else { this.$message.error(data.msg) }
        })
      })
    },

    doAudit() {
      if (!this.auditForm.sfsh) return this.$message.warning('请选择审核结果')
      if (this.auditForm.sfsh === '已驳回' && !this.auditForm.shhf) return this.$message.warning('驳回时请填写审核意见')
      const data = {
        ...this.ruleForm,
        sfsh: this.auditForm.sfsh,
        shhf: this.auditForm.shhf
      }
      this.$http({ url: 'jingsaibaoming/update', method: 'post', data }).then(({ data: res }) => {
        if (res && res.code === 0) {
          this.$message.success('审核完成')
          this.singleClose()
          if (this.parent && this.parent.getDataList) this.parent.getDataList()
        } else {
          this.$message.error((res && res.msg) || '审核失败')
        }
      })
    },

    singleClose() {
      this.parent.showFlag = true
      this.parent.addOrUpdateFlag = false
      if (this.parent.jingsaibaomingCrossAddOrUpdateFlag !== undefined) {
        this.parent.jingsaibaomingCrossAddOrUpdateFlag = false
      }
    }
  }
}
</script>

<style scoped>
.addEdit-block { padding: 16px; }
</style>
