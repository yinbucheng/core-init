package com.intellif.web;

import com.intellif.annotation.PrintAll;
import com.intellif.core.HQLWrapper;
import com.intellif.core.ServerResult;
import com.intellif.core.Wrapper;
import com.intellif.domain.Person;
import com.intellif.service.IPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
	@PrintAll
	public Object test(String name,Integer age){
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
	@PrintAll
	public Object listAll(){
		Wrapper wrapper = new HQLWrapper(Person.class);
		wrapper.showSelect("name","age").orderBy("id",false);
		List<?> datas = personService.findWrapper(wrapper);
		return ServerResult.success(datas);
	}
}