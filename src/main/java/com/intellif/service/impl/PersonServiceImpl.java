package com.intellif.service.impl;

import com.intellif.annotation.Print;
import com.intellif.annotation.PrintArgsDetail;
import com.intellif.annotation.PrintMethodTime;
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
@Print
public class PersonServiceImpl extends ServiceImpl<PersonDao,Person> implements IPersonService{


    @Override
    @PrintMethodTime
    public void savePerson(Person person) {
        baseDao.save(person);
    }

    @Override
    @PrintMethodTime
    @PrintArgsDetail
    public Object listPerson() {
        return baseDao.findAll();
    }
}