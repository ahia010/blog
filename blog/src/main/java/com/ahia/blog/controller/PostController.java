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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ahia.blog.entity.table.CommentTableDef.COMMENT;
import static com.ahia.blog.entity.table.PostTableDef.POST;
import static com.ahia.blog.entity.table.UserTableDef.USER;
import static com.mybatisflex.core.query.QueryMethods.number;
import static com.mybatisflex.core.query.QueryMethods.select;


@RestController
@RequestMapping("/api/post")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;


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


    @Authentication(role = {2})
    @DeleteMapping("delete")
    public R delete(Integer[] ids) {
        if (postService.removeByIds(Arrays.asList(ids)))
            return R.ok("删除成功");
        return R.error("删除失败");
    }


    @Authentication(role = {2})
    @PutMapping("update")
    public R update(@RequestBody Post post) {
        if (post.getTitle() == null || post.getContent() == null || post.getKind() == null || post.getTitle().isEmpty() || post.getContent().isEmpty()) {
            return R.error("标题、内容和类型不能为空");
        }
        post.setUpdateTime(LocalDateTime.now());
        if (postService.updateById(post))
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
    public List<Post> list() {
        return postService.list();
    }

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

        UpdateChain.of(Post.class)
                .set(POST.PAGE_VIEW, POST.PAGE_VIEW.add(1))
                .where(POST.ID.eq(id))
                .update();
        Map<String, Object> map = new HashMap<>();
        map.put("post", postService.getOne(queryWrapper));
        map.put("comments", commentService.list(queryWrapper1));
        return R.ok("获取成功", map);
    }

    @GetMapping("getInfoAdmin/{id}")
    public R getInfoAdmin(@PathVariable Serializable id) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select(POST.ALL_COLUMNS, USER.USERNAME)
                .from(POST)
                .innerJoin(USER).on(POST.USER_ID.eq(USER.ID))
                .where(POST.ID.eq(id));
        return R.ok("获取成功", postService.getOne(queryWrapper));
    }

    /**
     * 分页查询。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public R page(Page<Post> page, String search, String soft, Integer asc) {
        QueryWrapper queryWrapper = QueryWrapper.create();
        if (search != null && !search.isEmpty())
                queryWrapper.where(POST.TITLE.like(search))
                .or(POST.CONTENT.like(search))
                .or(POST.KIND.like(search));
        if (asc==null)
            asc=1;
        if (soft == null || soft.isEmpty())
            soft = "";
        switch (soft) {
            case "pageView" -> queryWrapper.orderBy(POST.PAGE_VIEW, asc == 1);
            case "updateTime" -> queryWrapper.orderBy(POST.UPDATE_TIME, asc == 1);
            case "kind" -> queryWrapper.orderBy(POST.KIND, asc == 1);
        }
        return R.ok("获取成功", postService.page(page, queryWrapper));
    }


    @GetMapping("pageAdmin")
    public R pageAdmin(Page<Post> page, String title,String content) {
        page.setPageSize(100);
        QueryWrapper queryWrapper = QueryWrapper.create();
        queryWrapper.where(number(1).eq(1));
        if (title != null && !title.isEmpty())
            queryWrapper.and(POST.TITLE.like(title));
        if (content != null && !content.isEmpty())
            queryWrapper.and(POST.CONTENT.like(content));
        return R.ok("获取成功", postService.page(page, queryWrapper));

    }





    @GetMapping("getHome")
    public R getHome() {
        QueryWrapper queryWrapperPosts = QueryWrapper.create()
                .select(POST.ALL_COLUMNS, USER.USERNAME)
                .from(POST)
                .innerJoin(USER).on(POST.USER_ID.eq(USER.ID))
                .orderBy(POST.UPDATE_TIME.desc())
                .limit(10);
        Map<String, Object> map = new HashMap<>();
        map.put("posts", postService.list(queryWrapperPosts));
        map.put("postNum", postService.count());
        map.put("comment", commentService.count());
        map.put("lastPost", QueryChain.of(Post.class).select(POST.UPDATE_TIME).from(POST).orderBy(Post::getUpdateTime).desc().limit(1).one());
        map.put("pageView", QueryChain.of(Post.class).select().from(POST).orderBy(Post::getPageView).desc().limit(5).list());
        return R.ok("获取成功", map);
    }


}
