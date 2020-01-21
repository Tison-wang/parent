import { post, get } from './axios'


let uploadBase = ''

if(process.env.NODE_ENV === 'production'){
  uploadBase = 'https://cloud.inschos.com'
}

export default {
  get: (data) => { return get('/jersey/resource/user/' + data.name, data) },
  post: (data) => { return post('/hello', data) },
  queryUserList: (data) => { return post('/jersey/resource/user', data) }
}

