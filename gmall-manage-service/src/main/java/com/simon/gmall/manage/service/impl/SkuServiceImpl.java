package com.simon.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.simon.gmall.bean.*;
import com.simon.gmall.manage.mapper.PmsSkuAttrValueMapper;
import com.simon.gmall.manage.mapper.PmsSkuImageMapper;
import com.simon.gmall.manage.mapper.PmsSkuInfoMapper;
import com.simon.gmall.manage.mapper.PmsSkuSaleAttrValueMapper;
import com.simon.gmall.service.SkuService;
import com.simon.gmall.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

@Service
public class SkuServiceImpl implements SkuService {


    @Autowired
    private PmsSkuInfoMapper pmsSkuInfoMapper;

    @Autowired
    private PmsSkuAttrValueMapper pmsSkuAttrValueMapper;

    @Autowired
    private PmsSkuSaleAttrValueMapper pmsSkuSaleAttrValueMapper;

    @Autowired
    private PmsSkuImageMapper pmsSkuImageMapper;

    @Autowired
    private RedisUtil redisUtil;


    /**
     * 插入Sku记录
     * @param pmsSkuInfo
     */
    @Override
    public void saveSkuInfo(PmsSkuInfo pmsSkuInfo) {


        //保存skuInfo
        pmsSkuInfoMapper.insertSelective(pmsSkuInfo);
        String skuInfoId = pmsSkuInfo.getId();


        //保存平台属性关联
        List<PmsSkuAttrValue> skuAttrValueList = pmsSkuInfo.getSkuAttrValueList();
        for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
            pmsSkuAttrValue.setSkuId(skuInfoId);
            pmsSkuAttrValueMapper.insertSelective(pmsSkuAttrValue);
        }

        //插入销售属性关联
        List<PmsSkuSaleAttrValue> skuSaleAttrValueList = pmsSkuInfo.getSkuSaleAttrValueList();
        for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : skuSaleAttrValueList) {
            pmsSkuSaleAttrValue.setSkuId(skuInfoId);
            pmsSkuSaleAttrValueMapper.insertSelective(pmsSkuSaleAttrValue);

        }

        //插入图片相关联数据
        List<PmsSkuImage> skuImageList = pmsSkuInfo.getSkuImageList();
        for (PmsSkuImage pmsSkuImage : skuImageList) {
            pmsSkuImage.setSkuId(skuInfoId);
            pmsSkuImageMapper.insertSelective(pmsSkuImage);
        }
    }

    /**
     * 展示商品详情页
     * @param skuId
     * @return
     */
    @Override
    public PmsSkuInfo getSkuById(String skuId)  {

        //连接缓存数据库
        Jedis jedis = redisUtil.getJedis();
        //尝试获取缓存信息
        String skuKey = "sku:"+skuId+":info";
        String skuJson = jedis.get(skuKey);
        PmsSkuInfo pmsSkuInfo= new PmsSkuInfo();

        //如果缓存中有数据，将缓存数据字符串转换成对象
        if(StringUtils.isNotBlank(skuJson)){
            pmsSkuInfo = JSON.parseObject(skuJson,PmsSkuInfo.class);
        }else {
            //如果缓存中没有数据，查询数据库

            //防止缓存击穿，使用Redis分布式锁
            String OK = jedis.set("sku:"+skuId+":lock","1","nx","px",10);
            //判断是否上锁成功
            if (StringUtils.isNotBlank(OK)&&OK.equals("OK")){

                //其中一个线程拿到锁
                pmsSkuInfo = getSkuByIdFromDb(skuId);
                if (pmsSkuInfo!=null){

                    jedis.set("sku:"+skuId+":info",JSON.toJSONString(pmsSkuInfo));

                }else {
                    // 数据库中不存在该sku
                    // 为了防止缓存穿透将，null或者空字符串值设置给redis
                    jedis.setex("sku:"+skuId+":info",60*3,JSON.toJSONString(""));
                }

            }else {
                //其他没有拿到锁的线程，自旋
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return getSkuById(skuId) ;
            }
        }
        jedis.close();
        return pmsSkuInfo;
    }

    public PmsSkuInfo getSkuByIdFromDb(String skuId){

        PmsSkuInfo pmsSkuInfo= new PmsSkuInfo();
        //根据skuId查找到一条skuInfo记录
        pmsSkuInfo.setId(skuId);
        PmsSkuInfo skuInfo = pmsSkuInfoMapper.selectOne(pmsSkuInfo);

        //封装包含的图片信息
        PmsSkuImage pmsSkuImage = new PmsSkuImage();
        pmsSkuImage.setSkuId(skuId);
        List<PmsSkuImage> pmsSkuImages = pmsSkuImageMapper.select(pmsSkuImage);
        skuInfo.setSkuImageList(pmsSkuImages);

        /*//封装销售属性信息
        PmsSkuSaleAttrValue pmsSkuSaleAttrValue = new PmsSkuSaleAttrValue();
        pmsSkuSaleAttrValue.setSkuId(skuId);
        List<PmsSkuSaleAttrValue> skuSaleAttrValueList  = pmsSkuSaleAttrValueMapper.select(pmsSkuSaleAttrValue);

        skuInfo.setSkuSaleAttrValueList(skuSaleAttrValueList );*/

        return skuInfo;
    }


    /**
     * 根据销售属性的选择，切换sku页面
     * @param productId
     * @return
     */
    @Override
    public List<PmsSkuInfo> getSkuSaleAttrValueListBySpu(String productId) {

        List<PmsSkuInfo> pmsSkuInfos = pmsSkuInfoMapper.selectSkuSaleAttrValueListBySpu(productId);

        return pmsSkuInfos;
    }

    @Override
    public List<PmsSkuInfo> getAllSku() {

        List<PmsSkuInfo> pmsSkuInfos = pmsSkuInfoMapper.selectAll();

        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfos) {

            String skuId = pmsSkuInfo.getId();
            PmsSkuAttrValue pmsSkuAttrValue = new PmsSkuAttrValue();
            pmsSkuAttrValue.setSkuId(skuId);
            List<PmsSkuAttrValue> pmsSkuAttrValues = pmsSkuAttrValueMapper.select(pmsSkuAttrValue);
            pmsSkuInfo.setSkuAttrValueList(pmsSkuAttrValues);
        }

        return pmsSkuInfos;

    }
}
