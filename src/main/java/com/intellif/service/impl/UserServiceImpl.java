package com.intellif.service.impl;
import com.intellif.core.*;
import com.intellif.domain.User;
import com.intellif.dao.UserDao;
import org.springframework.stereotype.Service;
import com.intellif.service.IUserService;

import javax.jws.soap.SOAPBinding;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserDao,User> implements IUserService{
    @Override
    public Object findSql(String sql,int pageNum,int pageSize) {
       return baseDao.findSql(sql,User.class);
    }
}