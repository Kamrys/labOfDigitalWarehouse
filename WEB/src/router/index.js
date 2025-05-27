/**
 * 路由没有生效解决方法：
 * 在App.vue中将标签换成 router-view
 */

// 导入 Vue Router 模块
import VueRouter from "vue-router";

// 定义路由配置数组
const routes = [
    // 登录界面
    {
        // 路由路径，表示根路径 '/'
        path: '/',
        // 登录--路由名称，可以用来在代码中引用这个路由
        name: 'login',
        // 异步组件加载，使用动态导入语法来懒加载组件
        component: () => import('../components/Login/Login.vue')
    },
    // 主页面，登录成功跳转过来
    {
        path: '/Index',
        name: 'index',
        component: () => import('../components/index.vue'),
        // 子路由
        children:[
            {
                path: '/Home',
                name: 'home',
                meta:{
                    title: '首页'
                },
                component:()=>import('../components/Home/Home.vue')
            },
            /* {
                path: '/Admin',
                name: 'admin',
                meta:{
                    title: '超级管理员'
                },
                component:()=>import('../components/admin/AdminManage.vue')
            },
            {
                path: '/User',
                name: 'user',
                meta:{
                    title: '用户管理'
                },
                component:()=>import('../components/user/UserManage.vue')
            } */
        ]
    }
];

// 创建 Vue Router 实例
const router = new VueRouter({
    // 设置路由模式为 history 模式，这样 URL 不会有 # 号
    mode: 'history',
    // 传递路由配置数组
    routes
});

// 避免重复路由
export function resetRouter() {
    // 重置路由匹配器（matcher）
    // 创建一个新的 VueRouter 实例，模式为 history，初始路由配置为空数组
    router.matcher = new VueRouter({
        mode: 'history', // 使用 HTML5 History 模式
        routes: [] // 初始路由配置为空数组，表示没有路由
    }).matcher; // 获取新创建的 VueRouter 实例的 matcher 并赋值给当前路由实例的 matcher
}

// 避免路由冲突解决方法之一     保存原始的 push 方法
const VueRouterPush = VueRouter.prototype.push
// 重写 VueRouter 的 push 方法
VueRouter.prototype.push = function push (to) {
    // 调用原始的 push 方法，并捕获可能抛出的错误
    return VueRouterPush.call(this, to).catch(err => err)
}

// 导出路由实例，以便在主应用中使用
export default router;