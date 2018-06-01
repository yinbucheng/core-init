package com.intellif.web;

import com.intellif.annotation.Print;
import com.intellif.annotation.PrintAll;
import com.intellif.annotation.PrintArgsDetail;
import com.intellif.annotation.PrintMethodTime;
import com.intellif.core.ServerResult;
import com.intellif.domain.Person;
import com.intellif.service.IPersonService;
import com.intellif.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
* 作者:步程
* 创建时间:2018-05-24
**/
@RestController
@RequestMapping("person")
@Print
public class PersonController{

	@Autowired
	private IPersonService  personService;

	@RequestMapping("test")
	@PrintArgsDetail()
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
		Object datas = personService.listPerson();
		return ServerResult.success(datas);
	}
}