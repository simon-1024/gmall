package com.simon.gmall.manage.mapper;

import com.simon.gmall.bean.PmsProductSaleAttr;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface PmsProductSaleAttrMapper extends Mapper<PmsProductSaleAttr> {

    List<PmsProductSaleAttr> getSpuSaleAttrListCheckBySku(
            @Param("productId") String productId,
            @Param("skuId") String skuId
    );

}
