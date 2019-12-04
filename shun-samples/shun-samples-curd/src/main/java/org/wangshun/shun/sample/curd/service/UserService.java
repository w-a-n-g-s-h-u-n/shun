package org.wangshun.shun.sample.curd.service;

import org.springframework.stereotype.Service;
import org.wangshun.shun.sample.curd.dao.UserDao;
import org.wangshun.shun.sample.curd.entity.PO.User;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service
public class UserService extends ServiceImpl<UserDao, User> {

}
