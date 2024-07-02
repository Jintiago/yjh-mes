import request from '@/utils/request'

// 查询天润薄膜核算维护列表
export function listBomoweihu(query) {
  return request({
    url: '/finance/bomoweihu/list',
    method: 'get',
    params: query
  })
}

// 查询天润薄膜核算维护详细
export function getBomoweihu(priceType) {
  return request({
    url: '/finance/bomoweihu/' + priceType,
    method: 'get'
  })
}

// 新增天润薄膜核算维护
export function addBomoweihu(data) {
  return request({
    url: '/finance/bomoweihu',
    method: 'post',
    data: data
  })
}

// 修改天润薄膜核算维护
export function updateBomoweihu(data) {
  return request({
    url: '/finance/bomoweihu',
    method: 'put',
    data: data
  })
}

// 删除天润薄膜核算维护
export function delBomoweihu(priceType) {
  return request({
    url: '/finance/bomoweihu/' + priceType,
    method: 'delete'
  })
}
