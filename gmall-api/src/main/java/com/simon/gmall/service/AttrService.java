package com.simon.gmall.service;

import com.simon.gmall.bean.PmsBaseAttrInfo;

import java.util.List;

public interface AttrService {

    List<PmsBaseAttrInfo> getAttrInfoList(String catalog3Id);

}
