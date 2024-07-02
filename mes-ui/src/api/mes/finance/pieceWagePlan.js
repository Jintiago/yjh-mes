import request from '@/utils/request'

// 查询计件工资方案列表
export function listPieceWagePlan(query) {
  return request({
    url: '/finance/pieceWagePlan/list',
    method: 'get',
    params: query
  })
}

// 查询计件工资方案详细
export function getPieceWagePlan(id) {
  return request({
    url: '/finance/pieceWagePlan/' + id,
    method: 'get'
  })
}

// 新增计件工资方案
export function addPieceWagePlan(data) {
  return request({
    url: '/finance/pieceWagePlan',
    method: 'post',
    data: data
  })
}

// 修改计件工资方案
export function updatePieceWagePlan(data) {
  return request({
    url: '/finance/pieceWagePlan',
    method: 'put',
    data: data
  })
}

// 删除计件工资方案
export function delPieceWagePlan(id) {
  return request({
    url: '/finance/pieceWagePlan/' + id,
    method: 'delete'
  })
}

// 保存计件工资方案
export function savePieceWagePlan(data) {
  return request({
    url: '/finance/pieceWagePlan/save',
    method: 'post',
    data: data
  })
}
