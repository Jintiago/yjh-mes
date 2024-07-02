import request from '@/utils/request'

// 查询设备维修单列表
export function listRepairOrder(query) {
  return request({
    url: '/equipment/repairOrder/list',
    method: 'get',
    params: query
  })
}

// 查询设备维修单详细
export function getRepairOrder(id) {
  return request({
    url: '/equipment/repairOrder/' + id,
    method: 'get'
  })
}

// 新增设备维修单
export function addRepairOrder(data) {
  return request({
    url: '/equipment/repairOrder',
    method: 'post',
    data: data
  })
}

// 修改设备维修单
export function updateRepairOrder(data) {
  return request({
    url: '/equipment/repairOrder',
    method: 'put',
    data: data
  })
}

// 删除设备维修单
export function delRepairOrder(id) {
  return request({
    url: '/equipment/repairOrder/' + id,
    method: 'delete'
  })
}
