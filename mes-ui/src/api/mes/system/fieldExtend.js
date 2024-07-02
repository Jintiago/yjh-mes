import request from '@/utils/request'

// 查询字段扩展列表
export function listFieldExtend(query) {
  return request({
    url: '/system/fieldExtend/list',
    method: 'get',
    params: query
  })
}

// 查询字段扩展详细
export function getFieldExtend(id) {
  return request({
    url: '/system/fieldExtend/' + id,
    method: 'get'
  })
}

// 新增字段扩展
export function addFieldExtend(data) {
  return request({
    url: '/system/fieldExtend',
    method: 'post',
    data: data
  })
}

// 修改字段扩展
export function updateFieldExtend(data) {
  return request({
    url: '/system/fieldExtend',
    method: 'put',
    data: data
  })
}

// 删除字段扩展
export function delFieldExtend(id) {
  return request({
    url: '/system/fieldExtend/' + id,
    method: 'delete'
  })
}
