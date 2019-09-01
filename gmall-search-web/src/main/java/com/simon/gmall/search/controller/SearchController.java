package com.simon.gmall.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.simon.gmall.bean.*;
import com.simon.gmall.service.AttrService;
import com.simon.gmall.service.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@CrossOrigin
public class SearchController {


    @Reference
    private SearchService searchService;

    @Reference
    private AttrService attrService;


    /**
     * 实现sku的搜索功能
     *
     * @param pmsSearchParam
     * @param modelMap
     * @return
     */
    @RequestMapping("list.html")
    public String list(PmsSearchParam pmsSearchParam, ModelMap modelMap) {

        //调用搜索服务，返回搜索结果
        List<PmsSearchSkuInfo> pmsSearchSkuInfos = searchService.list(pmsSearchParam);

        //将结果返回给页面
        modelMap.put("skuLsInfoList", pmsSearchSkuInfos);

        //抽取出搜索结果所包含的平台属性的集合
        Set<String> valueIdSet = new HashSet<>();
        for (PmsSearchSkuInfo pmsSearchSkuInfo : pmsSearchSkuInfos) {
            //sku中包含的平台属性集合
            List<PmsSkuAttrValue> skuAttrValueList = pmsSearchSkuInfo.getSkuAttrValueList();
            //获取平台属性的valueId
            for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
                String valueId = pmsSkuAttrValue.getValueId();
                valueIdSet.add(valueId);
            }
        }

        //根据valueId将属性列表查询出来
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = attrService.getAttrValueListByValueId(valueIdSet);
        modelMap.put("attrList", pmsBaseAttrInfos);

        String urlParam = getUrlParam(pmsSearchParam);
        modelMap.put("urlParam", urlParam);
        String keyword = pmsSearchParam.getKeyword();

        if (StringUtils.isNotBlank(keyword)) {
            modelMap.put("keyword", keyword);
        }

        //对平台属性集合进一步处理，去除当前的条件中的vauleId所在的属性组
        String[] delValueIds = pmsSearchParam.getValueId();
        List<PmsSearchCrumb> pmsSearchCrumbs = new ArrayList<>();
        Iterator<PmsBaseAttrInfo> iterator = pmsBaseAttrInfos.iterator();
        if (delValueIds != null) {
            for (String delValueId : delValueIds) {
                PmsSearchCrumb pmsSearchCrumb = new PmsSearchCrumb();
                //生成面包屑的参数
                pmsSearchCrumb.setValueId(delValueId);
                pmsSearchCrumb.setUrlParam(getUrlParamForCrumb(pmsSearchParam, delValueId));
                while (iterator.hasNext()) {
                    PmsBaseAttrInfo pmsBaseAttrInfo = iterator.next();
                    List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
                    for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
                        String valueId = pmsBaseAttrValue.getId();
                        if (delValueId.equals(valueId)) {
                            pmsSearchCrumb.setValueName(pmsBaseAttrValue.getValueName());
                            //删除该属性组
                            iterator.remove();
                        }
                    }
                }
                pmsSearchCrumbs.add(pmsSearchCrumb);
            }
        }

        //制作面包屑
        //多个面包屑碎片
        //单独制作
/*        List<PmsSearchCrumb> pmsSearchCrumbs = new ArrayList<>();
        if (delValueIds != null) {
            //valueId参数不为空，说明当前请求中出现了面包屑参数，每一个属性参数对应一个面包屑
            for (String delValueId : delValueIds) {
                PmsSearchCrumb pmsSearchCrumb = new PmsSearchCrumb();
                //生成面包屑的参数
                pmsSearchCrumb.setValueId(delValueId);
                pmsSearchCrumb.setValueName(delValueId);
                pmsSearchCrumb.setUrlParam(getUrlParamForCrumb(pmsSearchParam, delValueId));
                pmsSearchCrumbs.add(pmsSearchCrumb);

            }
        }*/

        modelMap.put("attrValueSelectedList", pmsSearchCrumbs);


        return "list";
    }

    //制作面包屑URL
    private String getUrlParamForCrumb(PmsSearchParam pmsSearchParam, String delValueId) {

        String keyword = pmsSearchParam.getKeyword();
        String catalog3Id = pmsSearchParam.getCatalog3Id();
        String[] skuAttrValueList = pmsSearchParam.getValueId();

        String urlParam = "";

        if (StringUtils.isNotBlank(keyword)) {
            if (StringUtils.isNotBlank(urlParam)) {
                urlParam += "&";
            }
            urlParam = urlParam + "keyword=" + keyword;
        }

        if (StringUtils.isNotBlank(catalog3Id)) {
            if (StringUtils.isNotBlank(urlParam)) {
                urlParam += "&";
            }
            urlParam = urlParam + "catalog3Id=" + catalog3Id;
        }

        if (skuAttrValueList != null) {
            for (String pmsSkuAttrValue : skuAttrValueList) {
                if (!pmsSkuAttrValue.equals(delValueId)) {
                    urlParam = urlParam + "&valueId=" + pmsSkuAttrValue;
                }
            }
        }

        return urlParam;
    }

    /**
     * 拼接URL
     *
     * @param pmsSearchParam
     * @return
     */
    public String getUrlParam(PmsSearchParam pmsSearchParam) {

        String keyword = pmsSearchParam.getKeyword();
        String catalog3Id = pmsSearchParam.getCatalog3Id();
        String[] skuAttrValueList = pmsSearchParam.getValueId();

        String urlParam = "";

        if (StringUtils.isNotBlank(keyword)) {
            if (StringUtils.isNotBlank(urlParam)) {
                urlParam += "&";
            }
            urlParam = urlParam + "keyword=" + keyword;
        }

        if (StringUtils.isNotBlank(catalog3Id)) {
            if (StringUtils.isNotBlank(urlParam)) {
                urlParam += "&";
            }
            urlParam = urlParam + "catalog3Id=" + catalog3Id;
        }

        if (skuAttrValueList != null) {
            for (String pmsSkuAttrValue : skuAttrValueList) {
                urlParam = urlParam + "&valueId=" + pmsSkuAttrValue;
            }
        }
        return urlParam;
    }


    /**
     * 返回首页
     *
     * @return
     */
    @RequestMapping("index")
    public String index() {

        return "index";

    }

}
