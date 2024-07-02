import request from '@/utils/request'

// 查询设备项目列表
export function listInspectionItem(query) {
  return request({
    url: '/equipment/inspectionItem/list',
    method: 'get',
    params: query
  })
}

// 查询设备项目详细
export function getInspectionItem(id) {
  return request({
    url: '/equipment/inspectionItem/' + id,
    method: 'get'
  })
}

// 新增设备项目
export function addInspectionItem(data) {
  return request({
    url: '/equipment/inspectionItem',
    method: 'post',
    data: data
  })
}

// 修改设备项目
export function updateInspectionItem(data) {
  return request({
    url: '/equipment/inspectionItem',
    method: 'put',
    data: data
  })
}

// 删除设备项目
export function delInspectionItem(id) {
  return request({
    url: '/equipment/inspectionItem/' + id,
    method: 'delete'
  })
}
