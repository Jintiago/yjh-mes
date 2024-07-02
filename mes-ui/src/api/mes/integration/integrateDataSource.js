import request from '@/utils/request'

// 查询集成数据源列表
export function listIntegrateDataSource(query) {
  return request({
    url: '/integrateDataSource/list',
    method: 'get',
    params: query
  })
}

// 查询集成数据源详细
export function getIntegrateDataSource(id) {
  return request({
    url: '/integrateDataSource/' + id,
    method: 'get'
  })
}

// 新增集成数据源
export function addIntegrateDataSource(data) {
  return request({
    url: '/integrateDataSource',
    method: 'post',
    data: data
  })
}

// 修改集成数据源
export function updateIntegrateDataSource(data) {
  return request({
    url: '/integrateDataSource',
    method: 'put',
    data: data
  })
}

// 删除集成数据源
export function delIntegrateDataSource(id) {
  return request({
    url: '/integrateDataSource/' + id,
    method: 'delete'
  })
}

// 测试连接
export function testConnection(data) {
  return request({
    url: '/integrateDataSource/test',
    method: 'post',
    data: data
  })
}
