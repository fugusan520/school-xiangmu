// src/api/expressApi.js
import service from '../utils/request'

export function fetchExpressInList(params) {
  return service({
    url: '/express/in/list',
    method: 'get',
    params: params
  })
}

export function saveExpressIn (data) {
  return service({
    url: '/express/in/save',
    method: 'post',
    data: data
  })
}

export function deleteExpressIn(id) {
  return service({
    url: `/express/${id}`,
    method: 'delete'
  })
}
