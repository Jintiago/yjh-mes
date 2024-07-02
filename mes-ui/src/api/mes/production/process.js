import request from '@/utils/request'

// 查询工序列表
export function listProcess(query) {
  return request({
    url: '/production/process/list',
    method: 'get',
    params: query
  })
}

// 查询工序详细
export function getProcess(id) {
  return request({
    url: '/production/process/' + id,
    method: 'get'
  })
}

// 新增工序
export function addProcess(data) {
  return request({
    url: '/production/process',
    method: 'post',
    data: data
  })
}

// 修改工序
export function updateProcess(data) {
  return request({
    url: '/production/process',
    method: 'put',
    data: data
  })
}

// 删除工序
export function delProcess(id) {
  return request({
    url: '/production/process/' + id,
    method: 'delete'
  })
}
