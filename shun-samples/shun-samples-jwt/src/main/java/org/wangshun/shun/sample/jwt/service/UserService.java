package org.wangshun.shun.sample.jwt.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.wangshun.shun.sample.jwt.dao.UserDao;
import org.wangshun.shun.sample.jwt.entity.PO.SysUser;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service
public class UserService extends ServiceImpl<UserDao, SysUser> implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = this.getOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, username));
        if (null == user) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        return user;
    }

}
