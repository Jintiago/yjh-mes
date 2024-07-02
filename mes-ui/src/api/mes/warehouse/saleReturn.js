import request from '@/utils/request'

// 查询销售退货单列表
export function listSaleReturn(query) {
  return request({
    url: '/warehouse/saleReturn/list',
    method: 'get',
    params: query
  })
}

// 查询销售退货单详细
export function getSaleReturn(id) {
  return request({
    url: '/warehouse/saleReturn/' + id,
    method: 'get'
  })
}

// 新增销售退货单
export function addSaleReturn(data) {
  return request({
    url: '/warehouse/saleReturn',
    method: 'post',
    data: data
  })
}

// 修改销售退货单
export function updateSaleReturn(data) {
  return request({
    url: '/warehouse/saleReturn',
    method: 'put',
    data: data
  })
}

// 删除销售退货单
export function delSaleReturn(id) {
  return request({
    url: '/warehouse/saleReturn/' + id,
    method: 'delete'
  })
}
