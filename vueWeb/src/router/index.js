import Vue from 'vue'
import Router from 'vue-router'
import main from '../components/main'
import user from  '../components/user/User'

Vue.use(Router)

export default new Router({
  //mode:'history',
  routes: [
    {
      path: '/',
      name: 'main',
      component: main
    },
    {
      path: '/user/User',
      name: '用户模块',
      component: user
    }
  ]
})
