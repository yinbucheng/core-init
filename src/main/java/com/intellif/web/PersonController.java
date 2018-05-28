package com.intellif.web;

import com.intellif.core.ServerResult;
import com.intellif.domain.Person;
import com.intellif.service.IPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
* 作者:步程
* 创建时间:2018-05-24
**/
@RestController
@RequestMapping("person")
public class PersonController{

	@Autowired
	private IPersonService  personService;

	@RequestMapping("test")
	public Object test(){
		return "success";
	}

	@RequestMapping("/save")
	public Object save(){
		Person person = new Person();
		person.setName("yinchong");
		person.setIdCard("nicegood");
		person.setAge(20);
		personService.savePerson(person);
		return ServerResult.success();
	}

	@RequestMapping("/listAll")
	public Object listAll(){
		Object datas = personService.listPerson();
		return ServerResult.success(datas);
	}
}