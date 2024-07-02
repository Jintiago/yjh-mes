import request from '@/utils/request'

// 查询销售出库对账单列表
export function listSaleOutAccountBill(query) {
  return request({
    url: '/finance/saleOutAccountBill/list',
    method: 'get',
    params: query
  })
}

// 查询销售出库对账单详细
export function getSaleOutAccountBill(id) {
  return request({
    url: '/finance/saleOutAccountBill/' + id,
    method: 'get'
  })
}

// 新增销售出库对账单
export function addSaleOutAccountBill(data) {
  return request({
    url: '/finance/saleOutAccountBill',
    method: 'post',
    data: data
  })
}

// 修改销售出库对账单
export function updateSaleOutAccountBill(data) {
  return request({
    url: '/finance/saleOutAccountBill',
    method: 'put',
    data: data
  })
}

// 删除销售出库对账单
export function delSaleOutAccountBill(id) {
  return request({
    url: '/finance/saleOutAccountBill/' + id,
    method: 'delete'
  })
}
