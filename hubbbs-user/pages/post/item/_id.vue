<style scoped>
  .layout {
    border: 1px solid #d7dde4;
    background: #f5f7f9;
    position: relative;
    border-radius: 4px;
    overflow: hidden;
  }

  .layout-logo {
    width: 100px;
    height: 30px;
    background: #5b6270;
    border-radius: 3px;
    float: left;
    position: relative;
    top: 15px;
    left: 20px;
  }

  .layout-nav {
    width: 420px;
    margin: 0 auto;
    margin-right: 20px;
  }

  .layout-footer-center {
    text-align: center;
  }

  .rightCards .ivu-card-bordered {
    margin-bottom: 5%;
  }

  .rightNum {
    text-align: center;
    font-size: 20px;
    color: red;
  }

  .rightNum .down {
    color: rgba(0, 0, 0, 0.38);
    font-size: 12px;
  }

  .rightCards .rightOpr {
    text-align: center;
    margin-top: 5%;
  }

  .rightCards .rightOpr button {
    width: 64%;
  }
</style>
<template>
  <Row class="layout">
    <Col span="17"
         :style="{padding: '24px', minHeight: '800px', background: '#fff',marginRight: '3%',}"
         :on-reach-bottom="isShowReplyArea = true">
      <Card dis-hover class="postCard">
        <Row style=" font-size: 10px; color: #b0a5a5;" slot="title">
          <div style="font-size: 20px;font-weight:700;padding: 3% 0;color: #000;">{{postDetail.postTitle}}</div>
          <Row>
            <Col span="4" style=" font-size: 10px; color: #b0a5a5;">类别：{{postDetail.typeNickname}}</Col>
            <Col span="4">UP：{{postDetail.userNickname}}</Col>
            <Col span="4">{{postDetail.postCreateTime}}</Col>
            <Col span="4">查看：{{postDetail.postClickNum}}</Col>
            <Col span="4">
              <Poptip :disabled="cookieSure" word-wrap width="80">
                <img style="width: 10px;" :src="publicTencent+(postDetail.isCookie?imgCookiesTrue:imgCookiesFalse)"
                     alt="饼干真好吃">
                饼干：{{postDetail.postCookieNum}}
                <div slot="content" style="text-align: center">
                  <div>确定喂食?</div>
                  <Button type="dashed" @click.native="giveCookie">确定</Button>
                </div>
              </Poptip>
            </Col>
            <Col span="4" @click.native="collectionToggle">
              <Icon :type="postDetail.isCollection?collectionTrue:collectionFalse"></Icon>
              收藏：{{postDetail.postCollectionNum}}
            </Col>
          </Row>
        </Row>
        <div style="margin: 2% 0; color: #000;" class="postMain" v-html="postDetail.postBody">
        </div>
        <Divider></Divider>
        <div>
          <div style="">
            <Row type="flex" justify="end">
              <Col span="2" style="text-align: center;font-size: 12px;line-height: 32px;">分享</Col>
              <Col span="2">
                <MessageBtn
                  :messageBtnBridge="messageBtnBridge('举报','text','report','post',postDetail.postId)"></MessageBtn>
              </Col>
            </Row>
            <Divider></Divider>
            <TagCard :tagCardMes="tagCardMes"></TagCard>
          </div>
          <Divider></Divider>
          <div style="margin: 2% 0px;">
            发表评论
          </div>
          <div>
            <WangEditor v-if="isShowWangEditor" :editorMes="editorMes" v-on:getHtml="getHtml"></WangEditor>
            <Button type="success" @click="publishThis">发布评论</Button>
          </div>
          <Divider></Divider>
          <ReplyArea v-if="isShowReplyArea" :postId="postDetail.postId"></ReplyArea>
        </div>
      </Card>
    </Col>
    <Col span="6" hide-trigger :style="{background: '#f5f7f9',}" class="rightCards">
      <Card>
        <Row slot="title">
          <Col span="7">
            <router-link :to="'/person/user/'+postDetail.userId">
              <img :src="postDetail.userIcon" style="width: 100%;border-radius: 50%;"
                   alt="icon">
            </router-link>
          </Col>
          <Col span="17" style="float:left;">
            <div style="margin: 10px 0">
              <router-link :to="'/person/user/'+postDetail.userId">
                <span>{{postDetail.userNickname}}</span>
              </router-link>
              <span style="margin: 0 10px">LV{{postDetail.userGrade}}</span>
            </div>
            <div style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;">
              {{postDetail.userSummary}}
            </div>
          </Col>
        </Row>
        <Row>
          <Col span="8" class="rightNum">
            <div>{{postDetail.userPostNum}}</div>
            <div class="down">投稿</div>
          </Col>
          <Col span="8" class="rightNum">
            <div>{{postDetail.userAttenterNum}}</div>
            <div class="down">关注</div>
          </Col>
          <Col span="8" class="rightNum">
            <div>{{postDetail.userFansNum}}</div>
            <div class="down">粉丝</div>
          </Col>
        </Row>
        <Row class="rightOpr">
          <Col style="text-align: center" span="8">
            <Button type="primary" @click="incAttention" v-if="!attenterBtn">关注</Button>
            <Button type="dashed" @click="decAttention" v-if="attenterBtn">关注中</Button>
          </Col>
          <Col style="text-align: center" span="8">
            <MessageBtn
              :messageBtnBridge="messageBtnBridge('私信','dashed','message','people',postDetail.userId)"></MessageBtn>
          </Col>
          <Col style="text-align: center" span="8">
            <!--<ReportButton v-bind:reportProps="reportProps"></ReportButton>-->
            <MessageBtn
              :messageBtnBridge="messageBtnBridge('举报','text','report','user',postDetail.userId)"></MessageBtn>
          </Col>
        </Row>
      </Card>
      <Card>
        <p slot="title">UP最新文章</p>
        <p v-for="item in upLastPostList" :value="item.value" :key="item.value" style="margin-bottom: 5px">
          <nuxt-link :to="'/post/item/'+item.id">{{item.title}}</nuxt-link>
        </p>
      </Card>
      <Card>
        <p slot="title">相关标签文章</p>
        <p v-for="item in relateTagPostList" :value="item.value" :key="item.value" style="margin-bottom: 5px">
          <nuxt-link :to="'/post/item/'+item.id">{{item.title}}</nuxt-link>
        </p>
      </Card>
      <Card>
        <p slot="title">相关类型文章</p>
        <p v-for="item in relateTypePostList" :value="item.value" :key="item.value" style="margin-bottom: 5px">
          <nuxt-link :to="'/post/item/'+item.id">{{item.title}}</nuxt-link>
        </p>
      </Card>
    </Col>
  </Row>
