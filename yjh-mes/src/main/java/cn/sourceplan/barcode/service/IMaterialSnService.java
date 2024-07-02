package cn.sourceplan.barcode.service;

import java.util.List;
import cn.sourceplan.barcode.domain.MaterialSn;

/**
 * 物料SN码Service接口
 * 
 * @author jinzhong
 * @date 2023-04-15
 */
public interface IMaterialSnService 
{
    /**
     * 查询物料SN码
     * 
     * @param id 物料SN码主键
     * @return 物料SN码
     */
    public MaterialSn selectMaterialSnById(Long id);

    /**
     * 查询物料SN码列表
     * 
     * @param materialSn 物料SN码
     * @return 物料SN码集合
     */
    public List<MaterialSn> selectMaterialSnList(MaterialSn materialSn);

    /**
     * 新增物料SN码
     * 
     * @param materialSn 物料SN码
     * @return 结果
     */
    public int insertMaterialSn(MaterialSn materialSn);

    /**
     * 修改物料SN码
     * 
     * @param materialSn 物料SN码
     * @return 结果
     */
    public int updateMaterialSn(MaterialSn materialSn);

    /**
     * 批量删除物料SN码
     * 
     * @param ids 需要删除的物料SN码主键集合
     * @return 结果
     */
    public int deleteMaterialSnByIds(Long[] ids);

    /**
     * 删除物料SN码信息
     * 
     * @param id 物料SN码主键
     * @return 结果
     */
    public int deleteMaterialSnById(Long id);
}
