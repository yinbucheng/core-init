package com.intellif.web;
import com.intellif.domain.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;
import com.intellif.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("user")
public class UserController{

	@Autowired
	private IUserService  userService;

	@RequestMapping("test")
	public Object test(int pageNum,int pageSize) {
		return userService.findSql("select id from db1.t_user",pageNum,pageSize);
	}

	@RequestMapping("save")
	public Object save(){
		User user = new User();
		user.setName("yinchong"+new Random().nextInt(100));
		user.setAge(new Random().nextInt(100));
		userService.save(user);
		return "success";
	}


	@RequestMapping("findAll")
	public Object listAll(){
		return userService.listAll();
	}

	@RequestMapping("findByName")
	public Object findByName(String name){
		return userService.listEqualField("name",name);
	}

	@RequestMapping("findLikeName")
	public Object findLikeName(String name){
		return userService.listLikeField("name",name+"%");
	}

	@RequestMapping("findAgeBetween")
	public Object findAgeBetween(Integer startAge,Integer endAge){
		return userService.listBetweenField("age",startAge,endAge);
	}



}