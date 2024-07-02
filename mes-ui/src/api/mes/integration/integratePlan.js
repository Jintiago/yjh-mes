import request from '@/utils/request'

// 查询集成方案列表
export function listIntegratePlan(query) {
  return request({
    url: '/integratePlan/list',
    method: 'get',
    params: query
  })
}

// 查询集成方案详细
export function getIntegratePlan(id) {
  return request({
    url: '/integratePlan/' + id,
    method: 'get'
  })
}

// 新增集成方案
export function addIntegratePlan(data) {
  return request({
    url: '/integratePlan',
    method: 'post',
    data: data
  })
}

// 修改集成方案
export function updateIntegratePlan(data) {
  return request({
    url: '/integratePlan',
    method: 'put',
    data: data
  })
}

// 删除集成方案
export function delIntegratePlan(id) {
  return request({
    url: '/integratePlan/' + id,
    method: 'delete'
  })
}

// 查询mes系统中的所有属性
export function mesField(query) {
  return request({
    url: '/integratePlan/mesFieldList',
    method: 'get',
    params: query
  })
}
