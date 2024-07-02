import request from '@/utils/request'

// 查询物料BOM列表
export function listBom(query) {
  return request({
    url: '/masterdata/bom/list',
    method: 'get',
    params: query
  })
}

// 查询物料BOM详细
export function getBom(id) {
  return request({
    url: '/masterdata/bom/' + id,
    method: 'get'
  })
}

// 新增物料BOM
export function addBom(data) {
  return request({
    url: '/masterdata/bom',
    method: 'post',
    data: data
  })
}

// 修改物料BOM
export function updateBom(data) {
  return request({
    url: '/masterdata/bom',
    method: 'put',
    data: data
  })
}

// 删除物料BOM
export function delBom(id) {
  return request({
    url: '/masterdata/bom/' + id,
    method: 'delete'
  })
}
