package com.intellif.web;
import com.intellif.domain.Person;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.intellif.service.*;
import org.springframework.beans.factory.annotation.Autowired;
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
}