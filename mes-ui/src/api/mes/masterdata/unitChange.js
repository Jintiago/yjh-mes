import request from '@/utils/request'

// 查询计量单位转换列表
export function listUnitChange(query) {
  return request({
    url: '/masterdata/unitChange/list',
    method: 'get',
    params: query
  })
}

// 查询计量单位转换详细
export function getUnitChange(id) {
  return request({
    url: '/masterdata/unitChange/' + id,
    method: 'get'
  })
}

// 新增计量单位转换
export function addUnitChange(data) {
  return request({
    url: '/masterdata/unitChange',
    method: 'post',
    data: data
  })
}

// 修改计量单位转换
export function updateUnitChange(data) {
  return request({
    url: '/masterdata/unitChange',
    method: 'put',
    data: data
  })
}

// 删除计量单位转换
export function delUnitChange(id) {
  return request({
    url: '/masterdata/unitChange/' + id,
    method: 'delete'
  })
}