</template>

<script>
  import postApi from '@/api/post'
  import replyApi from '@/api/reply'
  import relCollecionApi from '@/api/relcollection'
  import cookieApi from '@/api/cookie'
  import axios from 'axios'
  import attenterApi from '@/api/attenter'
  import messageApi from '@/api/message'
  import reportApi from '@/api/report'
  import ReplyArea from '@/components/ReplyArea'
  import LoadingV from '@/components/LoadingV'
  import WangEditor from '@/components/WangEditor'
  import {toTime, toTimes, messageBtnBridge, resMessage} from '@/utils/publicMethod'
  import ReportButton from '@/components/ReportButton'
  import MessageBtn from "@/components/MessageBtn";
  import {publicTencent, imgCookiesTrue, imgCookiesFalse} from "@/utils/publicConst";
  import TagCard from "@/components/TagCard"

  export default {
    data() {
      return {
        cookieSure: true,
        publicTencent,
        imgCookiesTrue,
        imgCookiesFalse,
        attenterBtn: true,
        messageMes: {
          body: "",
          receiverId: "",
        },
        reportMes: {
          thing: "",
          thingId: "",
          reason: "",
        },
        replyList: [],
        isShowWangEditor: true,
        isShowReplyArea: true,
        postId: '',
        cookieIcon: "",
        collectionFalse: "ios-star-outline",
        collectionTrue: "ios-star",
        editorMes: {
          menus: [
            'bold',  // 粗体
            'fontSize',  // 字号
            'italic',  // 斜体
            'underline',  // 下划线
            'strikeThrough',  // 删除线
            'foreColor',  // 文字颜色
            'emoticon',  // 表情
            'image',  // 插入图片
            'code',  // 插入代码
            'undo',  // 撤销
            'redo'  // 重复
          ]
        },
        reply: {
          body: '',
          postId: ''
        },
      };
    },
    asyncData({params}) {
      return axios.all([
        postApi.findByIdDetail(params.id),
        postApi.search(1, 5, {id: params.id}),
        postApi.findByTag(1, 5, params.id),
        postApi.findByType(1, 5, params.id)
      ]).then(axios.spread(function (postDetail, upLastPostList, relateTagPostList, relateTypePostList) {
        if (postDetail.data.flag === false) {
          alert(postDetail.data.message);
        } else {
          postDetail.data.data["postCreateTime"] = toTime(postDetail.data.data["postCreateTime"]);
          return {
            reply: {
              postId: params.id,
            },
            postDetail: postDetail.data.data,
            userId: postDetail.data.data.userId,
            attenterBtn: postDetail.data.data.isAtter,
            postUserRelate: postApi.search(1, 5, {userId: postDetail.data.data.userId})
              .then(res => {
                return res.data.data.rows
              }),
            reportProps: {
              thing: "user",
              thingId: postDetail.data.data.userId,
            },
            tagCardMes: {
              postId: params.id,
            },
            upLastPostList: upLastPostList.data.data.rows,
            relateTagPostList: relateTagPostList.data.data === null ? [] : relateTagPostList.data.data.rows,
            relateTypePostList: relateTypePostList.data.data.rows,
            // }
          }
        }
      }));
    },
    methods: {
      messageBtnBridge,
      getHtml(data) {
        this.reply.body = data;
      },
      publishThis() {
        // this.reply.body = this.reply.body.replace("\"", "\\\"");
        replyApi.save(this.reply)
          .then(res => {
            if (res.data.flag) {
              //保存用户信息
              this.isShowReplyArea = false;
              this.isShowWangEditor = false;
              this.$Message.success(res.data.message);
              this.$nextTick(() => {
                this.isShowReplyArea = true;
                this.isShowWangEditor = true;
              });
            } else {
              this.$Message.error(res.data.message);
            }
          })
      },
      incAttention() {
        attenterApi.save({userId: this.userId})
          .then(res => {
            if (res.data.flag) {
              this.$Message.success(res.data.message);
              this.attenterBtn = true;
              this.postDetail.userFansNum++;
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
              this.attenterBtn = false;
              this.postDetail.userFansNum--;
            } else {
              this.$Message.error(res.data.message);
            }
          })
      },
      collectionToggle() {
        if (!this.postDetail.isCollection) {
          relCollecionApi.save({postId: this.postDetail.postId,})
            .then(res => {
              if (res.data.flag) {
                this.postDetail.postCollectionNum++;
                this.postDetail.isCollection = !this.postDetail.isCollection;
                this.$Message.success(res.data.message);
              } else {
                this.$Message.error(res.data.message);
              }
            });
        } else {
          relCollecionApi.deleteById(this.postDetail.postId)
            .then(res => {
              if (res.data.flag) {
                this.postDetail.postCollectionNum--;
                this.postDetail.isCollection = !this.postDetail.isCollection;
                this.$Message.success(res.data.message);
              } else {
                this.$Message.error(res.data.message);
              }
            });
        }
      },
      giveCookie() {
        if (this.postDetail.isCookie) {
          this.$Message.error("饼干喂过了！");
        } else {
          // this.postDetail.cookieIsCookie
          cookieApi.save({
            postId: this.postDetail.postId,
            summary: "喂食",
            variance: -1,
          }).then(res => {
            if (res.data.flag) {
              this.postDetail.postCookieNum++;
              this.postDetail.isCookie = true;
              this.cookieSure = false;
              this.$Message.success(res.data.message);
            } else {
              this.$Message.error(res.data.message);
            }
          })
        }
      }
    },
    components: {
      ReplyArea,
      WangEditor,
      LoadingV,
      ReportButton,
      MessageBtn,
      TagCard,
    }
  }
</script>

<style scoped>
  .replyShowWays {
    float: right;
  }

  .layout .postCard img {
    width: 100%;
  }

  .postMain >>> img {
    max-width: 100%;
  }
</style>
