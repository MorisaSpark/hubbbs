import request from '@/utils/request'

const group_name = 'hubbbs'
const api_name = 'post'
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
  getListSummary(page, size, type) {
    return request({
      url: `/${group_name}/${api_name}/listSummary/${page}/${size}`,
      method: 'post',
      data: type
    })
  },
  searchByFlowers(page, size, postMes) {
    return request({
      url: `/${group_name}/${api_name}/Flowers/${page}/${size}`,
      method: 'post',
      data: postMes
    })
  },
  save(pojo) {
    return request({
      url: `/${group_name}/${api_name}`,
      method: 'post',
      data: pojo
    })
  },
  saveSelf(pojo) {
    return request({
      url: `/${group_name}/${api_name}/saveSelf`,
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
  findByIdDetail(id) {
    return request({
      url: `/${group_name}/${api_name}/detail/${id}`,
      method: 'get'
    })
  },
  findSumClickNumByUserId(userId) {
    return request({
      url: `/${group_name}/${api_name}/sumClickNum/${userId}`,
      method: 'post'
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
  searchByUserIdSelf(page, size) {
    return request({
      url: `/${group_name}/${api_name}/searchByUserIdSelf/${page}/${size}`,
      method: 'post'
    })
  },
  findSumClickNumByUserIdSelf() {
    return request({
      url: `/${group_name}/${api_name}/findSumClickNumByUserIdSelf`,
      method: 'post'
    })
  },
  findByTag(page, size, postId) {
    return request({
      url: `/${group_name}/${api_name}/findByTag/${postId}/${page}/${size}`,
      method: 'get'
    })
  },
  findByType(page, size, postId) {
    return request({
      url: `/${group_name}/${api_name}/findByType/${postId}/${page}/${size}`,
      method: 'get'
    })
  },
  findByUserIdType(type,orderBy, page, size) {
    return request({
      url: `/${group_name}/${api_name}/findByUserIdType/${type}/${orderBy}/${page}/${size}`,
      method: 'get'
    })
  }
}
