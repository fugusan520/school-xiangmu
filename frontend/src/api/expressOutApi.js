// src/api/expressApi.js
import service from '../utils/request'

export function fetchExpressOutList(params) {
  return service({
    url: '/express/out/list',
    method: 'get',
    params: params
  })
}

export function   saveExpressOut(data) {
  return service({
    url: '/express/out/save',
    method: 'post',
    data: data
  })
}