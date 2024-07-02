import request from '@/utils/request'

// 查询质检等级列表
export function listQualityLevel(query) {
  return request({
    url: '/quality/qualityLevel/list',
    method: 'get',
    params: query
  })
}

// 查询质检等级详细
export function getQualityLevel(id) {
  return request({
    url: '/quality/qualityLevel/' + id,
    method: 'get'
  })
}

// 新增质检等级
export function addQualityLevel(data) {
  return request({
    url: '/quality/qualityLevel',
    method: 'post',
    data: data
  })
}

// 修改质检等级
export function updateQualityLevel(data) {
  return request({
    url: '/quality/qualityLevel',
    method: 'put',
    data: data
  })
}

// 删除质检等级
export function delQualityLevel(id) {
  return request({
    url: '/quality/qualityLevel/' + id,
    method: 'delete'
  })
}
