import request from '@/utils/request'

// 查询销售出库单主表列表
export function listSaleOut(query) {
  return request({
    url: '/warehouse/saleOut/list',
    method: 'get',
    params: query
  })
}

// 查询销售出库单主表详细
export function getSaleOut(id) {
  return request({
    url: '/warehouse/saleOut/' + id,
    method: 'get'
  })
}

// 新增销售出库单主表
export function addSaleOut(data) {
  return request({
    url: '/warehouse/saleOut',
    method: 'post',
    data: data
  })
}

// 修改销售出库单主表
export function updateSaleOut(data) {
  return request({
    url: '/warehouse/saleOut',
    method: 'put',
    data: data
  })
}

// 删除销售出库单主表
export function delSaleOut(id) {
  return request({
    url: '/warehouse/saleOut/' + id,
    method: 'delete'
  })
}
