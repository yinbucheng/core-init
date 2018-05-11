package com.intellif.dao.impl;
import com.intellif.core.*;
import com.intellif.domain.User;
import org.springframework.stereotype.Repository;
import com.intellif.dao.UserDao;
@Repository
public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao{
}