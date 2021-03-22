<template>
  <Layout class="pagePane">
    <Content :style="{padding: '24px 0', minHeight: '280px', background: '#fff'}">
      <Row>
        <Col span="4" class="sider" hide-trigger :style="{background: '#fff'}">
          <Menu :theme="'light'" active-name="0">
            <MenuGroup title="大势所趋">
              <div class="xie">
                <nuxt-link to="/post/publish">发帖</nuxt-link>
              </div>
            </MenuGroup>
            <MenuGroup title="分类">
              <div width="auto" v-for="(item, index) in typesList" :key="index" :active-name="item.id">
                <MenuItem @click.native="toThisType(item.id)" :name="item.id">
                  <Row>
                    <Col span="8">
                      <Icon type="ios-keypad"></Icon>
                    </Col>
                    <Col span="11">{{item.nickname}}</Col>
                    <span>{{item.postNum}}</span>
                  </Row>
                </MenuItem>
              </div>
            </MenuGroup>
          </Menu>
        </Col>
        <Col span="20" :style="{padding: '24px', minHeight: '280px', background: '#fff'}"
             v-infinite-scroll="loadMore">
          <div style="    display: inline-block;width: 100%;">
            <label>
              <Select v-model="type.orderBy" style="width:200px;float: right" @on-change="toThisOrder">
                <Option v-for="item in postShowWay" :value="item.value" :key="item.value">{{ item.label }}</Option>
              </Select>
            </label>
            <div style="display: inline;float: right;    line-height: 270%;">排序方式：</div>
          </div>
          <div v-for="(item, index) in postsList" :key="index">
            <Card>
              <Row>
                <Col span="4">
                  <div style="text-align: center;font-size: 20px;color: red">{{item.postReplyNum}}</div>
                  <div style="text-align: center;font-size: 10px; color: #b0a5a5;">评论</div>
                </Col>
                <Col span="20">
                  <Row>
                    <Col span="18" style="font-size: 20px;font-weight:700">
                      <nuxt-link :to="getUser().token===null?'/':'/post/item/'+item.postId">{{item.postTitle}}</nuxt-link>
                    </Col>
                    <Col span="6" style=" font-size: 10px; color: #b0a5a5;">类别：{{item.typeNickname}}
                    </Col>
                  </Row>
                  <Row style=" font-size: 10px; color: #b0a5a5;">
                    <Col span="4">UP：{{item.userNickname}}</Col>
                    <Col span="6">{{item.postCreateTime}}</Col>
                    <Col span="4">查看：{{item.postClickNum}}</Col>
                    <Col span="4">饼干：{{item.postCookieNum}}</Col>
                    <Col span="4">收藏：{{item.postCollectionNum}}</Col>
                  </Row>
                </Col>
              </Row>
            </Card>
          </div>
        </Col>
      </Row>
    </Content>
  </Layout>
</template>

<script>
  import typeApi from '@/api/type'
  import postApi from '@/api/post'
  import axios from 'axios'
  import {toTime, toTimes} from '@/utils/publicMethod'
  import {getUser} from '@/utils/auth'

  export default {
    data() {
      return {
        currentPageNo: 1, //当前页数
        pageSize: 10,
        postShowWay: [
          {
            id: "0",
            value: "lastReplyTime",
            label: '按回复时间',
          },
          {
            id: "1",
            value: "createTime",
            label: '按发帖时间'
          },

        ],
        type: {
          typeId: "0",
          orderBy: "lastReplyTime",
        },
        reportMes: {},
      };
    },
    asyncData() {
      return axios.all([typeApi.getList(), postApi.getListSummary(1, 10, {typeId: "0", orderBy: "lastReplyTime"})])
        .then(axios.spread(function (typesList, postsList) {
          postsList.data.data.rows = toTimes(postsList.data.data.rows, "postCreateTime");
          return {
            typesList: typesList.data.data,
            postsList: postsList.data.data.rows,
            postsTotal: postsList.data.data.total,
          }
        }));
    },
    methods: {
      getUser,
      loadMore() {
        if (this.currentPageNo * this.pageSize <= this.postsTotal) {
          this.currentPageNo++;
          postApi.getListSummary(this.currentPageNo, this.pageSize, {
            typeId: this.type.typeId,
            orderBy: this.type.orderBy
          }).then(res => {
            if (res.data.data.rows.length > 0) {
              this.postsList = this.postsList.concat(toTimes(res.data.data.rows, "postCreateTime")) // 1.3合并新查询到的形成瀑布流
            } else {
              this.$Message.error(res.data.message);
            }
          })
        }
      },
      resetType: function () {
        this.type.orderBy = "lastReplyTime";
        this.currentPageNo = 1;
      },
      toThisType(currentTypeId) {
        this.resetType();
        this.type.typeId = currentTypeId;
        postApi.getListSummary(this.currentPageNo, 10, {typeId: this.type.typeId, orderBy: this.type.orderBy})
          .then(res => {
            if (res.data.flag) {
              this.postsList = toTimes(res.data.data.rows, "postCreateTime");
              this.postsTotal = res.data.data.total
            } else {
              this.$Message.error(res.data.message);
            }
          })
      },
      toThisOrder(value) {
        this.resetType();
        this.type.orderBy = value;
        console.log("orderBy====" + value);
        postApi.getListSummary(this.currentPageNo, 10, {typeId: this.type.typeId, orderBy: this.type.orderBy})
          .then(res => {
            if (res.data.flag) {
              this.postsList = toTimes(res.data.data.rows, "postCreateTime");
              this.postsTotal = res.data.data.total
            } else {
              this.$Message.error(res.data.message);
            }
          })
      },
    }
  }
</script>

<style>
  .xie {
    text-align: center;
    font-size: 2em;
  }

  .pagePane .sider .ivu-menu-vertical {
    width: 100% !important;
  }
</style>

