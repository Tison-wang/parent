<template>
  <div id="first">{{title}}</div>
</template>

<script>
  export default {
    data() {
      return {
        title: '第一个vue插件！thank you！'
      }
    }
  }
</script>

<style>
  div#first {
    width: 220px;
    height: 50px;
    line-height: 50px;
    border: #0a1b50;
    margin: 10px;
    align-content: center;
    background: lightgreen;
  }

</style>
