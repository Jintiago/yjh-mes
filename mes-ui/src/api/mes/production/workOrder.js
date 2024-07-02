import request from '@/utils/request'

// 查询工单列表
export function listWorkOrder(query) {
  return request({
    url: '/production/workOrder/list',
    method: 'get',
    params: query
  })
}

// 查询工单详细
export function getWorkOrder(id) {
  return request({
    url: '/production/workOrder/' + id,
    method: 'get'
  })
}

// 查询工单详细
export function getWorkOrderByEntryId(id) {
  return request({
    url: '/production/workOrder/getInfoByEntryId/' + id,
    method: 'get'
  })
}

// 新增工单
export function addWorkOrder(data) {
  return request({
    url: '/production/workOrder',
    method: 'post',
    data: data
  })
}

// 批量新增工单
export function batchAddWorkOrder(data) {
  return request({
    url: '/production/workOrder/batchAdd',
    method: 'post',
    data: data
  })
}
// 批量新增工单,传递json
export function batchAddWorkOrderByJson(data) {
  return request({
    url: '/production/workOrder/batchAddByJson',
    method: 'post',
    data: data
  })
}


// 修改工单
export function updateWorkOrder(data) {
  return request({
    url: '/production/workOrder',
    method: 'put',
    data: data
  })
}



// 删除工单
export function delWorkOrder(id) {
  return request({
    url: '/production/workOrder/' + id,
    method: 'delete'
  })
}

// 修改工单生产状态
export function editProStatus(params) {
  return request({
    url: '/production/workOrder/editProStatus',
    method: 'put',
    params: params
  })
}

//预览计划排产的生产工单
export function listWorkOrderPreview(params) {
  return request({
    url: '/production/workOrder/preview',
    method: 'get',
    params: params
  })
}

//查询前一道工序
export function getFrontProcessReport(params) {
  return request({
    url: '/production/workOrder/getFrontProcessReport',
    method: 'get',
    params: params
  })
}

// 工单自定义排序
export function updateCustomSort(data) {
  return request({
    url: '/production/workOrder/updateCustomSort',
    method: 'post',
    data: data
  })
}
