package com.ahia.blog.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.ahia.blog.entity.Post;
import com.ahia.blog.mapper.PostMapper;
import com.ahia.blog.service.PostService;
import org.springframework.stereotype.Service;

/**
 *  服务层实现。
 *
 * @author ahia
 * @since 2024-05-11
 */
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {

}
