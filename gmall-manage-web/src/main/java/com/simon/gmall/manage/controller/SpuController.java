package com.simon.gmall.manage.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.simon.gmall.bean.PmsBaseAttrValue;
import com.simon.gmall.bean.PmsProductImage;
import com.simon.gmall.bean.PmsProductInfo;
import com.simon.gmall.bean.PmsProductSaleAttr;
import com.simon.gmall.manage.util.PmsUploadUtil;
import com.simon.gmall.service.SpuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@CrossOrigin
public class SpuController {


    @Reference
    private SpuService spuService;


    @RequestMapping("spuImageList")
    @ResponseBody
    public List<PmsProductImage> spuImageList(String spuId){

        List<PmsProductImage> pmsProductImages = spuService.getSpuImageList(spuId);

        return pmsProductImages ;

    }



    /**
     * 添加Sku时回显平台属性值：
     * 根据spuId
     */
    @RequestMapping("spuSaleAttrList")
    @ResponseBody
    public List<PmsProductSaleAttr> spuSaleAttrList(String spuId){

        List<PmsProductSaleAttr> pmsProductSaleAttrs = spuService.spuSaleAttrList(spuId);

        return pmsProductSaleAttrs ;
    }


    /**
     * 根据三级id返回该分类下的商品列表
     * @param catalog3Id
     * @return
     */
    @RequestMapping("spuList")
    @ResponseBody
    public List<PmsProductInfo> spuList(String catalog3Id){

        List<PmsProductInfo> pmsProductInfos = spuService.spuList(catalog3Id);
        return  pmsProductInfos ;

    }

    /**
     * 上传Spu图片 ，并将上传后的url返回给前端，回显图片
     * @param multipartFile
     * @return
     */
    @RequestMapping("fileUpload")
    @ResponseBody
    public String fileUpload(@RequestParam("file") MultipartFile multipartFile){

        String uploadImage = PmsUploadUtil.uploadImage(multipartFile);

        return uploadImage;

    }


    /**
     * 添加Spu,保存操作
     * @param pmsProductInfo
     * @return
     */
    @RequestMapping("saveSpuInfo")
    @ResponseBody
    public String saveSpuInfo(@RequestBody PmsProductInfo pmsProductInfo){

        spuService.saveSpuInfo(pmsProductInfo);

        return "success";
    }
}
