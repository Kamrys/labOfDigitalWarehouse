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
          <el-popconfirm
              confirm-button-text='好的'
              cancel-button-text='不用了'
              icon="el-icon-info"
              icon-color="red"
              title="这是批量删除，您确定删除已选的吗？"
              style="margin-left: 10px;"
              @confirm="dels"
              >
              <el-button slot="reference" type="danger"><i class="el-icon-delete-solid"></i>一键删除</el-button>
          </el-popconfirm>

          <div>
              <el-button class="el-button--mini is-plain" type="primary" style="margin-top: 9px;" @click="add">
                  <i class="el-icon-plus"></i>新增
              </el-button>
          </div>
      </div>

      <!-- 表格，列表 -->
      <el-table :data="tableData" :header-cell-style="{ background: '#f2f5fc', color: '#555555' }" border
          @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55"></el-table-column>

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
          <el-table-column prop="phone" label="电话" width="180">
          </el-table-column>
          <el-table-column prop="roleId" label="角色" width="120">
              <template slot-scope="scope">
                  <el-tag :type="scope.row.roleId === 0 ? 'danger' : (scope.row.roleId === 1 ? 'primary' : 'success')"
                      disable-transitions>{{ scope.row.roleId === 0 ? '超级管理员' :
                          (scope.row.roleId === 1 ? '管理员' : '用户') }}</el-tag>
              </template>
          </el-table-column>
          <el-table-column prop="operate" label="操作">
              <template slot-scope="scope">
                  <!-- scope.row：当前行的数据对象，可以用来访问该行的各个字段。传入mod编辑方法中 -->
                   <!-- 编辑思路：先通过插槽scope传入数据mod方法中，
                    获取到我们表单中的数据显示在表格中，
                    然后通过save方法判断是新增还是编辑，
                    如果id冲突那么就是编辑，反之新增，
                    然后通过doMod方法连接后端接口所进行修改数据 -->
                  <el-button type="warning" size="small" @click="mod(scope.row)"><i class="el-icon-edit"></i>编辑</el-button>
                  <!-- confirm:点击确认按钮时触发; cancel：取消时触发 -->
                  <el-popconfirm
                      title="您确定删除吗？"
                      @confirm="del(scope.row.id)"
                      style="margin-left: 5px;"
                      >
                      <el-button slot="reference" type="danger" size="small"><i class="el-icon-delete"></i>删除</el-button>
                  </el-popconfirm>
              </template>
          </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination style="margin-top: 1%;" @size-change="handleSizeChange" @current-change="handleCurrentChange"
          :current-page="pageNum" :page-sizes="[5, 10, 20]" :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>

      <!-- Dialog 对话框：在element官网中的Dialog 对话框-居中布局    -->
      <el-dialog title="提示" :visible.sync="centerDialogVisible" width="30%" center :before-close="handleClose">
          <!-- form表单：新增表单     :rules="rules"：通过 rules 属性传入约定的验证规则 -->
          <el-form ref="form" :model="form" :rules="rules" label-width="80px">
              <el-form-item label="账号:" prop="no">
                  <el-col :span="20">
                      <el-input v-model="form.no"></el-input>
                  </el-col>
              </el-form-item>
              <el-form-item label="姓名:" prop="name">
                  <el-col :span="20">
                      <el-input v-model="form.name"></el-input>
                  </el-col>
              </el-form-item>
              <el-form-item label="密码:" prop="password">
                  <el-col :span="20">
                      <el-input v-model="form.password"></el-input>
                  </el-col>
              </el-form-item>
              <el-form-item label="年龄:" prop="age">
                  <el-col :span="20">
                      <el-input v-model="form.age"></el-input>
                  </el-col>
              </el-form-item>
              <el-form-item label="性别:">
                  <el-radio-group v-model="form.sex">
                      <el-radio label="1">男</el-radio>
                      <el-radio label="0">女</el-radio>
                  </el-radio-group>
              </el-form-item>

            <el-form-item label="身份:">
              <el-radio-group v-model="form.roleId">
                <el-radio label="1">管理员</el-radio>
                <el-radio label="2">用户</el-radio>
              </el-radio-group>
            </el-form-item>

              <el-form-item label="电话:" prop="phone">
                  <el-col :span="20">
                      <el-input v-model="form.phone"></el-input>
                  </el-col>
              </el-form-item>
          </el-form>
          <span slot="footer" class="dialog-footer">
              <el-button @click="centerDialogVisible = false">取 消</el-button>
              <el-button type="primary" @click="save">确 定</el-button>
          </span>
      </el-dialog>
  </div>
