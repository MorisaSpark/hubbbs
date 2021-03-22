<template>
  <div style="display:inline-block">
    <Button :type="messageBtnBridge.btnType"
            v-on:click="sendMesToCenter(messageBtnBridge.messageType, messageBtnBridge.messageTable, messageBtnBridge.messageId)">
      {{messageBtnBridge.btnName}}
    </Button>
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
  import axios from 'axios'
  import replyApi from "@/api/reply"
  import {toTime, toTimes} from '@/utils/publicMethod'
  import attenterApi from '@/api/attenter'
  import messageApi from '@/api/message'
  import reportApi from '@/api/report'

  export default {
    name: "MessageBtn",
    props: ['messageBtnBridge'],
    data() {
      return {
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
    methods: {
      sendMesToCenter(messageType, messageTable, messageId) {
        this.mesCenter.isShow = true;
        this.mesCenter.type = messageType;
        if (messageTable !== "people") {
          this.mesCenter.report.thing = messageTable;
          this.mesCenter.report.thingId = messageId;
        } else {
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
        console.log(this.messageBtnBridge.btnType);
        this.$Message.info('Clicked cancel');
      }
    }
  }
</script>

<style scoped>

</style>
