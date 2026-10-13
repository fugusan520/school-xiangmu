// src/api/expressApi.js
import service from '../utils/request'

export function fetchExpressList(params) {
  return service({
    url: '/express/list',
    method: 'get',
    params: params
  })
}
export function findAllCanUseList() {
  return service({
    url: '/express/listByStatus?status=0',
    method: 'get',
  })
}
export function findAllCanOutList() {
  return service({
    url: '/express/listByStatus?status=1',
    method: 'get',
  })
}

export function saveExpress(data) {
  return service({
    url: '/express/save',
    method: 'post',
    data: data
  })
}

export function deleteExpress(id) {
  return service({
    url: `/express/${id}`,
    method: 'delete'
  })
}
