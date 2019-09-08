package com.simon.gmall.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.simon.gmall.bean.OmsCartItem;
import com.simon.gmall.cart.mapper.OmsCartItemMapper;
import com.simon.gmall.service.CartService;
import com.simon.gmall.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {


    @Autowired
    private OmsCartItemMapper omsCartItemMapper;

    @Autowired
    private RedisUtil redisUtil;


    //从数据库中查找是否有该条商品记录
    @Override
    public OmsCartItem ifCartExistByUser(String memberId, String skuId) {

        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(memberId);
        omsCartItem.setProductSkuId(skuId);
        OmsCartItem omsCartItemFromDB = omsCartItemMapper.selectOne(omsCartItem);
        return omsCartItemFromDB;
    }

    @Override
    public void addCart(OmsCartItem omsCartItem) {

        if (StringUtils.isNotBlank(omsCartItem.getMemberId())) {

            omsCartItemMapper.insertSelective(omsCartItem);
        }
    }

    @Override
    public void updateCart(OmsCartItem omsCartItemFromDB) {

        Example example = new Example(OmsCartItem.class);
        example.createCriteria().andEqualTo("id", omsCartItemFromDB.getId());
        omsCartItemMapper.updateByExampleSelective(omsCartItemFromDB, example);

    }


    @Override
    public void flushCartCache(String memberId) {

        //先将数据从数据库中查询出来
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(memberId);
        List<OmsCartItem> omsCartItems = omsCartItemMapper.select(omsCartItem);

        //同步到缓存中
        Jedis jedis = redisUtil.getJedis();
        Map<String, String> map = new HashMap<>();

        for (OmsCartItem cartItem : omsCartItems) {
            map.put(cartItem.getProductSkuId(), JSON.toJSONString(cartItem));
        }

        jedis.del("user:" + memberId + ":cart");
        jedis.hmset("user:" + memberId + ":cart", map);
        jedis.close();
    }

    //根据用户id查询购物车信息
    @Override
    public List<OmsCartItem> cartList(String memberId) {

        Jedis jedis = null;
        List<OmsCartItem> omsCartItems = new ArrayList<>();
        try {
            jedis = redisUtil.getJedis();
            List<String> hvals = jedis.hvals("user:" + memberId + ":cart");

            for (String hval : hvals) {
                OmsCartItem omsCartItem = JSON.parseObject(hval, OmsCartItem.class);
                omsCartItems.add(omsCartItem);
            }
        } catch (Exception e) {
            //处理系统的异常
            e.printStackTrace();
            return null;
        } finally {
            jedis.close();
        }
        return omsCartItems;
    }

    @Override
    public void checkCart(OmsCartItem omsCartItem) {

        Example example = new Example(OmsCartItem.class);
        example.createCriteria().andEqualTo("memberId",omsCartItem.getMemberId()).andEqualTo("productSkuId",omsCartItem.getProductSkuId());
        omsCartItemMapper.updateByExampleSelective(omsCartItem,example);

        // 缓存同步
        flushCartCache(omsCartItem.getMemberId());
    }
}
