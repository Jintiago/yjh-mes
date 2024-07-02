import request from '@/utils/request'

// 查询编码规则列表
export function listSysCodeRule(query) {
  return request({
    url: '/common/sysCodeRule/list',
    method: 'get',
    params: query
  })
}

// 查询编码规则详细
export function getSysCodeRule(id) {
  return request({
    url: '/common/sysCodeRule/' + id,
    method: 'get'
  })
}

// 新增编码规则
export function addSysCodeRule(data) {
  return request({
    url: '/common/sysCodeRule',
    method: 'post',
    data: data
  })
}

// 修改编码规则
export function updateSysCodeRule(data) {
  return request({
    url: '/common/sysCodeRule',
    method: 'put',
    data: data
  })
}

// 删除编码规则
export function delSysCodeRule(id) {
  return request({
    url: '/common/sysCodeRule/' + id,
    method: 'delete'
  })
}
