import Vue from 'vue'
import App from './App.vue'
import ElementUI from 'element-ui';   // 全局引用element-ui ↓
import 'element-ui/lib/theme-chalk/index.css';
import './assets/global.css';
import axios from 'axios';
import VueRouter from 'vue-router'; 
import router from './router';    // 路由
import store from './store';      // vuex

Vue.prototype.$axios=axios;
Vue.prototype.$httpUrl='http://localhost:8088'
Vue.config.productionTip = false
Vue.use(VueRouter);
Vue.use(ElementUI, {size: 'small'});     // 使用


new Vue({
  store,
  router,
  render: h => h(App),
}).$mount('#app')
