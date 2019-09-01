package com.simon.gmall.manage.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.simon.gmall.bean.PmsBaseAttrInfo;
import com.simon.gmall.bean.PmsBaseAttrValue;
import com.simon.gmall.bean.PmsBaseSaleAttr;
import com.simon.gmall.service.AttrService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin
public class AttrController {

    @Reference
    private AttrService attrService;

    /**
     * 根据三级分类的id获得平台属性展示出来
     * @param catalog3Id
     * @return
     */
    @RequestMapping("attrInfoList")
    public List<PmsBaseAttrInfo> getAttrInfoList(String catalog3Id){

        List<PmsBaseAttrInfo> pmsBaseAttrInfos= attrService.getAttrInfoList(catalog3Id);

        return pmsBaseAttrInfos;

    }

    /**
     * 新增或者修改平台属性通用方法
     * @param pmsBaseAttrInfo
     * @return
     */
    @RequestMapping("saveAttrInfo")
    public String saveAttrInfo(@RequestBody PmsBaseAttrInfo pmsBaseAttrInfo){

        String success = attrService.saveAttrInfo(pmsBaseAttrInfo);

        return "success";
    }


    /**
     * 点击修改平台属性
     * 回显给页面该属性的值列表
     * @param attrId
     * @return
     */
    @RequestMapping("getAttrValueList")
    public List<PmsBaseAttrValue> getAttrValueList(String attrId){


        List<PmsBaseAttrValue> pmsBaseAttrValues = attrService.getAttrValueList(attrId);
        return pmsBaseAttrValues;

    }


    @RequestMapping("baseSaleAttrList")
    @ResponseBody
    public List<PmsBaseSaleAttr> baseSaleAttrList(){

        List<PmsBaseSaleAttr> pmsBaseSaleAttrs = attrService.baseSaleAttrList();
        return pmsBaseSaleAttrs;
    }

}
