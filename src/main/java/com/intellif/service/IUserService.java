package com.intellif.service;
import com.intellif.core.*;
import com.intellif.domain.User;
public interface IUserService extends IService<User>{
    Object findSql(String sql,int pageNum,int pageSize);
}