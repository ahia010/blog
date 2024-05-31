package com.ahia.blog.controller;

import com.ahia.blog.entity.R;
import com.ahia.blog.security.Authentication;
import com.ahia.blog.util.PasswordUtil;
import com.ahia.blog.util.TokenUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.ahia.blog.entity.User;
import com.ahia.blog.service.UserService;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 控制层。
 *
 * @author ahia
 * @since 2024-05-10
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("login")
    public R login(User user) {
        User user1 = userService.getOne(QueryWrapper.create().eq("username", user.getUsername()));
        if (user1 == null || !PasswordUtil.verifyPassword(user.getPassword(), user1.getPassword())) {
            return R.error(401, "用户名或密码错误");
        }
        user1.setToken(TokenUtil.generateToken(user1.getUsername(), user1.getRole()));
        return R.ok("登录成功", user1);
    }

    @PostMapping("register")
    public R register(User user) {
        User user1 = userService.getOne(QueryWrapper.create().eq("username", user.getUsername()));
        if (user1 != null) {
            return R.error("用户名已存在");
        }

        user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
        userService.save(user);
        return R.ok("注册成功");
    }

    /**
     * 添加。
     *
     * @param user
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @Authentication(role = {2})
    @PostMapping("save")
    public boolean save(@RequestBody User user) {
        return userService.save(user);
    }

    /**
     * 根据主键删除。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @Authentication(role = {2})
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return userService.removeById(id);
    }

    /**
     * 根据主键更新。
     *
     * @param user
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @Authentication(role = {2})
    @PutMapping("update")
    public boolean update(@RequestBody User user) {
        return userService.updateById(user);
    }

    /**
     * 查询所有。
     *
     * @return 所有数据
     */
    @Authentication(role = {2})
    @GetMapping("list")
    public List<User> list() {
        return userService.list();
    }

    /**
     * 根据主键获取详细信息。
     *
     * @param id 主键
     * @return 详情
     */
    @Authentication(role = {2})
    @GetMapping("getInfo/{id}")
    public User getInfo(@PathVariable Serializable id) {
        return userService.getById(id);
    }

    /**
     * 分页查询。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @Authentication(role = {2})
    @GetMapping("page")
    public Page<User> page(Page<User> page) {
        return userService.page(page);
    }


}
