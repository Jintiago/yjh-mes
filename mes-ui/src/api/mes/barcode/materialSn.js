import request from '@/utils/request'

// 查询物料SN码列表
export function listMaterialSn(query) {
  return request({
    url: '/barcode/materialSn/list',
    method: 'get',
    params: query
  })
}

// 查询物料SN码详细
export function getMaterialSn(id) {
  return request({
    url: '/barcode/materialSn/' + id,
    method: 'get'
  })
}

// 新增物料SN码
export function addMaterialSn(data) {
  return request({
    url: '/barcode/materialSn',
    method: 'post',
    data: data
  })
}

// 修改物料SN码
export function updateMaterialSn(data) {
  return request({
    url: '/barcode/materialSn',
    method: 'put',
    data: data
  })
}

// 删除物料SN码
export function delMaterialSn(id) {
  return request({
    url: '/barcode/materialSn/' + id,
    method: 'delete'
  })
}
