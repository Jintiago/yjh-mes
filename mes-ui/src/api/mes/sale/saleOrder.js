import request from '@/utils/request'

// 查询销售订单列表
export function listSaleOrder(query) {
  return request({
    url: '/sale/saleOrder/list',
    method: 'get',
    params: query
  })
}
// 查询销售订单子表列表
export function listEntryByIds(query) {
  return request({
    url: '/sale/saleOrder/listEntryByIds',
    method: 'get',
    params: query
  })
}


// 查询销售订单详细
export function getSaleOrder(id) {
  return request({
    url: '/sale/saleOrder/' + id,
    method: 'get'
  })
}

// 新增销售订单
export function addSaleOrder(data) {
  return request({
    url: '/sale/saleOrder',
    method: 'post',
    data: data
  })
}

// 修改销售订单
export function updateSaleOrder(data) {
  return request({
    url: '/sale/saleOrder',
    method: 'put',
    data: data
  })
}

// 删除销售订单
export function delSaleOrder(id) {
  return request({
    url: '/sale/saleOrder/' + id,
    method: 'delete'
  })
}

// 修改订单明细状态
export function batchUpdateEntryStatus(params) {
  return request({
    url: '/sale/saleOrder/batchUpdateEntryStatus',
    method: 'put',
    params: params
  })
}
