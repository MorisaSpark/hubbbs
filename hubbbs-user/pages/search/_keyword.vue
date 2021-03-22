<template>
  <div>
    <div v-for="(item, index) in searchList" :key="index">
      <Card>
        <Row>
          <Col span="20">
            <Row>
              <Col span="18" style="font-size: 20px;font-weight:700">
                <nuxt-link :to="'/post/item/'+item.id">{{item.title}}</nuxt-link>
              </Col>
            </Row>
            <Row style=" font-size: 10px; color: #b0a5a5;">
              <Col span="24">{{item.body}}</Col>
            </Row>
          </Col>
        </Row>
      </Card>
    </div>
  </div>
</template>

<script>
  import postSearchApi from "@/api/postSearch"

  export default {
    name: "index.vue",
    data() {
      return {
        searchBridge: {
          keyword: "",
          page: 1,
          size: 20,
        },
        searchList: [],
      }
    },
    asyncData({params}) {
      return postSearchApi.search(params.keyword, 1, 20)
        .then(res => {
          for (let i = 0; i < res.data.data.rows.length; i++) {
            res.data.data.rows[i]['body'] = res.data.data.rows[i]['body'].replace(/<[^>]+>/g, "").substr(0, 200);
          }
          return {searchList: res.data.data.rows};
        })
    },
    methods: {
      search() {
        postSearchApi.search(this.searchBridge.keyword, this.searchBridge.page, this.searchBridge.size)
          .then(res => {
            if (res.data.flag) {
              this.searchList = res.data.data.rows;
              this.$Message.success(res.data.message);
            } else {
              this.$Message.error(res.data.message);
            }
          });
      }
    }
  }
</script>

<style scoped>

</style>
