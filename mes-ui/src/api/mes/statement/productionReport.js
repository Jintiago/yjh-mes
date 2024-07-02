import request from "@/utils/request";

// 根据生产订单id查询各道工序生产清空
export function queryProcessBySaleOrderEntryId(id) {
  return request({
    url: "/reports/production/queryProcessBySaleOrderEntryId",
    method: "get",
    params: id,
  });
}

// 根据工序查询生产情况
export function queryProductionExecutionByProcess(processName) {
  return request({
    url: "/reports/production/queryProductionExecutionByProcess",
    method: "get",
    params: processName,
  });
}


