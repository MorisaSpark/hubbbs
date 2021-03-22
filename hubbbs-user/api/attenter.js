import request from '@/utils/request'
const group_name = 'hubbbs'
const api_name = 'attenter'
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
  delete(pojo){
    return request({
      url: `/${group_name}/${api_name}/`,
      method: 'delete',
      data: pojo
    })
  },
  findIsSelf(userId){
    return request({
      url: `/${group_name}/${api_name}/IsSelf/${userId}`,
      method: 'post',
    })
  },
  findUserNameSelf() {
    return request({
      url: `/${group_name}/${api_name}/self`,
      method: 'post'
    })
  },
  searchByTypeSelf(page, size, pojo) {
    return request({
      url: `/${group_name}/${api_name}/type/${page}/${size}`,
      method: 'post',
      data: pojo
    })
  }
}
