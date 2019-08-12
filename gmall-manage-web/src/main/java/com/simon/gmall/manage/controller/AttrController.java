package com.simon.gmall.manage.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.simon.gmall.bean.PmsBaseAttrInfo;
import com.simon.gmall.bean.PmsBaseAttrValue;
import com.simon.gmall.service.AttrService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class AttrController {

    @Reference
    private AttrService attrService;

    @RequestMapping("attrInfoList")
    public List<PmsBaseAttrInfo> getAttrInfoList(String catalog3Id){

        List<PmsBaseAttrInfo> pmsBaseAttrInfos= attrService.getAttrInfoList(catalog3Id);

        return pmsBaseAttrInfos;

    }


    @RequestMapping("saveAttrInfo")
    public String saveAttrInfo(@RequestBody PmsBaseAttrInfo pmsBaseAttrInfo){

        return "success";
    }

}
