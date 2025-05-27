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
                title="这是批量删除，您确定删除已选的吗？" style="margin-left: 10px;" @confirm="dels" v-if="!(user.roleId === 1 || user.roleId === 2)">
                <el-button slot="reference" type="danger"><i class="el-icon-delete-solid"></i>一键删除</el-button>
            </el-popconfirm>
        </div>

        <!-- 表格，列表 -->
        <el-table :data="tableData" :header-cell-style="{ background: '#f2f5fc', color: '#555555' }" border @selection-change="handleSelectionChange">
            <el-table-column v-if="!(user.roleId === 1 || user.roleId === 2)" type="selection" width="55"></el-table-column>

            <el-table-column prop="id" label="ID" width="60">
            </el-table-column>
            <el-table-column prop="goodsname" label="物品名称" width="120">
            </el-table-column>
            <el-table-column prop="storagename" label="仓库名称" width="120">
            </el-table-column>
            <el-table-column prop="goodstypename" label="分类名称" width="120">
            </el-table-column>
            <el-table-column prop="adminname" label="操作人" width="120">
            </el-table-column>
            <el-table-column prop="username" label="申请人" width="120">
            </el-table-column>
            <el-table-column prop="count" label="数量" width="80">
            </el-table-column>
            <el-table-column prop="createtime" label="操作时间" width="180">
            </el-table-column>
            <el-table-column prop="remark" label="备注">
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
    name: "RecordManagePage",
    data() {
        return {
            user: JSON.parse(sessionStorage.getItem('CurUser')),    // 获取sessionStorage中的用户数据
            storageData: [],    // 存放仓库storage表全部的数据
            multipleSelection: [],   // 多选框，获取全部id，多删除
            goodstypeData: [],  // 存放分类goodstype表全部的数据
            tableData: [],      // 全部数据
            pageSize: 10,       // 每页显示多少条，默认十条
            pageNum: 1,         // 默认第一页
            total: 0,           // 显示一共多少条
            name: '',           // 姓名，查询
            storage: '',        // 仓库，查询
            goodstype: '',      // 分类，查询
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
        // 多删除     传入后端数据    deletes
        dels() {
            const ids = this.multipleSelection.map(row => row.id);
            this.$axios.get(this.$httpUrl + '/record/deletes?ids=' + ids)
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
        // 多选框，多删除，获取多选后的id放到multipleSelection字段数组里面
        handleSelectionChange(val) {
            // console.log("多选===" ,val)
            this.multipleSelection = val;
        },
        // 分页以及查询方法和接口传参   listPage
        loadPost() {
            this.$axios.post(this.$httpUrl + '/record/listPage', {
                pageSize: this.pageSize,    // 传入分页的大小过去后端
                pageNum: this.pageNum,      // 传页数
                param: {
                    name: this.name,        // 传入姓名用于查询
                    storage: this.storage + '',  // 传入仓库用于查询
                    goodstype: this.goodstype + '',   // 传入分类用于查询
                    roleId: this.user.roleId + '',       // 获取到登录后的用户权限id
                    userId: this.user.id + ''            // 获取到登录后的用户id
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
            })
        },
        // 获取物品分类的名称
        loadGoodstype() {
            this.$axios.get(this.$httpUrl + '/goodstype/list').then(res => res.data).then(res => {
                // console.log(res)
                if (res.code == 200) {
                    this.goodstypeData = res.data
                } else {
                    alert('获取数据失败')
                }
            })
        },
    },

    // 载入前，初始化操作
    beforeMount() {
        this.loadPost();
        this.loadStorage();
        this.loadGoodstype();
        // console.log(this.user.roleId);
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