package org.wangshun.shun.sample.jwt.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.wangshun.shun.core.exception.ServiceException;
import org.wangshun.shun.core.http.R;
import org.wangshun.shun.sample.jwt.entity.PO.SysUser;
import org.wangshun.shun.sample.jwt.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/")
    public R save(@RequestBody SysUser user) {
        userService.save(user);
        return R.success(user);
    }

    @PutMapping("/{userId}")
    public R update(@PathVariable("userId") long userId, @RequestBody SysUser user) {
        if (null == user.getId()) {
            user.setId(userId);
        } else if (userId != user.getId()) {
            throw new ServiceException("参数错误");
        }
        return R.of(userService.updateById(user));
    }

    @GetMapping("/{userId}")
    public R getById(@PathVariable("userId") long userId) {
        return R.success(userService.getById(userId));
    }

    @GetMapping("/list")
    public R list(@RequestParam(defaultValue = "1") long current, @RequestParam(defaultValue = "10") long size,
                  SysUser user) {
        return R.success(userService.page(new Page<>(current, size), Wrappers.query(user)));
    }

    @DeleteMapping("/{userId}")
    public R delete(@PathVariable("userId") long userId) {
        return R.of(userService.removeById(userId));
    }
}
