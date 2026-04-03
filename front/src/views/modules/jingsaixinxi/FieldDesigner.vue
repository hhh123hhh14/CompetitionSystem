<template>
  <div class="field-designer">
    <div class="designer-header">
      <div class="designer-title">
        <i class="el-icon-setting" style="color:#667eea;margin-right:6px"></i>
        报名信息字段配置
      </div>
      <el-button type="primary" size="small" icon="el-icon-plus" @click="addField">
        添加字段
      </el-button>
    </div>

    <div class="field-tip">
      <i class="el-icon-info-circle" style="color:#909399"></i>
      配置学生报名时需要填写的额外信息字段，支持文本、单选、多选、下拉、日期等多种类型。
    </div>

    <!-- 字段列表 -->
    <div v-if="fields.length === 0" class="field-empty">
      <i class="el-icon-document-add"></i>
      <p>暂无自定义字段，点击「添加字段」开始配置</p>
    </div>

    <div v-else class="field-list">
      <div v-for="(field, index) in fields" :key="field._key" class="field-card">
        <!-- 卡片头部 -->
        <div class="field-card-header">
          <div class="field-card-title">
            <el-tag size="mini" :type="typeTagMap[field.fieldType] || 'info'">
              {{ typeNameMap[field.fieldType] || field.fieldType }}
            </el-tag>
            <span class="field-label-preview">{{ field.fieldLabel || '（未命名字段）' }}</span>
            <el-tag v-if="field.required === '是'" size="mini" type="danger" effect="plain">必填</el-tag>
          </div>
          <div class="field-card-actions">
            <el-button type="text" icon="el-icon-top" size="mini" :disabled="index === 0"
                       @click="moveUp(index)" title="上移"></el-button>
            <el-button type="text" icon="el-icon-bottom" size="mini" :disabled="index === fields.length - 1"
                       @click="moveDown(index)" title="下移"></el-button>
            <el-button type="text" icon="el-icon-edit" size="mini" @click="editField(index)"
                       style="color:#409eff">编辑</el-button>
            <el-button type="text" icon="el-icon-delete" size="mini" @click="removeField(index)"
                       style="color:#f56c6c">删除</el-button>
          </div>
        </div>

        <!-- 字段预览 -->
        <div class="field-preview">
          <el-input v-if="['text','number','textarea'].includes(field.fieldType)"
                    :type="field.fieldType === 'textarea' ? 'textarea' : 'text'"
                    :placeholder="field.placeholder || '学生填写' + field.fieldLabel"
                    size="small" disabled></el-input>
          <el-select v-else-if="field.fieldType === 'select'"
                     :placeholder="field.placeholder || '请选择' + field.fieldLabel"
                     size="small" disabled style="width:100%">
            <el-option v-for="opt in parseOptions(field.fieldOptions)" :key="opt" :label="opt" :value="opt"></el-option>
          </el-select>
          <el-radio-group v-else-if="field.fieldType === 'radio'" size="small" disabled>
            <el-radio v-for="opt in parseOptions(field.fieldOptions)" :key="opt" :label="opt">{{ opt }}</el-radio>
          </el-radio-group>
          <el-checkbox-group v-else-if="field.fieldType === 'checkbox'" size="small" disabled>
            <el-checkbox v-for="opt in parseOptions(field.fieldOptions)" :key="opt" :label="opt">{{ opt }}</el-checkbox>
          </el-checkbox-group>
          <el-date-picker v-else-if="field.fieldType === 'date'"
                          type="date" size="small" disabled
                          :placeholder="field.placeholder || '请选择日期'"
                          style="width:100%"></el-date-picker>
        </div>
      </div>
    </div>

    <!-- 编辑字段弹窗 -->
    <el-dialog :title="editingIndex === -1 ? '添加字段' : '编辑字段'" :visible.sync="dialogVisible"
               width="620px" :close-on-click-modal="false" @close="resetEditForm">
      <el-form :model="editForm" :rules="editRules" ref="editForm" label-width="100px" size="small">

        <el-form-item label="字段类型" prop="fieldType">
          <el-select v-model="editForm.fieldType" @change="onTypeChange" placeholder="请选择字段类型" style="width:100%">
            <el-option v-for="t in fieldTypes" :key="t.value" :label="t.label" :value="t.value">
              <span>{{ t.label }}</span>
              <span style="color:#999;margin-left:8px;font-size:12px">{{ t.desc }}</span>
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="字段名称" prop="fieldLabel">
          <el-input v-model="editForm.fieldLabel" placeholder="如：学院名称、特长描述" maxlength="50" show-word-limit></el-input>
        </el-form-item>

        <el-form-item label="字段KEY" prop="fieldKey">
          <el-input v-model="editForm.fieldKey" placeholder="英文/拼音，如：xueyuan、tezhang（用于数据存储）" maxlength="50">
            <template slot="prepend">ext_</template>
          </el-input>
          <div class="form-tip">只能使用英文字母和数字，不能重复</div>
        </el-form-item>

        <el-form-item label="是否必填">
          <el-switch v-model="editForm.required" active-value="是" inactive-value="否"
                     active-text="必填" inactive-text="选填"></el-switch>
        </el-form-item>

        <el-form-item label="占位文字">
          <el-input v-model="editForm.placeholder" placeholder="输入框提示文字（留空则自动生成）" maxlength="100"></el-input>
        </el-form-item>

        <el-form-item label="默认值">
          <el-input v-model="editForm.defaultValue" placeholder="默认填充的值（选填）" maxlength="200"></el-input>
        </el-form-item>

        <!-- 选项配置（针对 radio/checkbox/select） -->
        <el-form-item v-if="['radio','checkbox','select'].includes(editForm.fieldType)" label="选项列表">
          <div class="options-editor">
            <div v-for="(opt, i) in optionsList" :key="i" class="option-row">
              <el-input v-model="optionsList[i]" :placeholder="'选项 ' + (i+1)" size="small" style="flex:1"></el-input>
              <el-button type="text" icon="el-icon-delete" size="mini"
                         @click="removeOption(i)" style="color:#f56c6c;margin-left:8px"></el-button>
            </div>
            <el-button type="dashed" size="mini" icon="el-icon-plus" @click="addOption" style="width:100%;margin-top:8px">
              添加选项
            </el-button>
          </div>
        </el-form-item>

      </el-form>

      <span slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmEdit">确认</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'FieldDesigner',
  props: {
    // 传入竞赛ID（编辑时使用，新建时为null）
    jingsaixinxiid: {
      type: [Number, String],
      default: null
    },
    // 外部传入的初始字段列表
    value: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      fields: [],
      dialogVisible: false,
      editingIndex: -1, // -1=新增，>=0=编辑
      editForm: this.defaultEditForm(),
      optionsList: [''],
      editRules: {
        fieldType: [{ required: true, message: '请选择字段类型', trigger: 'change' }],
        fieldLabel: [{ required: true, message: '请输入字段名称', trigger: 'blur' }],
        fieldKey: [
          { required: true, message: '请输入字段KEY', trigger: 'blur' },
          { pattern: /^[a-zA-Z][a-zA-Z0-9_]*$/, message: '只能以字母开头，包含字母、数字、下划线', trigger: 'blur' }
        ]
      },
      fieldTypes: [
        { value: 'text',     label: '单行文本', desc: '普通文字输入' },
        { value: 'number',   label: '数字输入', desc: '只允许输入数字' },
        { value: 'textarea', label: '多行文本', desc: '适合较长说明' },
        { value: 'radio',    label: '单选按钮', desc: '从选项中选一个' },
        { value: 'checkbox', label: '多选框',   desc: '可选多个选项' },
        { value: 'select',   label: '下拉选择', desc: '下拉菜单选择' },
        { value: 'date',     label: '日期选择', desc: '日历选择日期' },
      ],
      typeTagMap: {
        text: '',
        number: 'info',
        textarea: '',
        radio: 'warning',
        checkbox: 'warning',
        select: 'success',
        date: 'danger',
      },
      typeNameMap: {
        text: '文本',
        number: '数字',
        textarea: '多行',
        radio: '单选',
        checkbox: '多选',
        select: '下拉',
        date: '日期',
      }
    }
  },
  watch: {
    value: {
      immediate: true,
      handler(val) {
        if (val && val.length) {
          this.fields = val.map((f, i) => ({ ...f, _key: f.id || Date.now() + i }))
        }
      }
    },
    fields: {
      deep: true,
      handler(val) {
        this.$emit('input', val)
        this.$emit('change', val)
      }
    }
  },
  methods: {
    defaultEditForm() {
      return {
        fieldType: 'text',
        fieldLabel: '',
        fieldKey: '',
        required: '否',
        defaultValue: '',
        placeholder: '',
        fieldOptions: ''
      }
    },
    addField() {
      this.editingIndex = -1
      this.editForm = this.defaultEditForm()
      this.optionsList = ['']
      this.dialogVisible = true
    },
    editField(index) {
      this.editingIndex = index
      const f = this.fields[index]
      this.editForm = { ...f }
      this.optionsList = this.parseOptions(f.fieldOptions)
      if (!this.optionsList.length) this.optionsList = ['']
      this.dialogVisible = true
    },
    removeField(index) {
      this.$confirm('确定删除该字段吗？', '提示', { type: 'warning' }).then(() => {
        this.fields.splice(index, 1)
      })
    },
    moveUp(index) {
      if (index === 0) return
      const arr = this.fields
      ;[arr[index - 1], arr[index]] = [arr[index], arr[index - 1]]
      this.fields = [...arr]
    },
    moveDown(index) {
      if (index >= this.fields.length - 1) return
      const arr = this.fields
      ;[arr[index], arr[index + 1]] = [arr[index + 1], arr[index]]
      this.fields = [...arr]
    },
    onTypeChange(type) {
      if (['radio', 'checkbox', 'select'].includes(type)) {
        this.optionsList = ['']
      }
    },
    addOption() {
      this.optionsList.push('')
    },
    removeOption(i) {
      if (this.optionsList.length === 1) return this.$message.warning('至少保留一个选项')
      this.optionsList.splice(i, 1)
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
    confirmEdit() {
      this.$refs.editForm.validate(valid => {
        if (!valid) return
        // 检查KEY唯一性
        const key = this.editForm.fieldKey
        const duplicate = this.fields.some((f, i) => f.fieldKey === key && i !== this.editingIndex)
        if (duplicate) {
          return this.$message.error('字段KEY已存在，请更换一个')
        }

        // 序列化选项
        if (['radio', 'checkbox', 'select'].includes(this.editForm.fieldType)) {
          const opts = this.optionsList.filter(o => o.trim())
          if (!opts.length) return this.$message.error('请至少添加一个选项')
          this.editForm.fieldOptions = JSON.stringify(opts)
        } else {
          this.editForm.fieldOptions = ''
        }

        const field = {
          ...this.editForm,
          _key: Date.now(),
          sortOrder: this.editingIndex === -1 ? this.fields.length : this.editingIndex
        }

        if (this.editingIndex === -1) {
          this.fields.push(field)
        } else {
          this.$set(this.fields, this.editingIndex, field)
        }

        this.dialogVisible = false
      })
    },
    resetEditForm() {
      this.$refs.editForm && this.$refs.editForm.resetFields()
    },
    // 供父组件调用：获取字段列表（去掉内部属性）
    getFields() {
      return this.fields.map(({ _key, ...rest }) => rest)
    }
  }
}
</script>

