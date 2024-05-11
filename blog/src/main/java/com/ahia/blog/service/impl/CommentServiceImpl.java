package com.ahia.blog.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.ahia.blog.entity.Comment;
import com.ahia.blog.mapper.CommentMapper;
import com.ahia.blog.service.CommentService;
import org.springframework.stereotype.Service;

/**
 *  服务层实现。
 *
 * @author ahia
 * @since 2024-05-11
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

}
