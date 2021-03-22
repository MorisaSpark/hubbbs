import request from '@/utils/request'
export function login(username, password) {
  return request({
    url: '/index/login',
    method: 'post',
    data: {
      username,
      password
    }
  })
}

export function getInfo(token) {
  return request({
    url: '/index/info',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: '/account/user/userLogout/user',
    method: 'post'
  })
}
