package com.intellif.web;
import com.intellif.core.ServerResult;
import com.intellif.domain.Person;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;
import com.intellif.service.*;
import org.springframework.beans.factory.annotation.Autowired;
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

	@RequestMapping("save")
	public Object save(){
		Person person = new Person();
		person.setAge(20);
		person.setIdCard("12334");
		person.setName("tesst");
		personService.save(person);
		return ServerResult.success();
	}

	@RequestMapping("listAll")
	public Object listAll(){
		return ServerResult.success(personService.listAll());
	}
}