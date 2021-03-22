<template>
  <div class="postPane">
    <Card>
      <div slot="title">
        <label>
          <Select v-model="type.value">
            <Option v-for="item in pcShowWay" :value="item.value" :key="item.value" @click.native="toThisType(item)">
              {{ item.label }}
            </Option>
          </Select>
        </label>
      </div>
      <div>
        <Card>
          <Row slot="title">
          <span>
            <span>文章</span>
            <!--<span>{{total}}</span>-->
          </span>
            <div style="display: inline;">
              <label>
                <Select v-model="postMes.orderBy" style="width:200px;float: right" @on-change="toThisOrder"
                        :label-in-value="true">
                  <Option v-for="item in postShowWay" :value="item.value" :key="item.value">{{ item.label }}</Option>
                </Select>
              </label>
            </div>
          </Row>
          <div v-for="(item, index) in postList" :key="index">
            <Card>
              <Row>
                <Col span="4">
                  <div style="text-align: center;font-size: 20px;color: red">{{item.replyNum}}</div>
                  <div style="text-align: center;font-size: 10px; color: #b0a5a5;">评论</div>
                </Col>
                <Col span="20">
                  <Row>
                    <Col span="18" style="font-size: 20px;font-weight:700">
                      <nuxt-link :to="'/post/item/'+item.id">{{item.title}}</nuxt-link>
                    </Col>
                    <Col span="4" style="font-size: 20px;font-weight:700" v-if="type.value==='post'">
                      <Button type="primary" @click="deletePost(index)">删除该文章</Button>
                    </Col>
                  </Row>
                  <Row style=" font-size: 10px; color: #b0a5a5;">
                    <Col span="4">UP：{{item.userNickname}}</Col>
                    <Col span="6">{{item.createTime}}</Col>
                    <Col span="4">查看：{{item.clickNum}}</Col>
                    <Col span="4">饼干：{{item.cookieNum}}</Col>
                    <Col span="4">收藏：{{item.collectionNum}}</Col>
                  </Row>
                </Col>
              </Row>
            </Card>
          </div>
          <Page :total="total" show-elevator @on-change="toThisPage" :current="currentPageNo"
                :page-size="20" style="margin: 10px 0"></Page>
        </Card>
        <Modal
          v-model="deletePostMes.isShow"
          title="删除确定"
          @on-ok="ok"
          @on-cancel="cancel">
          <p>
            文章名<br>
            <strong>{{deletePostMes.post["title"]}} </strong>
          </p>
        </Modal>
      </div>
    </Card>
  </div>
</template>

<script>
  import {toTimes, toRealSex} from "@/utils/publicMethod"
  import postApi from '@/api/post'
  import {getUser} from "@/utils/auth";

  export default {
    name: "index",
    data() {
      return {
        pcShowWay: [
          {
            id: 0,
            label: "发表的文章",
            value: "post"
          },
          {
            id: 1,
            label: "收藏的文章",
            value: "collection"
          },
        ],
        type: {
          value: "post",
        },
        pcList: {},
        postShowWay: [
          {
            id: "0",
            label: "最新",
            value: "createTime"
          },
          {
            id: "1",
            label: "阅读量",
            value: "clickNum"
          },
          {
            id: "2",
            label: "饼干量",
            value: "cookieNum"
          },
          {
            id: "3",
            label: "评论量",
            value: "replyNum"
          },
        ],
        postMes: {
          orderBy: "createTime",
          userId: '',
        },
        size: 20,
        currentPageNo: 1,
        postList: {},
        total: 0,
        userId: '',
        user: {},
        deletePostMes: {
          isShow: false,
          postId: "",
          post: {},
          index: 0,
        }
      }
    },
    components: {},
    asyncData() {
      return postApi.findByUserIdType("post", "createTime", 1, 20)
        .then(res => {
          res.data.data.rows = toTimes(res.data.data.rows, "createTime");
          return {
            postList: res.data.data.rows,
            total: res.data.data.total
          };
        });
    },
    methods: {
      toThisPage(value) {
        this.currentPageNo = value;
        this.findByFlower();
      },
      toThisType(item) {
        this.currentPageNo = 1;
        this.type.value = item.value;
        this.postMes.orderBy = "createTime";
        this.findByFlower();
      },
      toThisOrder(data) {
        this.currentPageNo = 1;
        this.postMes.orderBy = data.value;
        this.postMes.userId = this.user.userId;
        this.findByFlower();
      },
      findByFlower() {
        postApi.findByUserIdType(this.type.value, this.postMes.orderBy, this.currentPageNo, this.size)
          .then(res => {
            if (res.data.flag) {
              res.data.data.rows = toTimes(res.data.data.rows, "createTime");
              this.postList = res.data.data.rows;
              this.total = res.data.data.total;
              this.$Message.success(res.data.message);
            } else {
              this.$Message.error(res.data.message);
            }
          })
      },
      deletePost(index) {
        this.deletePostMes.index = index;
        this.deletePostMes.postId = this.postList[index].id;
        this.deletePostMes.post = this.postList[index];
        this.deletePostMes.isShow = true;
      },
      ok() {
        this.$Message.success("click");
        postApi.deleteById(this.deletePostMes.postId)
          .then(res => {
            if (res.data.flag) {
              this.postList.splice(this.deletePostMes.index, 1);
              this.$Message.success(res.data.message);
            } else {
              this.$Message.error(res.data.message);
            }
          })
      },
      cancel() {
        this.$Message.info("cancel");
      }

    }
  }
</script>

<style scoped>

</style>
