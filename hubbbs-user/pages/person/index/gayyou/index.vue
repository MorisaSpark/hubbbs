<template>
  <div class="gayyouPane">
    <Card>
      <div slot="title">
        <label>
          <Select v-model="type.value">
            <Option v-for="item in attenterShowWay" @click.native="toThisType(item)"
                    :value="item.value" :key="item.value">{{ item.label }}
            </Option>
          </Select>
        </label>
      </div>
      <div>
        <div class="messageMain" v-for="(item, index) in attenterList" :key="index">
          <card>
            <Row>
              <Col span="4">
                <router-link :to="'/person/user/'+(type.value==='fan'?item.attenterFanId:item.attenterUserId)">
                  <img style="width: 100%" :src="item.userIcon" alt="狗头">
                </router-link>
              </Col>
              <Col span="14">
                <div style="margin: 10px">
                  <router-link :to="'/person/user/'+(type.value==='fan'?item.attenterFanId:item.attenterUserId)">
                    {{item.userNickname}}
                  </router-link>
                  LV{{item.userGrade}}
                </div>
                <div style="margin: 10px">{{item.userSummary}}</div>
                <Row style="margin: 10px">
                  <Col span="4">发帖：{{item.userPostNum}}</Col>
                  <!--<Col span="4">关注{{}}</Col>-->
                  <Col span="4">粉丝：{{item.userFansNum}}</Col>
                </Row>
              </Col>
              <Col span="4">
                <Row>
                  <Col style="text-align: center" span="8">
                    <Button type="primary" @click="sendMesToCenter('message','people',item.userId)">私信</Button>
                    <Button type="text" @click="sendMesToCenter('report','user',item.userId)">举报</Button>
                  </Col>
                </Row>
              </Col>
            </Row>
          </card>
        </div>
        <Page :total="total" show-elevator @on-change="toThisPage" :current="currentPage" :page-size="pageSize"
              style="margin: 10px 0"></Page>
      </div>
    </Card>
    <Modal
      v-model="mesCenter.isShow"
      title="填写内容"
      @on-ok="ok"
      @on-cancel="cancel">
      <Input v-model="mesCenter.body" type="textarea" :autosize="{minRows: 2,maxRows: 5}"
             placeholder="Enter something..."></Input>
    </Modal>
  </div>
</template>

<script>
  import attenterApi from "@/api/attenter"
  import messageApi from "@/api/message"
  import reportApi from "@/api/report"
  import {toTimes} from "@/utils/publicMethod"

  export default {
    name: "index",
    data() {
      return {
        mesMain: {
          mesPane: false,
          mesType: "",
          mesPaneBody: "",
        },
        attenterShowWay: [
          {
            id: 0,
            label: '关注',
            value: 'attenter',
          },
          {
            id: 1,
            label: '粉丝',
            value: 'fan',
          }
        ],
        currentPage: 1,
        pageSize: 20,
        type: {
          typeId: "0",
          value: "attenter",
        },
        attenterBtn: false,
        attenterList: {},
        mesCenter: {
          isShow: false,
          type: "",
          body: "",
          report: {
            thing: "",
            thingId: "",
            reason: "",
          },
          message: {
            body: "",
            receiverId: "",
          }
        }
      }
    },
    asyncData() {
      return attenterApi.searchByTypeSelf(1, 20, {value: 'attenter'})
        .then(res => {
          res.data.data.rows = toTimes(res.data.data.rows, 'attenterTime');
          console.log(res.data.data.rows);
          return {
            total: res.data.data.total,
            attenterList: res.data.data.rows,
          }
        })
    },
    methods: {
      toThisType(value) {
        this.type.value = this.attenterShowWay[value.id]['value'];
        this.currentPage = 1;
        this.thisType();
      },
      toThisPage(value) {
        this.currentPage = value;
        this.thisType();
      },
      thisType() {
        attenterApi.searchByTypeSelf(this.currentPage, this.pageSize, {value: this.type.value})
          .then(res => {
            this.total = res.data.data.total;
            this.attenterList = res.data.data.rows;
          });
      },
      sendMesToCenter(messageType, messageTable, messageId) {
        this.mesCenter.isShow = true;
        this.mesCenter.type = messageType;
        if(messageTable!=="people"){
          this.mesCenter.report.thing = messageTable;
          this.mesCenter.report.thingId = messageId;
        }else{
          this.mesCenter.message.receiverId = messageId;
        }
      },
      ok() {
        let pojo = {};
        this.$Message.info('Clicked ok');
        if (this.mesCenter.type === 'report') {
          this.mesCenter.report.reason = this.mesCenter.body;
          pojo = this.mesCenter.report;
          console.log(pojo);
          reportApi.save(pojo)
            .then(res => {
              if (res.data.flag) {
                this.$Message.success(res.data.message);
                this.mesCenter.isShow = false;
                this.mesCenter.body = "";
                this.mesCenter.report.reason = "";
              } else {
                this.$Message.error(res.data.message);
              }
            })
        } else {
          this.mesCenter.message.body = this.mesCenter.body;
          pojo = this.mesCenter.message;
          messageApi.save(pojo)
            .then(res => {
              if (res.data.flag) {
                this.$Message.success(res.data.message);
                this.mesCenter.isShow = false;
                this.mesCenter.body = "";
                this.mesCenter.message.body = "";
              } else {
                this.$Message.error(res.data.message);
              }
            })
        }
      },
      cancel() {
        this.$Message.info('Clicked cancel');
      },
    },
  }
</script>

<style scoped>

</style>
