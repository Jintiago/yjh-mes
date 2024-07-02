import request from '@/utils/request'

// 查询条码生成记录列表
export function listBarcodeRecord(query) {
  return request({
    url: '/barcode/barcodeRecord/list',
    method: 'get',
    params: query
  })
}

// 查询条码生成记录详细
export function getBarcodeRecord(id) {
  return request({
    url: '/barcode/barcodeRecord/' + id,
    method: 'get'
  })
}

// 新增条码生成记录
export function addBarcodeRecord(data) {
  return request({
    url: '/barcode/barcodeRecord',
    method: 'post',
    data: data
  })
}

// 修改条码生成记录
export function updateBarcodeRecord(data) {
  return request({
    url: '/barcode/barcodeRecord',
    method: 'put',
    data: data
  })
}

// 删除条码生成记录
export function delBarcodeRecord(id) {
  return request({
    url: '/barcode/barcodeRecord/' + id,
    method: 'delete'
  })
}
