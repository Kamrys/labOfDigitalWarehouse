import Vue from 'vue';
import Vuex from 'vuex';
import router,{resetRouter} from '@/router';
// npm i vuex-persistedstate
import createPersistedState from "vuex-persistedstate"      // 解决vuex持久化的方法

Vue.use(Vuex);

function addNewRoute(menuList) {
    // console.log("store-menuList===", menuList);
    // router等于暴露出来的router
    let routes = router.options.routes      // 拿到这个路由
    console.log("store-routes===", routes);     // 打印路由出来
    // 循环路由
    routes.forEach(routeItem=>{
        // 判断
        if(routeItem.path=="/Index"){
            // 继续循环子路由
            menuList.forEach(menu=>{
                let childRoute = {
                    path: '/' + menu.menuclick,     // 从上面的menuList打印得出
                    name: menu.menuname,
                    meta:{
                        title: menu.menuname
                    },
                    component:()=>import('../components/' + menu.menucomponent)
                }
                // 从上面的打印路由所得，把children动态写入childRoute中
                routeItem.children.push(childRoute)
            })
        }
    })
    // 调用路由那边的解决重复路由方法
    resetRouter()
    // 合并路由
    router.addRoutes(routes)

}

export default new Vuex.Store({
    state: {
        // 创建一个数组
        menu: []
    },
    // 管理
    mutations: {
        // 通过Login登录后获得对应的数据传入这里进行更新
        setMenu(state, menuList) {
            // 获取数据库那边对应的角色权限
            state.menu = menuList;
            // console.log("store-state.menu===", state.menu);
            
            // 添加路由
            addNewRoute(menuList)
        }
    },
    // 访问
    getters: {
        getMenu(state) {
            return state.menu
        }
    },
    plugins: [createPersistedState()]
});