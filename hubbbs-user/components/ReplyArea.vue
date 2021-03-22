<template>
  <div>
    <div>
      <div style="margin: 2% 0;">
        所有评论 {{replyListTotal}}
      </div>
      <div class="replyShowWays" style="display: inline-block;width: 100%;" v-show="false">
        <label>
          <Select v-model="type.orderBy" style="width:200px;float: right" @on-change="toThisOrder">
            <Option v-for="item in replyShowWay" :value="item.value" :key="item.value">{{ item.label }}</Option>
          </Select>
        </label>
        <div style="display: inline;float: right;">评论查看方式：</div>
      </div>
    </div>
    <Page :total="replyListTotal" show-elevator @on-change="toThisPage" :current="currentPageNo" :page-size="20"
          style="margin: 10px 0"></Page>
    <div>
      <div class="floorTh" v-if="type.typeId==='1'" v-for="(item, index) in replyList" :key="index">
        <Card class="replyArea">
          <div>
            <div>
              <Poptip placement="right" width="400" trigger="hover">
                <router-link :to="'/person/user/'+item.user.id">
                  <Avatar :src="item.user.icon"></Avatar>
                  <span style="margin: 0 10px">{{item.user.nickname}} </span>
                </router-link>
                <span>LV{{item.user.grade}}</span>
                <Card class="api" slot="content" style="width: 100%" :bordered="false">
                  <Row slot="title">
                    <Col span="5">
                      <Avatar :src="item.user.icon" style="width: 50px;height: 50px"></Avatar>
                    </Col>
                    <Col span="19">
                      <div style="width: 100%;margin: 10px 10px">
                        <span>{{item.user.nickname}} </span>
                        <span>LV{{item.user.grade}}</span>
                      </div>
                      <div style="white-space:normal; ">{{item.user['summary']}}</div>
                    </Col>
                  </Row>
                  <div>
                    <Row style="margin: 10px 0;text-align: center">
                      <Col span="8" class="rightNum">
                        <div class="down">投稿 {{item.user.postNum}}</div>
                      </Col>
                      <Col span="8" class="rightNum">
                        <div class="down">关注 {{item.user.attenterNum}}</div>
                      </Col>
                      <Col span="8" class="rightNum">
                        <div class="down">粉丝 {{item.user.fansNum}}</div>
                      </Col>
                    </Row>
                  </div>
                </Card>
              </Poptip>
              <span style="float: right"> #{{item.reply.floorTh}}</span>
            </div>
            <div class="replyBody" v-html="item.reply.body">
            </div>
            <Row>
              <Col span="6" class="downLeft">发表于 {{item['reply']['createTime']}}</Col>
              <Col span="4" class="downRight" style="float: right">
                <MessageBtn
                  :messageBtnBridge="messageBtnBridge('私信','dashed','message','people',item.reply.id)"></MessageBtn>
                <MessageBtn
                  :messageBtnBridge="messageBtnBridge('举报','text','report','reply',item.reply.id)"></MessageBtn>
              </Col>
            </Row>
          </div>
        </Card>
      </div>
      <Spin fix v-if="loadSpin"></Spin>
    </div>
    <Page :total="replyListTotal" show-elevator @on-change="toThisPage" :current="currentPageNo" :page-size="20"
          style="margin: 10px 0"></Page>
  </div>
</template>

