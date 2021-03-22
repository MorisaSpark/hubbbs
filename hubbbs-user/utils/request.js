import axios from 'axios'
import {getUser} from "./auth";

axios.defaults.headers.common['Authorization'] =  getUser().token===undefined?"Hello ":"Bearer "+getUser().token;
// 创建axios实例
const service = axios.create({
  // baseURL: 'https://easy-mock.com/mock/5c829abb98a1ca19efdafa89', // api 的 base_url
  baseURL: 'http://localhost:9009', // api 的 base_url
  timeout: 51000 // 请求超时时间
});

export default service
