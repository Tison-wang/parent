import axios from 'axios';
import qs from 'qs';
import vue from 'vue';
import {Toast} from 'vant'
//import store from '../store/index'

/**
 * 提示函数
 * 禁止点击蒙层、显示一秒后关闭
 */
const tip = msg => {
  Toast({
    type: 'warning',
    message: msg,
    duration: 1500,
    forbidClick: true
  })
}

// 环境的切换
if (process.env.NODE_ENV == 'development') {
  axios.defaults.baseURL = '/springboot';
} else if (process.env.NODE_ENV == 'debug') {
  axios.defaults.baseURL = '';
} else if (process.env.NODE_ENV == 'production') {
  axios.defaults.baseURL = 'http://localhost:8083/springboot/';
}

// 请求超时时间
axios.defaults.timeout = 10000;

// post请求头
axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=UTF-8';

// 请求拦截器
axios.interceptors.request.use(
  config => {
    // 每次发送请求之前判断是否存在token，如果存在，则统一在http请求的header都加上token，不用每次请求都手动添加了
    config.headers.token = "test";
    // 即使本地存在token，也有可能token是过期的，所以在响应拦截器中要对返回状态进行判断
    // const token = store.state.token;
    // token && (config.headers.Authorization = token);
    return config;
  },
  error => {
    return Promise.error(error);
  })

// 响应拦截器
axios.interceptors.response.use(
  response => {
    if (response.status === 200) {
      return Promise.resolve(response);
    } else {
      return Promise.reject(response);
    }
  },
  // 服务器状态码不是200的情况
  error => {
    if (error.response.status) {
      /*switch (error.response.status) {
        case 400:
          tip("网络请求发生错误！");
          break;
        // 401: 未登录
        // 未登录则跳转登录页面，并携带当前页面的路径
        // 在登录成功后返回当前页面，这一步需要在登录页操作。
        case 401:
          router.replace({
            path: '/login',
            query: {redirect: router.currentRoute.fullPath}
          });
          break;
        // 403 token过期
        // 登录过期对用户进行提示
        // 清除本地token和清空vuex中token对象
        // 跳转登录页面
        case 403:
          tip('登录过期，请重新登录');
          // 清除token
          localStorage.removeItem('token');
          //store.commit('loginSuccess', null);
          // 跳转登录页面，并将要浏览的页面fullPath传过去，登录成功后跳转需要访问的页面
          setTimeout(() => {
            router.replace({
              path: '/login',
              query: {
                redirect: router.currentRoute.fullPath
              }
            });
          }, 1000);
          break;
        // 404请求不存在
        case 404:
          tip('网络请求不存在');
          break;
        case 500:
          tip('请求的服务不可用, 请稍后重试');
          break;
        case 503:
          tip('服务内部异常：' + error.response.data.msg);
          break;
        // 其他错误，直接抛出错误提示
        default:
          tip(error.response.data.msg);
      }*/
      return Promise.reject(error);
    }
  }
);

/**
 * get方法，对应get请求
 * @param {String} url [请求的url地址]
 * @param {Object} params [请求时携带的参数]
 */
export function get(url, params) {
  return new Promise((resolve, reject) => {
    axios.get(url, {
      params: params
    })
      .then(res => {
        resolve(res.data);
      })
      .catch(err => {
        vue.prototype.$message.error(err.response.data.msg);
        reject(err.response.data);
      })
  });
}

/**
 * post方法，对应post请求
 * @param {String} url [请求的url地址]
 * @param {Object} params [请求时携带的参数]
 */
export function post(url, params) {
  return new Promise((resolve, reject) => {
    axios.post(url, qs.stringify(params), {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    })
      .then(res => {
        resolve(res.data);
      })
      .catch(err => {
        vue.prototype.$message.error(err.response.data.msg);
        reject(err.response.data)
      })
  });
}
