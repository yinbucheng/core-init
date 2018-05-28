package com.intellif.service;
import com.intellif.core.*;
import com.intellif.domain.Person;
/**
* 作者:步程
* 创建时间:2018-05-24
**/
public interface IPersonService extends IService<Person>{

    void savePerson(Person person);

    Object listPerson();
}