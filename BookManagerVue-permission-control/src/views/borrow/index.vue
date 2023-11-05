<template>
  <div class="app-container">
    <!-- 顶部功能 -->
    <div class="filter-container" style="margin-bottom: 15px">
      <!-- 用户名输入 -->
      <el-input v-permission="['admin']" v-model="queryParam.username" placeholder="用户名" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <!-- 图书名输入 -->
      <el-input v-model="queryParam.bookname" placeholder="图书名" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <!-- 一些按钮 -->
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        搜索
      </el-button>
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleShowAll">
        显示全部
      </el-button>
      <el-button v-permission="['admin']" class="filter-item" style="margin-left: 10px;" type="danger" icon="el-icon-delete" @click="handleDeleteSome">
        批量删除
      </el-button>
      <!-- <el-button class="filter-item" style="margin-left: 10px;" type="success" icon="el-icon-edit" @click="handleReturnSome">
        批量还书
      </el-button> -->
    </div>

    <!--弹出框-->
    <el-dialog title="修改有效时长" :visible.sync="dialogFormVisible" width="40%">
      <!--普通表单-->
      <el-form :model="form"  label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" disabled></el-input>
        </el-form-item>
        <el-form-item label="图书名称" prop="bookname">
          <el-input v-model="form.bookname" disabled></el-input>
        </el-form-item>
        <el-form-item label="有效时长" prop="limitDays">
          <el-input v-model="form.limitDays"></el-input>

        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>


    <!--数据表格-->
    <el-table
        ref="multipleTable"
        :data="tableData"
        border
        style="width: 100%">
      <el-table-column
          fixed
          type="selection"
          width="55">
      </el-table-column>
      <el-table-column
          fixed
          prop="borrowid"
          label="序号"
          width="100">
      </el-table-column>
      <el-table-column
          prop="username"
          label="用户名"
          show-overflow-tooltip>
      </el-table-column>
      <el-table-column
          prop="bookname"
          label="图书名"
          show-overflow-tooltip>
      </el-table-column>
      <el-table-column
          prop="borrowtimestr"
          label="借书时间">
      </el-table-column>
      <el-table-column
          label="还书时间">
          <template slot-scope="scope">
            <span v-if="scope.row.returntimestr === null || scope.row.returntimestr === ''" style="color: red">等待还书</span>
            <span v-else style="color: #1aac1a">{{scope.row.returntimestr}}</span>
          </template>
      </el-table-column>
      <el-table-column
          prop="limitDays"
          show-overflow-tooltip
          label="有效时长">    
      </el-table-column>
      <el-table-column
          label="当前状态"
          width="100">
          <template slot-scope="scope">
            <el-tag
              :type="scope.row.returntimestr === null || scope.row.returntimestr === '' ? (checkDate(scope.row)  > 0 ? `danger` :'success')  : 'success'"
              disable-transitions>
              {{ (scope.row.returntimestr === null || scope.row.returntimestr === '') ? (checkDate(scope.row)  > 0 ? `罚款 ${checkDate(scope.row)} 元` :'正常') : '正常'}}
            </el-tag>
          </template>
      </el-table-column>
      <el-table-column
          fixed="right"
          label="操作"
          :width="roleIsAdmin?'280px':'110px'">
        <template slot-scope="scope">
          <el-button v-permission="['admin']" @click="handleDelete(scope.row,scope.$index)" type="danger" size="small">删除</el-button>
          <el-button v-permission="['admin']" @click="handleVaildDays(scope.row,scope.$index)" type="success" size="small">修改有效时长</el-button>
          <el-button @click="handleReturn(scope.row,scope.$index)" type="success" size="small">归还图书</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!--分页条-->
    <el-pagination
        background
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page.sync="queryParam.page"
        :page-sizes="[5, 10, 20, 50]"
        :page-size="queryParam.limit"
        layout="total, sizes, prev, pager, next, jumper"
        :total="recordTotal"
        style="margin-top: 15px">
    </el-pagination>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import permission from '@/directive/permission/index.js' // 权限判断指令
import waves from '@/directive/waves' // waves directive
import { getCount,queryBorrows,queryBorrowsByPage,addBorrow,deleteBorrow,deleteBorrows,updateBorrow,returnBook,updateLimitDays } from '@/api/borrow'

