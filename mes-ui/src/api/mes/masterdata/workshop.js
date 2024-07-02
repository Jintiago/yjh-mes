import request from '@/utils/request'

// 查询车间列表
export function listWorkshop(query) {
  return request({
    url: '/masterdata/workshop/list',
    method: 'get',
    params: query
  })
}

// 查询车间详细
export function getWorkshop(id) {
  return request({
    url: '/masterdata/workshop/' + id,
    method: 'get'
  })
}

// 新增车间
export function addWorkshop(data) {
  return request({
    url: '/masterdata/workshop',
    method: 'post',
    data: data
  })
}

// 修改车间
export function updateWorkshop(data) {
  return request({
    url: '/masterdata/workshop',
    method: 'put',
    data: data
  })
}

// 删除车间
export function delWorkshop(id) {
  return request({
    url: '/masterdata/workshop/' + id,
    method: 'delete'
  })
}
