package com.simon.gmall.item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.simon.gmall.bean.PmsProductSaleAttr;
import com.simon.gmall.bean.PmsSkuInfo;
import com.simon.gmall.bean.PmsSkuSaleAttrValue;
import com.simon.gmall.service.SkuService;
import com.simon.gmall.service.SpuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin
public class ItemController {

    @Reference
    private SkuService skuService;

    @Reference
    private SpuService spuService;


    @RequestMapping("/{skuId}.html")
    public String getSkuInfo(@PathVariable("skuId") String skuId, ModelMap map) {

        PmsSkuInfo pmsSkuInfo = skuService.getSkuById(skuId);

        List<PmsProductSaleAttr> pmsProductSaleAttrs = spuService.spuSaleAttrListCheckBySku(pmsSkuInfo.getProductId(), skuId);

        map.put("skuInfo", pmsSkuInfo);
        map.put("spuSaleAttrListCheckBySku", pmsProductSaleAttrs);

        //查询当前sku的spu的其他sku的集合hash表
        Map<String,String> skuSaleAttrHash = new HashMap<>();
        List<PmsSkuInfo> pmsSkuInfos=skuService.getSkuSaleAttrValueListBySpu(pmsSkuInfo.getProductId());

        for (PmsSkuInfo skuInfo : pmsSkuInfos) {

            String k="";
            String v = skuInfo.getId();
            List<PmsSkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();

            for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : skuSaleAttrValueList) {

                k+=pmsSkuSaleAttrValue.getSaleAttrValueId()+"|";

            }
            skuSaleAttrHash.put(k,v);
        }

        // 将sku的销售属性hash表放到页面
        String skuSaleAttrHashJsonStr = JSON.toJSONString(skuSaleAttrHash);
        map.put("skuSaleAttrHashJsonStr",skuSaleAttrHashJsonStr);

        return "item";

    }

}