export default {
  name: 'Bookinfo',
  directives: { waves, permission },
  // 创建后
  created() {
    // 从服务器获取数据表格第一页的信息
    queryBorrowsByPage(this.queryParam).then(res => {
      console.log('首页数据获取成功',res)
      this.tableData = res.data
      this.recordTotal = res.count
    })
  },
  methods: {
    // 分页大小改变监听
    handleSizeChange(curSize) {
      const params = this.queryParam
      params.limit = curSize
      queryBorrowsByPage(params).then(res => {
            console.log('分页数据获取成功',res)
            this.tableData = res.data
            this.recordTotal = res.count
      })
    },

    // 点击分页监听方法
    handleCurrentChange(curPage) {
      const params = this.queryParam
      params.page = curPage
      queryBorrowsByPage(params).then(res => {
            console.log('分页数据获取成功',res)
            this.tableData = res.data
            this.recordTotal = res.count
      })
    },

    // 搜索
    handleFilter() {
      this.queryParam.page = 1
      queryBorrowsByPage(this.queryParam).then(res => {
        if(res.code === 0) {
          this.tableData = res.data
          this.recordTotal = res.count
        }
      })
    },

    // 显示全部
    handleShowAll() {
      this.queryParam.page = 1
      this.queryParam.username = null
      this.queryParam.bookname = null
      queryBorrowsByPage(this.queryParam).then(res => {
        if(res.code === 0) {
          this.tableData = res.data
          this.recordTotal = res.count
        }
      })
    },

    // 删除记录
    handleDelete(row, index) {
      this.$confirm('确定要删除该条记录吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteBorrow(row).then(res => {
          if(res === 1) {
            this.$message.success('删除记录成功')
            this.tableData.splice(index, 1)
            // 如果删完了，获取上一页
            if(this.tableData.length === 0) {
              this.queryParam.page = this.queryParam.page - 1
              this.handleCurrentChange(this.queryParam.page)
            }
          } else {
            this.$message.error('删除记录失败')
          }
        })
      })
    },

    // 删除一些
    handleDeleteSome() {
      this.$confirm('确定要删除这些记录吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 获取选中的对象数组
        const items = this.$refs.multipleTable.selection
        deleteBorrows(items).then(res => {
          if(res > 0) {
            this.$message.success('删除' + res + '条记录成功')
            if(this.tableData.length === res) {  //如果本页内容全部删光了
              //当前页为上一页
              if(this.queryParam.page !== 0) {
                this.queryParam.page = this.queryParam.page - 1
              }
            }
            // 重载当前页
            this.handleCurrentChange(this.queryParam.page)
          } else {
            this.$message.error('删除记录失败')
          }
        })
      })
    },

    // 还书
    handleReturn(row, index) {
      this.$confirm('确定要还书吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        returnBook(row.borrowid, row.bookid).then(res => {
          if(res === 1) {
            this.$message.success('还书成功')
            this.handleCurrentChange(this.queryParam.page)
          } else {
            this.$message.error('还书失败')
          }
        })
      })
    },

    // 修改有效时间
    handleVaildDays(row, index){
      this.form = {
        bookid: row.bookid,
        bookname: row.bookname,
        borrowid: row.borrowid,
        borrowtime: row.borrowtime,
        borrowtimestr: row.borrowtimestr,
        userid: row.userid,
        username: row.username,
        limitDays: row.limitDays
      }

      // 显示表单框
      this.dialogFormVisible = true
    },

    // 检查是否超时
    checkDate(row){
      // 将目标日期字符串解析为日期对象
      const targetDate = new Date(row.borrowtimestr);

      // 获取当前日期
      const currentDate = new Date();

      // 计算时间差
      const timeDifference = currentDate - targetDate;

      // 将毫秒数转换为天数
      const daysDifference = Math.floor(timeDifference / (1000 * 60 * 60 * 24));

      console.log(`距离 ${row.borrowtimestr} 相差 ${daysDifference} 天。`);

      console.log('------daysDifference-----',daysDifference / 30)
      let money =  Math.floor(daysDifference / 30) * 5;

      return money
    },

    // 确定修改
    submitForm(){


      console.log('----limitDays--->>>>',this.form.limitDays-0)
      console.log('----borrowid---->>>>',this.form.borrowid)
      let submitData = {
        borrowid : this.form.borrowid-0,
        limitdays : this.form.limitDays-0,
      }
      updateLimitDays(submitData).then(res => {
        console.log('----res---',res)
          if(res.status === 'ok') {
            this.$message.success('修改时长成功')
            this.handleCurrentChange(this.queryParam.page)
          } else {
            this.$message.error('修改时长失败'+ res.message)
          }
        })


      // 显示表单框
      this.dialogFormVisible = false
    }
    

    // 批量还书
    // handleReturn(row, index) {
    //   this.$confirm('确定要还书吗?', '提示', {
    //     confirmButtonText: '确定',
    //     cancelButtonText: '取消',
    //     type: 'warning'
    //   }).then(() => {
    //     returnBook(row.borrowid, row.bookid).then(res => {
    //       if(res === 1) {
    //         this.$message.success('还书成功')
    //         this.handleCurrentChange(this.queryParam.page)
    //       } else {
    //         this.$message.error('还书失败')
    //       }
    //     })
    //   })
    // },

  },
  data() {
    return {
      // 表格数据
      tableData: [],
      // 记录总数
      recordTotal: 0,
      // 查询参数
      queryParam: {
        page: 1,
        limit: 10,
        userid: null,
        username: null,
        bookname: null,
      },
      // 表单显示
      dialogFormVisible:false,
      //表单数据
      form:{
        bookid: null,
        bookname: "",
        borrowid: null,
        borrowtime: "",
        borrowtimestr: "",
        userid: null,
        username: "",
        limitDays:null
      }
    }
  },
  computed: {
    // 获得user信息
    ...mapGetters(['id','name','roles']),
    roleIsAdmin() {
      if(this.roles[0] === 'admin') return true
      else return false
    }
  },
  watch: {
    'queryParam.userid': {
      immediate: true,
      handler() {
        console.log("我被触发了")
        if (this.roleIsAdmin) {
          this.queryParam.userid = null
        } else {
          this.queryParam.userid = this.id
        }
      }
    }
  }
}

</script>

<style scoped>
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 150px;
    height: 200px;
    display: block;
  }
</style>
