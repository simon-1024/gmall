package com.simon.gmall.service;

import com.simon.gmall.bean.PmsBaseCatalog1;
import com.simon.gmall.bean.PmsBaseCatalog2;
import com.simon.gmall.bean.PmsBaseCatalog3;

import java.util.List;

public interface CatagoryServie {

    List<PmsBaseCatalog1> getCatalog1();

    List<PmsBaseCatalog2> getCatalog2(String catalog1Id);

    List<PmsBaseCatalog3> getCatalog3(String catalog2Id);


}
