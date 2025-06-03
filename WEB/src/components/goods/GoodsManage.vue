<!-- 主菜单 -->
<template>
    <div>
        <!-- 输入框及按钮 -->
        <div style="margin: 6px 0px 12px 0px;">
            <!-- 
              @keyup.enter.native="loadPost"：添加回车事件，按回车执行 
              v-model="name" ：获取input框中输入的姓名，放到下面的data中，然后loadPost传入后端进行查询
          -->
            <el-input class="input_name" v-model="name" placeholder="请输入物品名称" @keyup.enter.native="loadPost"
                suffix-icon="el-icon-search">
            </el-input>

            <!-- 仓库名称下拉框（查询） -->
            <el-select v-model="storage" placeholder="请选择仓库" class="el-select">
                <el-option v-for="item in storageData" :key="item.id" :label="item.name" :value="item.id">
                </el-option>
            </el-select>

            <!-- 分类下拉框（查询） -->
            <el-select v-model="goodstype" placeholder="请选择分类" class="el-select">
                <el-option v-for="item in goodstypeData" :key="item.id" :label="item.name" :value="item.id">
                </el-option>
            </el-select>

            <!-- 按钮部分 -->
            <el-button type="success" @click="loadPost" style="margin-left: 10px;">
                <i class="el-icon-search"></i>查询
            </el-button>
            <el-button @click="resetParam">
                <i class="el-icon-refresh"></i>重置
            </el-button>
            <el-popconfirm confirm-button-text='好的' cancel-button-text='不用了' icon="el-icon-info" icon-color="red"
                title="这是批量删除，您确定删除已选的吗？" style="margin-left: 10px;" @confirm="dels">
                <el-button v-if="user.roleId != 2" slot="reference" type="danger"><i class="el-icon-delete-solid"></i>一键删除</el-button>
            </el-popconfirm>

            <div>
                <el-button v-if="user.roleId != 2" class="el-button--mini is-plain" type="primary" style="margin-top: 9px;" @click="add">
                    <i class="el-icon-plus"></i>新增
                </el-button>
                <el-button v-if="user.roleId != 2" class="el-button--mini is-plain" type="primary" style="margin-top: 9px;" @click="inGoods">
                    <i class="el-icon-document-copy"></i>入库
                </el-button>
                <el-button v-if="user.roleId != 2" class="el-button--mini is-plain" type="primary" style="margin-top: 9px;" @click="outGoods">
                    <i class="el-icon-takeaway-box"></i>出库
                </el-button>
            </div>

        </div>

        <!-- 表格，列表 -->
        <el-table :data="tableData" :header-cell-style="{ background: '#f2f5fc', color: '#555555' }" border
            highlight-current-row @current-change="handleRadioCurrentChange" @selection-change="handleSelectionChange">
            <el-table-column v-if="user.roleId != 2" type="selection" width="55"></el-table-column>

            <el-table-column prop="id" label="ID" width="60">
            </el-table-column>
            <el-table-column prop="name" label="物品名称" width="120">
            </el-table-column>
            <!-- :formatter="formatStorage"：用于对表格列中的数据进行自定义格式化（可用于连表） -->
            <el-table-column prop="storage" label="仓库名称" width="120" :formatter="formatStorage">
            </el-table-column>
            <el-table-column prop="goodstype" label="分类名称" width="120" :formatter="formatGoodsType">
            </el-table-column>
            <el-table-column prop="count" label="数量" width="80">
            </el-table-column>
            <el-table-column prop="remark" label="备注">
            </el-table-column>

            <el-table-column prop="operate" label="操作" v-if="user.roleId != 2">
                <template slot-scope="scope">
                    <!-- 看AdminManage那边的备注意思 -->
                    <el-button type="warning" size="small" @click="mod(scope.row)"><i
                            class="el-icon-edit"></i>编辑</el-button>
                    <!-- confirm:点击确认按钮时触发; cancel：取消时触发 -->
                    <el-popconfirm title="您确定删除吗？" @confirm="del(scope.row.id)" style="margin-left: 5px;">
                        <el-button slot="reference" type="danger" size="small"><i
                                class="el-icon-delete"></i>删除</el-button>
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
        <el-dialog title="新增物品" :visible.sync="centerDialogVisible" width="30%" center :before-close="handleClose">
            <!-- form表单：新增表单     :rules="rules"：通过 rules 属性传入约定的验证规则 -->
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="物品名称" prop="name">
                    <el-col :span="20">
                        <el-input v-model="form.name"></el-input>
                    </el-col>
                </el-form-item>
                <el-form-item label="仓库名称" prop="storage">
                    <el-col :span="20">
                        <el-select v-model="form.storage" placeholder="请选择仓库">
                            <el-option v-for="item in storageData" :key="item.id" :label="item.name" :value="item.id">
                            </el-option>
                        </el-select>
                    </el-col>
                </el-form-item>
                <el-form-item label="分类名称" prop="goodstype">
                    <el-col :span="20">
                        <el-select v-model="form.goodstype" placeholder="请选择分类">
                            <el-option v-for="item in goodstypeData" :key="item.id" :label="item.name" :value="item.id">
                            </el-option>
                        </el-select>
                    </el-col>
                </el-form-item>
                <el-form-item label="数量" prop="count">
                    <el-col :span="20">
                        <el-input v-model="form.count"></el-input>
                    </el-col>
                </el-form-item>
                <el-form-item label="备注" prop="remark">
                    <el-col :span="20">
                        <el-input type="textarea" placeholder="不能超过200字符" v-model="form.remark"></el-input>
                    </el-col>
                </el-form-item>
                <el-form-item label="物品图片">
                    <el-upload
                        class="upload-demo"
                        action=""
                        :http-request="handleOcrUpload"
                        :show-file-list="false"
                        accept="image/*"
                    >
                        <el-button size="small" type="primary">点击上传图片识别名称</el-button>
                    </el-upload>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="centerDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="save">确 定</el-button>
            </span>
        </el-dialog>


        <!-- 点击入库按钮弹出的dialog -->
        <el-dialog title="出入库" :visible.sync="inDialogVisible" width="30%" center :before-close="handleClose">
            <!-- 嵌套表单（dialog） -->
            <el-dialog width="58%" title="用户选择" :visible.sync="innerVisible" append-to-body>
                <!-- 使用用户界面放在dialog中,@doSelectUser是user那边提交过来的单选列表数据用户 -->
                <SelectUser @doSelectUser="doSelectUser"></SelectUser>

                <span slot="footer" class="dialog-footer">
                    <el-button @click="innerVisible = false">取 消</el-button>
                    <el-button type="primary" @click="confirmUser">确 定</el-button>
                </span>
            </el-dialog>
            <!-- form表单：新增表单     :rules="rules"：通过 rules 属性传入约定的验证规则 -->
            <el-form ref="formIn" :model="formIn" :rules="rulesIn" label-width="80px">
                <el-form-item label="物品名称">
                    <el-col :span="20">
                        <el-input v-model="formIn.goodsname" readonly></el-input>
                    </el-col>
                </el-form-item>
                <el-form-item label="申请人">
                    <el-col :span="20">
                        <el-input v-model="formIn.username" readonly @click.native="selectUser"></el-input>
                    </el-col>
                </el-form-item>
                <el-form-item label="数量" prop="count">
                    <el-col :span="20">
                        <el-input v-model="formIn.count" :placeholder="currentRow && currentRow.count ? currentRow.count : ''"></el-input>
                    </el-col>
                </el-form-item>
                <el-form-item label="备注" prop="remark">
                    <el-col :span="20">
                        <el-input type="textarea" placeholder="不能超过200字符" v-model="formIn.remark"></el-input>
                    </el-col>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="inDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="doInGoods">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
