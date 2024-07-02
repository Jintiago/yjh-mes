import request from '@/utils/request'

// 查询完工入库单列表
export function listManufactureInto(query) {
  return request({
    url: '/warehouse/manufactureInto/list',
    method: 'get',
    params: query
  })
}

// 查询完工入库单详细
export function getManufactureInto(id) {
  return request({
    url: '/warehouse/manufactureInto/' + id,
    method: 'get'
  })
}

// 新增完工入库单
export function addManufactureInto(data) {
  return request({
    url: '/warehouse/manufactureInto',
    method: 'post',
    data: data
  })
}

// 修改完工入库单
export function updateManufactureInto(data) {
  return request({
    url: '/warehouse/manufactureInto',
    method: 'put',
    data: data
  })
}

// 删除完工入库单
export function delManufactureInto(id) {
  return request({
    url: '/warehouse/manufactureInto/' + id,
    method: 'delete'
  })
}
