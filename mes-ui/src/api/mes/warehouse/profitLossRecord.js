import request from '@/utils/request'

// 查询盘点盈亏记录列表
export function listProfitLossRecord(query) {
  return request({
    url: '/warehouse/profitLossRecord/list',
    method: 'get',
    params: query
  })
}

// 查询盘点盈亏记录详细
export function getProfitLossRecord(id) {
  return request({
    url: '/warehouse/profitLossRecord/' + id,
    method: 'get'
  })
}

// 新增盘点盈亏记录
export function addProfitLossRecord(data) {
  return request({
    url: '/warehouse/profitLossRecord',
    method: 'post',
    data: data
  })
}

// 根据盘点单下推生成盘盈记录单
export function addByTakeInventory(id) {
  return request({
    url: '/warehouse//profitLossRecord/addByTakeInventory/'+id,
    method: 'get',
  })
}


// 修改盘点盈亏记录
export function updateProfitLossRecord(data) {
  return request({
    url: '/warehouse/profitLossRecord',
    method: 'put',
    data: data
  })
}

// 删除盘点盈亏记录
export function delProfitLossRecord(id) {
  return request({
    url: '/warehouse/profitLossRecord/' + id,
    method: 'delete'
  })
}
