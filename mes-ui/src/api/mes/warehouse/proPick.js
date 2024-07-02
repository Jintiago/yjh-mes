import request from '@/utils/request'

// 查询生产领料单列表
export function listProPick(query) {
  return request({
    url: '/warehouse/proPick/list',
    method: 'get',
    params: query
  })
}

// 查询生产领料单详细
export function getProPick(id) {
  return request({
    url: '/warehouse/proPick/' + id,
    method: 'get'
  })
}

// 新增生产领料单
export function addProPick(data) {
  return request({
    url: '/warehouse/proPick',
    method: 'post',
    data: data
  })
}

// 修改生产领料单
export function updateProPick(data) {
  return request({
    url: '/warehouse/proPick',
    method: 'put',
    data: data
  })
}

// 删除生产领料单
export function delProPick(id) {
  return request({
    url: '/warehouse/proPick/' + id,
    method: 'delete'
  })
}

// 批量审核
export function batchCheck(data) {
  return request({
    url: '/warehouse/proPick/batchCheck',
    method: 'put',
    params: data
  })
}
