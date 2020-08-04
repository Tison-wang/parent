import {post, get} from './axios'


let uploadBase = ''

if (process.env.NODE_ENV === 'production') {
  uploadBase = 'https://cloud.inschos.com'
}

export default {
  get: (data) => {
    return get('/jersey/resource/user/' + data.name, data)
  },
  post: (data) => {
    return post('/hello', data)
  },
  queryUserList: (data) => {
    return post('/jersey/resource/user', data)
  },
  loadClass1: (data) => {
    return get('/jersey/classloader/loadClass1', data)
  },
  loadClass2: (data) => {
    return get('/jersey/classloader/loadClass2', data)
  },
  loadClass3: (data) => {
    return get('/jersey/classloader/loadClass3', data)
  },
  print: (data) => {
    return get('/jersey/classloader/print', data)
  }
}

