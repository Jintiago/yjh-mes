import request from '@/utils/request'

// 查询工位列表
export function listStation(query) {
  return request({
    url: '/masterdata/station/list',
    method: 'get',
    params: query
  })
}

// 查询工位详细
export function getStation(id) {
  return request({
    url: '/masterdata/station/' + id,
    method: 'get'
  })
}

// 新增工位
export function addStation(data) {
  return request({
    url: '/masterdata/station',
    method: 'post',
    data: data
  })
}

// 修改工位
export function updateStation(data) {
  return request({
    url: '/masterdata/station',
    method: 'put',
    data: data
  })
}

// 删除工位
export function delStation(id) {
  return request({
    url: '/masterdata/station/' + id,
    method: 'delete'
  })
}
