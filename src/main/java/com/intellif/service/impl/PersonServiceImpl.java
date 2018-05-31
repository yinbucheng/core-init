package com.intellif.service.impl;

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
public class PersonServiceImpl extends ServiceImpl<PersonDao,Person> implements IPersonService{


    @Override
    public void savePerson(Person person) {
        baseDao.save(person);
    }

    @Override
    public Object listPerson() {
        return baseDao.findAll();
    }
}