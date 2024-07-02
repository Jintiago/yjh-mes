import request from '@/utils/request'

// 查询其他出库单主表列表
export function listOtherOut(query) {
  return request({
    url: '/warehouse/otherOut/list',
    method: 'get',
    params: query
  })
}

// 查询其他出库单主表详细
export function getOtherOut(id) {
  return request({
    url: '/warehouse/otherOut/' + id,
    method: 'get'
  })
}

// 新增其他出库单主表
export function addOtherOut(data) {
  return request({
    url: '/warehouse/otherOut',
    method: 'post',
    data: data
  })
}

// 修改其他出库单主表
export function updateOtherOut(data) {
  return request({
    url: '/warehouse/otherOut',
    method: 'put',
    data: data
  })
}

// 删除其他出库单主表
export function delOtherOut(id) {
  return request({
    url: '/warehouse/otherOut/' + id,
    method: 'delete'
  })
}
