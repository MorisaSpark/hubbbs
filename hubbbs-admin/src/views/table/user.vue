<template>
  <div>
    <br>
    <el-form :inline="true">
      <el-form-item label="用户昵称">
        <el-input v-model="searchMap.nickname" placeholder="用户昵称"></el-input>
      </el-form-item>
      <el-form-item label="性别（0：女，1：男）">
        <el-input v-model="searchMap.sex" placeholder="性别（0：女，1：男）"></el-input>
      </el-form-item>
      <el-form-item label="年级">
        <el-input v-model="searchMap.grade" placeholder="年级"></el-input>
      </el-form-item>
      <el-form-item label="创号时间">
        <el-input v-model="searchMap.createTime" placeholder="创号时间"></el-input>
      </el-form-item>
      <el-form-item label="最近登录时间">
        <el-input v-model="searchMap.lastLoginTime" placeholder="最近登录时间"></el-input>
      </el-form-item>
      <el-form-item label="上一次登录时间">
        <el-input v-model="searchMap.lastLoginTimeB" placeholder="上一次登录时间"></el-input>
      </el-form-item>
      <el-form-item label="最后改名时间">
        <el-input v-model="searchMap.lastRenameTime" placeholder="最后改名时间"></el-input>
      </el-form-item>
      <el-form-item label="头像地址">
        <el-input v-model="searchMap.icon" placeholder="头像地址"></el-input>
      </el-form-item>
      <el-form-item label="大学名称">
        <el-input v-model="searchMap.college" placeholder="大学名称"></el-input>
      </el-form-item>
      <el-form-item label="专业名称">
        <el-input v-model="searchMap.major" placeholder="专业名称"></el-input>
      </el-form-item>
      <el-form-item label="简介">
        <el-input v-model="searchMap.summary" placeholder="简介"></el-input>
      </el-form-item>
      <el-form-item label="状态">
        <el-input v-model="searchMap.isBan" placeholder="状态"></el-input>
      </el-form-item>
      <el-form-item label="饼干个数">
        <el-input v-model="searchMap.cookieNum" placeholder="饼干个数"></el-input>
      </el-form-item>
      <el-form-item label="发表文章量">
        <el-input v-model="searchMap.postNum" placeholder="发表文章量"></el-input>
      </el-form-item>
      <el-form-item label="粉丝量">
        <el-input v-model="searchMap.fansNum" placeholder="粉丝量"></el-input>
      </el-form-item>
      <el-form-item label="关注量">
        <el-input v-model="searchMap.attenterNum" placeholder="关注量"></el-input>
      </el-form-item>

      <el-button type="primary" @click="fetchData()">查询</el-button>
      <el-button type="primary" @click="handleEdit('')">新增</el-button>
    </el-form>
    <el-table
      :data="list"
      border
      style="width: 100%">
      <el-table-column type="index"></el-table-column>
      <el-table-column prop="id" label="userId" width="180"></el-table-column>
      <el-table-column prop="nickname" label="用户昵称" width="180"></el-table-column>
      <el-table-column prop="sex" label="性别" width="50"></el-table-column>
      <el-table-column prop="grade" label="年级" width="50"></el-table-column>
      <el-table-column prop="createTime" label="创号时间" width="150"></el-table-column>
      <el-table-column prop="lastLoginTime" label="最近登录时间" width="150"></el-table-column>
      <el-table-column prop="lastLoginTimeB" label="上一次登录时间" width="150"></el-table-column>
      <el-table-column prop="lastRenameTime" label="最后改名时间" width="150"></el-table-column>
      <el-table-column prop="icon" label="头像地址" width="250"></el-table-column>
      <el-table-column prop="college" label="大学名称" width="180"></el-table-column>
      <el-table-column prop="major" label="专业名称" width="180"></el-table-column>
      <el-table-column prop="summary" label="简介" width="200"></el-table-column>
      <el-table-column prop="isBan" label="状态" width="50"></el-table-column>
      <el-table-column prop="cookieNum" label="饼干个数" width="100"></el-table-column>
      <el-table-column prop="postNum" label="发表文章量" width="100"></el-table-column>
      <el-table-column prop="fansNum" label="粉丝量" width="100"></el-table-column>
      <el-table-column prop="attenterNum" label="关注量" width="100"></el-table-column>

      <el-table-column
        fixed="right"
        label="操作"
        width="100">
        <template slot-scope="scope">
          <el-button @click="handleEdit(scope.row.id)" type="text" size="small">修改</el-button>
          <el-button @click="handleDelete(scope.row.id)" type="text" size="small">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="fetchData"
      @current-change="fetchData"
      :current-page.sync="currentPage"
      :page-sizes="[5,10,20]"
      :page-size="10"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total">
    </el-pagination>
    <el-dialog title="编辑" :visible.sync="dialogFormVisible">
      <el-form label-width="100px">
        <el-form-item label="用户昵称">
          <el-input v-model="pojo.nickname"></el-input>
        </el-form-item>
        <el-form-item label="性别（0：女，1：男）">
          <el-input v-model="pojo.sex"></el-input>
        </el-form-item>
        <el-form-item label="年级">
          <el-input v-model="pojo.grade"></el-input>
        </el-form-item>
        <el-form-item label="创号时间">
          <el-input v-model="pojo.createTime"></el-input>
        </el-form-item>
        <el-form-item label="最近登录时间">
          <el-input v-model="pojo.lastLoginTime"></el-input>
        </el-form-item>
        <el-form-item label="上一次登录时间">
          <el-input v-model="pojo.lastLoginTimeB"></el-input>
        </el-form-item>
        <el-form-item label="最后改名时间">
          <el-input v-model="pojo.lastRenameTime"></el-input>
        </el-form-item>
        <el-form-item label="头像地址">
          <el-input v-model="pojo.icon"></el-input>
        </el-form-item>
        <el-form-item label="大学名称">
          <el-input v-model="pojo.college"></el-input>
        </el-form-item>
        <el-form-item label="专业名称">
          <el-input v-model="pojo.major"></el-input>
        </el-form-item>
        <el-form-item label="简介">
          <el-input v-model="pojo.summary"></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-input v-model="pojo.isBan"></el-input>
        </el-form-item>
        <el-form-item label="饼干个数">
          <el-input v-model="pojo.cookieNum"></el-input>
        </el-form-item>
        <el-form-item label="发表文章量">
          <el-input v-model="pojo.postNum"></el-input>
        </el-form-item>
        <el-form-item label="粉丝量">
          <el-input v-model="pojo.fansNum"></el-input>
        </el-form-item>
        <el-form-item label="关注量">
          <el-input v-model="pojo.attenterNum"></el-input>
        </el-form-item>

        <el-button type="primary" @click="handleSave()">保存</el-button>
        <el-button @click="dialogFormVisible = false">关闭</el-button>
      </el-form>
    </el-dialog>
  </div>
