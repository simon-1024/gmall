package com.simon.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.simon.gmall.bean.*;
import com.simon.gmall.manage.mapper.PmsProductImageMapper;
import com.simon.gmall.manage.mapper.PmsProductInfoMapper;
import com.simon.gmall.manage.mapper.PmsProductSaleAttrMapper;
import com.simon.gmall.manage.mapper.PmsProductSaleAttrValueMapper;
import com.simon.gmall.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class SpuServiceImpl implements SpuService {

    @Autowired
    private PmsProductInfoMapper pmsProductInfoMapper;

    @Autowired
    private PmsProductImageMapper pmsProductImageMapper;

    @Autowired
    private PmsProductSaleAttrMapper pmsProductSaleAttrMapper;

    @Autowired
    private PmsProductSaleAttrValueMapper pmsProductSaleAttrValueMapper;


    @Override
    public List<PmsProductInfo> spuList(String catalog3Id) {


        PmsProductInfo pmsProductInfo = new PmsProductInfo();
        pmsProductInfo.setCatalog3Id(catalog3Id);
        List<PmsProductInfo> pmsProductInfos = pmsProductInfoMapper.select(pmsProductInfo);

        return pmsProductInfos;
    }

    /**
     * 保存新增的Spu信息
     * 分成三个部分：
     * 商品信息
     * 商品图片信息
     * 商品属性
     * 商品属性值
     *
     * @param pmsProductInfo
     */
    @Override
    public void saveSpuInfo(PmsProductInfo pmsProductInfo) {


        //保存商品信息
        pmsProductInfoMapper.insertSelective(pmsProductInfo);

        String pid = pmsProductInfo.getId();

        //保存商品图片
        List<PmsProductImage> spuImageList = pmsProductInfo.getSpuImageList();
        for (PmsProductImage pmsProductImage : spuImageList) {
            pmsProductImage.setProductId(pid);
            pmsProductImageMapper.insertSelective(pmsProductImage);

        }

        //保存商品销售属性
        List<PmsProductSaleAttr> spuSaleAttrList = pmsProductInfo.getSpuSaleAttrList();
        for (PmsProductSaleAttr pmsProductSaleAttr : spuSaleAttrList) {

            pmsProductSaleAttr.setProductId(pid);
            pmsProductSaleAttrMapper.insertSelective(pmsProductSaleAttr);

            //保存商品销售属性值
            List<PmsProductSaleAttrValue> spuSaleAttrValueList = pmsProductSaleAttr.getSpuSaleAttrValueList();
            for (PmsProductSaleAttrValue pmsProductSaleAttrValue : spuSaleAttrValueList) {
                pmsProductSaleAttrValue.setProductId(pid);
                pmsProductSaleAttrValueMapper.insertSelective(pmsProductSaleAttrValue);

            }
        }
    }


    /**
     * 添加sku时回显属性
     *
     * @param spuId
     * @return
     */
    @Override
    public List<PmsProductSaleAttr> spuSaleAttrList(String spuId) {

        //查到销售属性列表
        PmsProductSaleAttr pmsProductSaleAttr = new PmsProductSaleAttr();
        pmsProductSaleAttr.setProductId(spuId);
        List<PmsProductSaleAttr> pmsProductSaleAttrs = pmsProductSaleAttrMapper.select(pmsProductSaleAttr);

        //查销售属性值
        for (PmsProductSaleAttr productSaleAttr : pmsProductSaleAttrs) {

            //这个商品的属性值
            PmsProductSaleAttrValue pmsProductSaleAttrValue = new PmsProductSaleAttrValue();
            pmsProductSaleAttrValue.setProductId(spuId);
            //这个属性的属性值列表
            pmsProductSaleAttrValue.setSaleAttrId(productSaleAttr.getSaleAttrId());
            List<PmsProductSaleAttrValue> pmsProductSaleAttrValues = pmsProductSaleAttrValueMapper.select(pmsProductSaleAttrValue);
            productSaleAttr.setSpuSaleAttrValueList(pmsProductSaleAttrValues);

        }

        return pmsProductSaleAttrs;
    }

    /**
     * 添加Sku时回显图片
     *
     * @param spuId
     * @return
     */
    @Override
    public List<PmsProductImage> getSpuImageList(String spuId) {

        PmsProductImage pmsProductImage = new PmsProductImage();
        pmsProductImage.setProductId(spuId);
        List<PmsProductImage> pmsProductImages = pmsProductImageMapper.select(pmsProductImage);

        return pmsProductImages;
    }



    public List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(String productId,String skuId) {

/*        PmsProductSaleAttr pmsProductSaleAttr = new PmsProductSaleAttr();
        pmsProductSaleAttr.setProductId(productId);
        List<PmsProductSaleAttr> pmsProductSaleAttrs = pmsProductSaleAttrMapper.select(pmsProductSaleAttr);

        for (PmsProductSaleAttr productSaleAttr : pmsProductSaleAttrs) {

            String saleAttrId = productSaleAttr.getSaleAttrId();
            PmsProductSaleAttrValue pmsProductSaleAttrValue = new PmsProductSaleAttrValue();
            pmsProductSaleAttrValue.setProductId(productId);
            pmsProductSaleAttrValue.setSaleAttrId(saleAttrId);
            List<PmsProductSaleAttrValue> pmsProductSaleAttrValues = pmsProductSaleAttrValueMapper.select(pmsProductSaleAttrValue);
            productSaleAttr.setSpuSaleAttrValueList(pmsProductSaleAttrValues);

        }*/
        List<PmsProductSaleAttr> pmsProductSaleAttrs = pmsProductSaleAttrMapper.getSpuSaleAttrListCheckBySku(productId,skuId);

        return pmsProductSaleAttrs;
    }
}
