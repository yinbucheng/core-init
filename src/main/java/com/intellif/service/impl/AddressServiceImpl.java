package com.intellif.service.impl;
import com.intellif.core.*;
import com.intellif.domain.Address;
import com.intellif.dao.AddressDao;
import org.springframework.stereotype.Service;
import com.intellif.service.IAddressService;
/**
* 作者:步程
**/
@Service
public class AddressServiceImpl extends ServiceImpl<AddressDao,Address> implements IAddressService{
}