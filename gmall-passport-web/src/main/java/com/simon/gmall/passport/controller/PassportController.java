package com.simon.gmall.passport.controller;


import com.simon.gmall.bean.UmsMember;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@CrossOrigin
public class PassportController {

    /**
     * 验证token真假
    * @param token
     * @return
     */
    @RequestMapping("verify")
    @ResponseBody
    public String verify(String token){

        return "success" ;

    }


    /**
     * 用户登录，登录成功返回token给用户
     * @param umsMember
     * @return
     */
    @RequestMapping("login")
    @ResponseBody
    public String login(UmsMember umsMember){

        //调用用户服务验证用户名和密码
        return "token" ;
    }


    /**
     * 返回登录页面，并且携带上来源地址
     * @param returnUrl
     * @param modelMap
     * @return
     */
    @RequestMapping("index")
    public String index(String returnUrl, ModelMap modelMap){

        modelMap.put("ReturnUrl",returnUrl);

        return "index" ;
    }

}
