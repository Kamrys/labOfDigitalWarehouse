<!-- 菜单头 -->
<template>
    <div class="top-header">
        <div style="margin-top: 6px; cursor: pointer;">
            <!-- 收缩图标 -->
            <i :class="icon" @click="collapse" style="font-size: 22px;"></i>
        </div>
        <div class="hello">
            欢迎来到数字识别入库管理系统
        </div>
        <span style="font-size: 13px;">欢迎您：{{user.name}}</span>
        <el-dropdown>
            <i class="el-icon-arrow-down" style="margin-left: 8px;"></i>
            <el-dropdown-menu slot="dropdown">
                <!-- native: 告诉 Vue 你希望监听的是子组件内部的原生 DOM 事件，而不是组件自定义的事件 -->
                <el-dropdown-item @click.native="toUser">个人中心</el-dropdown-item>
                <el-dropdown-item @click.native="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
        </el-dropdown>
    </div>
</template>

<script>
export default {
    name: "HeaderMenu",
    data(){
        return {
            user: JSON.parse(sessionStorage.getItem('CurUser'))
        }
    },
    props: {
        icon: String
    },
    methods: {
        // 个人中心
        toUser() {
            console.log('to_user');
            this.$router.push("/Home")
        },
        // 退出登录
        logout() {
            this.$confirm('您确定要退出登录吗?', '提示', {
                confirmButtonText: '确定',  //确认按钮的文字显示
                type: 'warning',    // 样式
                center: true, //文字居中显示
            }).then(() => {
                this.$message({
                    type:'success',
                    message:'退出登录成功'
                })
                this.$router.push("/")
                sessionStorage.clear()
            }).catch(() => {
                this.$message({
                    type:'info',
                    message:'已取消退出登录'
                })
            })
        },
        // 收缩图标
        collapse() {
            this.$emit('doCollapse')
        }
    },
    // 登录成功后自动跳转到个人中心
    created(){
        // 避免路由冲突解决方法之一
        if (this.$route.path !== '/Home') {
            this.$router.push('/Home');
        }
    }
}
</script>

<style scoped>
.top-header {
    display: flex;
    line-height: 60px;
}

.hello {
    flex: 1;
    text-align: center;
    font-size: 25px;
}
</style>