<template>
  <div>
    <br>
    <el-form :inline="true">
      <el-form-item label="标题">
        <el-input v-model="searchMap.title" placeholder="标题"></el-input>
      </el-form-item>
      <el-form-item label="内容">
        <el-input v-model="searchMap.body" placeholder="内容"></el-input>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-input v-model="searchMap.createTime" placeholder="创建时间"></el-input>
      </el-form-item>
      <el-form-item label="最新评论时间">
        <el-input v-model="searchMap.lastReplyTime" placeholder="最新评论时间"></el-input>
      </el-form-item>
      <el-form-item label="评论量">
        <el-input v-model="searchMap.replyNum" placeholder="评论量"></el-input>
      </el-form-item>
      <el-form-item label="点击量">
        <el-input v-model="searchMap.clickNum" placeholder="点击量"></el-input>
      </el-form-item>
      <el-form-item label="喂食量">
        <el-input v-model="searchMap.cookieNum" placeholder="喂食量"></el-input>
      </el-form-item>
      <el-form-item label="收藏量">
        <el-input v-model="searchMap.collectionNum" placeholder="收藏量"></el-input>
      </el-form-item>
      <el-form-item label="是否热门">
        <el-input v-model="searchMap.isHot" placeholder="是否热门"></el-input>
      </el-form-item>
      <el-form-item label="状态">
        <el-input v-model="searchMap.isBan" placeholder="状态"></el-input>
      </el-form-item>
      <el-form-item label="作者Id">
        <el-input v-model="searchMap.userId" placeholder="作者Id"></el-input>
      </el-form-item>
      <el-form-item label="类型Id">
        <el-input v-model="searchMap.typeId" placeholder="类型Id"></el-input>
      </el-form-item>

      <el-button type="primary" @click="fetchData()">查询</el-button>
      <el-button type="primary" @click="handleEdit('')">新增</el-button>
    </el-form>
    <el-table
      :data="list"
      border
      style="width: 100%">
      <el-table-column type="index"></el-table-column>
      <el-table-column prop="id" label="postId" width="180"></el-table-column>
      <el-table-column prop="title" label="标题" width="200" show-overflow-tooltip></el-table-column>
      <!--<el-table-column prop="body" label="内容" width="80" show-overflow-tooltip></el-table-column>-->
      <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
      <el-table-column prop="lastReplyTime" label="最新评论时间" width="180"></el-table-column>
      <el-table-column prop="replyNum" label="评论量" width="100"></el-table-column>
      <el-table-column prop="clickNum" label="点击量" width="100"></el-table-column>
      <el-table-column prop="cookieNum" label="喂食量" width="100"></el-table-column>
      <el-table-column prop="collectionNum" label="收藏量" width="100"></el-table-column>
      <el-table-column prop="isHot" label="是否热门" width="50"></el-table-column>
      <el-table-column prop="isBan" label="状态" width="50"></el-table-column>
      <el-table-column prop="userId" label="作者Id" width="180"></el-table-column>
      <el-table-column prop="typeId" label="类型Id" width="180"></el-table-column>

      <el-table-column
        fixed="right"
        label="操作"
        width="100">
        <template slot-scope="scope">
          <el-button @click="open(scope.row.body)" type="text" size="small">查看内容</el-button>
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
        <el-form-item label="标题">
          <el-input v-model="pojo.title"></el-input>
        </el-form-item>
        <el-form-item label="内容">
          <el-input v-model="pojo.body"></el-input>
        </el-form-item>
        <el-form-item label="创建时间">
          <el-input v-model="pojo.createTime"></el-input>
        </el-form-item>
        <el-form-item label="最新评论时间">
          <el-input v-model="pojo.lastReplyTime"></el-input>
        </el-form-item>
        <el-form-item label="评论量">
          <el-input v-model="pojo.replyNum"></el-input>
        </el-form-item>
        <el-form-item label="点击量">
          <el-input v-model="pojo.clickNum"></el-input>
        </el-form-item>
        <el-form-item label="喂食量">
          <el-input v-model="pojo.cookieNum"></el-input>
        </el-form-item>
        <el-form-item label="收藏量">
          <el-input v-model="pojo.collectionNum"></el-input>
        </el-form-item>
        <el-form-item label="是否热门">
          <el-input v-model="pojo.isHot"></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-input v-model="pojo.isBan"></el-input>
        </el-form-item>
        <el-form-item label="作者Id">
          <el-input v-model="pojo.userId"></el-input>
        </el-form-item>
        <el-form-item label="类型Id">
          <el-input v-model="pojo.typeId"></el-input>
        </el-form-item>

        <el-button type="primary" @click="handleSave()">保存</el-button>
        <el-button @click="dialogFormVisible = false">关闭</el-button>
      </el-form>
    </el-dialog>
    <el-dialog
      title="内容"
      :visible.sync="centerDialogVisible"
      width="80%"
      center>
      <span class="pojoBody" v-html="pojo.body"></span>
      <span slot="footer" class="dialog-footer">
    <el-button @click="centerDialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="centerDialogVisible = false">确 定</el-button>
  </span>
    </el-dialog>
  </div>
</template>
<script>
  import postApi from '@/api/post'

  export default {
    data() {
      return {
        list: [],
        centerDialogVisible: false,
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
      open(body) {
        this.centerDialogVisible = true;
        this.pojo.body = body;
      },
      fetchData() {
        postApi.search(this.currentPage, this.pageSize, this.searchMap).then(response => {
          this.list = response.data.rows
          this.total = response.data.total
        })
      },
      handleSave() {
        postApi.update(this.id, this.pojo).then(response => {
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
          postApi.findById(id).then(response => {
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
          postApi.deleteById(id).then(response => {
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
<style>
  .pojoBody img{
    max-width: 100%;
  }
</style>
