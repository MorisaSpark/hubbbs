<template>
  <div class="userPane">
    <card>
      <Row slot="title">
        <Col span="3" style="    padding: 1em;">
          <router-link :to="'/person/user/'+userId">
            <img style="width: 100%" :src="userDetail.icon"/>
          </router-link>
        </Col>
        <Col span="15" style="">
          <Row style="margin: 3em 0 1em 0">
            <span style="font-size: 1em;margin-right: 30px;">昵称：{{userDetail.nickname}}</span>
            <span style="margin-right: 1em">性别：{{userDetail.sex}}</span>
          </Row>
          <Row style="margin: 15px 10px 15px 0;">
            <span style="font-size: 1em;margin-right: 30px;">学校：{{userDetail.college}}</span>
            <span style="margin-right: 30px;">专业：{{userDetail.major}}</span>
            <span>等级：LV{{userDetail.grade}}</span>
          </Row>
          <Row>
            <Col span="24" style="color: rgba(0,0,0,0.4)">简介：{{userDetail['summary']}}</Col>
          </Row>
        </Col>
        <Col span="6" style="margin: 25px 0">
          <Row v-if="!isSelf">
            <Col style="text-align: center" span="8">
              <Button type="primary" @click="incAttention" v-if="attenterBtn">关注</Button>
              <Button type="dashed" @click="decAttention" v-if="!attenterBtn">关注中</Button>
            </Col>
            <Col style="text-align: center" span="8">
              <Button type="dashed" @click="sendMessage">私信</Button>
            </Col>
            <Col style="text-align: center" span="8">
              <Button type="text" @click="sendReport">举报</Button>
            </Col>
            <Modal
              v-model="mesMain.mesPane"
              title="填写内容"
              @on-ok="ok"
              @on-cancel="cancel">
              <Input v-model="mesMain.mesPaneBody" type="textarea" :autosize="{minRows: 2,maxRows: 5}"
                     placeholder="Enter something..."></Input>
            </Modal>
          </Row>
          <Row v-else-if="isSelf">
            <Col style="text-align: center" span="8">
              <Button type="primary" disabled>关注</Button>
            </Col>
            <Col style="text-align: center" span="8">
              <Button type="dashed" disabled>私信</Button>
            </Col>
            <Col style="text-align: center" span="8">
              <Button type="text" disabled>举报</Button>
            </Col>
          </Row>
          <Row style="margin: 30px 0">
            <Col style="text-align: center" span="8">
              <div>关注</div>
              <br>
              <div>{{userDetail.attenterNum}}</div>
            </Col>
            <Col style="text-align: center" span="8">
              <div>粉丝</div>
              <br>
              <div>{{userDetail.fansNum}}</div>
            </Col>
            <Col style="text-align: center" span="8">
              <div>阅读</div>
              <br>
              <div>{{sumClickNum}}</div>
            </Col>
          </Row>
        </Col>
      </Row>
      <Card>
        <Row slot="title">
          <span>
            <span>文章</span>
            <span>{{postsGeneral['postNum']}}</span>
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
                  <!--<Col span="6" style=" font-size: 10px; color: #b0a5a5;">类别：{{item.typeName}}-->
                  <!--</Col>-->
                </Row>
                <Row style=" font-size: 10px; color: #b0a5a5;">
                  <Col span="4">UP：{{userDetail.nickname}}</Col>
                  <Col span="6">{{item.createTime}}</Col>
                  <Col span="4">查看：{{item.clickNum}}</Col>
                  <Col span="4">饼干：{{item.cookieNum}}</Col>
                  <Col span="4">收藏：{{item.collectionNum}}</Col>
                </Row>
              </Col>
            </Row>
          </Card>
        </div>
        <Page :total="postsGeneral['postNum']" show-elevator @on-change="toThisPage" :current="currentPageNo"
              :page-size="20" style="margin: 10px 0"></Page>
      </Card>
    </card>
  </div>
</template>

