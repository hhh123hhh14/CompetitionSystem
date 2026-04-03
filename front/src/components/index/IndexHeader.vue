<template>
  <div class="navbar">
    <!-- 左侧：Logo + 系统名 -->
    <div class="navbar-left">
      <div class="logo-wrap">
        <i class="el-icon-trophy logo-icon"></i>
      </div>
      <span class="title-name">{{ $project.projectName }}</span>
    </div>

    <!-- 右侧：用户信息 + 退出 -->
    <div class="navbar-right">
      <div class="user-block">
        <div class="avatar-circle">
          <i class="el-icon-user"></i>
        </div>
        <div class="user-detail">
          <span class="user-role">{{ $storage.get('role') }}</span>
          <span class="user-name">{{ $storage.get('adminName') }}</span>
        </div>
      </div>
      <div class="divider"></div>
      <div class="logout-btn" @click="onLogout">
        <i class="el-icon-switch-button"></i>
        <span>退出</span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  methods: {
    onLogout() {
      this.$storage.clear()
      this.$router.replace({ name: 'login' })
    }
  }
}
</script>

<style lang="scss" scoped>
.navbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  background: linear-gradient(135deg, #1a1a2e 0%, #16213e 50%, #0f3460 100%);
  box-shadow: 0 2px 20px rgba(0, 0, 0, 0.4);
  box-sizing: border-box;

  .navbar-left {
    display: flex;
    align-items: center;
    gap: 12px;

    .logo-wrap {
      width: 38px;
      height: 38px;
      background: linear-gradient(135deg, #667eea, #764ba2);
      border-radius: 10px;
      display: flex;
      align-items: center;
      justify-content: center;
      box-shadow: 0 4px 12px rgba(102, 126, 234, 0.5);

      .logo-icon {
        font-size: 20px;
        color: #fff;
      }
    }

    .title-name {
      font-size: 18px;
      font-weight: 700;
      color: #fff;
      letter-spacing: 0.5px;
      text-shadow: 0 1px 4px rgba(0,0,0,0.3);
    }
  }

  .navbar-right {
    display: flex;
    align-items: center;
    gap: 16px;

    .user-block {
      display: flex;
      align-items: center;
      gap: 10px;

      .avatar-circle {
        width: 36px;
        height: 36px;
        border-radius: 50%;
        background: linear-gradient(135deg, #f093fb, #f5576c);
        display: flex;
        align-items: center;
        justify-content: center;
        box-shadow: 0 2px 8px rgba(240, 147, 251, 0.4);

        i {
          color: #fff;
          font-size: 18px;
        }
      }

      .user-detail {
        display: flex;
        flex-direction: column;
        line-height: 1.3;

        .user-role {
          font-size: 11px;
          color: rgba(255,255,255,0.55);
          font-weight: 400;
        }

        .user-name {
          font-size: 14px;
          color: #fff;
          font-weight: 600;
        }
      }
    }

    .divider {
      width: 1px;
      height: 28px;
      background: rgba(255,255,255,0.15);
    }

    .logout-btn {
      display: flex;
      align-items: center;
      gap: 6px;
      padding: 8px 14px;
      border-radius: 8px;
      color: rgba(255,255,255,0.7);
      cursor: pointer;
      transition: all 0.2s;
      font-size: 14px;

      i {
        font-size: 16px;
      }

      &:hover {
        background: rgba(255,255,255,0.1);
        color: #fff;
      }
    }
  }
}
</style>
