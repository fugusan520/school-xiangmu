import service from "../utils/request";
// 查询列表
export function FindAllExpressFeedback(params) {
  return service({
    url: "/expressFeedback/list",
    method: "get",
    params,
  });
}

// 新增 / 修改（你后端是 save）
export function SaveExpressFeedback(data) {
  return service({
    url: "/expressFeedback/save",
    method: "post",
    data,
  });
}
export function UpdateExpressFeedback(data) {
    return service({
      url: "/expressFeedback/update",
      method: "post",
      data,
    });
  }
// 删除
export function DeleteExpressFeedback(id) {
  return service({
    url: `/expressFeedback/${id}`,
    method: "delete",
  });
}
