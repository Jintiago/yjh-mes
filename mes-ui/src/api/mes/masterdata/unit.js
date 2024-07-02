import request from '@/utils/request'

// 查询计量单位列表
export function listUnit(query) {
  return request({
    url: '/masterdata/unit/list',
    method: 'get',
    params: query
  })
}

// 查询计量单位详细
export function getUnit(id) {
  return request({
    url: '/masterdata/unit/' + id,
    method: 'get'
  })
}

// 新增计量单位
export function addUnit(data) {
  return request({
    url: '/masterdata/unit',
    method: 'post',
    data: data
  })
}

// 修改计量单位
export function updateUnit(data) {
  return request({
    url: '/masterdata/unit',
    method: 'put',
    data: data
  })
}

// 删除计量单位
export function delUnit(id) {
  return request({
    url: '/masterdata/unit/' + id,
    method: 'delete'
  })
}
