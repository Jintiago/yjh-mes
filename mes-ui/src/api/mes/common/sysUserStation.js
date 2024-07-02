import request from '@/utils/request'

// 查询用户工位绑定列表
export function listSysUserStation(query) {
  return request({
    url: '/common/sysUserStation/list',
    method: 'get',
    params: query
  })
}

// 查询用户工位绑定详细
export function getSysUserStation(userId) {
  return request({
    url: '/common/sysUserStation/' + userId,
    method: 'get'
  })
}

// 新增用户工位绑定
export function addSysUserStation(data) {
  return request({
    url: '/common/sysUserStation',
    method: 'post',
    data: data
  })
}

// 修改用户工位绑定
export function updateSysUserStation(data) {
  return request({
    url: '/common/sysUserStation',
    method: 'put',
    data: data
  })
}

// 删除用户工位绑定
export function delSysUserStation(id) {
  return request({
    url: '/common/sysUserStation/' + id,
    method: 'delete'
  })
}
