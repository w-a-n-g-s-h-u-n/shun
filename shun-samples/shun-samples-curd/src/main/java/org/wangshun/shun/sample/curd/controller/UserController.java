 package org.wangshun.shun.sample.curd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.wangshun.shun.core.exception.ServiceException;
import org.wangshun.shun.core.http.R;
import org.wangshun.shun.sample.curd.entity.PO.User;
import org.wangshun.shun.sample.curd.service.UserService;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

@RestController
@RequestMapping("/user")
 public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/")
    public R save(@RequestBody User user) {
        userService.save(user);
        return R.success(user);
    }

    @PutMapping("/{userId}")
    public R update(@PathVariable("userId") long userId, @RequestBody User user) {
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
        User user) {
        return R.success(userService.page(new Page<>(current, size), Wrappers.query(user)));
    }

    @DeleteMapping("/{userId}")
    public R delete(@PathVariable("userId") long userId) {
        return R.of(userService.removeById(userId));
    }
}