</template>
<script>
  import userApi from '@/api/user'

  export default {
    data() {
      return {
        list: [],
        total: 0, // 总记录数
        currentPage: 1, // 当前页
        pageSize: 10, // 每页大小
        searchMap: {}, // 查询条件
        dialogFormVisible: false, // 编辑窗口是否可见
        pojo: {}, // 编辑表单绑定的实体对象
        cityList: [], // 城市列表
        id: '' // 当前用户修改的ID
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      fetchData() {
        userApi.search(this.currentPage, this.pageSize, this.searchMap).then(response => {
          this.list = response.data.rows
          this.total = response.data.total
        })
      },
      handleSave() {
        userApi.update(this.id, this.pojo).then(response => {
          this.$message({
            message: response.message,
            type: (response.flag ? 'success' : 'error')
          })
          if (response.flag) { // 如果成功
            this.fetchData() // 刷新列表
          }
        })
        this.dialogFormVisible = false // 关闭窗口
      },
      handleEdit(id) {
        this.id = id
        this.dialogFormVisible = true // 打开窗口
        if (id !== '') { // 修改
          userApi.findById(id).then(response => {
            if (response.flag) {
              this.pojo = response.data
            }
          })
        } else {
          this.pojo = {} // 清空数据
        }
      },
      handleDelete(id) {
        this.$confirm('确定要删除此纪录吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          userApi.deleteById(id).then(response => {
            this.$message({message: response.message, type: (response.flag ? 'success' : 'error')})
            if (response.flag) {
              this.fetchData() // 刷新数据
            }
          })
        })
      }
    }
  }
</script>
