<template>
  <div class="personPane">
    <Row>
      <Col span="6">
        <Menu :theme="'light'" :active-name="activeItem">
          <MenuItem name="1" to="/person">
            <Icon type="md-document"/>
            个人中心
          </MenuItem>
          <MenuItem name="2" to="/person/message">
            <Icon type="md-chatbubbles"/>
            消息中心
          </MenuItem>
          <MenuItem name="3" to="/person/gayyou/">
            <Icon type="ios-bookmark"/>
            交友中心
          </MenuItem>
          <MenuItem name="4" to="/person/post/">
            <Icon type="ios-bookmark"/>
            文章中心
          </MenuItem>
        </Menu>
      </Col>
      <Col span="18">
        <nuxt-child :style="{padding: '24px', minHeight: '280px', background: '#fff'}"></nuxt-child>
      </Col>
    </Row>
  </div>
</template>

<script>
  import {getUser} from "../../utils/auth";

  export default {
    name: "index",
    data() {
      return {
        activeItem: '1',
        user: {
          userId: "",
        }
      }
    },
    created() {
      this.user.userId = getUser().userId;
    },
    methods: {
    }
  }
</script>

<style scoped>

</style>
