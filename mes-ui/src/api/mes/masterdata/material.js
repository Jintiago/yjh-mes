import request from '@/utils/request'

// 查询物料列表
export function listMaterial(query) {
  return request({
    url: '/masterdata/material/list',
    method: 'get',
    params: query
  })
}

// 查询物料详细
export function getMaterial(id) {
  return request({
    url: '/masterdata/material/' + id,
    method: 'get'
  })
}

// 新增物料
export function addMaterial(data) {
  return request({
    url: '/masterdata/material',
    method: 'post',
    data: data
  })
}

// 修改物料
export function updateMaterial(data) {
  return request({
    url: '/masterdata/material',
    method: 'put',
    data: data
  })
}

// 删除物料
export function delMaterial(id) {
  return request({
    url: '/masterdata/material/' + id,
    method: 'delete'
  })
}
