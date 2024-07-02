package cn.sourceplan.common.utils;

import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import cn.sourceplan.common.mapper.SqlCheckMapper;
import cn.sourceplan.system.domain.FieldExtend;
import cn.sourceplan.system.mapper.FieldExtendMapper;
import cn.sourceplan.system.service.IFieldExtendService;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * 自定义字段工具类
 * @author jinzhong
 */
@Component
public class FieldExtendUtil
{

    @Autowired
    private IFieldExtendService fieldExtendServiceT;

    private  static IFieldExtendService fieldExtendService;

    @PostConstruct
    public void init (){
        fieldExtendService=fieldExtendServiceT;
    }

    /**
     * 根据json进行excel导出
     */
    public static JSONObject exportExcelByJson(String sourceBill, JSONArray jsonArray, HttpServletResponse response) throws IOException {
        FieldExtend fieldExtendQW = new FieldExtend();
        fieldExtendQW.setSourceBill(sourceBill);
        List<FieldExtend> fieldExtendList = fieldExtendService.selectFieldExtendList(fieldExtendQW);

        // 通过工具类创建writer
        ExcelWriter writer = ExcelUtil.getWriter(true);

        //自定义标题别名
        for (int i = 0; i < fieldExtendList.size(); i++) {
            FieldExtend fieldExtend = fieldExtendList.get(i);
            writer.addHeaderAlias(fieldExtend.getField(), fieldExtend.getFieldName());
            String field = fieldExtend.getField();
            String[] split = field.split("\\.");
            //整理json数据格式
            for (int j = 0; j < jsonArray.size(); j++) {
                JSONObject jb = jsonArray.getJSONObject(j);
                //如果为扩展字段，则去fieldExtend字段中取值
                if(!"Y".equals(fieldExtend.getIsSystem())){
                    //如saleOrder
                    JSONObject feJb = jb.getJSONObject("extendField");
                    //如number
                    if(feJb!=null){
                        Object o = feJb.get(field);
                        jb.put(field,o);
                    }
                }else{
                    //为系统自带字段，并且具有子属性，例如saleOrder.number
                    if(split.length>1){
                        jsonArray.set(j,setSonAttributes(field,jb,split));
                    }
                }
            }
            // 合并单元格后的标题行，使用默认标题样式,包含客户名称customer的进行合并
            //writer.merge()
        }
        // 默认的，未添加alias的属性也会写出，如果想只写出加了别名的字段，可以调用此方法排除之
        writer.setOnlyAlias(true);
        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(jsonArray, true);
        //日期格式化
        DataFormat dataFormat = writer.getWorkbook().createDataFormat();
        //部分格式可参考org.apache.poi.ss.usermodel.BuiltinFormats
        short format = dataFormat.getFormat("yyyy-mm-dd");
        StyleSet styleSet = writer.getStyleSet();
        styleSet.getCellStyleForDate()
                .setDataFormat(format);
        //列宽自适应
        setSizeColumn(writer.getSheet(),fieldExtendList.size()-1);

        // number type
        CellStyle numberCellStyle = styleSet.getCellStyleForNumber(); //获取单元格的数值样式

        numberCellStyle.setDataFormat(dataFormat.getFormat("0")); //设置小数样式

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename="+sourceBill+".xlsx");
        ServletOutputStream out=response.getOutputStream();
        writer.flush(out, true);
        // 关闭writer，释放内存
        writer.close();
        IoUtil.close(out);

        return  null;
    }

    /**
     * 自适应宽度(中文支持)
     * @param sheet
     * @param size 因为for循环从0开始，size值为 列数-1
     */
    public static void setSizeColumn(Sheet sheet, int size) {
        for (int columnNum = 0; columnNum <= size; columnNum++) {
            int columnWidth = sheet.getColumnWidth(columnNum) / 256;
            for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
                Row currentRow;
                //当前行未被使用过
                if (sheet.getRow(rowNum) == null) {
                    currentRow = sheet.createRow(rowNum);
                } else {
                    currentRow = sheet.getRow(rowNum);
                }

                if (currentRow.getCell(columnNum) != null) {
                    Cell currentCell = currentRow.getCell(columnNum);
                    if (currentCell.getCellType() == CellType.STRING) {
                        int length = currentCell.getStringCellValue().getBytes().length;
                        if (columnWidth < length) {
                            columnWidth = length;
                        }
                    }
                }
            }
            sheet.setColumnWidth(columnNum, columnWidth * 256);
        }
    }

    /**
     * 设置子属性
     * @param field
     * @param jb
     * @param split
     */
    public static JSONObject  setSonAttributes( String field,JSONObject jb,String[] split){
        //如saleOrder
        JSONObject sonJb = jb.getJSONObject(split[0]);
        //如number
        jb.put(field,sonJb.get(split[1]));
        return jb;
    }

}
