import request from '@/utils/request'

// 查询即时库存列表
export function listInventory(query) {
  return request({
    url: '/warehouse/inventory/list',
    method: 'get',
    params: query
  })
}

// 查询有库存的规格型号
export function getSpecificationList(query) {
  return request({
    url: '/warehouse/inventory/getSpecificationList',
    method: 'get',
    params: query
  })
}


// 新增即时库存
export function addInventory(data) {
  return request({
    url: '/warehouse/inventory',
    method: 'post',
    data: data
  })
}

// 修改即时库存
export function updateInventory(data) {
  return request({
    url: '/warehouse/inventory',
    method: 'put',
    data: data
  })
}

// 删除即时库存
export function delInventory(aa) {
  return request({
    url: '/warehouse/inventory/' + aa,
    method: 'delete'
  })
}
