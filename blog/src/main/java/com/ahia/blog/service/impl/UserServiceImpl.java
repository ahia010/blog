package com.ahia.blog.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.ahia.blog.entity.User;
import com.ahia.blog.mapper.UserMapper;
import com.ahia.blog.service.UserService;
import org.springframework.stereotype.Service;

/**
 *  服务层实现。
 *
 * @author ahia
 * @since 2024-05-10
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
