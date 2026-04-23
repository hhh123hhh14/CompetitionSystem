<template>
  <div class="forgot-page">
    <!-- 背景装饰 -->
    <div class="bg-decor">
      <div class="circle c1"></div>
      <div class="circle c2"></div>
      <div class="circle c3"></div>
    </div>

    <!-- 找回密码卡片 -->
    <div class="forgot-card">
      <!-- 左侧装饰面板 -->
      <div class="forgot-left">
        <div class="brand">
          <div class="brand-icon"><i class="el-icon-trophy"></i></div>
          <div class="brand-name">大学生竞赛<br>管理系统</div>
        </div>
        <div class="brand-desc">College Student Competition Management System</div>
        <div class="brand-features">
          <div class="feature-item"><i class="el-icon-unlock"></i> 输入账号与手机号</div>
          <div class="feature-item"><i class="el-icon-circle-check"></i> 验证身份信息</div>
          <div class="feature-item"><i class="el-icon-key"></i> 设置全新密码</div>
        </div>
      </div>

      <!-- 右侧表单 -->
      <div class="forgot-right">
        <div class="form-title">找回密码</div>
        <div class="form-subtitle">请验证您的身份以重置密码</div>

        <!-- 角色选择 -->
        <div class="role-tabs">
          <div v-for="item in menus" :key="item.roleName"
               v-if="item.hasBackLogin === '是'"
               class="role-tab"
               :class="{ active: role === item.roleName }"
               @click="switchRole(item.roleName)">
            {{ item.roleName }}
          </div>
        </div>

        <!-- 步骤1：验证身份（学生/教师） -->
        <template v-if="role !== '管理员'">
          <template v-if="step === 1">
            <div class="input-group">
              <label class="input-label">{{ role === '学生' ? '学号' : '工号' }}</label>
              <div class="input-wrap">
                <i class="el-icon-user input-icon"></i>
                <input class="form-input" type="text" v-model="form.username"
                       :placeholder="'请输入' + (role === '学生' ? '学号' : '工号')" @keyup.enter="verifyPhone" />
              </div>
            </div>
            <div class="input-group">
              <label class="input-label">手机号</label>
              <div class="input-wrap">
                <i class="el-icon-phone input-icon"></i>
                <input class="form-input" type="text" v-model="form.phone"
                       placeholder="请输入注册时的手机号" @keyup.enter="verifyPhone" />
              </div>
            </div>
            <button class="action-btn" @click="verifyPhone" :disabled="loading">
              <span v-if="!loading">下一步</span>
              <span v-else><i class="el-icon-loading"></i> 验证中...</span>
            </button>
          </template>

          <!-- 步骤2：设置新密码 -->
          <template v-if="step === 2">
            <div class="step-hint">
              <i class="el-icon-success"></i> 身份验证通过，请设置新密码
            </div>
            <div class="input-group">
              <label class="input-label">新密码</label>
              <div class="input-wrap">
                <i class="el-icon-lock input-icon"></i>
                <input class="form-input" :type="showPwd ? 'text' : 'password'" v-model="form.newPassword"
                       placeholder="请输入新密码" @keyup.enter="resetPassword" />
                <i :class="showPwd ? 'el-icon-view' : 'el-icon-hide'" class="input-icon-right"
                   @click="showPwd = !showPwd"></i>
              </div>
            </div>
            <div class="input-group">
              <label class="input-label">确认密码</label>
              <div class="input-wrap">
                <i class="el-icon-lock input-icon"></i>
                <input class="form-input" :type="showPwd2 ? 'text' : 'password'" v-model="form.confirmPassword"
                       placeholder="请再次输入新密码" @keyup.enter="resetPassword" />
                <i :class="showPwd2 ? 'el-icon-view' : 'el-icon-hide'" class="input-icon-right"
                   @click="showPwd2 = !showPwd2"></i>
              </div>
            </div>
            <button class="action-btn" @click="resetPassword" :disabled="loading">
              <span v-if="!loading">重置密码</span>
              <span v-else><i class="el-icon-loading"></i> 重置中...</span>
            </button>
          </template>
        </template>

        <!-- 管理员：直接输入用户名重置 -->
        <template v-if="role === '管理员'">
          <div class="input-group">
            <label class="input-label">用户名</label>
            <div class="input-wrap">
              <i class="el-icon-user input-icon"></i>
              <input class="form-input" type="text" v-model="form.username"
                     placeholder="请输入管理员用户名" @keyup.enter="resetAdminPassword" />
            </div>
          </div>
          <button class="action-btn" @click="resetAdminPassword" :disabled="loading">
            <span v-if="!loading">重置密码</span>
            <span v-else><i class="el-icon-loading"></i> 重置中...</span>
          </button>
        </template>

        <!-- 返回登录 -->
        <div class="back-link">
          <span @click="$router.push('/login')">
            <i class="el-icon-arrow-left"></i> 返回登录
          </span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import menu from "@/utils/menu"
