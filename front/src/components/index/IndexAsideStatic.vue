<template>
  <el-aside class="index-aside" width="220px">
    <div class="aside-inner">
      <div v-for="item in menuList" :key="item.roleName" v-if="role === item.roleName">

        <!-- 首页 -->
        <div class="menu-item" :class="{ active: activeMenu === '/' }"
             @click="menuHandler('', '/')">
          <i class="el-icon-s-home menu-icon"></i>
          <span>首页</span>
        </div>

        <!-- 个人中心 -->
        <div class="menu-group">
          <div class="menu-group-title" @click="toggleGroup('personal')">
            <div class="group-title-left">
              <i class="el-icon-user-solid menu-icon"></i>
              <span>个人中心</span>
            </div>
            <i class="el-icon-arrow-down arrow" :class="{ 'rotated': openGroups.personal }"></i>
          </div>
          <transition name="slide">
            <div v-if="openGroups.personal" class="menu-children">
              <div class="menu-child-item" :class="{ active: activeMenu === '/updatePassword' }"
                   @click="menuHandler('updatePassword', '/updatePassword')">
                修改密码
              </div>
              <div class="menu-child-item" :class="{ active: activeMenu === '/center' }"
                   @click="menuHandler('center', '/center')">
                个人信息
              </div>
            </div>
          </transition>
        </div>

        <!-- 动态菜单 -->
        <div class="menu-group" v-for="(menu, index) in item.backMenu" :key="menu.menu">
          <div class="menu-group-title" @click="toggleGroup(menu.menu)">
            <div class="group-title-left">
              <i :class="getMenuIcon(menu.menu)" class="menu-icon"></i>
              <span>{{ menu.menu }}</span>
            </div>
            <i class="el-icon-arrow-down arrow" :class="{ 'rotated': openGroups[menu.menu] }"></i>
          </div>
          <transition name="slide">
            <div v-if="openGroups[menu.menu]" class="menu-children">
              <div v-for="child in menu.child" :key="child.tableName"
                   class="menu-child-item"
                   :class="{ active: activeMenu === '/' + child.tableName }"
                   @click="menuHandler(child.tableName, '/' + child.tableName)">
                {{ child.menu }}
              </div>
            </div>
          </transition>
        </div>

      </div>
    </div>
  </el-aside>
</template>

<script>
import menu from '@/utils/menu'
export default {
  data() {
    return {
      menuList: [],
      role: '',
      activeMenu: '/',
      openGroups: { personal: false }
    }
  },
  mounted() {
    this.menuList = menu.list()
    this.role = this.$storage.get('role')
    this.activeMenu = this.$route.path || '/'
    // Auto-expand the group containing the active menu
    this.autoExpand()
  },
  watch: {
    $route(to) {
      this.activeMenu = to.path
    }
  },
  methods: {
    toggleGroup(key) {
      this.$set(this.openGroups, key, !this.openGroups[key])
    },
    menuHandler(name, path) {
      this.activeMenu = path || ('/' + name)
      this.$router.push('/' + name)
    },
    autoExpand() {
      const path = this.activeMenu
      if (path === '/updatePassword' || path === '/center') {
        this.$set(this.openGroups, 'personal', true)
        return
      }
      for (const item of this.menuList) {
        if (item.roleName !== this.role) continue
        for (const m of (item.backMenu || [])) {
          for (const child of (m.child || [])) {
            if ('/' + child.tableName === path) {
              this.$set(this.openGroups, m.menu, true)
              return
            }
          }
        }
      }
    },
    getMenuIcon(menuName) {
      const iconMap = {
        '赛前': 'el-icon-date',
        '信息发布': 'el-icon-s-promotion',
        '报名': 'el-icon-edit-outline',
        '作品': 'el-icon-folder-opened',
        '评审': 'el-icon-s-check',
        '赛中': 'el-icon-video-play',
        '赛后': 'el-icon-trophy',
        '成绩': 'el-icon-medal-1',
        '证书': 'el-icon-postcard',
        '统计': 'el-icon-data-analysis',
        '系统': 'el-icon-setting',
        '获奖': 'el-icon-trophy',
        '竞赛': 'el-icon-s-cooperation',
      }
      for (const key of Object.keys(iconMap)) {
        if (menuName.includes(key)) return iconMap[key]
      }
      return 'el-icon-menu'
    }
  }
}
</script>

<style lang="scss" scoped>
.index-aside {
  position: fixed;
  top: 60px;
  left: 0;
  bottom: 0;
  z-index: 999;
  overflow: hidden;
  background: linear-gradient(180deg, #1a1a2e 0%, #16213e 100%);
  box-shadow: 2px 0 20px rgba(0,0,0,0.3);
  width: 220px !important;
}

.aside-inner {
  height: 100%;
  overflow-y: auto;
  overflow-x: hidden;
  padding: 12px 0 24px;
  scrollbar-width: thin;
  scrollbar-color: rgba(255,255,255,0.1) transparent;

  &::-webkit-scrollbar {
    width: 4px;
  }
  &::-webkit-scrollbar-thumb {
    background: rgba(255,255,255,0.1);
    border-radius: 2px;
  }
}

.menu-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px 20px;
  color: rgba(255,255,255,0.65);
  cursor: pointer;
  transition: all 0.2s;
  font-size: 14px;
  border-radius: 0 24px 24px 0;
  margin-right: 16px;

  &:hover {
    color: #fff;
    background: rgba(102, 126, 234, 0.15);
  }

  &.active {
    color: #fff;
    background: linear-gradient(135deg, rgba(102,126,234,0.4), rgba(118,75,162,0.4));
    border-left: 3px solid #667eea;
    padding-left: 17px;
  }
}

.menu-icon {
  font-size: 16px;
  flex-shrink: 0;
}

.menu-group {
  margin-bottom: 2px;
}

.menu-group-title {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px 12px 20px;
  color: rgba(255,255,255,0.7);
  cursor: pointer;
  transition: all 0.2s;
  font-size: 14px;
  font-weight: 500;

  &:hover {
    color: #fff;
    background: rgba(255,255,255,0.05);
  }

  .group-title-left {
    display: flex;
    align-items: center;
    gap: 10px;
  }

  .arrow {
    font-size: 12px;
    transition: transform 0.3s;
    color: rgba(255,255,255,0.4);

    &.rotated {
      transform: rotate(180deg);
    }
  }
}

.menu-children {
  overflow: hidden;
}

.menu-child-item {
  padding: 10px 20px 10px 50px;
  color: rgba(255,255,255,0.5);
  cursor: pointer;
  font-size: 13px;
  transition: all 0.2s;
  position: relative;

  &::before {
    content: '';
    position: absolute;
    left: 32px;
    top: 50%;
    transform: translateY(-50%);
    width: 5px;
    height: 5px;
    border-radius: 50%;
    background: rgba(255,255,255,0.2);
    transition: all 0.2s;
  }

  &:hover {
    color: #fff;
    background: rgba(102, 126, 234, 0.1);

    &::before {
      background: #667eea;
    }
  }

  &.active {
    color: #667eea;
    background: rgba(102, 126, 234, 0.12);

    &::before {
      background: #667eea;
      width: 7px;
      height: 7px;
    }
  }
}

.slide-enter-active, .slide-leave-active {
  transition: all 0.25s ease;
  max-height: 400px;
}
.slide-enter, .slide-leave-to {
  max-height: 0;
  opacity: 0;
}
</style>
