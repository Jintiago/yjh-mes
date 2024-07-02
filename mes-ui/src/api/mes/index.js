import request from "@/utils/request";

// 获取产能走势图
export function outputTrend(query) {
  return request({
    url: "/statement/pcIndex/outputTrend",
    method: "get",
    params: query,
  });
}

// 工单延期率
export function defermentFactor(query) {
  return request({
    url: "/statement/pcIndex/defermentFactor",
    method: "get",
    params: query,
  });
}

// 生产合格率
export function qualificationRate(query) {
  return request({
    url: "/statement/pcIndex/qualificationRate",
    method: "get",
    params: query,
  });
}

// 不合格原因
export function unqualifiedReason(query) {
  return request({
    url: "/statement/pcIndex/unqualifiedReason",
    method: "get",
    params: query,
  });
}
