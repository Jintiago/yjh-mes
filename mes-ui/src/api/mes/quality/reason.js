import request from '@/utils/request'

// 查询质检原因列表
export function listReason(query) {
  return request({
    url: '/quality/reason/list',
    method: 'get',
    params: query
  })
}

// 查询质检原因详细
export function getReason(id) {
  return request({
    url: '/quality/reason/' + id,
    method: 'get'
  })
}

// 新增质检原因
export function addReason(data) {
  return request({
    url: '/quality/reason',
    method: 'post',
    data: data
  })
}

// 修改质检原因
export function updateReason(data) {
  return request({
    url: '/quality/reason',
    method: 'put',
    data: data
  })
}

// 删除质检原因
export function delReason(id) {
  return request({
    url: '/quality/reason/' + id,
    method: 'delete'
  })
}
