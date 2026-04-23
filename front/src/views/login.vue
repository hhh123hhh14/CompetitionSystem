<template>
  <div class="login-page">
    <!-- 背景装饰 -->
    <div class="bg-decor">
      <div class="circle c1"></div>
      <div class="circle c2"></div>
      <div class="circle c3"></div>
    </div>

    <!-- 登录卡片 -->
    <div class="login-card">
      <!-- 左侧装饰面板 -->
      <div class="login-left">
        <div class="brand">
          <div class="brand-icon"><i class="el-icon-trophy"></i></div>
          <div class="brand-name">大学生竞赛<br>管理系统</div>
        </div>
        <div class="brand-desc">College Student Competition Management System</div>
        <div class="brand-features">
          <div class="feature-item"><i class="el-icon-s-promotion"></i> 竞赛信息发布与报名</div>
          <div class="feature-item"><i class="el-icon-folder-opened"></i> 作品提交与评审打分</div>
          <div class="feature-item"><i class="el-icon-medal-1"></i> 获奖公示与证书管理</div>
          <div class="feature-item"><i class="el-icon-data-analysis"></i> 统计分析与数据可视化</div>
        </div>
      </div>

      <!-- 右侧登录表单 -->
      <div class="login-right">
        <div class="form-title">欢迎回来</div>
        <div class="form-subtitle">请登录您的账户</div>

        <el-form class="login-form" ref="loginForm">
          <!-- 角色选择 -->
          <div class="role-tabs">
            <div v-for="item in menus" :key="item.roleName"
                 v-if="item.hasBackLogin === '是'"
                 class="role-tab"
                 :class="{ active: rulesForm.role === item.roleName }"
                 @click="rulesForm.role = item.roleName">
              {{ item.roleName }}
            </div>
          </div>

          <!-- 用户名 -->
          <div class="input-group">
            <label class="input-label">用户名</label>
            <div class="input-wrap">
              <i class="el-icon-user input-icon"></i>
              <input class="form-input" type="text" v-model="rulesForm.username"
                     placeholder="请输入用户名" @keyup.enter="login" />
            </div>
          </div>

          <!-- 密码 -->
          <div class="input-group">
            <label class="input-label">密码</label>
            <div class="input-wrap">
              <i class="el-icon-lock input-icon"></i>
              <input class="form-input" :type="showPwd ? 'text' : 'password'" v-model="rulesForm.password"
                     placeholder="请输入密码" @keyup.enter="login" />
              <i :class="showPwd ? 'el-icon-view' : 'el-icon-hide'" class="input-icon-right"
                 @click="showPwd = !showPwd"></i>
            </div>
          </div>

          <!-- 登录按钮 -->
          <button class="login-btn" @click="login" :disabled="loading">
            <span v-if="!loading">登录</span>
            <span v-else><i class="el-icon-loading"></i> 登录中...</span>
          </button>

          <!-- 忘记密码与注册链接 -->
          <div class="register-links">
            <span class="register-link" @click="$router.push('/forgot-password')">
              <i class="el-icon-question"></i> 忘记密码
            </span>
            <span class="sep">|</span>
            <span class="register-link" @click="register('xuesheng')">
              <i class="el-icon-edit"></i> 学生注册
            </span>
            <span class="sep">|</span>
            <span class="register-link" @click="register('jiaoshi')">
              <i class="el-icon-edit"></i> 教师注册
            </span>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
import menu from "@/utils/menu"
export default {
  data() {
    return {
      rulesForm: { username: '', password: '', role: '' },
      menus: [],
      tableName: '',
      showPwd: false,
      loading: false
    }
  },
  mounted() {
    this.menus = menu.list()
    // 默认选中第一个有登录权限的角色
    const first = this.menus.find(m => m.hasBackLogin === '是')
    if (first) this.rulesForm.role = first.roleName
  },
  methods: {
    register(tableName) {
      this.$storage.set("loginTable", tableName)
      this.$router.push({ path: '/register' })
    },
    login() {
      if (!this.rulesForm.username) return this.$message.error('请输入用户名')
      if (!this.rulesForm.password) return this.$message.error('请输入密码')
      if (!this.rulesForm.role) return this.$message.error('请选择角色')

      for (const m of this.menus) {
        if (m.roleName === this.rulesForm.role) this.tableName = m.tableName
      }

      this.loading = true
      this.$http({
        url: `${this.tableName}/login?username=${this.rulesForm.username}&password=${this.rulesForm.password}`,
        method: 'post'
      }).then(({ data }) => {
        this.loading = false
        if (data && data.code === 0) {
          this.$storage.set('Token', data.token)
          this.$storage.set('role', this.rulesForm.role)
          this.$storage.set('sessionTable', this.tableName)
          this.$storage.set('adminName', this.rulesForm.username)
          this.$router.replace({ path: '/index/' })
        } else {
          this.$message.error(data.msg || '登录失败')
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
.login-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #1a1a2e 0%, #16213e 40%, #0f3460 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
}

// 背景圆形装饰
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

// 登录卡片
.login-card {
  display: flex;
  width: 860px;
  height: 540px;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 25px 60px rgba(0,0,0,0.4);
  position: relative;
  z-index: 1;
}

// 左侧装饰
.login-left {
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

// 右侧表单
.login-right {
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

// 角色选择
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

// 输入框组
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

// 登录按钮
.login-btn {
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

// 注册链接
.register-links {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  margin-top: 16px;

  .register-link {
    font-size: 13px;
    color: #667eea;
    cursor: pointer;
    transition: all 0.2s;

    &:hover {
      color: #764ba2;
      text-decoration: underline;
    }
  }

  .sep {
    color: #e4e7ed;
    font-size: 12px;
  }
}
</style>
