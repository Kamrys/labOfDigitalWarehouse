<template>
  <div id="app">
     <router-view/>
  </div>
</template>

<script>

export default {
  name: 'App',
  components: {},
  data() {
    return {
      user: JSON.parse(sessionStorage.getItem('CurUser'))
    }
  },
  // 解决vux持久化刷新就没的问题↕
  watch: {
    '$store.state.menu': {
      handler(val, old){        
        if(!old && this.user && this.user.no){
          this.$store.commit('setMenu', val)
        }
      },
      immediate: true
    }
  }
}
</script>

<style>
  #app {
    height: 100%;
  }
</style>