<script>
  import postApi from '@/api/post'
  import userApi from '@/api/user'
  import attenterApi from '@/api/attenter'
  import messageApi from '@/api/message'
  import reportApi from '@/api/report'
  import {toTime, toTimes, toRealSex} from '@/utils/publicMethod'
  import {getUser} from '@/utils/auth'
  import axios from 'axios'

  export default {
    data() {
      return {
        mesMain: {
          mesPane: false,
          mesType: "",
          mesPaneBody: "",
        },

        currentPageNo: 1,
        userId: '',
        postList: {},
        isSelf: false,
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
        messageMes: {
          body: "",
          receiverId: "",
        },
        reportMes: {
          thing: "",
          thingId: "",
          reason: "",
        },
        postMes: {
          orderBy: "createTime",
          userId: '',
        }
      }
    },
    asyncData({params}) {
      return axios.all([postApi.search(1, 20, {userId: params.id}), userApi.findById(params.id), postApi.findSumClickNumByUserId(params.id)])
        .then(axios.spread(function (postList, userDetail, sumClickNum) {
          postList.data.data.rows = toTimes(postList.data.data.rows, 'createTime');
          postList.data.data.rows = toTimes(postList.data.data.rows, 'lastReplyTime');
          userDetail.data.data.user.sex = toRealSex(userDetail.data.data.user.sex);
          return {
            postsGeneral: postList.data.data,
            postList: postList.data.data.rows,
            userDetail: userDetail.data.data.user,
            userId: params.id,
            sumClickNum: sumClickNum.data.data,
            attenterBtn: !userDetail.data.data.isAtter,
          }
        }));
    },
    methods: {
      searchByFlowers(currentPageNo) {
        postApi.searchByFlowers(currentPageNo, 20, this.postMes)
          .then(res => {
            if (res.data.flag) {
              res.data.data.rows = toTimes(res.data.data.rows, 'createTime');
              res.data.data.rows = toTimes(res.data.data.rows, 'lastReplyTime');
              this.postList = res.data.data.rows;
              this.loadSpin = !this.loadSpin;
            } else {
              this.$Message.error(res.data.message);
            }
          })
      },
      toThisPage(value) {
        this.currentPageNo = value;
        this.searchByFlowers(value);
      },
      toThisOrder(data) {
        this.currentPageNo = 1;
        this.postMes.orderBy = data.value;
        this.postMes.userId = this.userId;
        this.searchByFlowers(this.currentPageNo);
      },
      incAttention() {
        attenterApi.save({userId: this.userId})
          .then(res => {
            if (res.data.flag) {
              this.$Message.success(res.data.message);
              this.attenterBtn = !this.attenterBtn;
            } else {
              this.$Message.error(res.data.message);
            }
          })
      },
      decAttention() {
        attenterApi.delete({userId: this.userId})
          .then(res => {
            if (res.data.flag) {
              this.$Message.success(res.data.message);
              this.attenterBtn = !this.attenterBtn;
            } else {
              this.$Message.error(res.data.message);
            }
          })
      },
      sendMessage() {
        this.mesMain.mesType = "message";
        this.mesMain.mesPane = true;
      },
      sendReport() {
        this.mesMain.mesType = "report";
        this.mesMain.mesPane = true;
      },
      ok() {
        this.$Message.info('Clicked ok');
        if (this.mesMain.mesType === "message") {
          this.messageMes.body = this.mesMain.mesPaneBody;
          this.messageMes.receiverId = this.userId;
          messageApi.save(this.messageMes)
            .then(res => {
              if (res.data.flag) {
                this.$Message.success(res.data.message);
              } else {
                this.$Message.error(res.data.message);
              }
            })
        } else {
          this.reportMes.thing = "user";
          this.reportMes.thingId = this.userId;
          this.reportMes.reason = this.mesMain.mesPaneBody;
          reportApi.save(this.reportMes)
            .then(res => {
              if (res.data.flag) {
                this.$Message.success(res.data.message);
              } else {
                this.$Message.error(res.data.message);
              }
            })
        }
      },
      cancel() {
        this.$Message.info('Clicked cancel');
      },
    }
  }
</script>

<style scoped>
  .userMes span:nth-of-type(1) {
    margin-right: 1em;
  }
</style>
