package com.simon.gmall.service;

import com.simon.gmall.bean.PmsProductImage;
import com.simon.gmall.bean.PmsProductInfo;
import com.simon.gmall.bean.PmsProductSaleAttr;

import java.util.List;

public interface SpuService {

    public List<PmsProductInfo> spuList(String catalog3Id) ;

    void saveSpuInfo(PmsProductInfo pmsProductInfo);

    List<PmsProductSaleAttr> spuSaleAttrList(String spuId);

    List<PmsProductImage> getSpuImageList(String spuId);

    List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(String productId,String skuId);
}
