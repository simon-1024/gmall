package com.simon.gmall.service;

import com.simon.gmall.bean.PmsSearchParam;
import com.simon.gmall.bean.PmsSearchSkuInfo;

import java.util.List;

public interface SearchService {


    List<PmsSearchSkuInfo> list(PmsSearchParam pmsSearchParam);
}
