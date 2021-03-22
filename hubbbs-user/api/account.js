import request from '@/utils/request'

const group_name = 'hubbbs'
const api_name = 'account'
export default {
  getList() {
    return request({
      url: `/${group_name}/${api_name}`,
      method: 'get'
    })
  },
  search(page, size, searchMap) {
    return request({
      url: `/${group_name}/${api_name}/search/${page}/${size}`,
      method: 'post',
      data: searchMap
    })
  },
  save(pojo) {
    return request({
      url: `/${group_name}/${api_name}`,
      method: 'post',
      data: pojo
    })
  },
  findById(id) {
    return request({
      url: `/${group_name}/${api_name}/${id}`,
      method: 'get'
    })
  },
  update(id, pojo) {
    if (id === null || id === '') {
      return this.save(pojo)
    }
    return request({
      url: `/${group_name}/${api_name}/${id}`,
      method: 'put',
      data: pojo
    })
  },
  deleteById(id) {
    return request({
      url: `/${group_name}/${api_name}/${id}`,
      method: 'delete'
    })
  },
  login(username, password) {
    return request({
      url: `/${group_name}/${api_name}/login`,
      method: 'post',
      data: {
        username,
        password
      }
    })
  },
  register(username, password, code) {
    return request({
      url: `/${group_name}/${api_name}/register/${code}`,
      method: 'post',
      data: {
        username,
        password
      }
    })
  },
  sendSMS(username) {
    return request({
      url: `/${group_name}/${api_name}/sendSMS/${username}`,
      method: 'post',
    })
  },
  findAccountDetailSelf() {
    return request({
      url: `/${group_name}/${api_name}/self`,
      method: 'post'
    })
  },
  updatePassword(pojo){
    return request({
      url: `/${group_name}/${api_name}/password`,
      method: 'put',
      data: pojo
    })
  },
  updateUsername(pojo, code){
    return request({
      url: `/${group_name}/${api_name}/username/${code}`,
      method: 'put',
      data: pojo
    })
  },
}
