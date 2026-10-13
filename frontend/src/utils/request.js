import request from "axios";
import { message } from "ant-design-vue";
import router from "../router/index";

var token = localStorage.getItem("token");
var service = request.create({
  baseURL: "http://localhost:8099/api",
  timeout: 50000,
});

service.interceptors.request.use(
  (config) => {
    console.log("执行config");
    let token = localStorage.getItem("token");
    if (token != null) {
      config.headers["Authorization"] = token; // 设置请求头
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

service.interceptors.response.use(
  (response) => {
    if (
      response.config.skipInterceptor ||
      response.request.responseType === "blob" ||
      response.request.responseType === "arraybuffer"
    ) {
      return response;
    }
    const res = response.data;
    //判断response状态
    if (!res.status) {
      console.error("请求响应数据:", res);
      message.error("请求错误: " + res.msg);
    }
    if (res.code === 403) router.push("/403");
    return res;
  },
  (error) => {
    message.error(error);
    router.push("/500");
    return Promise.reject(error);
  }
);
console.log("执行request.js2");

export default service;
