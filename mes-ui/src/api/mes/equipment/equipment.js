import request from '@/utils/request'

// 查询设备信息列表
export function listEquipment(query) {
  return request({
    url: '/equipment/equipment/list',
    method: 'get',
    params: query
  })
}

// 查询设备信息详细
export function getEquipment(id) {
  return request({
    url: '/equipment/equipment/' + id,
    method: 'get'
  })
}

// 新增设备信息
export function addEquipment(data) {
  return request({
    url: '/equipment/equipment',
    method: 'post',
    data: data
  })
}

// 修改设备信息
export function updateEquipment(data) {
  return request({
    url: '/equipment/equipment',
    method: 'put',
    data: data
  })
}

// 删除设备信息
export function delEquipment(id) {
  return request({
    url: '/equipment/equipment/' + id,
    method: 'delete'
  })
}

// 查询设备OEE稼动率
export function oeeUtilizationList(query) {
  return request({
    url: '/equipment/equipment/oeeUtilizationList',
    method: 'get',
    params: query
  })
}


