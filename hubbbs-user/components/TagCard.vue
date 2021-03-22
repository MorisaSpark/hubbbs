<template>
  <div style="margin: 2% 0;">
    <Tag v-for="item in tagPart.tagList" :key="item" :name="item" closable @on-close="deleteTag">{{ item }}
    </Tag>
    <Button icon="ios-add" type="dashed" size="small" @click="addTag">添加标签</Button>
    <Modal
      v-model="tagPart.addTagPaneIsShow"
      @on-ok="tagOk"
      @on-cancel="tagCancel">
      <div>
        <Input v-model="tagPart.newTag" placeholder="Enter Tag..." style="width: 300px" long></Input>
      </div>
    </Modal>
  </div>
</template>

<script>
  import relTagApi from '@/api/reltag'
  import {resMessage} from '@/utils/publicMethod'

  export default {
    name: "TagCard",
    props: ["tagCardMes"],
    data() {
      return {
        tagPart: {
          addTagPaneIsShow: false,
          tagList: [],
          newTag: '',
        },
      }
    },
    created(){
      relTagApi.findByPostId(this.tagCardMes.postId)
        .then(res=>{
          this.tagPart.tagList = res.data.data;
        })
    },
    methods: {
      addTag() {
        this.tagPart.addTagPaneIsShow = true;
      },
      deleteTag(event, name) {
        const index = this.tagPart.tagList.indexOf(name);
        relTagApi.deleteInPost(this.tagCardMes.postId, name).then(res => {
          if (res.data.flag) {
            this.tagPart.tagList.splice(index, 1);
            this.$Message.success(res.data.message);
          } else {
            this.$Message.error(res.data.message);
          }
        })
      },
      tagOk() {
        relTagApi.saveInPost(this.tagCardMes.postId, this.tagPart.newTag,).then(res => {
          if (res.data.flag) {
            this.tagPart.tagList.push(this.tagPart.newTag);
            this.tagPart.newTag = '';
            this.$Message.success(res.data.message);
          } else {
            this.$Message.error(res.data.message);
          }
        })
      },
      tagCancel() {
        this.tagPart.addTagPaneIsShow = false;
      },
    }
  }
</script>

<style scoped>

</style>
