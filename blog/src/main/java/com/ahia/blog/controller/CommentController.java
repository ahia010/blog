package com.ahia.blog.controller;

import com.ahia.blog.security.Authentication;
import com.mybatisflex.core.paginate.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.ahia.blog.entity.Comment;
import com.ahia.blog.service.CommentService;
import org.springframework.web.bind.annotation.RestController;
import java.io.Serializable;
import java.util.List;

/**
 *  控制层。
 *
 * @author ahia
 * @since 2024-05-11
 */
@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 添加。
     *
     * @param comment 
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @Authentication(role = {1,2})
    @PostMapping("save")
    public boolean save(@RequestBody Comment comment) {
        return commentService.save(comment);
    }

    /**
     * 根据主键删除。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     * //todo 1
     */
    @Authentication(role = {1,2})
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return commentService.removeById(id);
    }

    /**
     * 根据主键更新。
     *
     * @param comment 
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody Comment comment) {
        return commentService.updateById(comment);
    }

    /**
     * 查询所有。
     *
     * @return 所有数据
     */
    @Authentication(role = {1})
    @GetMapping("list")
    public List<Comment> list() {
        return commentService.list();
    }

    /**
     * 根据主键获取详细信息。
     *
     * @param id 主键
     * @return 详情
     */
    @Authentication(role = {2})
    @GetMapping("getInfo/{id}")
    public Comment getInfo(@PathVariable Serializable id) {
        return commentService.getById(id);
    }

    /**
     * 分页查询。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @Authentication(role = {2})
    @GetMapping("page")
    public Page<Comment> page(Page<Comment> page) {
        return commentService.page(page);
    }

}
