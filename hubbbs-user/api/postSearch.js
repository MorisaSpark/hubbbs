import request from '@/utils/request'

const group_name = 'hubbbs'
const api_name = 'postsearch'
export default {
  search(keyword, page, size) {
    return request({
      url: `/${group_name}/${api_name}/${keyword}/${page}/${size}`,
      method: 'get',
      data:keyword
    })
  },
}
