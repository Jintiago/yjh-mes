package cn.sourceplan.barcode.service;

import java.util.List;
import cn.sourceplan.barcode.domain.BarcodeRecord;

/**
 * 条码生成记录Service接口
 * 
 * @author jinzhong
 * @date 2023-04-15
 */
public interface IBarcodeRecordService 
{
    /**
     * 查询条码生成记录
     * 
     * @param id 条码生成记录主键
     * @return 条码生成记录
     */
    public BarcodeRecord selectBarcodeRecordById(Long id);

    /**
     * 查询条码生成记录列表
     * 
     * @param barcodeRecord 条码生成记录
     * @return 条码生成记录集合
     */
    public List<BarcodeRecord> selectBarcodeRecordList(BarcodeRecord barcodeRecord);

    /**
     * 新增条码生成记录
     * 
     * @param barcodeRecord 条码生成记录
     * @return 结果
     */
    public int insertBarcodeRecord(BarcodeRecord barcodeRecord);

    /**
     * 修改条码生成记录
     * 
     * @param barcodeRecord 条码生成记录
     * @return 结果
     */
    public int updateBarcodeRecord(BarcodeRecord barcodeRecord);

    /**
     * 批量删除条码生成记录
     * 
     * @param ids 需要删除的条码生成记录主键集合
     * @return 结果
     */
    public int deleteBarcodeRecordByIds(Long[] ids);

    /**
     * 删除条码生成记录信息
     * 
     * @param id 条码生成记录主键
     * @return 结果
     */
    public int deleteBarcodeRecordById(Long id);
}
