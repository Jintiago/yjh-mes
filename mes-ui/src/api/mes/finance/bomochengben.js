import request from '@/utils/request'

// 查询薄膜核算列表
export function listBomochengben(query) {
  return request({
    url: '/finance/bomochengben/list',
    method: 'get',
    params: query
  })
}

// 查询薄膜核算详细
export function getBomochengben(id) {
  return request({
    url: '/finance/bomochengben/' + id,
    method: 'get'
  })
}

// 新增薄膜核算
export function addBomochengben(data) {
  return request({
    url: '/finance/bomochengben',
    method: 'post',
    data: data
  })
}

// 修改薄膜核算
export function updateBomochengben(data) {
  return request({
    url: '/finance/bomochengben',
    method: 'put',
    data: data
  })
}

// 删除薄膜核算
export function delBomochengben(id) {
  return request({
    url: '/finance/bomochengben/' + id,
    method: 'delete'
  })
}


/*   内贸------------------------------------*/
// 查询薄膜核算列表
export function listBomochengbenNeimao(query) {
  return request({
    url: '/finance/bomochengben/listNeimao',
    method: 'get',
    params: query
  })
}

// 查询薄膜核算详细
export function getBomochengbenNeimao(id) {
  return request({
    url: '/finance/bomochengben/neimao/' + id,
    method: 'get'
  })
}

// 新增薄膜核算
export function addBomochengbenNeimao(data) {
  return request({
    url: '/finance/bomochengben/neimao',
    method: 'post',
    data: data
  })
}

// 修改薄膜核算
export function updateBomochengbenNeimao(data) {
  return request({
    url: '/finance/bomochengben/neimao',
    method: 'put',
    data: data
  })
}

// 删除薄膜核算
export function delBomochengbenNeimao(id) {
  return request({
    url: '/finance/bomochengben/neimao/' + id,
    method: 'delete'
  })
}
/*   外贸------------------------------------*/
// 查询薄膜核算列表
export function listBomochengbenWaimao(query) {
  return request({
    url: '/finance/bomochengben/listWaimao',
    method: 'get',
    params: query
  })
}

// 查询薄膜核算详细
export function getBomochengbenWaimao(id) {
  return request({
    url: '/finance/bomochengben/waimao/' + id,
    method: 'get'
  })
}

// 新增薄膜核算
export function addBomochengbenWaimao(data) {
  return request({
    url: '/finance/bomochengben/waimao',
    method: 'post',
    data: data
  })
}

// 修改薄膜核算
export function updateBomochengbenWaimao(data) {
  return request({
    url: '/finance/bomochengben/waimao',
    method: 'put',
    data: data
  })
}

// 删除薄膜核算
export function delBomochengbenWaimao(id) {
  return request({
    url: '/finance/bomochengben/waimao/' + id,
    method: 'delete'
  })
}
