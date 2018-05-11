package com.intellif.web;
import com.intellif.domain.Address;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;
import com.intellif.service.*;
import org.springframework.beans.factory.annotation.Autowired;@Controller
@RequestMapping("address")
public class AddressController{

	@Autowired
	private IAddressService  addressService;

	@RequestMapping("test")
	@ResponseBody
	public Object test(){
		return "success";
	}

}