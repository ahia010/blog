package com.ahia.blog.controller;

import com.ahia.blog.entity.R;
import com.ahia.blog.entity.User;
import com.ahia.blog.security.Authentication;
import com.ahia.blog.util.TokenUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.core.update.UpdateWrapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.ahia.blog.entity.Post;
import com.ahia.blog.service.PostService;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import static com.ahia.blog.entity.table.PostTableDef.POST;
import static com.ahia.blog.entity.table.UserTableDef.USER;

/**
 * 控制层。
 *
 * @author ahia
 * @since 2024-05-11
 */
@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    /**
     * 添加。
     *
     * @param post
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @Authentication(role = {2})
    @PostMapping("save")
    public R save(@RequestHeader(name = "Token", defaultValue = "") String token, Post post) {
        String username = TokenUtil.extractUsername(token);
        if (post.getTitle() == null || post.getContent() == null) {
            return R.error("标题和内容不能为空");
        }
        Post newPost = Post.builder()
                .title(post.getTitle())
                .kind(post.getKind())
                .content(post.getContent())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();

        postService.save(UpdateWrapper.of(newPost)
                .setRaw(Post::getUserId, "(SELECT id FROM user WHERE username = '" + username + "')")
                .toEntity());


        return R.ok("添加成功");
    }

    /**
     * 根据主键删除。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return postService.removeById(id);
    }

    /**
     * 根据主键更新。
     *
     * @param post
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody Post post) {
        return postService.updateById(post);
    }

    /**
     * 查询所有。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<Post> list() {
        return postService.list();
    }

    /**
     * 根据主键获取详细信息。
     *
     * @param id 主键
     * @return 详情
     */
    @GetMapping("getInfo/{id}")
    public R getInfo(@PathVariable Serializable id) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select(POST.ALL_COLUMNS, USER.USERNAME)
                .from(POST)
                .innerJoin(USER).on(POST.USER_ID.eq(USER.ID))
                .where(POST.ID.eq(id));
//        QueryChain.of(Post.class)
//                .select(POST.ALL_COLUMNS, USER.USERNAME)
//                .from(POST)
//                .innerJoin(USER).on(POST.USER_ID.eq(USER.ID))
//                .where(POST.ID.eq(id))

        return R.ok("获取成功",postService.getOne(queryWrapper));
    }

    /**
     * 分页查询。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public R page(Page<Post> page) {
        return R.ok("获取成功", postService.page(page));
    }

}
