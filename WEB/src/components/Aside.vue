<!-- 导航栏 -->
<template>
    <!-- collapse:是否水平折叠收起菜单 -->
    <el-menu 
            background-color="#304156" 
            text-color="#bfcbd9"
            active-text-color="#409EFF"
            style="height: 100%;"
            default-active="/Home"
            :collapse="isCollapse"
            :collapse-transition="false" 
            router
            >
        <el-menu-item index="/Home">
            <i class="el-icon-s-home"></i>
            <span slot="title">首页</span>
        </el-menu-item>

        <el-menu-item :index="'/' + item.menuclick"  v-for="(item,i) in menu" :key="i">
            <i :class="item.menuicon"></i>
            <span slot="title">{{ item.menuname }}</span>
        </el-menu-item>
    </el-menu>
</template>

<script>

export default {
    name: "AsideMenu",
    data(){
        return {
            // 设置左侧菜单栏数据,(写死)
            /* menu: [
                {
                    menuClick: 'Admin',
                    menuName: '超级管理员',
                    menuIcon: 'el-icon-s-custom'
                },
                {
                    menuClick: 'User',
                    menuName: '用户管理',
                    menuIcon: 'el-icon-user-solid'
                }
            ] */
        }
    },
    // 计算属性,动态获取数据库相对于的菜单，也就是管理员有多少权限……用户有多少权限……（动态路由、权限路由）
    computed:{
        "menu":{
            get(){                
                return this.$store.state.menu
            }
        }
    },
    // props：用于从父组件向子组件传递数据的属性
    props: {
        isCollapse: Boolean
    }
}
</script>

<style></style>