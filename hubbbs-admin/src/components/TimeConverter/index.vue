<template>
  <div style="display: inline-block; position: absolute;right:280px;;">
    <el-date-picker
      v-model="timeBefore"
      type="datetime"
      placeholder="选择日期"
      format="yyyy 年 MM 月 dd 日"
    >
    </el-date-picker>
    <span class="demonstration" style="width: 100px">时间戳：{{ timeBefore===''?'':Date.parse(new Date(timeBefore))+8*3600*1000 }}</span>
  </div>
</template>

<script>
  export default {
    name: "timeConverter",
    data() {
      return {
        timeBefore: '',
      }
    }
  }
</script>

<style scoped>

</style>
