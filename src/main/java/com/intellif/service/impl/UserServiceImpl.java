package com.intellif.service.impl;
import com.intellif.core.*;
import com.intellif.domain.User;
import com.intellif.dao.UserDao;
import org.springframework.stereotype.Service;
import com.intellif.service.IUserService;
@Service
public class UserServiceImpl extends ServiceImpl<UserDao,User> implements IUserService{
}