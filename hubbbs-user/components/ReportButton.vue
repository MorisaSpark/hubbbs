<template>
  <div style="display: inline-block">
    <Button type="text" @click="sendReport">举报</Button>
    <Modal
      v-model="mesMain.mesPane" title="填写内容" @on-ok="ok" @on-cancel="cancel">
      <Input v-model="mesMain.mesPaneBody" type="textarea" :autosize="{minRows: 2,maxRows: 5}"
             placeholder="Enter something..."></Input>
    </Modal>
  </div>
</template>

<script>
  import reportApi from '@/api/report'

  export default {
    name: "ReportButton",
    props: ['reportProps'],
    data() {
      return {
        mesMain: {
          mesPane: false,
          mesType: "",
          mesPaneBody: "",
        },
        reportMes: {
          thing: "",
          thingId: "",
          reason: "",
        },
      }
    },
    methods: {
      sendReport() {
        this.mesMain.mesType = "report";
        this.mesMain.mesPane = true;
      },
      ok() {
        this.$Message.info('Clicked ok');
        this.reportMes.thing = this.reportProps.thing;
        this.reportMes.thingId = this.reportProps.thingId;
        this.reportMes.reason = this.mesMain.mesPaneBody;
        reportApi.save(this.reportMes)
          .then(res => {
            if (res.data.flag) {
              this.$Message.success(res.data.message);
              this.mesMain.mesPane = false;
              this.mesMain.mesPaneBody = "";
            } else {
              this.$Message.error(res.data.message);
            }
          })
      },
      cancel() {
        this.$Message.info('Clicked cancel');
      },

    },
  }
</script>

<style scoped>

</style>
