import service from "../utils/request";

export function fetchExpressStockList(params) {
    return service({
        url: '/express/stock/list',
        method: 'get',
        params: params
    })
}

// api/expressStockApi.js
export const exportExpressStock = (params) => {
    return request({
      url: '/express/stock/export',
      method: 'get',
      params,
      responseType: 'blob',
      skipInterceptor: true  // ⚡ 这个自定义标识
    });
  };
  