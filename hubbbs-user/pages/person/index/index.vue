<template>
  <div class="indexPane">
    <Card>
      <div slot="title">
        <Row :gutter="16">
          <Col span="5">
            <UploadHeader></UploadHeader>
          </Col>
          <Col span="17" class="mes">
            <Row>
              <Col span="8">用户名：{{userDetail.nickname}}</Col>
              <Col span="8">用户id: {{userDetail.id}}</Col>
            </Row>
            <Row>
              <Col span="8">等级: LV{{userDetail.grade}}</Col>
              <Col span="8">饼干：{{userDetail.cookieNum}}</Col>
            </Row>
            <Row>
              <Col span="10">用户创建: {{userDetail.createTime}}</Col>
              <Col span="10">最近登录: {{userDetail.lastLoginTimeB}}</Col>
            </Row>
          </Col>
        </Row>
      </div>
      <div>
        <Collapse simple>
          <Panel name="1">
            用户信息
            <Form slot="content" label-position="right" :label-width="100">
              <FormItem label="昵称">
                <label>
                  <Input v-model="userFormNickname"></Input>
                </label>
              </FormItem>
              <FormItem label="简介">
                <label>
                  <Input v-model="userFormSummary"></Input>
                </label>
              </FormItem>
              <FormItem label="性别">
                <Select v-model="userFormSex" style="width:200px">
                  <Option v-for="item in sexList" :value="item.id" :key="item.id">{{ item.label }}</Option>
                </Select>
              </FormItem>
              <FormItem label="学校">
                <label>
                  <Input v-model="userFormCollege"></Input>
                </label>
              </FormItem>
              <FormItem label="专业">
                <label>
                  <Input v-model="userFormMajor"></Input>
                </label>
              </FormItem>
              <FormItem>
                <Button type="primary" @click="handleSubmit('userFormDetail')">保存</Button>
                <!--<Button @click="handleReset('userFormReset')" style="margin-left: 8px">重置</Button>-->
              </FormItem>
            </Form>
          </Panel>
          <Panel name="2">
            账号信息
            <Form slot="content" label-position="right" :label-width="100">
              <FormItem label="手机号">
                <label>
                  <Input v-model="accountFormUsername" disabled></Input>
                </label>
              </FormItem>
              <FormItem label="旧密码">
                <label>
                  <Input v-model="accountFormOldPassword"></Input>
                </label>
              </FormItem>
              <FormItem label="新密码">
                <label>
                  <Input v-model="accountFormNewPassword"></Input>
                </label>
              </FormItem>
              <FormItem>
                <Button type="primary" @click="handleSubmit('accountFormPass')">保存</Button>
                <!--<Button @click="handleReset('accountFormReset')" style="margin-left: 8px">重置</Button>-->
              </FormItem>
            </Form>
            <Form slot="content" label-position="right" :label-width="100">
              <FormItem label="新手机号">
                <label>
                  <Input v-model="accountFormUsername"></Input>
                </label>
              </FormItem>
              <FormItem label="验证码(原)">
                <label>
                  <Input v-model="accountFormCode"></Input>
                </label>
              </FormItem>
              <FormItem>
                <!--<Button @click="handleReset('accountFormReset')" style="margin-left: 8px">重置</Button>-->
                <Button type="primary" @click="handleSubmit('accountFormCode')">第一步.发送验证码</Button>
                <Button type="primary" @click="handleSubmit('accountFormPhone')">第二步.修改手机号</Button>
              </FormItem>
            </Form>
          </Panel>
        </Collapse>
      </div>
    </Card>
  </div>
</template>

<script>
  import UploadHeader from '@/components/UploadHeader'
  import userApi from '@/api/user'
  import accountApi from '@/api/account'
  import axios from 'axios'
  import {toTime} from "@/utils/publicMethod";
  import {setThing} from "@/utils/auth";

  export default {
    data() {
      return {
        sexList: [
          {
            id: 1,
            value: "man",
            label: "男",
          },
          {
            id: 0,
            value: "woman",
            label: "女",
          },
          {
            id: -1,
            value: "woman",
            label: "未知",
          }
        ],
        pojo: {},
        postDetail: {},

        userFormNickname: "",
        userFormSex: "",
        userFormCollege: "",
        userFormMajor: "",
        userFormSummary: "",

        accountFormUsername: "",
        accountFormOldPassword: "",
        accountFormNewPassword: "",
        accountFormCode: '',

        identityNet: "",
        identityUsername: "",
        identityPassword: "",
      }
    },
    components: {
      UploadHeader
    },
    asyncData() {
      return axios.all([userApi.findUserDetailSelf(), accountApi.findAccountDetailSelf()])
        .then(axios.spread(function (userDetail, accountDetail) {
          userDetail.data.data.createTime = toTime(userDetail.data.data.createTime);
          userDetail.data.data.lastLoginTimeB = toTime(userDetail.data.data.lastLoginTimeB);
          return {
            userDetail: userDetail.data.data,
            accountDetail: accountDetail.data.data,

            userFormNickname: userDetail.data.data.nickname,
            userFormSex: userDetail.data.data.sex,
            userFormCollege: userDetail.data.data.college,
            userFormMajor: userDetail.data.data.major,
            userFormSummary: userDetail.data.data['summary'],

            accountFormUsername: accountDetail.data.data.username,
          }
        }))
    },
    methods: {
      handleSubmit(name) {
        this.pojo = {};
        console.log(name);
        console.log(name === 'accountFormPass');
        if (name === 'userFormDetail') {
          this.pojo.nickname = this.userFormNickname;
          this.pojo.sex = this.userFormSex;
          this.pojo.college = this.userFormCollege;
          this.pojo.major = this.userFormMajor;
          this.pojo.summary = this.userFormSummary;
          userApi.updateSelf(this.pojo)
            .then(res => {
              if (res.data.flag) {
                setThing("nickname", this.pojo.nickname);
                this.userDetail.nickname = this.pojo.nickname;
                this.$Message.success('Success!');
              } else {
                this.$Message.error('Fail!');
              }
            });
        } else if (name === 'accountFormPass') {
          this.pojo.accountFormOldPassword = this.accountFormOldPassword;
          this.pojo.accountFormNewPassword = this.accountFormNewPassword;
          accountApi.updatePassword(this.pojo)
            .then(res => {
              if (res.data.flag) {
                this.$Message.success('Success!');
              } else {
                this.$Message.error('Fail!');
              }
            });
        } else if (name === 'accountFormCode') {
          this.pojo.accountFormUsername = this.accountFormUsername;
          accountApi.sendSMS(this.accountFormUsername)
            .then(res => {
              if (res.data.flag) {
                this.$Message.success('验证码发送成功!');
              } else {
                this.$Message.error('验证码发送失败!');
              }
            });
        } else {
          this.pojo.accountFormUsername = this.accountFormUsername;
          accountApi.updateUsername(this.pojo, this.accountFormCode)
            .then(res => {
              if (res.data.flag) {
                this.$Message.success('Success!');
              } else {
                this.$Message.error('Fail!');
              }
            });
        }
      },
      handleReset(name) {
        if (name === 'userFormDetail') {
          this.$Message.success('Success!');
        } else {
          this.$Message.error('Fail!');
        }
      }
    }
  }
</script>

<style scoped>
  .indexPane .mes .ivu-col {
    font-size: 12px;
    padding: 10px;
  }
</style>
