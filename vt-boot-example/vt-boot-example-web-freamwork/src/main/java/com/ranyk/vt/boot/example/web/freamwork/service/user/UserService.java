package com.ranyk.vt.boot.example.web.freamwork.service.user;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ranyk.vt.boot.example.web.freamwork.domain.user.entity.User;
import com.ranyk.vt.boot.example.web.freamwork.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * CLASS_NAME: UserService.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 用户信息业务逻辑类
 * @date: 2026-03-17
 */
@Slf4j
@Service
public class UserService extends ServiceImpl<UserRepository, User> {
}
