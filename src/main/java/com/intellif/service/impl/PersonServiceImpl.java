package com.intellif.service.impl;
import com.intellif.core.*;
import com.intellif.domain.Person;
import com.intellif.dao.PersonDao;
import org.springframework.stereotype.Service;
import com.intellif.service.IPersonService;
/**
* 作者:步程
* 创建时间:2018-05-24
**/
@Service
public class PersonServiceImpl extends ServiceImpl<PersonDao,Person> implements IPersonService{
}