<style scoped>
.field-designer {
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  overflow: hidden;
}

.designer-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 14px 16px;
  background: linear-gradient(135deg, #f0f4ff, #f5f7ff);
  border-bottom: 1px solid #e4e7ed;
}

.designer-title {
  font-size: 14px;
  font-weight: 600;
  color: #303133;
}

.field-tip {
  padding: 10px 16px;
  background: #fafbff;
  color: #606266;
  font-size: 12px;
  border-bottom: 1px dashed #e4e7ed;
}

.field-empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 48px 0;
  color: #c0c4cc;

  i {
    font-size: 48px;
    margin-bottom: 12px;
  }

  p {
    font-size: 13px;
  }
}

.field-list {
  padding: 12px;
  display: flex;
  flex-direction: column;
  gap: 10px;
  background: #f8f9fc;
}

.field-card {
  background: #fff;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  overflow: hidden;
  transition: box-shadow 0.2s;

  &:hover {
    box-shadow: 0 2px 12px rgba(0,0,0,0.08);
  }
}

.field-card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px 14px;
  background: #fafafa;
  border-bottom: 1px solid #f0f0f0;
}

.field-card-title {
  display: flex;
  align-items: center;
  gap: 8px;

  .field-label-preview {
    font-size: 13px;
    color: #303133;
    font-weight: 500;
  }
}

.field-card-actions {
  display: flex;
  align-items: center;
  gap: 4px;
}

.field-preview {
  padding: 12px 14px;
}

.options-editor {
  .option-row {
    display: flex;
    align-items: center;
    margin-bottom: 8px;
  }
}

.form-tip {
  font-size: 11px;
  color: #909399;
  margin-top: 4px;
}
</style>