export default {
  data() {
    return {
      menus: [],
      role: '',
      tableName: '',
      step: 1,
      showPwd: false,
      showPwd2: false,
      loading: false,
      form: {
        username: '',
        phone: '',
        newPassword: '',
        confirmPassword: ''
      }
    }
  },
  mounted() {
    this.menus = menu.list()
    const first = this.menus.find(m => m.hasBackLogin === '是')
    if (first) {
      this.role = first.roleName
      this.tableName = first.tableName
    }
  },
  methods: {
    switchRole(roleName) {
      this.role = roleName
      this.tableName = this.menus.find(m => m.roleName === roleName).tableName
      this.step = 1
      this.form = { username: '', phone: '', newPassword: '', confirmPassword: '' }
    },
    verifyPhone() {
      if (!this.form.username) return this.$message.error('请输入' + (this.role === '学生' ? '学号' : '工号'))
      if (!this.form.phone) return this.$message.error('请输入手机号')

      this.loading = true
      this.$http({
        url: `${this.tableName}/verifyPhone?username=${this.form.username}&phone=${this.form.phone}`,
        method: 'post'
      }).then(({ data }) => {
        this.loading = false
        if (data && data.code === 0) {
          this.step = 2
          this.$message.success('验证通过')
        } else {
          this.$message.error(data.msg || '验证失败')
        }
      }).catch(() => {
        this.loading = false
        this.$message.error('网络异常，请稍后重试')
      })
    },
    resetPassword() {
      if (!this.form.newPassword) return this.$message.error('请输入新密码')
      if (this.form.newPassword.length < 6) return this.$message.error('密码长度不能少于6位')
      if (this.form.newPassword !== this.form.confirmPassword) return this.$message.error('两次密码输入不一致')

      this.loading = true
      this.$http({
        url: `${this.tableName}/resetPass?username=${this.form.username}&phone=${this.form.phone}&newPassword=${this.form.newPassword}`,
        method: 'post'
      }).then(({ data }) => {
        this.loading = false
        if (data && data.code === 0) {
          this.$message({
            message: '密码重置成功，请使用新密码登录',
            type: 'success',
            duration: 2000,
            onClose: () => {
              this.$router.replace('/login')
            }
          })
        } else {
          this.$message.error(data.msg || '重置失败')
        }
      }).catch(() => {
        this.loading = false
        this.$message.error('网络异常，请稍后重试')
      })
    },
    resetAdminPassword() {
      if (!this.form.username) return this.$message.error('请输入用户名')

      this.loading = true
      this.$http({
        url: `users/resetPass?username=${this.form.username}`,
        method: 'post'
      }).then(({ data }) => {
        this.loading = false
        if (data && data.code === 0) {
          this.$message({
            message: data.msg || '密码已重置为：123456',
            type: 'success',
            duration: 3000,
            onClose: () => {
              this.$router.replace('/login')
            }
          })
        } else {
          this.$message.error(data.msg || '重置失败')
        }
      }).catch(() => {
        this.loading = false
        this.$message.error('网络异常，请稍后重试')
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.forgot-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #1a1a2e 0%, #16213e 40%, #0f3460 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
}

.bg-decor {
  position: absolute;
  inset: 0;
  pointer-events: none;

  .circle {
    position: absolute;
    border-radius: 50%;
    opacity: 0.08;
    background: linear-gradient(135deg, #667eea, #764ba2);
  }

  .c1 { width: 500px; height: 500px; top: -150px; right: -100px; }
  .c2 { width: 300px; height: 300px; bottom: -80px; left: -80px; }
  .c3 { width: 200px; height: 200px; bottom: 100px; right: 200px; opacity: 0.05; }
}

.forgot-card {
  display: flex;
  width: 860px;
  min-height: 540px;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 25px 60px rgba(0,0,0,0.4);
  position: relative;
  z-index: 1;
}

.forgot-left {
  width: 340px;
  background: linear-gradient(160deg, #667eea 0%, #764ba2 100%);
  padding: 48px 40px;
  display: flex;
  flex-direction: column;
  color: #fff;

  .brand {
    display: flex;
    align-items: center;
    gap: 14px;
    margin-bottom: 16px;

    .brand-icon {
      width: 52px;
      height: 52px;
      background: rgba(255,255,255,0.2);
      border-radius: 14px;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 26px;
      backdrop-filter: blur(10px);
    }

    .brand-name {
      font-size: 20px;
      font-weight: 700;
      line-height: 1.4;
    }
  }

  .brand-desc {
    font-size: 11px;
    opacity: 0.65;
    margin-bottom: 40px;
    line-height: 1.5;
  }

  .brand-features {
    display: flex;
    flex-direction: column;
    gap: 16px;

    .feature-item {
      display: flex;
      align-items: center;
      gap: 10px;
      font-size: 13px;
      opacity: 0.85;

      i {
        font-size: 16px;
        opacity: 0.9;
        width: 18px;
      }
    }
  }
}

.forgot-right {
  flex: 1;
  background: #fff;
  padding: 48px 44px;
  display: flex;
  flex-direction: column;

  .form-title {
    font-size: 26px;
    font-weight: 700;
    color: #1a1a2e;
    margin-bottom: 6px;
  }

  .form-subtitle {
    font-size: 14px;
    color: #909399;
    margin-bottom: 28px;
  }
}

.role-tabs {
  display: flex;
  gap: 8px;
  margin-bottom: 24px;
  background: #f5f7fa;
  padding: 4px;
  border-radius: 10px;

  .role-tab {
    flex: 1;
    text-align: center;
    padding: 8px 0;
    border-radius: 8px;
    font-size: 13px;
    color: #606266;
    cursor: pointer;
    transition: all 0.2s;

    &.active {
      background: #fff;
      color: #667eea;
      font-weight: 600;
      box-shadow: 0 2px 8px rgba(102,126,234,0.2);
    }

    &:hover:not(.active) {
      background: rgba(255,255,255,0.6);
    }
  }
}

.step-hint {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 16px;
  background: #f0f9eb;
  border-radius: 10px;
  color: #67c23a;
  font-size: 13px;
  margin-bottom: 20px;

  i { font-size: 18px; }
}

.input-group {
  margin-bottom: 16px;

  .input-label {
    display: block;
    font-size: 13px;
    color: #606266;
    margin-bottom: 6px;
    font-weight: 500;
  }

  .input-wrap {
    position: relative;
    display: flex;
    align-items: center;
    background: #f8f9fc;
    border: 1.5px solid #e4e7ed;
    border-radius: 10px;
    transition: all 0.2s;

    &:focus-within {
      border-color: #667eea;
      background: #fff;
      box-shadow: 0 0 0 3px rgba(102,126,234,0.1);
    }

    .input-icon {
      padding: 0 12px;
      color: #c0c4cc;
      font-size: 16px;
      flex-shrink: 0;
    }

    .form-input {
      flex: 1;
      border: none;
      outline: none;
      background: transparent;
      padding: 12px 0;
      font-size: 14px;
      color: #303133;

      &::placeholder {
        color: #c0c4cc;
      }
    }

    .input-icon-right {
      padding: 0 12px;
      color: #c0c4cc;
      font-size: 16px;
      cursor: pointer;
      flex-shrink: 0;

      &:hover { color: #667eea; }
    }
  }
}

.action-btn {
  width: 100%;
  height: 46px;
  border: none;
  border-radius: 10px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: #fff;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  margin-top: 8px;
  transition: all 0.2s;
  letter-spacing: 1px;

  &:hover:not(:disabled) {
    transform: translateY(-1px);
    box-shadow: 0 6px 20px rgba(102,126,234,0.4);
  }

  &:disabled {
    opacity: 0.7;
    cursor: not-allowed;
  }
}

.back-link {
  display: flex;
  justify-content: center;
  margin-top: 20px;

  span {
    font-size: 13px;
    color: #667eea;
    cursor: pointer;
    transition: all 0.2s;

    &:hover {
      color: #764ba2;
      text-decoration: underline;
    }
  }
}
</style>
