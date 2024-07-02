import request from '@/utils/request'

// 查询工资单列表
export function listPayroll(query) {
  return request({
    url: '/finance/payroll/list',
    method: 'get',
    params: query
  })
}

// 查询工资单详细
export function getPayroll(id) {
  return request({
    url: '/finance/payroll/' + id,
    method: 'get'
  })
}

// 新增工资单
export function addPayroll(data) {
  return request({
    url: '/finance/payroll',
    method: 'post',
    data: data
  })
}

// 修改工资单
export function updatePayroll(data) {
  return request({
    url: '/finance/payroll',
    method: 'put',
    data: data
  })
}

// 删除工资单
export function delPayroll(id) {
  return request({
    url: '/finance/payroll/' + id,
    method: 'delete'
  })
}

// 工资单预览
export function payRollPreview(ids) {
  return request({
    url: '/finance/payroll/preview/' + ids,
    method: 'get'
  })
}

// 批量新增工资单
export function batchAdd(list) {
  return request({
    url: '/finance/payroll/batchAdd',
    method: 'post',
    data: list
  })
}
