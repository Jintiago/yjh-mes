package cn.sourceplan.barcode.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.sourceplan.common.annotation.Log;
import cn.sourceplan.common.core.controller.BaseController;
import cn.sourceplan.common.core.domain.AjaxResult;
import cn.sourceplan.common.enums.BusinessType;
import cn.sourceplan.barcode.domain.BarcodeRecord;
import cn.sourceplan.barcode.service.IBarcodeRecordService;
import cn.sourceplan.common.utils.poi.ExcelUtil;
import cn.sourceplan.common.core.page.TableDataInfo;

/**
 * 条码生成记录Controller
 *
 * @author jinzhong
 * @date 2023-04-15
 */
@RestController
@RequestMapping("/barcode/barcodeRecord")
public class BarcodeRecordController extends BaseController
{
    @Autowired
    private IBarcodeRecordService barcodeRecordService;

    /**
     * 查询条码生成记录列表
     */
    @GetMapping("/list")
    public TableDataInfo list(BarcodeRecord barcodeRecord)
    {
        startPage();
        List<BarcodeRecord> list = barcodeRecordService.selectBarcodeRecordList(barcodeRecord);
        return getDataTable(list);
    }

    /**
     * 导出条码生成记录列表
     */
    @Log(title = "条码生成记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BarcodeRecord barcodeRecord)
    {
        List<BarcodeRecord> list = barcodeRecordService.selectBarcodeRecordList(barcodeRecord);
        ExcelUtil<BarcodeRecord> util = new ExcelUtil<BarcodeRecord>(BarcodeRecord.class);
        util.exportExcel(response, list, "条码生成记录数据");
    }

    /**
     * 获取条码生成记录详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(barcodeRecordService.selectBarcodeRecordById(id));
    }

    /**
     * 生成条码
     */
    @Log(title = "条码生成记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BarcodeRecord barcodeRecord)
    {
        return toAjax(barcodeRecordService.insertBarcodeRecord(barcodeRecord));
    }

    /**
     * 修改条码生成记录
     */
    @Log(title = "条码生成记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BarcodeRecord barcodeRecord)
    {
        return toAjax(barcodeRecordService.updateBarcodeRecord(barcodeRecord));
    }

    /**
     * 删除条码生成记录
     */
    @Log(title = "条码生成记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(barcodeRecordService.deleteBarcodeRecordByIds(ids));
    }

    /**
     * 打印成功后次数加1
     */
    @PostMapping(value = "/printAdd")
    public void callback(@RequestBody JSONObject json) {
        System.out.println("===================记录打印次数====================");
        System.out.println(json.toJSONString());
    }
}
