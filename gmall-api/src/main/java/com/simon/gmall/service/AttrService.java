package com.simon.gmall.service;

import com.simon.gmall.bean.PmsBaseAttrInfo;
import com.simon.gmall.bean.PmsBaseAttrValue;
import com.simon.gmall.bean.PmsBaseSaleAttr;

import java.util.List;
import java.util.Set;

public interface AttrService {

    List<PmsBaseAttrInfo> getAttrInfoList(String catalog3Id);

    String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo);

    List<PmsBaseAttrValue> getAttrValueList(String attrId);

    List<PmsBaseSaleAttr> baseSaleAttrList();

    List<PmsBaseAttrInfo> getAttrValueListByValueId(Set<String> valueIdSet);
}
