import request from '@/utils/request'

// 查询工序路线列表
export function listRoute(query) {
  return request({
    url: '/production/route/list',
    method: 'get',
    params: query
  })
}

// 查询工序路线详细
export function getRoute(id) {
  return request({
    url: '/production/route/' + id,
    method: 'get'
  })
}

// 新增工序路线
export function addRoute(data) {
  return request({
    url: '/production/route',
    method: 'post',
    data: data
  })
}

// 修改工序路线
export function updateRoute(data) {
  return request({
    url: '/production/route',
    method: 'put',
    data: data
  })
}

// 删除工序路线
export function delRoute(id) {
  return request({
    url: '/production/route/' + id,
    method: 'delete'
  })
}
