<template>
  <div class="publishPane" :style="{ background: '#fff', minHeight: '500px'}">
    <Row style="z-index: 49">
      <Col span="5">
        <Select v-model="post.typeNickname" size="large" @on-change="getTypeMes" :label-in-value="true">
          <Option v-for="item in typeList" :value="item.id" :key="item.id">{{ item.nickname }}</Option>
        </Select>
      </Col>
      <Col span="19" style="height:100%;">
        <Input v-model="post.title" placeholder="在此输入标题"></Input>
      </Col>
    </Row>
    <WangEditor :editorMes="editorMes" v-on:getHtml="getHtml"></WangEditor>
    <Button type="primary" @click="publishThis">发帖</Button>
  </div>
</template>

<script>
  import WangEditor from '@/components/WangEditor'
  import typeApi from '@/api/type'
  import postApi from '@/api/post'

  export default {
    data() {
      return {
        post: {
          typeId: '',
          typeNickname: "",
          title: '',
          body: "",
        },
        editorMes:
          {
            menus: [
              // 'head',  // 标题
              'bold',  // 粗体
              'fontSize',  // 字号
              'fontName',  // 字体
              'italic',  // 斜体
              'underline',  // 下划线
              'strikeThrough',  // 删除线
              'foreColor',  // 文字颜色
              // 'backColor',  // 背景颜色
              'link',  // 插入链接
              'list',  // 列表
              'justify',  // 对齐方式
              'quote',  // 引用
              'emoticon',  // 表情
              'image',  // 插入图片
              'table',  // 表格
              // 'video',  // 插入视频
              'code',  // 插入代码
              'undo',  // 撤销
              'redo'  // 重复
            ]
          },
      }
    },
    asyncData() {
      return typeApi.getList().then(res => {
        console.log(res.data.data);
        return {typeList: res.data.data}
      })
    },
    components: {
      WangEditor
    },
    methods: {
      publishThis() {
        // this.post.body = this.post.body.replace("\"", "\\\"");
        postApi.saveSelf(this.post)
          .then(res => {
            if (res.data.flag) {
              //保存用户信息
              this.$Message.success(res.data.message);
              window.location.href = '/';
            } else {
              this.$Message.error(res.data.message);
            }
          })
      },
      getTypeMes(data) {  //lable:体育 value:22
        this.post.typeId = data.value;
      },
      getHtml(data) {
        this.post.body = data;
      }
    }
  }
</script>

<style scoped>
  .publishPane {
    margin: 0 20px;
    z-index: 50;
  }

  .publishPane .gkd {
    margin: 4% 15%;
    width: 10%;
    height: 5em;
    font-size: 18px;
    box-sizing: border-box;
  }
</style>
