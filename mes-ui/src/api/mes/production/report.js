import request from '@/utils/request'

// 查询报工单列表
export function listReport(query) {
  return request({
    url: '/production/report/list',
    method: 'get',
    params: query
  })
}

// 查询报工单详细
export function getReport(id) {
  return request({
    url: '/production/report/' + id,
    method: 'get'
  })
}

// 新增报工单
export function addReport(data) {
  return request({
    url: '/production/report',
    method: 'post',
    data: data
  })
}

// 修改报工单
export function updateReport(data) {
  return request({
    url: '/production/report',
    method: 'put',
    data: data
  })
}

// 删除报工单
export function delReport(id) {
  return request({
    url: '/production/report/' + id,
    method: 'delete'
  })
}

// 批量新增/修改报工单
export function batchSave(data) {
  return request({
    url: '/production/report/batchSave',
    method: 'post',
    data: data
  })
}
