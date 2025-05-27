<!-- 主菜单 -->
<template>
  <div>
      <!-- 输入框及按钮 -->
      <div style="margin: 6px 0px 12px 0px;">
          <!-- 
              @keyup.enter.native="loadPost"：添加回车事件，按回车执行 
              v-model="name" ：获取input框中输入的姓名，放到下面的data中，然后loadPost传入后端进行查询
          -->
          <el-input class="input_name" v-model="name" placeholder="请输入名称" @keyup.enter.native="loadPost"
              suffix-icon="el-icon-search"></el-input>
          <el-select v-model="sex" filterable placeholder="请选择" class="select_sex">
              <el-option v-for="item in sexs" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
          </el-select>
          <el-input class="input_phone" v-model="phone" placeholder="请输入电话" @keyup.enter.native="loadPost"
              suffix-icon="el-icon-search"></el-input>
          <!-- 按钮部分 -->
          <el-button type="success" @click="loadPost">
              <i class="el-icon-search"></i>查询
          </el-button>
          <el-button @click="resetParam">
              <i class="el-icon-refresh"></i>重置
          </el-button>
      </div>

      <!-- 表格，列表 -->
      <el-table :data="tableData" :header-cell-style="{ background: '#f2f5fc', color: '#555555' }" border
          highlight-current-row
          @current-change="handleRadioCurrentChange"
          >
          <el-table-column prop="id" label="ID" width="60">
          </el-table-column>
          <el-table-column prop="no" label="账号" width="180">
          </el-table-column>
          <el-table-column prop="name" label="姓名" width="180">
          </el-table-column>
          <el-table-column prop="age" label="年龄" width="80">
          </el-table-column>
          <el-table-column prop="sex" label="性别" width="80">
              <template slot-scope="scope">
                  <el-tag :type="scope.row.sex === 1 ? 'primary' : 'success'" disable-transitions>{{ scope.row.sex ===
                      1 ? '男' : '女' }}</el-tag>
              </template>
          </el-table-column>
          <el-table-column prop="phone" label="电话">
          </el-table-column>
          <el-table-column prop="roleId" label="角色" width="120">
              <template slot-scope="scope">
                  <el-tag :type="scope.row.roleId === 0 ? 'danger' : (scope.row.roleId === 1 ? 'primary' : 'success')"
                      disable-transitions>{{ scope.row.roleId === 0 ? '超级管理员' :
                          (scope.row.roleId === 1 ? '管理员' : '用户') }}</el-tag>
              </template>
          </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination style="margin-top: 1%;" @size-change="handleSizeChange" @current-change="handleCurrentChange"
          :current-page="pageNum" :page-sizes="[5, 10, 20]" :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>

  </div>
</template>

<script>
export default {
  name: "SelectUserPage",
  data() {
      return {
          tableData: [],      // 全部数据
          pageSize: 10,       // 每页显示多少条，默认十条
          pageNum: 1,         // 默认第一页
          total: 0,           // 显示一共多少条
          name: '',           // 姓名，查询
          phone: '',          // 电话，查询
          sex: '',            // 性别，查询
          // 奖监控到的性别放入数组里
          sexs: [
              {
                  value: '',
                  label: '请选择'
              },
              {
                  value: '1',
                  label: '男'
              },
              {
                  value: '0',
                  label: '女'
              }
          ],
      }
  },
  methods: {
    // 单选列表，获取数据，然后放入data的currentRow中
    handleRadioCurrentChange(val) {
        this.$emit("doSelectUser", val)     // 直接提交到Goods入库申请人的页面
    },
      // 获取每页有多少条数据
      handleSizeChange(val) {
          // console.log(`每页 ${val} 条`);
          this.pageNum = 1          // 设置每次分页都从第一页开始
          this.pageSize = val       // 点击分页条数每页多少条
          this.loadPost()         // 重新调用
      },
      // 获取当前页为第几页
      handleCurrentChange(val) {
          // console.log(`当前页: ${val}`);
          this.pageNum = val        // 设置当前页第几页
          this.loadPost()
      },
      // 重置按钮事件
      resetParam() {
          this.name = ''
          this.sex = ''
          this.phone = ''
      },
      // list，列表接口
      loadGet() {
          this.$axios.get(this.$httpUrl + '/user/list').then(res => res.data).then(res => {
              console.log(res);
          })
      },
      // 分页以及查询方法和接口传参   listPageC1
      loadPost() {
          this.$axios.post(this.$httpUrl + '/user/listPageC1', {
              pageSize: this.pageSize,    // 传入分页的大小过去后端
              pageNum: this.pageNum,      // 传页数
              param: {
                  name: this.name,        // 传入姓名用于查询
                  phone: this.phone,      // 传入手机号用于查询
                  sex: this.sex,          // 传入性别用于查询
                  roleId: '2'
              }
          }).then(res => res.data).then(res => {      // 就是当.then()前的方法执行完后再执行then()内部的程序
              // console.log(res);
              if (res.code == 200) {
                  this.tableData = res.data
                  this.total = res.total
              } else {
                  alert('获取数据失败');
              }
          }).catch(error => {
              console.error('请求失败:', error);     // 在控制台打印错误信息
              alert('网络请求失败，请检查网络连接');   // 提示用户网络请求失败
          });
      },
  },

  // 载入前，初始化操作
  beforeMount() {
      this.loadPost();
  }
}
</script>

<style scoped>
i {
  margin-right: 3px;
}

.input_name {
  width: 200px;
}

.input_phone {
  width: 200px;
  margin-left: 1%;
  margin-right: 2%;
}

.select_sex {
  width: 93px;
  margin-left: 1%;
}

@media (max-width: 768px) {
  .input_name,
  .input_phone,
  .select_sex {
      min-width: 100%;
      /* 在小屏幕上，输入框和选择框占满一行 */
      margin-bottom: 10px;
      /* 添加底部间距 */
  }
}

/* 解决aria-hidden="true"报错 */
/* 确保焦点元素可见 */
[aria-hidden] :focus {
  visibility: visible !important;
  display: block !important;
  position: relative !important;
  clip: auto !important;
  height: auto !important;
  width: auto !important;
  overflow: visible !important;
  white-space: normal !important;
  padding: 0 !important;
  margin: 0 !important;
  border: none !important;
  box-shadow: none !important;
  outline: none !important;
  background: transparent !important;
  color: inherit !important;
  font: inherit !important;
}

/* 确保焦点元素有明显的焦点样式 */
input:focus {
  outline: 2px solid blue; /* 你可以根据需要调整焦点样式 */
}
</style>