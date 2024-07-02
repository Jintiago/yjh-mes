package cn.sourceplan.integration.controller;

import cn.hutool.http.HttpRequest;

import cn.sourceplan.common.annotation.Log;
import cn.sourceplan.common.core.controller.BaseController;
import cn.sourceplan.common.core.domain.AjaxResult;
import cn.sourceplan.common.core.page.TableDataInfo;
import cn.sourceplan.common.enums.BusinessType;
import cn.sourceplan.common.utils.StringUtils;
import cn.sourceplan.common.utils.poi.ExcelUtil;
import cn.sourceplan.integration.domain.InteDataSourceField;
import cn.sourceplan.integration.domain.IntegrateDataSource;
import cn.sourceplan.integration.service.IIntegrateDataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.HttpCookie;
import java.util.List;

/**
 * 集成数据源Controller
 *
 * @author 靳忠
 * @date 2023-02-09
 */
@RestController
@RequestMapping("/integrateDataSource")
public class IntegrateDataSourceController extends BaseController
{
    @Autowired
    private IIntegrateDataSourceService integrateDataSourceService;


    /**
     * 查询集成数据源列表
     */
    @GetMapping("/list")
    public TableDataInfo list(IntegrateDataSource integrateDataSource)
    {
        startPage();
        List<IntegrateDataSource> list = integrateDataSourceService.selectIntegrateDataSourceList(integrateDataSource);
        return getDataTable(list);
    }

    /**
     * 导出集成数据源列表
     */
    @Log(title = "集成数据源", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, IntegrateDataSource integrateDataSource)
    {
        List<IntegrateDataSource> list = integrateDataSourceService.selectIntegrateDataSourceList(integrateDataSource);
        ExcelUtil<IntegrateDataSource> util = new ExcelUtil<IntegrateDataSource>(IntegrateDataSource.class);
        util.exportExcel(response, list, "集成数据源数据");
    }

    /**
     * 获取集成数据源详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(integrateDataSourceService.selectIntegrateDataSourceById(id));
    }

    /**
     * 新增集成数据源
     */
    @Log(title = "集成数据源", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody IntegrateDataSource integrateDataSource)
    {

        return integrateDataSourceService.insertIntegrateDataSource(integrateDataSource);
    }

    /**
     * 修改集成数据源
     */
    @Log(title = "集成数据源", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody IntegrateDataSource integrateDataSource)
    {
        return integrateDataSourceService.updateIntegrateDataSource(integrateDataSource);
    }

    /**
     * 删除集成数据源
     */
    @Log(title = "集成数据源", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(integrateDataSourceService.deleteIntegrateDataSourceByIds(ids));
    }

    /**
     * 新增集成数据源
     */
    @Log(title = "集成数据源", businessType = BusinessType.INSERT)
    @PostMapping("/test")
    public AjaxResult test(@RequestBody IntegrateDataSource inteDataSource) throws Exception {

        //根据
        // 1.集成方式
        // 2.集成厂商
        // 3.集成系统名称
        // 4.集成系统版本
        //调用相对应厂商的集成工具类
        //1==1模拟上述条件
        if(1==1){
            //登录
            String s = "{\n" +
                    "    \"parameters\": [\n" +
                    "        \""+inteDataSource.getBookCode()+"\",\n" +
                    "        \""+inteDataSource.getUserName()+"\",\n" +
                    "        \""+inteDataSource.getAppId()+"\",\n" +
                    "        \""+inteDataSource.getSecretKey()+"\",\n" +
                    "        2052\n" +
                    "    ]\n" +
                    "}";
            List<HttpCookie> cookies =
                    HttpRequest.post(inteDataSource.getRequestAddress()+"Kingdee.BOS.WebApi.ServicesStub.AuthService.LoginByAppSecret.common.kdsvc")
                    .body(s).execute().getCookies();
            //查询
            //查询字段
            List<InteDataSourceField> inteDataSourceFieldList = inteDataSource.getInteDataSourceFieldList();
            String fieldKeys="";
            for (int i = 0; i < inteDataSourceFieldList.size(); i++) {
                if(i==inteDataSourceFieldList.size()-1){
                    fieldKeys+=inteDataSourceFieldList.get(i).getField();
                }else {
                    fieldKeys+=inteDataSourceFieldList.get(i).getField()+",";
                }
            }
            String formId = inteDataSource.getSourceBillCode();
            //过滤条件
            String filter= inteDataSource.getParameter1()== null ? "" : inteDataSource.getParameter1();;
            String s2 = inteDataSource.getParameter2() == null ? "1" : inteDataSource.getParameter2();
            String s3= inteDataSource.getParameter3() == null ? "10" : inteDataSource.getParameter3();

            String sB =
                    "{ \"parameters\" : [{" +
                            "\"FormId\":\""+formId+"\"," +
                            "\"FieldKeys\":\""+fieldKeys+"\","+
                            "\"FilterString\":\""+filter+"\","+
                            "\"OrderString\":\"\","+
                            "\"TopRowCount\":0," +
                            //分页信息
                            "\"StartRow\":"+s2+"," +
                            "\"Limit\":"+s3+"," +
                            "\"SubSystemId\":\"\"" +
                            "} ] }";
            String sBB= HttpRequest.post(inteDataSource.getRequestAddress()+"Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.ExecuteBillQuery.common.kdsvc")
                    .cookie(cookies)
                    .body(sB).execute().body();

            if (StringUtils.isBlank(sBB)){
                return AjaxResult.error(1002,"测试失败");
         }
            return AjaxResult.success(sBB);
        }
        return AjaxResult.error();
    }

}
