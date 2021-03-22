<template>
  <div class="messagePane">
    <Card>
      <div slot="title">
        <label>
          <Select v-model="showWay.label" :label-in-value="true">
            <Option v-for="item in messageShowWay" @click.native="toThisType(item)"
                    :value="item.value" :key="item.value">{{item.label }}
            </Option>
          </Select>
        </label>
      </div>
      <div>
        <div class="messageMain" v-for="(item, index) in messageList" :key="index">
          <card>
            <div slot="title" style="height: 35px;">
              <router-link :to="'/person/user/'+ item.userId" style="margin-right: 15px">
                <span><img style="height: 100%;" :src="item.userIcon" alt="头像"></span>
                <span>{{item.userNickname}}</span>
              </router-link>
              <span style="margin-right: 15px">{{item.messageSendTime}}</span>
              <Button type="text" style="float: right;color: #7f828b"
                      @click.native="sendMesToCenter('report','message',item.messageId)">
                举报
              </Button>
            </div>
            <div>
              {{item.messageBody}}
            </div>
          </card>
        </div>
        <Page :total="messageList.total" show-elevator @on-change="toThisPage" :current="mesPojo.currentPage"
              :page-size="20"></Page>
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
  import messageApi from "@/api/message"
  import reportApi from "@/api/report"
  import {getUser} from '@/utils/auth'
  import {toTimes} from "@/utils/publicMethod";

  export default {
    data() {
      return {
        mesPojo: {
          currentPage: 1,
          pageSize: 20,
        },
        showWay: {
          id: 1,
          value: 'receive',
        },
        messageList: {},
        currentPageNo: 1,
        messageShowWay: [
          {
            id: 1,
            label: '已接收',
            value: 'receive',
          },
          {
            id: 0,
            label: '已发送',
            value: 'send',
          }
        ],
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
      return messageApi.searchByTypeSelf(1, 20, {value: "send"})
        .then(messageList => {
          messageList.data.data.rows = toTimes(messageList.data.data.rows, 'messageSendTime');
          return {
            total: messageList.data.data.total,
            messageList: messageList.data.data.rows,
          }
        });
    },
    methods: {
      thisType() {
        messageApi.searchByTypeSelf(this.mesPojo.currentPage, this.mesPojo.pageSize, {value: this.showWay.value})
          .then(res => {
            res.data.data.rows = toTimes(res.data.data.rows, 'messageSendTime');
            this.messageList = res.data.data.rows;
          });
      },
      toThisType(value) {
        this.showWay.value = this.messageShowWay[value.id]['value'];
        this.mesPojo.currentPage = 1;
        this.thisType();
      },
      toThisPage(value) {
        this.mesPojo.currentPage = value;
        this.thisType();
      },
      sendMesToCenter(messageType,messageTable,messageId) {
        this.mesCenter.isShow = true;
        this.mesCenter.type = messageType;
        this.mesCenter.report.thing = messageTable;
        this.mesCenter.report.thingId = messageId;
      },
      ok() {
        let pojo = {};
        this.$Message.info('Clicked ok');
        if (this.mesCenter.type==='report'){
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
        } else{
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
    components: {}
  }
</script>

<style scoped>

</style>
