package com.simon.gmall.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.simon.gmall.bean.OmsCartItem;
import com.simon.gmall.bean.PmsSkuInfo;
import com.simon.gmall.service.SkuService;
import com.simon.gmall.util.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@CrossOrigin
public class CartController {

    @Reference
    private SkuService skuService;


    @RequestMapping("addToCart")
    public String addToCart(String skuId, int quantity, HttpServletRequest request, HttpServletResponse response, HttpSession session) {

        //创建购物项列表
        List<OmsCartItem> omsCartItems = new ArrayList<>();

        //调用商品服务，查询商品信息
        PmsSkuInfo skuInfo = skuService.getSkuById(skuId);

        //将商品封装成购物车信息
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setCreateDate(new Date());
        omsCartItem.setDeleteStatus(0);
        omsCartItem.setModifyDate(new Date());
        omsCartItem.setPrice(skuInfo.getPrice());
        omsCartItem.setProductAttr("");
        omsCartItem.setProductBrand("");
        omsCartItem.setProductCategoryId(skuInfo.getCatalog3Id());
        omsCartItem.setProductId(skuInfo.getProductId());
        omsCartItem.setProductName(skuInfo.getSkuName());
        omsCartItem.setProductPic(skuInfo.getSkuDefaultImg());
        omsCartItem.setProductSkuCode("11111111111");
        omsCartItem.setProductSkuId(skuId);
        omsCartItem.setQuantity(quantity);

        //判断用户是否登录
        String memberId = "";
        if (StringUtils.isBlank(memberId)) {

            //用户没有登陆
            //查看cookie中是否已经有购物车数据
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
            if (StringUtils.isBlank(cartListCookie)) {
                //cookie为空
                omsCartItems.add(omsCartItem);
            } else {
                //cookie不为空
                omsCartItems = JSON.parseArray(cartListCookie, OmsCartItem.class);
                // 判断添加的购物车数据在cookie中是否已经存在
                boolean exist = if_cart_exist(omsCartItems, omsCartItem);

                if (exist) {
                    //之前添加过，更新购物车的商品数量
                    for (OmsCartItem cartItem : omsCartItems) {

                        if (cartItem.getProductSkuId().equals(omsCartItem.getProductSkuId())) {

                            cartItem.setQuantity(cartItem.getQuantity() + omsCartItem.getQuantity());
                        }
                    }
                } else {
                    //之前没有添加过,即为新增购物车
                    omsCartItems.add(omsCartItem);
                }
            }
            CookieUtil.setCookie(request, response, "cartListCookie", JSON.toJSONString(omsCartItems), 60 * 60 * 72, true);

        } else {

        }
        return "redirect:/success.html";
    }

    //判断购物车中是否存在此项商品
    private boolean if_cart_exist(List<OmsCartItem> omsCartItems, OmsCartItem omsCartItem) {

        boolean result = false;
        for (OmsCartItem cartItem : omsCartItems) {
            String productSkuId = cartItem.getProductSkuId();
            if (productSkuId.equals(omsCartItem.getProductSkuId())) {
                result = true;
            }
        }
        return false;
    }

}
