import request from '@/utils/request'

export function login(username, password) {
  return request({
    url: '/hubbbs/account/login/admin',
    method: 'post',
    data: {
      username,
      password
    }
  })
}

export function getInfo(token) {
  return request({
    url: '/hubbbs/account/user/info',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: '/hubbbs/account/user/logout/admin',
    method: 'post'
  })
}
