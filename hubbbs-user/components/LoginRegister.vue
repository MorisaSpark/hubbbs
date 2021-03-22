<template>
  <div class="loginRegisterPane">
    <Button type="primary" @click="loginAlert = true">点此处 登录/注册</Button>
    <Modal
      v-model="loginAlert"
      title="登录"
      @on-ok="ok"
      @on-cancel="cancel('loginForm')"
      :footer-hide="true">
      <Form ref="loginForm" :model="loginForm" :rules="loginRule" :label-width="80">
        <FormItem label="手机号" prop="username">
          <Input type="text" v-model="loginForm.username" number></Input>
        </FormItem>
        <FormItem label="输入密码" prop="password">
          <Input type="password" v-model="loginForm.password"></Input>
        </FormItem>
        <FormItem>
          <Button type="primary" @click="handleSubmit('loginForm')">登录</Button>
        </FormItem>
        <FormItem>
          <Button type="primary" @click="toggleForm()">
            没有账号~点击注册
            <Icon type="ios-arrow-forward"></Icon>
          </Button>
        </FormItem>
      </Form>
    </Modal>
    <Modal
      v-model="registerAlert"
      title="注册"
      @on-ok="ok"
      @on-cancel="cancel('registerForm')"
      :footer-hide="true">
      <Form ref="registerForm" :model="registerForm" :rules="registerRule" :label-width="80">
        <FormItem label="手机号" prop="username">
          <Input type="text" v-model="registerForm.username" number></Input>
        </FormItem>
        <FormItem label="输入密码" prop="password">
          <Input type="password" v-model="registerForm.password"></Input>
        </FormItem>
        <FormItem label="确认密码" prop="passwordCheck">
          <Input type="password" v-model="registerForm.passwordCheck"></Input>
        </FormItem>
        <FormItem label="验证码" prop="code">
          <Input style="width: 70%;" type="text" v-model="registerForm.code"></Input>
          <Button style="width: 28%" type="primary" :loading="sendSMSFlag" @click="sendSMS">
            <span :disabled="sendSMSFlag">{{checkCodeCountDown}}</span>
          </Button>
        </FormItem>
        <FormItem>
          <Button type="primary" @click="handleSubmit('registerForm')">注册</Button>
          <Button @click="handleReset('registerForm')" style="margin-left: 8px">重置</Button>
        </FormItem>
        <FormItem>
          <Button type="primary" @click="toggleForm()">
            已有账号~点击登录
            <Icon type="ios-arrow-back"></Icon>
          </Button>
        </FormItem>
      </Form>
    </Modal>
  </div>
</template>
<style>
  .loginRegisterPane {
    padding: 0 25%;
    z-index: 100
  }
</style>
<script>
  import accountApi from '@/api/account';
  import {setUser, getUser} from '@/utils/auth'

  export default {
    data() {
      const validatePass = (rule, value, callback) => {
        console.log("validatePass");
        if (value === '') {
          callback(new Error('Please enter your password'));
        } else {
          // if (this.registerForm.passwordCheck !== '') {
          //   // 对第二个密码框单独验证
          //   this.$refs.registerForm.validateField('passwordCheck');
          // }
          callback();
        }
      };
      const validatePassCheck = (rule, value, callback) => {
        console.log("validatePassCheck");
        if (value === '') {
          callback(new Error('Please enter your password again'));
        } else if (value !== this.registerForm.password) {
          callback(new Error('The two input passwords do not match!'));
        } else {
          callback();
        }
      };
      const validateCode = (rule, value, callback) => {
        console.log("validateCode");
        console.log(value);
        if (!value) {
          return callback(new Error('Code cannot be empty'));
        }
        // 模拟异步验证效果
        // setTimeout(() => {
        // if (!Number.isInteger(value)) {
        //   callback(new Error('Please enter a numeric value'));
        // }else {
        callback();
        //   }
        // }, 1000);
      };

      return {

        registerAlert: false,
        loginAlert: true,
        sendSMSFlag: false,
        checkCodeCountDown: "获取验证码",
        registerForm: {
          username: '',
          password: '',
          passwordCheck: '',
          code: ''
        },
        loginForm: {
          username: '',
          password: ''
        },
        registerRule: {
          username: [],
          password: [
            {validator: validatePass, trigger: 'blur'}
          ],
          passwordCheck: [
            {validator: validatePassCheck, trigger: 'blur'}
          ],
          code: [
            {validator: validateCode, trigger: 'blur'}
          ]
        },
        loginRule: {
          username: [],
          password: [
            {validator: validatePass, trigger: 'blur'}
          ]
        }
      }
    },
    methods: {
      ok() {
        this.$Message.info('Clicked ok');
      },
      cancel(name) {
        this.$Message.info('Clicked cancel');
        this.handleReset(name);
      },
      handleSubmit(name) {
        console.log(name);
        this.$refs[name].validate((valid) => {
          if (valid) {
            if (name === 'registerForm') {
              this.register()
            } else {
              this.login()
            }
          } else {
            this.$Message.error('Fail!');
          }
        })
      },
      handleReset(name) {
        this.$refs[name].resetFields();
      },
      toggleForm() {
        this.registerAlert = !this.registerAlert;
        this.loginAlert = !this.loginAlert;
      },
      sendSMS() {
        this.sendSMSFlag = true;
        accountApi.sendSMS(this.registerForm.username)
          .then(res => {
            if (res.data.flag) {
              this.sendSMSFlag = false;
              this.$Message.success(res.data.message);
              this.countDown(60);
            } else {
              this.sendSMSFlag = false;
              this.$Message.error(res.data.message);
            }
          });
      },
      countDown(wait) {
        let _this = this;
        let _wait = wait;
        if (wait == 0) {
          this.sendSMSFlag = false;
          this.checkCodeCountDown = "获取验证码";
          wait = _wait;
        } else {
          this.sendSMSFlag = true;
          this.checkCodeCountDown = "验证码(" + wait + "s)";
          wait--;
          setTimeout(function () {
              _this.countDown(wait);
            },
            1000);
        }
      },
      login() {
        accountApi.login(this.loginForm.username, this.loginForm.password).then(res => {
          if (res.data.flag) {
            console.log(res.data.data);
            //保存用户信息
            setUser(res.data.data.token, res.data.data.roles, res.data.data.nickname, res.data.data.icon, res.data.data.userId);
            this.$Message.success(res.data.message);
            window.location.href = '/';
          } else {
            this.$Message.error(res.data.message);
            // this.loginForm.username = '';
            // this.loginForm.password = ''
          }
        })
      },
      register() {
        accountApi.register(this.registerForm.username, this.registerForm.password, this.registerForm.code).then(res => {
          if (res.data.flag) {
            //保存用户信息
            this.$Message.success(res.data.message);
            this.toggleForm();
            this.handleReset('registerForm');
          } else {
            this.$Message.error(res.data.message);
          }
        })
      },
    },

  }
</script>
