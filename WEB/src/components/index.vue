<template>
    <transition name="fade">
      <el-container v-if="isLoaded" style="height: 100vh; border: 1px solid #eee">
        <el-aside :width="aside_width">
          <Aside :isCollapse="is_Collapse"></Aside>
        </el-aside>
  
        <el-container style="height: 100%;">
          <el-header>
            <!-- @doCollapse="do_Collapse"：对接Header中collapse方法的this.$emit('doCollapse') -->
            <Header @doCollapse="do_Collapse" :icon="icon_s"></Header>
          </el-header>
  
          <el-main>
            <!-- <Main></Main> -->
            <!-- 个人中心作为二级路由，跳转到这一个位置 -->
            <router-view/>
          </el-main>
        </el-container>
      </el-container>
    </transition>
  </template>
  
  <script>
  import Aside from './Aside.vue';
  import Header from './Header.vue';
  // import Main from './Main.vue';
  
  export default {
    name: "IndexPage",
    components: {
      Aside,      // 导航
      Header,     // 头部
      // Main        // 主菜单
    },
    data() {
      return {
        is_Collapse: false,
        aside_width: '200px',
        icon_s: 'el-icon-s-fold',
        isLoaded: false,  // 控制内容的加载状态
      }
    },
    methods: {
      // 收缩图标：
      do_Collapse() {
        this.is_Collapse = !this.is_Collapse
        if (!this.is_Collapse) { // 展开
          this.aside_width = '200px'
          this.icon_s = 'el-icon-s-fold'
        } else {    // 收起
          this.aside_width = '64px'
          this.icon_s = 'el-icon-s-unfold'
        }
      },
    },
    mounted() {
        this.isLoaded = true;
    }
  };
  </script>
  
  <style scoped>
  .el-header {
    /* background-color: #B3C0D1; */
    color: #333;
    line-height: 60px;
    text-align: right;
    font-size: 12px;
    height: 100%;
    border-bottom: rgb(168, 168, 168, 0.3) 3px solid;
  }
  
  .el-main {
    height: 100%;
    padding: 5px;
  }
  
  .el-aside {
    color: #333;
    background-color: rgb(238, 241, 246);
    margin-left: -1px;
  }
  
  /* 渐显动画 */
  .fade-enter-active, .fade-leave-active {
    transition: opacity 0.3s;
  }
  .fade-enter, .fade-leave-to /* .fade-leave-active in <2.1.8 */ {
    opacity: 0;
  }
  </style>