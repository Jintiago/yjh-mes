import request from '@/utils/request'

// 查询物料分类列表
export function listMaterialClass(query) {
  return request({
    url: '/masterdata/materialClass/list',
    method: 'get',
    params: query
  })
}

// 查询物料分类详细
export function getMaterialClass(id) {
  return request({
    url: '/masterdata/materialClass/' + id,
    method: 'get'
  })
}

// 新增物料分类
export function addMaterialClass(data) {
  return request({
    url: '/masterdata/materialClass',
    method: 'post',
    data: data
  })
}

// 修改物料分类
export function updateMaterialClass(data) {
  return request({
    url: '/masterdata/materialClass',
    method: 'put',
    data: data
  })
}

// 删除物料分类
export function delMaterialClass(id) {
  return request({
    url: '/masterdata/materialClass/' + id,
    method: 'delete'
  })
}
