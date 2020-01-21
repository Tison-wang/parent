import displayComponent from './display.vue';

const display  = {};

// 注册display
display.install = function (Vue) {
  Vue.component("ElDisplay", displayComponent);
}

export default display
