package com.intellif.service.impl;

import cn.intellif.bucheng.cache.annotation.Cache;
import cn.intellif.bucheng.cache.annotation.DISABLED;
import cn.intellif.bucheng.cache.annotation.PUT;
import com.intellif.core.ServiceImpl;
import com.intellif.dao.PersonDao;
import com.intellif.domain.Person;
import com.intellif.service.IPersonService;
import org.springframework.stereotype.Service;
/**
* 作者:步程
* 创建时间:2018-05-24
**/
@Service
@Cache
public class PersonServiceImpl extends ServiceImpl<PersonDao,Person> implements IPersonService{


    @DISABLED(clazz = PersonServiceImpl.class,methodName = "listPerson",useAgs = false)
    @Override
    public void savePerson(Person person) {
        baseDao.save(person);
    }

    @PUT(expire = 100,useAgs = false)
    @Override
    public Object listPerson() {
        return baseDao.findAll();
    }
}