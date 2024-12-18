package com.ahia.blog.controller;

import com.ahia.blog.entity.R;
import com.ahia.blog.security.Authentication;
import com.ahia.blog.util.TokenUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateWrapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.ahia.blog.entity.Comment;
import com.ahia.blog.service.CommentService;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import static com.ahia.blog.entity.table.CommentTableDef.COMMENT;
import static com.ahia.blog.entity.table.UserTableDef.USER;
import static com.mybatisflex.core.query.QueryMethods.select;

/**
 * 控制层。
 *
 * @author ahia
 * @since 2024-05-11
 */
@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;


    @Authentication(role = {1, 2})
    @PostMapping("save")
    public R save(@RequestBody Comment comment, @RequestHeader(name = "Token", defaultValue = "") String token) {
        String username = TokenUtil.extractUsername(token);
        if (comment.getContent() == null) {
            return R.error("内容不能为空");
        }
        Comment newComment = Comment.builder()
                .content(comment.getContent())
                .postId(comment.getPostId())
                .createTime(LocalDateTime.now())
                .build();
        commentService.save(UpdateWrapper.of(newComment)
                .setRaw(Comment::getUserId,select(USER.ID).from(USER).where(USER.USERNAME.eq(username)))
                .toEntity());
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select(COMMENT.ALL_COLUMNS, USER.USERNAME, USER.AVATAR)
                .from(COMMENT)
                .innerJoin(USER).on(COMMENT.USER_ID.eq(USER.ID))
                .where(COMMENT.POST_ID.eq(comment.getPostId()));
        return R.ok("添加成功",commentService.list(queryWrapper));
    }

    /**
     * 根据主键删除。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     * //todo 1
     */
    @Authentication(role = {1, 2})
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id,@RequestHeader(name = "Token", defaultValue = "") String token) {
        return commentService.removeById(id);
    }


    @Authentication(role = {2})
    @PutMapping("update")
    public boolean update(@RequestBody Comment comment) {
        return commentService.updateById(comment);
    }


    @Authentication(role = {1})
    @GetMapping("list")
    public List<Comment> list() {
        return commentService.list();
    }


    @Authentication(role = {2})
    @GetMapping("getInfo/{id}")
    public Comment getInfo(@PathVariable Serializable id) {
        return commentService.getById(id);
    }


    @Authentication(role = {2})
    @GetMapping("page")
    public Page<Comment> page(Page<Comment> page) {
        return commentService.page(page);
    }

}
