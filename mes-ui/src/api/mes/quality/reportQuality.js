import request from '@/utils/request'

// 查询报工质检单列表
export function listReportQuality(query) {
  return request({
    url: '/quality/reportQuality/list',
    method: 'get',
    params: query
  })
}

// 查询报工质检单详细
export function getReportQuality(id) {
  return request({
    url: '/quality/reportQuality/' + id,
    method: 'get'
  })
}

// 新增报工质检单
export function addReportQuality(data) {
  return request({
    url: '/quality/reportQuality',
    method: 'post',
    data: data
  })
}

// 修改报工质检单
export function updateReportQuality(data) {
  return request({
    url: '/quality/reportQuality',
    method: 'put',
    data: data
  })
}

// 删除报工质检单
export function delReportQuality(id) {
  return request({
    url: '/quality/reportQuality/' + id,
    method: 'delete'
  })
}
