package com.intellif.service.impl;

import com.intellif.annotation.PrintAll;
import com.intellif.annotation.PrintMethodTime;
import com.intellif.annotation.ReadOnly;
import com.intellif.core.ServiceImpl;
import com.intellif.dao.PersonDao;
import com.intellif.domain.Person;
import com.intellif.service.IPersonService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
* 作者:步程
* 创建时间:2018-05-24
**/
@Service
public class PersonServiceImpl extends ServiceImpl<PersonDao,Person> implements IPersonService{


    @Override
    @PrintMethodTime
    @Transactional(propagation = Propagation.REQUIRED)
    public void savePerson(Person person) {
        baseDao.save(person);
    }

    @Override
    @PrintAll
    @ReadOnly
    public Object listPerson() {
        return baseDao.findAll();
    }
}