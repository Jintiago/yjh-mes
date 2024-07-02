import request from '@/utils/request'

// 查询库存盘点列表
export function listTakeInventory(query) {
  return request({
    url: '/warehouse/takeInventory/list',
    method: 'get',
    params: query
  })
}

// 查询库存盘点详细
export function getTakeInventory(id) {
  return request({
    url: '/warehouse/takeInventory/' + id,
    method: 'get'
  })
}

// 新增库存盘点
export function addTakeInventory(data) {
  return request({
    url: '/warehouse/takeInventory',
    method: 'post',
    data: data
  })
}

// 修改库存盘点
export function updateTakeInventory(data) {
  return request({
    url: '/warehouse/takeInventory',
    method: 'put',
    data: data
  })
}

// 删除库存盘点
export function delTakeInventory(id) {
  return request({
    url: '/warehouse/takeInventory/' + id,
    method: 'delete'
  })
}
