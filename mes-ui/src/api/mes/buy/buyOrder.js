import request from '@/utils/request'

// 查询采购订单列表
export function listBuyOrder(query) {
  return request({
    url: '/buy/buyOrder/list',
    method: 'get',
    params: query
  })
}

// 查询采购订单详细
export function getBuyOrder(id) {
  return request({
    url: '/buy/buyOrder/' + id,
    method: 'get'
  })
}

// 新增采购订单
export function addBuyOrder(data) {
  return request({
    url: '/buy/buyOrder',
    method: 'post',
    data: data
  })
}

// 修改采购订单
export function updateBuyOrder(data) {
  return request({
    url: '/buy/buyOrder',
    method: 'put',
    data: data
  })
}

// 删除采购订单
export function delBuyOrder(id) {
  return request({
    url: '/buy/buyOrder/' + id,
    method: 'delete'
  })
}
