package com.ahia.blog.controller;

import com.ahia.blog.entity.R;
import com.ahia.blog.security.Authentication;
import com.ahia.blog.util.PasswordUtil;
import com.ahia.blog.util.TokenUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.ahia.blog.entity.User;
import com.ahia.blog.service.UserService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.ahia.blog.entity.table.UserTableDef.USER;



@RestController
@RequestMapping("/api/user")
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
        userService.save(User.builder()
                .username(user.getUsername())
                .password(PasswordUtil.hashPassword(user.getPassword()))
                .avatar("/api/avatar/default.jpg")
                .role(1)
                .status(1)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build());

        return R.ok("注册成功");
    }


    @Authentication(role = {1,2})
    @PostMapping("update")
    public R update(User user) {
        if (TokenUtil.extractUsername(user.getToken()).equals(user.getUsername())) {
            if (user.getPassword() != null && user.getNewPassword() != null&&!user.getPassword().isEmpty()&&!user.getNewPassword().isEmpty()) {
                User user1 = userService.getOne(QueryWrapper.create().eq("username", user.getUsername()));
                if (user1 == null || !PasswordUtil.verifyPassword(user.getPassword(), user1.getPassword())) {
                    return R.error(401, "密码错误");
                }
                UpdateChain.of(User.class)
                        .set("password", PasswordUtil.hashPassword(user.getNewPassword()))
                        .set("updateTime", LocalDateTime.now())
                        .where(User::getUsername).eq(user.getUsername())
                        .update();
                return R.ok("修改成功");
            }
            if (user.getFile() != null) {
                UpdateChain.of(User.class)
                        .set("avatar", saveAvatar(user.getFile()))
                        .set("updateTime", LocalDateTime.now())
                        .where(USER.USERNAME.eq(user.getUsername()))
                        .update();
                return R.ok("修改成功");
            }
            UpdateChain.of(User.class)
                    .set("email", user.getEmail())
                    .set("phone", user.getPhone())
                    .set("updateTime", LocalDateTime.now())
                    .where(User::getUsername).eq(user.getUsername())
                    .update();
            return R.ok("修改成功");


        }
        return R.error("系统错误");
    }


    @RequestMapping("/chargePassword")
    public R chargePassword(User user) {
        if (user.getUsername() == null || user.getPassword() == null || user.getNewPassword() == null) {
            return R.error("用户名、密码和新密码不能为空");
        }
        User user1 = userService.getOne(QueryWrapper.create().eq("username", user.getUsername()));
        if (user1 == null || !PasswordUtil.verifyPassword(user.getPassword(), user1.getPassword())) {
            return R.error(401, "密码错误");
        }
        UpdateChain.of(User.class)
                .set("password", PasswordUtil.hashPassword(user.getNewPassword()))
                .set("updateTime", LocalDateTime.now())
                .where("username", user.getUsername())
                .update();
        return R.ok("修改成功");
    }


    @Authentication(role = {2})
    @DeleteMapping("delete")
    public R remove(Integer[] ids) {
        if (userService.removeByIds(Arrays.asList(ids)))
            return R.ok("删除成功");
        return R.error("删除失败");
//        System.out.println(Arrays.toString(ids));
//        return R.ok("删除成功");
    }

    /**
     * 根据主键更新。
     *
     * @param user
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @Authentication(role = {2})
    @PutMapping("save")
    public R save(User user) {
        System.out.println(user);
        if (user.getId() == null && !user.getUsername().isEmpty() && !user.getPassword().isEmpty()) {
            if (userService.getOne(QueryWrapper.create().eq(User::getUsername, user.getUsername())) != null) {
                return R.error("用户名已存在");
            }
            if (user.getFile() != null && !user.getFile().isEmpty()) {
                user.setAvatar(saveAvatar(user.getFile()));
            }
            userService.save(
                    User.builder()
                            .username(user.getUsername())
                            .password(PasswordUtil.hashPassword(user.getPassword()))
                            .email(user.getEmail())
                            .avatar(user.getAvatar())
                            .phone(user.getPhone())
                            .role(1)
                            .status(1)
                            .createTime(LocalDateTime.now())
                            .updateTime(LocalDateTime.now())
                            .build());
            return R.ok("添加成功");
        }
        user.setUsername(null);
        if (!user.getPassword().isEmpty())
            user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
        if (userService.updateById(user))
            return R.ok("修改成功");

        return R.error("修改失败");
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
    public R getInfo(@PathVariable Serializable id) {
        return R.ok("获取成功", userService.getById(id));
    }

    /**
     * 分页查询。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @Authentication(role = {2})
    @GetMapping("page")
    public R page(Page<User> page, User user) {
        if (user.getUsername().isEmpty() && user.getEmail().isEmpty()) {
            return R.ok("查询成功", userService.page(page));
        }
        if (user.getUsername().isEmpty())
            user.setUsername(null);
        if (user.getEmail().isEmpty())
            user.setEmail(null);
        QueryWrapper query = QueryWrapper.create()
                .eq("username", user.getUsername())
                .like("email", user.getEmail());
        return R.ok("查询成功", userService.page(page, query));
    }

    private String saveAvatar(MultipartFile files) {
        try {
            byte[] bytes = files.getBytes();
            String originalFilename = files.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
            String newFilename = UUID.randomUUID() + extension;
            Path path = Paths.get("./uploads/avatar/" + newFilename);
            Files.write(path, bytes);
            return "/api/avatar/" + newFilename;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("getUsername")
    public R info(@RequestHeader(name = "Token", defaultValue = "") String token) {
        try {
            return R.ok("获取成功", TokenUtil.extractUsername(token));
        } catch (Exception e) {
            return R.error(401, "登录过期");
        }
    }

    @GetMapping("getUserInfo")
    public R getUserInfo(@RequestHeader(name = "Token", defaultValue = "") String token) {
        try {
            String username = TokenUtil.extractUsername(token);
            return R.ok("获取成功",
                    QueryChain.of(User.class)
                            .select(USER.ALL_COLUMNS)
                            .from(USER)
                            .eq(User::getUsername, username)
                            .one());
        } catch (Exception e) {
            return R.error(401, "登录过期");
        }
    }

}
