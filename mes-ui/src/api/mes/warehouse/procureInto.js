import request from '@/utils/request'

// 查询采购入库单列表
export function listProcureInto(query) {
  return request({
    url: '/warehouse/procureInto/list',
    method: 'get',
    params: query
  })
}

// 查询采购入库单详细
export function getProcureInto(id) {
  return request({
    url: '/warehouse/procureInto/' + id,
    method: 'get'
  })
}

// 新增采购入库单
export function addProcureInto(data) {
  return request({
    url: '/warehouse/procureInto',
    method: 'post',
    data: data
  })
}

// 修改采购入库单
export function updateProcureInto(data) {
  return request({
    url: '/warehouse/procureInto',
    method: 'put',
    data: data
  })
}

// 删除采购入库单
export function delProcureInto(id) {
  return request({
    url: '/warehouse/procureInto/' + id,
    method: 'delete'
  })
}
