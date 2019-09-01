package com.simon.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.simon.gmall.bean.PmsBaseAttrInfo;
import com.simon.gmall.bean.PmsBaseAttrValue;
import com.simon.gmall.bean.PmsBaseSaleAttr;
import com.simon.gmall.manage.mapper.PmsBaseAttrInfoMapper;
import com.simon.gmall.manage.mapper.PmsBaseAttrValueMapper;
import com.simon.gmall.manage.mapper.PmsBaseSaleAttrMapper;
import com.simon.gmall.service.AttrService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class AttrServiceImpl implements AttrService {

    @Autowired
    private PmsBaseAttrInfoMapper pmsBaseAttrInfoMapper;

    @Autowired
    private PmsBaseAttrValueMapper pmsBaseAttrValueMapper;

    @Autowired
    private PmsBaseSaleAttrMapper pmsBaseSaleAttrMapper;

    /**
     * 根据三级分类id获得对应分类平台属性
     *
     * @param catalog3Id
     * @return
     */
    @Override
    public List<PmsBaseAttrInfo> getAttrInfoList(String catalog3Id) {

        PmsBaseAttrInfo pmsBaseAttrInfo = new PmsBaseAttrInfo();
        pmsBaseAttrInfo.setCatalog3Id(catalog3Id);
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = pmsBaseAttrInfoMapper.select(pmsBaseAttrInfo);
        for (PmsBaseAttrInfo attrInfo : pmsBaseAttrInfos) {

            PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
            pmsBaseAttrValue.setAttrId(attrInfo.getId());
            List<PmsBaseAttrValue> baseAttrValues = pmsBaseAttrValueMapper.select(pmsBaseAttrValue);
            attrInfo.setAttrValueList(baseAttrValues);

        }

        return pmsBaseAttrInfos;

    }

    /**
     * 新增和保存平台属性名和属性值
     *
     * @param pmsBaseAttrInfo
     * @return
     */
    @Override
    public String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo) {

        //获得平台属性的id
        String id = pmsBaseAttrInfo.getId();

        if(StringUtils.isBlank(id)){
            //id为空，新增平台属性

            //保存平台属性
            pmsBaseAttrInfoMapper.insertSelective(pmsBaseAttrInfo);
            //平台属性包含多个属性值
            List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
            for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {

                //设置其属性id
                pmsBaseAttrValue.setAttrId(pmsBaseAttrInfo.getId());
                //保存
                pmsBaseAttrValueMapper.insertSelective(pmsBaseAttrValue);
            }

        }else {
            //id不为空，更新平台属性

            //更新平台属性
            Example example = new Example(PmsBaseAttrInfo.class);
            example.createCriteria().andEqualTo("id",id);
            pmsBaseAttrInfoMapper.updateByExampleSelective(pmsBaseAttrInfo,example);


            //更新平台属性的值列表
            //先将原有的删除
            PmsBaseAttrValue pmsBaseAttrValueDel = new PmsBaseAttrValue();
            pmsBaseAttrValueDel.setAttrId(id);
            pmsBaseAttrValueMapper.delete(pmsBaseAttrValueDel);

            //更新
            List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
            for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {

                //设置其属性id
                pmsBaseAttrValue.setAttrId(pmsBaseAttrInfo.getId());
                //保存
                pmsBaseAttrValueMapper.insertSelective(pmsBaseAttrValue);
            }
        }

        return "success";
    }

    /**
     * 点击修改平台属性
     * 回显给页面该属性的值列表
     */
    @Override
    public List<PmsBaseAttrValue> getAttrValueList(String attrId) {

        PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
        pmsBaseAttrValue.setAttrId(attrId);
        List<PmsBaseAttrValue> pmsBaseAttrValues = pmsBaseAttrValueMapper.select(pmsBaseAttrValue);
        return pmsBaseAttrValues;

    }

    /**
     * 添加Spu,显示销售属性下拉列表
     * @return
     */
    @Override
    public List<PmsBaseSaleAttr> baseSaleAttrList() {

        return pmsBaseSaleAttrMapper.selectAll();
    }


    /**
     * 根据平台属性值的Id，查询到平台属性列表
     * @param valueIdSet
     * @return
     */
    @Override
    public List<PmsBaseAttrInfo> getAttrValueListByValueId(Set<String> valueIdSet) {

        //将集合转换成字符串
        String valueIdStr = StringUtils.join(valueIdSet, ",") ;

        List<PmsBaseAttrInfo> pmsBaseAttrInfos = pmsBaseAttrInfoMapper.selectAttrValueListByValueId(valueIdStr);

        return pmsBaseAttrInfos;
    }
}
