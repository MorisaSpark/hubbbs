<template>
  <div>
    <br>
    <el-form :inline="true">
      <el-form-item label="禁止数据类型">
        <el-input v-model="searchMap.thing" placeholder="禁止数据类型"></el-input>
      </el-form-item>
      <el-form-item label="禁止数据Id">
        <el-input v-model="searchMap.thingId" placeholder="禁止数据Id"></el-input>
      </el-form-item>
      <el-form-item label="原因">
        <el-input v-model="searchMap.reason" placeholder="原因"></el-input>
      </el-form-item>
      <el-form-item label="时间">
        <el-input v-model="searchMap.time" placeholder="时间"></el-input>
      </el-form-item>
      <el-form-item label="状态">
        <el-input v-model="searchMap.isBan" placeholder="状态"></el-input>
      </el-form-item>
      <el-form-item label="用户Id">
        <el-input v-model="searchMap.userId" placeholder="用户Id"></el-input>
      </el-form-item>

      <el-button type="primary" @click="fetchData()">查询</el-button>
      <el-button type="primary" @click="handleEdit('')">新增</el-button>
    </el-form>
    <el-table
      :data="list"
      border
      style="width: 100%">
      <el-table-column type="index"></el-table-column>
      <el-table-column prop="id" label="banId" width="180"></el-table-column>
      <el-table-column prop="thing" label="禁止数据类型" width="80"></el-table-column>
      <el-table-column prop="thingId" label="禁止数据Id" width="180"></el-table-column>
      <el-table-column prop="reason" label="原因" width="200" show-overflow-tooltip></el-table-column>
      <el-table-column prop="time" label="时间" width="150"></el-table-column>
      <el-table-column prop="isBan" label="状态" width="50"></el-table-column>
      <el-table-column prop="userId" label="用户Id" width="180"></el-table-column>

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
        <el-form-item label="禁止数据类型">
          <el-input v-model="pojo.thing"></el-input>
        </el-form-item>
        <el-form-item label="禁止数据Id">
          <el-input v-model="pojo.thingId"></el-input>
        </el-form-item>
        <el-form-item label="原因">
          <el-input v-model="pojo.reason"></el-input>
        </el-form-item>
        <el-form-item label="时间">
          <el-input v-model="pojo.time"></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-input v-model="pojo.isBan"></el-input>
        </el-form-item>
        <el-form-item label="用户Id">
          <el-input v-model="pojo.userId"></el-input>
        </el-form-item>

        <el-button type="primary" @click="handleSave()">保存</el-button>
        <el-button @click="dialogFormVisible = false">关闭</el-button>
      </el-form>
    </el-dialog>
  </div>
</template>
<script>
  import banApi from '@/api/ban'

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
        banApi.search(this.currentPage, this.pageSize, this.searchMap).then(response => {
          this.list = response.data.rows
          this.total = response.data.total
        })
      },
      handleSave() {
        banApi.update(this.id, this.pojo).then(response => {
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
          banApi.findById(id).then(response => {
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
          banApi.deleteById(id).then(response => {
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
