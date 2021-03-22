<template>
  <div>
    <div  @mouseleave="sendHtml" ref="editor" style="text-align:left"></div >
    <!--<button v-on:click="getContent">查看内容</button>-->
  </div>
</template>

<script>
  import E from 'wangeditor'
  import {uploadPicUrl} from '@/utils/publicConst'
  import service from '@/utils/request'

  export default {
    name: 'editor',
    props: ['editorMes'],
    data() {
      return {
        editorContent: ''
      }
    },
    methods: {
      sendHtml() {
        this.$emit("getHtml", this.editorContent)
      }
    },
    mounted() {
      var editor = new E(this.$refs.editor);
      editor.customConfig.onchange = (html) => {
        this.editorContent = html
      };
      editor.customConfig.menus = this.editorMes.menus;
      editor.customConfig.uploadImgServer = uploadPicUrl;
      editor.customConfig.uploadFileName = 'file';
      editor.customConfig.zIndex = 20;
      editor.create();
    }
  }
</script>

<style scoped>
</style>
