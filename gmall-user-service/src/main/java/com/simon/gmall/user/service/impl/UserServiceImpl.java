package com.simon.gmall.user.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.simon.gmall.bean.UmsMember;
import com.simon.gmall.bean.UmsMemberReceiveAddress;
import com.simon.gmall.service.UserService;
import com.simon.gmall.user.mapper.UmsMemberReceiveAddressMapper;
import com.simon.gmall.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl  implements UserService {

    @Autowired
    private UserMapper userMapper ;

    @Autowired
    private UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;


    @Override
    public List<UmsMember> getAllUser() {

        List<UmsMember> umsMembers = userMapper.selectAll();
        return umsMembers;
    }

    @Override
    public List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId) {

        Example e = new Example(UmsMemberReceiveAddress.class);
        e.createCriteria().equals(memberId);
        List<UmsMemberReceiveAddress> umsMemberReceiveAddresses = umsMemberReceiveAddressMapper.selectByExample(e);
        return umsMemberReceiveAddresses;

    }
}
