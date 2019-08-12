package com.simon.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.simon.gmall.bean.PmsBaseCatalog1;
import com.simon.gmall.bean.PmsBaseCatalog2;
import com.simon.gmall.bean.PmsBaseCatalog3;
import com.simon.gmall.service.CatagoryServie;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class CatagoryController {


    @Reference
    private CatagoryServie catagoryServie;


    /**
     * 查询一级分类
     * @return
     */
   @RequestMapping("getCatalog1")
    public List<PmsBaseCatalog1> getCatalog1(){

       List<PmsBaseCatalog1>  pmsBaseCatalog1s = catagoryServie.getCatalog1();

       return pmsBaseCatalog1s;
   }


    /**
     * 查询二级分类
     * @param catalog1Id
     * @return
     */
    @RequestMapping("getCatalog2")
    public List<PmsBaseCatalog2> getCatalog2(String catalog1Id){

        List<PmsBaseCatalog2>  pmsBaseCatalog2s = catagoryServie.getCatalog2(catalog1Id);

        return pmsBaseCatalog2s;
    }


    /**
     * 查询三级分类
     * @param catalog2Id
     * @return
     */
    @RequestMapping("getCatalog3")
    public List<PmsBaseCatalog3> getCatalog3(String catalog2Id){

        List<PmsBaseCatalog3>  pmsBaseCatalog3s = catagoryServie.getCatalog3(catalog2Id);

        return pmsBaseCatalog3s;
    }


}