<script>
  import axios from 'axios'
  import replyApi from "@/api/reply"
  import {toTime, toTimes} from '@/utils/publicMethod'
  import attenterApi from '@/api/attenter'
  import messageApi from '@/api/message'
  import reportApi from '@/api/report'
  import MessageBtn from "./MessageBtn";
  import {messageBtnBridge} from "@/utils/publicMethod";

  export default {
    data() {
      return {
        replyListTotal: 0,
        replyList: {},
        currentPageNo: 1,
        loadSpin: false,
        replyShowWay: [
          {
            id: "0",
            value: "default",
            label: '默认',
          },
          {
            id: "1",
            value: "floorTh",
            label: '楼层',
          },
          {
            id: "2",
            value: "random",
            label: '随机',
          },
        ],
        type: {
          typeId: "1",
          orderBy: "floorTh",
        },
        reply: {
          body: "",
          postId: '',
          toId: ''
        },
      };
    },
    asyncData() {
    },
    props: [
      'postId'
    ],
    components: {
      MessageBtn
    },
    created() {
      replyApi.searchFloorTh(1, 12, {postId: this.postId}).then(res => {
        if (res.data.flag) {
          for (let i = 0; i < res.data.data.rows.length; i++) {
            res.data.data.rows[i]['reply']['createTime'] = toTime(res.data.data.rows[i]['reply']['createTime'])
          }
          this.reply.postId = this.postId;
          this.replyList = res.data.data.rows;
          this.replyListTotal = res.data.data.total;
        }
      })
    },
    resetType: function () {
      this.type.orderBy = "floorTh";
      this.currentPageNo = 1;
    },
    methods: {
      toThisOrder(value) {
        this.resetType();
        this.type.orderBy = value;
        for (let i = 0; i < this.replyShowWay.length; i++) {
          if (value === this.replyShowWay[i]['value']) {
            this.type.typeId = this.replyShowWay[i]['id'];
            break;
          }
        }

        if (value === "default") {
          replyApi.findByPostIdDefault(this.replyList.postId)
            .then(res => {
              let str = '';
              if (res.data.flag) {
                for (let i = 0; i < res.data.data.rows.length; i++) {
                  res.data.data.rows[i]['reply']['createTime'] = toTime(res.data.data.rows[i]['reply']['createTime']);
                  if (res.data.data.rows[i]['reply']['toId'] !== 0) {
                    // str
                  }
                }
                this.replyList = res.data.data.rows;
              }
            })
        } else if (value === "floorTh") {
          replyApi.searchFloorTh(1, 12, {postId: this.replyList.postId})
            .then(res => {
              if (res.data.flag) {
                for (let i = 0; i < res.data.data.rows.length; i++) {
                  res.data.data.rows[i]['reply']['createTime'] = toTime(res.data.data.rows[i]['reply']['createTime'])
                }
                this.replyList = res.data.data.rows;
              }
            })
        } else if (value === "random") {
          replyApi.findRandom(12, {postId: this.replyList.postId})
            .then(res => {
              if (res.data.flag) {
                for (let i = 0; i < res.data.data.rows.length; i++) {
                  res.data.data.rows[i]['reply']['createTime'] = toTime(res.data.data.rows[i]['reply']['createTime'])
                }
                this.replyList = res.data.data.rows;
              }
            })
        }
      },
      searchFloorTh(currentPageNo) {
        this.loadSpin = !this.loadSpin;
        replyApi.searchFloorTh(currentPageNo, 12, {postId: this.reply.postId})
          .then(res => {
            if (res.data.flag) {
              for (let i = 0; i < res.data.data.rows.length; i++) {
                res.data.data.rows[i]['reply']['createTime'] = toTime(res.data.data.rows[i]['reply']['createTime'])
              }
              this.replyList = res.data.data.rows;
              this.loadSpin = !this.loadSpin;
            }
          })
      },
      toThisPage(value) {
        this.currentPageNo = value;
        this.searchFloorTh(value);
      },
      publishThis() {
        this.reply.body = this.reply.body.replace("\"", "\\\"");
        replyApi.save(this.reply)
          .then(res => {
            if (res.data.flag) {
              this.$Message.success(res.data.message);
            } else {
              this.$Message.error(res.data.message);
            }
          })
      },
      messageBtnBridge,
    }
  }
</script>

<style scoped>
  .replyArea .replyBody {
    margin: 1% 0;
    color: #000;
  }

  .replyArea .downRight div {
    display: inline;
    float: right;
    margin-left: 1%;
  }

  .replyArea .downRight {
    margin-bottom: 1%;
  }

  .replyArea .replyMain {
    padding: 1%;
  }

</style>
