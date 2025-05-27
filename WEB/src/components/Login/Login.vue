<style scoped>
	.center {
		width: 100%;
		height: 100%;
		background-image: url('../../assets/img/Login.jpg');
		background-size: 100% 100%;
		background-repeat: no-repeat;
		display: flex;
		align-items: center;
		justify-content: center;
		flex-direction: column;
	}
 
	h1 {
		font-size: 30px;
		color: black;
	}
 
	.logon {
		background-color: #fff;
		border-radius: 10px;
		box-shadow: 0 14px 28px rgba(0, 0, 0, 0.25), 0 10px 10px rgba(0, 0, 0, 0.22);
		/* position: relative;
		overflow: hidden; */
		width: 768px;
		max-width: 100%;
		min-height: 480px;
		margin-top: 20px;
		display: flex;
		background: -webkit-linear-gradient(right, #4284db, #29eac4);
		opacity: 0.8;
	}
 
	.overlaylong {
		border-radius: 10px 0 0 10px;
		width: 50%;
		height: 100%;
		background-color: #fff;
		display: flex;
		align-items: center;
		justify-content: center;
	}
 
	.overlaylongleft {
		border-radius: 0px 10px 10px 0px;
		width: 50%;
		height: 100%;
		background-color: #fff;
		transform: translateX(100%);
		transition: transform 0.6s ease-in-out;
		display: flex;
		align-items: center;
		justify-content: center;
	}
 
	.overlaylongright {
		border-radius: 10px 0 0 10px;
		width: 50%;
		height: 100%;
		background-color: #fff;
		transform: translateX(0%);
		transition: transform 0.6s ease-in-out;
		display: flex;
		align-items: center;
		justify-content: center;
	}
 
	.overlaytitle {
		border-radius: 0px 10px 10px 0px;
		width: 50%;
		height: 100%;
		background-color: rgba(0, 0, 0, 0);
		display: flex;
		align-items: center;
		justify-content: center;
	}
 
 
	.overlaytitleH2 {
		font-size: 30px;
		color: #fff;
		margin-top: 20px;
	}
 
	.overlaytitleP {
		font-size: 15px;
		color: #fff;
		margin-top: 20px;
	}
 
	.overlaytitleleft {
		border-radius: 0px 10px 10px 0px;
		width: 50%;
		height: 100%;
		background-color: rgba(0, 0, 0, 0);
		display: flex;
		align-items: center;
		justify-content: center;
		transform: translateX(0%);
		transition: transform 0.6s ease-in-out;
	}
 
	.overlaytitleright {
		border-radius: 0px 10px 10px 0px;
		width: 50%;
		height: 100%;
		background-color: rgba(0, 0, 0, 0);
		display: flex;
		align-items: center;
		justify-content: center;
		transform: translateX(-100%);
		transition: transform 0.6s ease-in-out;
	}
 
	.overlaytitle-Signin {
		display: flex;
		align-items: center;
		justify-content: center;
		flex-direction: column;
	}
 
	.overlaytitle-Signup {
		display: flex;
		align-items: center;
		justify-content: center;
		flex-direction: column;
	}
 
	.buttongohs {
		width: 180px;
		height: 40px;
		border-radius: 50px;
		border: 1px solid #fff;
		color: #fff;
		font-size: 15px;
		text-align: center;
		line-height: 40px;
		margin-top: 40px;
	}
 
	.overlaylongH2 {
		font-size: 25px;
		color: black;
		/* width: 250px; */
	}
 
	.overlaylong-Signin {
		display: flex;
		align-items: center;
		justify-content: center;
		flex-direction: column;
	}
 
	.overlaylong-Signup {
		display: flex;
		align-items: center;
		justify-content: center;
		flex-direction: column;
	}
 
	input {
		background-color: #eee;
		border: none;
		padding: 12px 15px;
		margin: 10px 0;
		width: 240px;
	}
	h3{
		font-size: 10px;
		margin-top: 10px;
		cursor: pointer;
	}
	.inupbutton{
		background-color: #29eac4;
		border: none;
		width: 180px;
		height: 40px;
		border-radius: 50px;
		font-size: 15px;
		color: #fff;	
		text-align: center;
		line-height: 40px;
		margin-top: 30px;
	}
	/* 加载图片样式 */
	.el-icon-loading {
		font-size: 100px;
		animation: spin 1s linear infinite;
	}
	/* 转速 */
	@keyframes spin {
		0% { transform: rotate(0deg); }
		100% { transform: rotate(360deg); }
	}

	.el-input {
		width: 207.67px; /* 统一宽度 */
	}
</style>
 
<!-- TODO 登录界面 -->
<template>
	<div class="center" style="width: 1707px; height: 930px;">
		<!-- 标题 -->
		<h1>登录/注册</h1>
		<!-- 
		:model="loginForm":将表单数据绑定到loginForm对象
		:rules="rules":绑定表单验证规则 
		ref="loginForm":用于获取表单实例的引用
		-->
		<el-form 
			class="logon" 
			:model="loginForm"
			:rules="rules"
			ref="loginForm">
			<!-- 动态切换登录注册界面 -->
			<div :class="overlaylong">
				<!-- 登录界面 -->
				<div class="overlaylong-Signin" v-if="disfiex == 0">
					<h2 class="overlaylongH2">登录</h2>
					<!-- 账号输入框：-->
					<el-form-item prop="no">
						<el-input v-model="loginForm.no" autocomplete="off" type="text" placeholder="账号"></el-input>
                    </el-form-item>
					<!-- 
						密码输入框：
						v-model：双向绑定密码输入
						show-password：显示/隐藏密码按钮
						autocomplete="off"：关闭自动完成
						@keyup.enter.native="confirm"：回车事件，输入完密码可以直接回车进行登录
					-->
					<el-form-item prop="password">
						<el-input
						v-model="loginForm.password"
						show-password
						autocomplete="off"
						type="password"
						placeholder="密码"
						@keyup.enter.native="confirm"
						></el-input>
                    </el-form-item>
					
					<h3 @click="forgotPassword">忘记密码？</h3>
					<!-- 登录按钮 -->
					<button class="inupbutton" @click="confirm" :disabled="confirm_disabled" type="button">登录</button>
				</div>

				<!-- 注册界面 -->
				<div class="overlaylong-Signup" v-if="disfiex == 1">
					<h2 class="overlaylongH2">注册账户</h2>
					<el-form-item prop="name">
						<el-input v-model="loginForm.name" autocomplete="off" type="text" placeholder="名称"></el-input>
                    </el-form-item>
					<el-form-item prop="no">
						<el-input v-model="loginForm.no" autocomplete="off" type="text" placeholder="账号"></el-input>
                    </el-form-item>
					<el-form-item prop="password">
						<el-input
						v-model="loginForm.password"
						show-password
						autocomplete="off"
						type="password"
						placeholder="密码"
						@keyup.enter.native="register"
						></el-input>
                    </el-form-item>
					<!-- 注册按钮 -->
					<button class="inupbutton" @click="register" :disabled="register_disabled" type="button">注册</button>
				</div>
			</div>

			<!-- 动态切换登录注册描述 -->
			<div :class="overlaytitle">
				<!-- 登录描述 -->
				<div class="overlaytitle-Signin" v-if="disfiex == 0">
					<h2 class="overlaytitleH2">嘿，朋友！</h2>
					<p class="overlaytitleP">
						输入您的个人详细信息，与我们一起开始旅程
					</p>
					<div class="buttongohs" @click="Signin">注册</div>  <!-- 切换到注册界面 -->
				</div>
				<!-- 注册描述 -->
				<div class="overlaytitle-Signup" v-if="disfiex == 1">
					<h2 class="overlaytitleH2">欢迎回来！</h2>
					<p class="overlaytitleP">要与我们保持联系，请使用您的个人信息登录</p>
					<div class="buttongohs" @click="Signup">登录</div>  <!-- 切换到登录界面 -->
				</div>
			</div>
		</el-form>
		<div v-if="loading" :fullscreen="true" :lock="true" :text="'加载中...'">
			<i class="el-icon-loading"></i>
		</div>
	</div>
</template>
 
<script>
export default {
	// 组件名称
	name: "LoginPage",
	// 数据属性
	data() {
		return {
			overlaylong: 'overlaylong',  	// 控制登录注册 界面 动画的类名
			overlaytitle: 'overlaytitle',  	// 控制登录注册 描述 动画的类名
			disfiex: 0,  					// 控制显示登录(0) 还是注册(1)界面
			loading: false,  				// 控制加载图标的显示
			confirm_disabled: false,  		// 是否禁用登录 按钮
			register_disabled: false,  		// 是否禁用注册 按钮
			loginForm: {  					// 表单的数据模型
				no: '',  		// 账号
				name: '',		// 姓名
				password: ''  	// 密码
			},
			// 表单验证规则
			rules: {
				no: [  	// 账号验证规则
					{ required: true, message: '请输入账号', trigger: 'blur' }  // 必填项，当失去焦点时触发
				],
				password: [  // 密码验证规则
					{ required: true, message: '请输密码', trigger: 'blur' }  	// 必填项，当失去焦点时触发
				],
				name: [  // 名称验证规则
					{ required: true, message: '请输名称', trigger: 'blur' }  	// 必填项，当失去焦点时触发
				],
			}
		}
	},
	// 方法属性
	methods: {
		// 切换到注册界面
		Signin() {
			this.overlaylong = "overlaylongleft"  // 更改登录界面动画类名
			this.overlaytitle = "overlaytitleright"  // 更改登录描述动画类名
			this.loginForm = {}			// 每次切换重新清空双向绑定的model
			setTimeout(() => {  // 延迟执行
				this.disfiex = 1  // 改变disfiex值以显示注册界面
			}, 200)  // 200毫秒后执行
		},
		// 切换到登录界面
		Signup() {
			this.overlaylong = "overlaylongright"  // 更改登录界面动画类名
			this.overlaytitle = "overlaytitleleft"  // 更改登录描述动画类名
			this.loginForm = {}			 // 每次切换重新清空双向绑定的model
			setTimeout(() => {  // 延迟执行
				this.disfiex = 0  // 改变disfiex值以显示登录界面
			}, 200)  // 200毫秒后执行
		},
		// 忘记密码按钮
		forgotPassword() {
			alert('忘记密码功能暂未开放!');
		},
		// 登录方法
		confirm() {
			this.confirm_disabled = true;  // 禁用登录按钮防止重复点击
			this.loading = true;  // 显示加载图标
			this.$refs.loginForm.validate((valid) => {  // 触发表单验证
				if (valid) {  // 如果验证通过
					// 发送登录请求
					this.$axios.post(this.$httpUrl + '/user/login', this.loginForm).then(res => res.data).then(res => {
						// console.log(res);
						if (res.code == 200) {  // 如果服务器返回状态码为200，表示登录成功
							// 存储用户信息到sessionStorage
							sessionStorage.setItem("CurUser", JSON.stringify(res.data.user))
							this.loading = false;  // 隐藏加载图标

							// console.log("login_data====", res.data)
							// console.log("user====", res.data.user)
							// console.log("Login-menu====", res.data.menuList)  // 打印菜单信息
							
							// 更新Vuex store中的菜单信息
							this.$store.commit("setMenu", res.data.menuList)

							// 跳转到主页
							this.$router.replace('/Index');
						} else {  // 如果登录失败
							this.confirm_disabled = false;  // 重新启用登录按钮
							this.loading = false;  			// 隐藏加载图标
							alert('校验失败，用户名或密码错误！');  // 弹出警告信息
							return false;
						}
						// 为了不在页面上显示错误信息，连接错误
					}).catch(error => {
						console.error('请求失败:', error);
						this.$message.error('网络错误，请重试:' + error.code);
						this.confirm_disabled = false;
						this.loading = false;  // 隐藏加载图标
					});
				} else {  // 如果验证未通过
					this.confirm_disabled = false;  // 重新启用登录按钮
					this.loading = false;  			// 隐藏加载图标
					console.log('校验失败');  		// 控制台打印验证失败信息
					return false;
				}
			});
		},
		// 注册方法
		register(){
			this.register_disabled = true
			this.$refs.loginForm.validate((valid) => {
				if(valid){
					this.$axios.post(this.$httpUrl + "/user/register", this.loginForm).then(res => res.data).then(res => {
						console.log(res);
						if (res.code == 200){
							this.$message({
								message: '注册成功，请登录',
								type: 'success'
							})
							this.Signup();
						} else {
							this.register_disabled = false
							alert('用户已存在')
							return false
						}
					}).catch(error => {
						console.error('请求失败:', error);
						this.$message.error('网络错误，请重试:' + error.code);
						this.confirm_disabled = false;
					})
				} else {
					this.register_disabled = false;
					console.log('校验失败');
					return false;
				}
			})
		}
	}
}
</script>