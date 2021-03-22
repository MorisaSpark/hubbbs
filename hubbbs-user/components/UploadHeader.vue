<template>
  <div class="updateHeaderPane">
    <Upload
      multiple
      type="drag"
      :action="uploadUrl" :on-success="uploadSuccess" class="upload" :show-upload-list="false" :header="header">
      <div style="height: 185px;box-sizing: border-box;padding: 15px 0;">
        <img
          :src="icon"
          alt="图片"
          style="height: 100%;">
        <div class="editHead">修改我的头像</div>
      </div>
    </Upload>
  </div>
</template>
<script>
  import {getUser, setThing} from '@/utils/auth'
  import request from '@/utils/request'
  import userApi from '@/api/user'
  import {Authorization,uploadPicUrl} from '@/utils/publicConst'

  export default {
    data() {
      return {
        icon: "",
        uploadUrl: {},
        list: {},
        header: {},
      }
    },
    methods: {
      updateIcon(res) {
        userApi.saveSelf({icon: res['data'][0]})
          .then(res => {
            if (res.data.flag) {
              setThing('icon', this.icon);
              this.$Message.success(res.data.message);
            } else {
              this.$Message.error(res.data.message);
            }
          })
      },
      uploadSuccess(res, file, fileList) { // 文件上传回调 上传成功后删除待上传文件
        if(res['errno']===0){
          this.icon = res['data'][0];
          this.updateIcon(res);
        }
      },
    },
    mounted() {
    },
    created() {
      this.icon = getUser().icon;
      this.uploadUrl = uploadPicUrl;
      this.header = {'Authorization': Authorization}
    },
    asyncData() {
      return {};
    }
  }
</script>
<style>
  .updateHeaderPane .upload {
    /*width: 500px;*/
    /*height: 500px;*/
  }

  .demo-upload-list img {
    width: 100%;
    height: 100%;
  }

  .demo-upload-list-cover i {
    color: #fff;
    font-size: 20px;
    cursor: pointer;
    margin: 0 2px;
  }

  .updateHeaderPane .editHead {
    position: absolute;
    top: 0px;
    right: 0px;
    width: 100%;
    height: 100%;
    background: rgba(1, 1, 1, 0.3);
    text-align: center;
    line-height: 188px;
    color: #fff;
  }
</style>