</template>

<script>
export default {
  name: "AdminManagePage",
  data() {
      // 年龄验证
      let checkAge = (rule, value, callback) => {
          if (value > 150) {
              callback(new Error('年龄输入过大'));
          } else {
              callback();
          }
      };

      // 账号的唯一性验证
      let checkDuplicate =(rule,value,callback)=>{
          console.log("callback" + callback);
          if(this.form.id){
              return callback();
          }
          this.$axios.get(this.$httpUrl+"/user/findByNo?no="+this.form.no).then(res=>res.data).then(res=>{
              if(res.code!=200){
                  callback()
              }else{
                  callback(new Error('账号已经存在'));
              }
          })
      };
      return {
          tableData: [],      // 全部数据
          pageSize: 10,       // 每页显示多少条，默认十条
          pageNum: 1,         // 默认第一页
          total: 0,           // 显示一共多少条
          multipleSelection: [],   // 多选框，获取全部id，多删除
          name: '',           // 姓名，查询
          phone: '',          // 电话，查询
          sex: '',            // 性别，查询
          // 将监控到的性别放入数组里
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
          centerDialogVisible: false,      // 默认dialog对话框窗口不显示
          // form表单(新增)的数据字段
          form: {
              id: '',          // id
              no: '',          // 账号
              name: '',        // 姓名
              password: '',    // 密码
              age: '',         // 年龄
              phone: '',       // 电话
              sex: '0',        // 性别
              roleId: '1'      // 角色(0超级管理员，1表示管理员，2表示普通用户)
          },
          // 新增表单验证规则
          rules: {
              no: [
                  { required: true, message: '请输入账号', trigger: 'blur' },
                  { min: 3, max: 12, message: '长度在 3 到 12 个字符', trigger: 'blur' },
                  {validator: checkDuplicate, trigger: 'blur'}    // 验证账号的唯一性
              ],
              name: [
                  { required: true, message: '请输入姓名', trigger: 'blur' },
                  { min: 2, max: 5, message: '长度在 2 到 5 个字符', trigger: 'blur' }
              ],
              password: [
                  { required: true, message: '请输入密码', trigger: 'blur' },
                  { min: 3, max: 8, message: '长度在 3 到 8 个字符', trigger: 'blur' }
              ],
              age: [
                  { required: true, message: '请输入年龄', trigger: 'blur' },
                  { min: 1, max: 3, message: '长度在 1 到 3 个位', trigger: 'blur' },
                  { pattern: /^([1-9][0-9]*){1,3}$/, message: '年龄必须为正整数字', trigger: "blur" },
                  { validator: checkAge, trigger: 'blur' }    // 验证年龄的最大限制
              ],
              phone: [
                  { required: true, message: "手机号不能为空", trigger: "blur" },
                  { pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: "请输入正确的手机号码", trigger: "blur" }
              ]
          }
      }
  },
  // 调用方法，事件处理
  methods: {
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
      // 重置表单方法——用于重置新增表单关闭窗口后清除数据     $refs：提供了对已注册过 ref 属性的元素或子组件的直接引用
      resetForm() {
          this.$refs.form.resetFields();
      },
      // 点击新增按钮方法，弹出新增表单窗口
      add() {
          this.centerDialogVisible = true       // 点击新增的时候dialog窗口变为true，显示对话框，新增
          // $resetFields：用于在下一次 DOM 更新循环结束后执行回调函数。
          this.$nextTick(() => {
              this.resetForm();
          })
      },
      // 当不小心点到 x 按钮以及空白处的时候判断是否确认关闭窗口--↑:before-close="handleClose"  
      handleClose(done) {
          this.$confirm('确认关闭？')
              .then(() => {
                  done();
              })
              .catch(() => { });
      },
      // 新增   传入后端数据    save
      doSave(){
          this.$axios.post(this.$httpUrl + '/user/save', this.form)
          .then(res => res.data).then(res => {        // 就是当.then()前的方法执行完后再执行then()内部的程序
              if (res.code == 200) {
                  this.$message({         // 雅观弹窗
                      message: '操作成功',
                      type: 'success'
                  });
                  this.centerDialogVisible = false     // 添加成功后关闭窗口
                  this.loadPost()         // 保存成功后自动刷新列表显示数据
                  this.resetForm()        // 重置表单数据
              } else {
                  this.$message.error('操作失败');
              }
          });
      },
      // 新增以及编辑表单点击确定方法
      save() {
          // 验证表单规则防止往下跑
          this.$refs.form.validate((valid) => {
              if (valid) {
                  // 判断需要新增还是需要修改，如果有id那么就是修改，没有就新增                    
                  if (this.form.id){
                      this.doMod();   // 编辑
                  } else {
                      this.doSave();  // 新增
                  }
              } else {
                  console.log('error submit!!');
                  return false;
              }
          });
      },
      // 删除     传入后端数据    delete
      del(id){
          // console.log(id)
          this.$axios.get(this.$httpUrl + '/user/delete?id=' + id)
          .then(res => res.data).then(res => {        // 就是当.then()前的方法执行完后再执行then()内部的程序
              if (res.code == 200) {
                  this.$message({         // 雅观弹窗
                      message: '操作成功',
                      type: 'success'
                  });
                  this.centerDialogVisible = false     // 删除成功后关闭窗口
                  this.handleSizeChange()             // 删除成功后重新获取每页有多少条数据方法，自动刷新列表显示数据
              } else {
                  this.$message.error('操作失败');
              }
          });
      },
      // 多删除     传入后端数据    deletes
      dels(){
          const ids = this.multipleSelection.map(row => row.id);
          // console.log(ids);
          this.$axios.get(this.$httpUrl + '/user/deletes?ids=' + ids)
          .then(res => res.data).then(res => {        // 就是当.then()前的方法执行完后再执行then()内部的程序
              if (res.code == 200) {
                  this.$message({         // 雅观弹窗
                      message: '操作成功',
                      type: 'success'
                  });
                  this.centerDialogVisible = false     // 删除成功后关闭窗口
                  this.handleSizeChange()
              } else {
                  this.$message.error('操作失败，请先选择需要删除的用户');
              }
          });
      },
      // 编辑     传入后端数据 update
      doMod(){
          this.$axios.post(this.$httpUrl + '/user/update', this.form)
          .then(res => res.data).then(res => {        // 就是当.then()前的方法执行完后再执行then()内部的程序
              if (res.code == 200) {
                  this.$message({         // 雅观弹窗
                      message: '操作成功',
                      type: 'success'
                  });
                  this.centerDialogVisible = false     // 编辑成功后关闭窗口
                  this.loadPost()         // 编辑成功后自动刷新列表显示数据
                  this.resetForm()        // 重置表单数据
              } else {
                  this.$message.error('操作失败');
              }
          });
      },
      // 修改-编辑
      mod(row){
          // 通过插槽传入进来的数据打印
          // console.log(row);
          this.centerDialogVisible = true
          // 赋值到表单中
          this.$nextTick(() => {
              this.form.id = row.id
              this.form.no = row.no
              this.form.name = row.name
              this.form.password = ''
              this.form.age = row.age + ''
              this.form.sex = row.sex + ''
              this.form.phone = row.phone
              this.form.roleId = row.roleId
          })
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
                  roleId: '1'
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
      // 多选框，多删除，获取多选后的id放到multipleSelection字段数组里面
      handleSelectionChange(val) {
          // const ids = val.map(row => row.id);
          // console.log(ids);
          this.multipleSelection = val;
      }
  },

  // 载入前，初始化操作
  beforeMount() {
      // this.loadGet();
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