import SelectUser from '../user/SelectUser.vue';

export default {
    // 注册使用组件
    components: {
        SelectUser
    },
    name: "GoodsManagePage",
    data() {
        // 新增的数量规则验证
        let checkCount = (rule, value, callback) => {
            if (value > 9999) {
                callback(new Error('数量输入过大'));
            } else {
                callback();
            }
        };

        // 验证
        let InCount = (rule, value, callback) => {
            if (value > this.currentRow.count) {
                callback(new Error('数量输入过大'));
            } else {
                callback();
            }
        };
        return {
            user: JSON.parse(sessionStorage.getItem('CurUser')),    // 获取sessionStorage中的用户数据
            storageData: [],    // 存放仓库storage表全部的数据
            goodstypeData: [],  // 存放分类goodstype表全部的数据
            tableData: [],      // 全部数据
            pageSize: 10,       // 每页显示多少条，默认十条
            pageNum: 1,         // 默认第一页
            total: 0,           // 显示一共多少条
            multipleSelection: [],   // 多选框，获取全部id，多删除
            currentRow: {},          // 单选列表，入库
            name: '',           // 姓名，查询
            storage: '',        // 仓库，查询
            goodstype: '',      // 分类，查询
            innerVisible: false,             // 嵌套表单(dialog)的显示与否
            centerDialogVisible: false,      // 新增表单-默认dialog对话框窗口不显示
            inDialogVisible: false,          // 入库表单-
            tempUser: '',       // 存放从SelectUser页面传过来的数据
            // form表单(新增)的数据字段 
            form: {
                id: '',          // id
                name: '',        // 物品名
                storage: '',      // 仓库名
                goodstype: '',    // 分类名
                count: '',        // 数量
                remark: '',      // 备注
            },
            // 入库表单的数据字段
            formIn: {
                goods: '',          // 货品id
                goodsname: '',      // 物品名
                count: '',          // 数量
                userid: '',         // 取货人/补货人id
                username: '',       // 取货人/补货人name
                adminId: '',        // 操作人id
                remark: '',         // 备注
                action: '1'         // 1是入库，2是出库，默认1
            },
            // 新增表单验证规则
            rules: {
                name: [
                    { required: true, message: '请输入物品名称', trigger: 'blur' },
                    { min: 2, max: 7, message: '长度在 2 到 7 个字符', trigger: 'blur' }
                ],
                storage: [
                    { required: true, message: '请选择仓库', trigger: 'blur' }
                ],
                goodstype: [
                    { required: true, message: '请选择分类', trigger: 'blur' }
                ],
                count: [
                    { required: true, message: '请输入数量', trigger: 'blur' },
                    { pattern: /^([1-9][0-9]*){1,4}$/, message: '数量必须为正整数字', trigger: "blur" },
                    { validator: checkCount, trigger: 'blur' }
                ],
                remark: [
                    { required: false, message: '请输入备注', trigger: 'blur' },
                    { max: 200, message: '不能超过200字符', trigger: 'blur' }
                ],
            },
            // 入库表单验证规则
            rulesIn: {
                remark: [
                    { required: false, message: '请输入备注', trigger: 'blur' },
                    { max: 200, message: '不能超过200字符', trigger: 'blur' }
                ],
                count: [
                    { required: true, message: '请输入数量', trigger: 'blur' },
                    { pattern: /^([1-9][0-9]*){1,4}$/, message: '数量必须为正整数字', trigger: "blur" },
                    { validator: InCount, trigger: 'blur' }
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
            this.storage = ''
            this.goodstype = ''
        },
        // 重置表单方法——用于重置新增表单关闭窗口后清除数据     $refs：提供了对已注册过 ref 属性的元素或子组件的直接引用
        resetForm() {
            this.$refs.form.resetFields();
        },
        // 重置入库表单方法
        resetInForm() {
            this.$refs.formIn.resetFields();
        },
        // 点击新增按钮方法，弹出新增表单窗口
        add() {
            this.centerDialogVisible = true       // 点击新增的时候dialog窗口变为true，显示对话框，新增
            // $resetFields：用于在下一次 DOM 更新循环结束后执行回调函数。
            this.$nextTick(() => {
                this.resetForm();
                this.form.id = ''
            })
        },
        // 点击入库按钮方法，弹出新增表单窗口
        inGoods() {
            if (this.formIn.username) {
                this.formIn.username = ''
            }

            if (!this.currentRow.id) {
                // alert("请点击需要入库的列表数据！！！")
                this.$message.error('请点击需要入库的列表数据！！！');
                return;
            }
            this.inDialogVisible = true       // 点击新增的时候dialog窗口变为true，显示对话框，新增
            // $resetFields：用于在下一次 DOM 更新循环结束后执行回调函数。
            this.$nextTick(() => {
                this.resetInForm();
            })
            this.formIn.goodsname = this.currentRow.name
            this.formIn.goods = this.currentRow.id
            this.formIn.adminId = this.user.id
            this.formIn.action = '1'      // 1是入库，2是出库
        },
        // 出库
        outGoods() {
            if (this.formIn.username) {
                this.formIn.username = ''
            }

            if (!this.currentRow.id) {
                this.$message.error('请点击需要出库的列表数据！！！');
                return;
            }

            this.inDialogVisible = true       // 点击新增的时候dialog窗口变为true，显示对话框，新增
            // $resetFields：用于在下一次 DOM 更新循环结束后执行回调函数。
            this.$nextTick(() => {
                this.resetInForm();
            })

            this.formIn.goodsname = this.currentRow.name
            this.formIn.goods = this.currentRow.id
            this.formIn.adminId = this.user.id
            this.formIn.action = '2'
        },
        // 入库  传入后端数据
        doInGoods() {
            this.$axios.post(this.$httpUrl + '/record/save', this.formIn)
                .then(res => res.data).then(res => {        // 就是当.then()前的方法执行完后再执行then()内部的程序
                    if (res.code == 200) {
                        this.$message({         // 雅观弹窗
                            message: '操作成功',
                            type: 'success'
                        });
                        this.inDialogVisible = false     // 添加成功后关闭窗口
                        this.loadPost()           // 保存成功后自动刷新列表显示数据
                        this.resetInForm()        // 重置表单数据
                    } else {
                        this.$message.error('操作失败:' + res.data);
                    }
                });
        },
        // 入库表单中的  申请人点击事件，点击后弹出嵌套dialog（新的表单-申请人选择）
        selectUser() {
            // 点击后显示出内层的dialog表单
            this.innerVisible = true
        },
        // 内嵌表单的申请人确定点击事件
        confirmUser() {
            this.formIn.username = this.tempUser.name
            this.formIn.userid = this.tempUser.id
            if (!this.tempUser.name) {
                this.innerVisible = true
                this.$message.error('您未选择，请选择用户!!!');
            } else {
                // 点击完成后关闭页面
                this.innerVisible = false
            }
        },
        // 从SelectUser用户页面中提交过来的用户数据
        doSelectUser(val) {
            // console.log(val);
            this.tempUser = val
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
        doSave() {
            this.$axios.post(this.$httpUrl + '/goods/save', this.form)
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
                    if (this.form.id) {
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
        del(id) {
            // console.log(id)
            this.$axios.get(this.$httpUrl + '/goods/delete?id=' + id)
                .then(res => res.data).then(res => {        // 就是当.then()前的方法执行完后再执行then()内部的程序
                    if (res.code == 200) {
                        this.$message({         // 雅观弹窗
                            message: '操作成功',
                            type: 'success'
                        });
                        this.handleSizeChange()             // 删除成功后重新获取每页有多少条数据方法，自动刷新列表显示数据
                    } else {
                        this.$message.error('操作失败');
                    }
                });
        },
        // 多删除     传入后端数据    deletes
        dels() {
            const ids = this.multipleSelection.map(row => row.id);
            this.$axios.get(this.$httpUrl + '/goods/deletes?ids=' + ids)
                .then(res => res.data).then(res => {        // 就是当.then()前的方法执行完后再执行then()内部的程序
                    if (res.code == 200) {
                        this.$message({         // 雅观弹窗
                            message: '操作成功',
                            type: 'success'
                        });
                        this.handleSizeChange()
                    } else {
                        this.$message.error('操作失败，请先选择需要删除的用户');
                    }
                });
        },
        // 编辑     传入后端数据 update
        doMod() {
            this.$axios.post(this.$httpUrl + '/goods/update', this.form)
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
        mod(row) {
            // 通过插槽传入进来的数据打印
            // console.log(row);
            this.centerDialogVisible = true
            // 赋值到表单中
            this.$nextTick(() => {
                this.form.id = row.id
                this.form.name = row.name
                this.form.storage = row.storage
                this.form.goodstype = row.goodstype
                this.form.count = row.count
                this.form.remark = row.remark
            })
        },
        // 分页以及查询方法和接口传参   listPage
        loadPost() {
            this.$axios.post(this.$httpUrl + '/goods/listPage', {
                pageSize: this.pageSize,    // 传入分页的大小过去后端
                pageNum: this.pageNum,      // 传页数
                param: {
                    name: this.name,        // 传入姓名用于查询
                    storage: this.storage + '',  // 传入仓库用于查询
                    goodstype: this.goodstype + ''   // 传入分类用于查询
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
        // 获取仓库的全部列表
        loadStorage() {
            this.$axios.get(this.$httpUrl + '/storage/list', {}).then(res => res.data).then(res => {
                // console.log(res);
                if (res.code == 200) {
                    this.storageData = res.data
                } else {
                    alert('获取数据失败');
                }
            }).catch(error => {
                console.error('请求失败:', error);     // 在控制台打印错误信息
                alert('网络请求失败，请检查网络连接');   // 提示用户网络请求失败
            });
        },
        // 获取物品分类的名称
        loadGoodsType() {
            this.$axios.get(this.$httpUrl + '/goodstype/list', {}).then(res => res.data).then(res => {
                // console.log(res);
                if (res.code == 200) {
                    this.goodstypeData = res.data
                } else {
                    alert('获取数据失败');
                }
            }).catch(error => {
                console.error('请求失败:', error);     // 在控制台打印错误信息
                alert('网络请求失败，请检查网络连接');   // 提示用户网络请求失败
            });
        },
        // 定义格式化函数，
        formatStorage(row) {
            // 获取storage仓库表中的数据，拿到数据后用Goods物品管理表的id字段连接storage表，然后替换Goods表中name字段等于storage表中的name
            let temp = this.storageData.find(item => {
                return item.id == row.storage
            })
            // 返回找到的仓库对象的名称，如果未找到则返回 undefined
            return temp && temp.name
        },
        formatGoodsType(row) {
            // 获取storage仓库表中的数据，拿到数据后用Goods物品管理表的id字段连接storage表，然后替换Goods表中name字段等于storage表中的name
            let temp = this.goodstypeData.find(item => {
                return item.id == row.goodstype
            })
            // 返回找到的仓库对象的名称，如果未找到则返回 undefined
            return temp && temp.name
        },
        // 多选框，多删除，获取多选后的id放到multipleSelection字段数组里面
        handleSelectionChange(val) {
            // console.log("多选===" ,val)
            this.multipleSelection = val;
        },
        // 单选列表，获取数据，然后放入data的currentRow中
        handleRadioCurrentChange(val) {
            // console.log("单选===", val);
            this.currentRow = val;
        },
        /**
         * OCR图片上传并识别
         */
        handleOcrUpload(param) {
            const formData = new FormData();
            formData.append('file', param.file);
            this.$axios.post(this.$httpUrl + '/ocr/upload', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            }).then(res => {
                // 兼容后端返回的字符串或对象
                let data = res.data;
                if (typeof data === 'string') {
                    try {
                        data = JSON.parse(data);
                    } catch (e) {
                        console.log("error")
                    }
                }
                // 百度OCR返回words_result数组
                let name = '';
                if (data.words_result && data.words_result.length > 0) {
                    name = data.words_result[0].words;
                } else if (data.words_result) {
                    name = data.words_result;
                } else if (data.words) {
                    name = data.words;
                }
                if (name) {
                    this.form.name = name;
                    this.$message.success('识别成功，物品名称已自动填入');
                } else {
                    this.$message.warning('未识别到有效文字');
                }
            }).catch(() => {
                this.$message.error('图片识别失败');
            });
        },
    },

    // 载入前，初始化操作
    beforeMount() {
        this.loadPost();
        this.loadStorage();
        this.loadGoodsType();
    }
}
</script>

<style scoped>
.el-select {
    margin-left: 9px;
}

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
[aria-hidden] :focus-visible {
    visibility: visible !important;
    display: block !important;
    position: static !important;
    clip: auto !important;
    height: auto !important;
    width: auto !important;
    overflow: visible !important;
    white-space: normal !important;
    padding: 0 !important;
    margin: 0 !important;
    border: none !important;
    box-shadow: none !important;
    outline: auto !important;
    /* 使用默认的轮廓线 */
    background: transparent !important;
    color: inherit !important;
    font: inherit !important;
}

/* 确保焦点元素有明显的焦点样式 */
input:focus {
    outline: 2px solid blue;
    /* 你可以根据需要调整焦点样式 */
}

/* 确保 aria-hidden 为 true 的 input 元素被隐藏 */
input[aria-hidden="true"] {
    display: none !important;
}

/* 移除 el-radio 获得焦点时的阴影效果 */
.el-radio:focus:not(.is-focus):not(:active):not(.is-disabled) .el-radio__inner {
    box-shadow: none !important;
}
</style>