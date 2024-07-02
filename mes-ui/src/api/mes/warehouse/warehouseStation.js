import request from '@/utils/request'

// 查询仓库工位绑定列表
export function listWarehouseStation(query) {
  return request({
    url: '/warehouse/warehouseStation/list',
    method: 'get',
    params: query
  })
}

// 查询仓库工位绑定详细
export function getWarehouseStation(id) {
  return request({
    url: '/warehouse/warehouseStation/' + id,
    method: 'get'
  })
}

// 新增仓库工位绑定
export function addWarehouseStation(data) {
  return request({
    url: '/warehouse/warehouseStation',
    method: 'post',
    data: data
  })
}

// 修改仓库工位绑定
export function updateWarehouseStation(data) {
  return request({
    url: '/warehouse/warehouseStation',
    method: 'put',
    data: data
  })
}

// 删除仓库工位绑定
export function delWarehouseStation(id) {
  return request({
    url: '/warehouse/warehouseStation/' + id,
    method: 'delete'
  })
}
