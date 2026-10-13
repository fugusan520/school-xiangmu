import service from "../utils/request";
// 查询列表
export function listNotice(params) {
  return service({
    url: "/inventory/listNotice",
    method: "get",
    params,
  });
}

// 新增 / 修改（你后端是 save）
export function addNotice(data) {
  return service({
    url: "/inventory/addNotice",
    method: "post",
    data,
  });
}
export function updateNotice(data) {
    return service({
      url: "/inventory/updateNotice",
      method: "post",
      data,
    });
  }
// 删除
export function deleteNotice(id) {
  return service({
    url: `/inventory/deleteNotice?id=` + id,
    method: "delete",
  });
}
