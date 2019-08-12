package com.simon.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.simon.gmall.bean.PmsBaseCatalog1;
import com.simon.gmall.bean.PmsBaseCatalog2;
import com.simon.gmall.bean.PmsBaseCatalog3;
import com.simon.gmall.manage.mapper.PmsBaseCatalog1Mapper;
import com.simon.gmall.manage.mapper.PmsBaseCatalog2Mapper;
import com.simon.gmall.manage.mapper.PmsBaseCatalog3Mapper;
import com.simon.gmall.service.CatagoryServie;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;


@Service
public class CatagoryServiceImpl implements CatagoryServie {


    @Autowired
    private PmsBaseCatalog1Mapper pmsBaseCatalog1Mapper;
    @Autowired
    private PmsBaseCatalog2Mapper pmsBaseCatalog2Mapper;
    @Autowired
    private PmsBaseCatalog3Mapper pmsBaseCatalog3Mapper;

    @Override
    public List<PmsBaseCatalog1> getCatalog1() {

        List<PmsBaseCatalog1> pmsBaseCatalog1s = pmsBaseCatalog1Mapper.selectAll();
        return pmsBaseCatalog1s;

    }

    @Override
    public List<PmsBaseCatalog2> getCatalog2(String catalog1Id) {

        PmsBaseCatalog2 pmsBaseCatalog2 = new PmsBaseCatalog2();
        pmsBaseCatalog2.setCatalog1Id(catalog1Id);
        List<PmsBaseCatalog2> pmsBaseCatalog2s = pmsBaseCatalog2Mapper.select(pmsBaseCatalog2);
        return pmsBaseCatalog2s;
    }

    @Override
    public List<PmsBaseCatalog3> getCatalog3(String catalog2Id) {

        PmsBaseCatalog3 pmsBaseCatalog3 = new PmsBaseCatalog3();
        pmsBaseCatalog3.setCatalog2Id(catalog2Id);
        List<PmsBaseCatalog3> pmsBaseCatalog3s = pmsBaseCatalog3Mapper.select(pmsBaseCatalog3);
        return pmsBaseCatalog3s;
    }
}
