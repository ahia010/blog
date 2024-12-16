package com.ahia.blog.controller;

import com.ahia.blog.entity.R;
import com.ahia.blog.entity.User;
import com.ahia.blog.security.Authentication;
import com.ahia.blog.service.CommentService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ahia.blog.entity.table.CommentTableDef.COMMENT;
import static com.ahia.blog.entity.table.PostTableDef.POST;
import static com.ahia.blog.entity.table.UserTableDef.USER;
import static com.mybatisflex.core.query.QueryMethods.select;


@RestController
@RequestMapping("/api/post")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    /**
     * 添加。
     *
     * @param post
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @Authentication(role = {2})
    @PostMapping("save")
    public R save(@RequestHeader(name = "Token", defaultValue = "") String token, @RequestBody Post post) {
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
                .setRaw(Post::getUserId, select(USER.ID).from(USER).where(USER.USERNAME.eq(username)))
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
    @Authentication(role = {2})
    @PutMapping("update")
    public R update(@RequestBody Post post) {
        if (post.getTitle() == null || post.getContent() == null || post.getKind() == null || post.getTitle().isEmpty() || post.getContent().isEmpty()) {
            return R.error("标题、内容和类型不能为空");
        }
        if (postService.updateById(post))
            return R.ok("修改成功");
        return R.error("修改失败");
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
        QueryWrapper queryWrapper1 = QueryWrapper.create()
                .select(COMMENT.ALL_COLUMNS, USER.USERNAME, USER.AVATAR)
                .from(COMMENT)
                .innerJoin(USER).on(COMMENT.USER_ID.eq(USER.ID))
                .where(COMMENT.POST_ID.eq(id));

        Map<String, Object> map = new HashMap<>();
        map.put("post", postService.getOne(queryWrapper));
        map.put("comments", commentService.list(queryWrapper1));
        return R.ok("获取成功", map);
    }

    /**
     * 分页查询。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public R page(Page<Post> page, String search) {
        if (search == null || search.isEmpty())
            return R.ok("获取成功", postService.page(page));
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(POST.TITLE.like(search))
                .or(POST.CONTENT.like(search))
                .or(POST.KIND.like(search));
        return R.ok("获取成功", postService.page(page, queryWrapper));
    }

}
