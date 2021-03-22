<style scoped>
  .layout {
    border: 1px solid #d7dde4;
    background: #f5f7f9;
    position: relative;
    border-radius: 4px;
    overflow: hidden;
  }

  .defaultPane .header {
    background: #fff;
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

  .ivu-row {
    height: 100%;
  }

  .layout-footer-center {
    text-align: center;
  }

  li {
    list-style: none;
  }

  .defaultPane .header {
    position: fixed;
    width: 100%;
    z-index: 50
  }

  .defaultPane .footerLink {
    color: rgba(22, 22, 22, 0.33);
  }

  .defaultPane .mainContent {
    margin-top: 100px;
    z-index: 1;
    border: 0;
    padding: 0 50px;
  }
</style>
<template>
  <div class="layout">
    <Layout class="defaultPane">
      <Header class="header">
        <Menu mode="horizontal" :theme="'light'" style="height: 100%;">
          <Row>
            <Col span="4" style="height: 100%;">
              <nuxt-link to="/">
                <img
                  :src="publicTencent+'/public/webIcon.png'"
                  alt="图片"
                  style="height: 100%;">
              </nuxt-link>
            </Col>
            <Col span="8" style="color: white;font-size: 23px;">
              <router-link to="/" tag="li" active-class="active" exact><a>主页</a></router-link>
            </Col>
            <Col span="7" style="">
              <Input v-model="searchBridge.keyword" icon="md-search" placeholder="在此搜索" @on-click="search"
                     style="width: 304px;"></Input>
            </Col>
            <Col span="5">
              <LoginRegister v-if="getUser().token===undefined"></LoginRegister>
              <Row class="sui‐nav pull‐right info" v-else type="flex" justify="center">
                <Col span="14">
                  <nuxt-link to="/person/" class="notice">
                    {{getUser().nickname}}
                    <Avatar :src="getUser().icon" :alt="getUser().nickname"></Avatar>
                  </nuxt-link>
                </Col>
                <Col span="8">
                  <Dropdown>
                    <Button type="primary">
                      更多操作
                      <Icon type="ios-arrow-down"></Icon>
                    </Button>
                    <DropdownMenu slot="list">
                      <DropdownItem>
                        <nuxt-link to="/post/publish">发帖</nuxt-link>
                      </DropdownItem>
                      <DropdownItem>
                        <nuxt-link :to="'/person/user/'+getUser().userId">个人主页</nuxt-link>
                      </DropdownItem>
                      <DropdownItem>
                        <nuxt-link to="/person/">个人中心</nuxt-link>
                      </DropdownItem>
                      <DropdownItem>
                        <nuxt-link to="/person/message/">消息中心</nuxt-link>
                      </DropdownItem>
                      <DropdownItem>
                        <nuxt-link to="/person/gayyou/">交友中心</nuxt-link>
                      </DropdownItem>
                      <DropdownItem>
                        <nuxt-link to="/person/post/">文章中心</nuxt-link>
                      </DropdownItem>
                      <DropdownItem>
                        <a @click="userLogout">退出登录</a>
                      </DropdownItem>
                    </DropdownMenu>
                  </Dropdown>
                </Col>
              </Row>
            </Col>
          </Row>
        </Menu>
      </Header>
      <div class="mainContent">
        <nuxt/>
      </div>
      <Footer class="layout-footer-center">
        <Row style="padding: 0 10%">
          <Col class="footerLink" span="6" v-for="{linksItem, linksIndex} in linkList" :key="linksIndex">
            <ul v-for="{linkItem, linkIndex} in linksItem" :key="linkIndex">
              <nuxt-link :to="linkItem.url">{{linkItem.name}}</nuxt-link>
            </ul>
          </Col>
        </Row>
      </Footer>
      <BackTop></BackTop>
    </Layout>
  </div>
</template>
<script>
  import LoginRegister from "@/components/LoginRegister.vue"
  import {getUser, removeUser} from '@/utils/auth'
  import {logout} from '@/api/login'
  import {publicTencent, linkList} from '@/utils/publicConst'

  export default {
    data() {
      return {
        user: {},
        publicTencent,
        linkList,
        searchBridge: {
          keyword: "",
        }
      }
    },
    methods: {
      userLogout() {
        removeUser();
        logout();
        location.href = "/";
      },
      search() {
        if (this.searchBridge.keyword === "") {
          this.$Message.error("搜索内容不能为空");
        } else if (getUser().token == null) {
          this.$Message.error("请登录后使用");
        } else {
          let keyword = this.searchBridge.keyword;
          this.$router.push({
            path: `/search/${keyword}`,
          })
        }
      },
      getUser
    },
    created() {
      this.user = getUser();
    },
    components: {
      LoginRegister
    }
  }
</script>
