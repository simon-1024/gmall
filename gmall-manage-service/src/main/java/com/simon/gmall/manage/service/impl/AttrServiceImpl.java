package com.simon.gmall.manage.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.simon.gmall.bean.PmsBaseAttrInfo;
import com.simon.gmall.manage.mapper.PmsBaseAttrInfoMapper;
import com.simon.gmall.service.AttrService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class AttrServiceImpl implements AttrService {

    @Autowired
    private PmsBaseAttrInfoMapper pmsBaseAttrInfoMapper;

    @Override
    public List<PmsBaseAttrInfo> getAttrInfoList(String catalog3Id) {

        PmsBaseAttrInfo pmsBaseAttrInfo = new PmsBaseAttrInfo();
        pmsBaseAttrInfo.setCatalog3Id(catalog3Id);
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = pmsBaseAttrInfoMapper.select(pmsBaseAttrInfo);

        return pmsBaseAttrInfos;

    }
}
