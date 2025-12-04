package com.cdwater.digitalproduct.service;

import com.cdwater.digitalproduct.entity.Address;

import java.util.List;

public interface AddressService {

    List<Address> list();

    void add(Address address);

    void removeOne(Integer id);

    void edit(Address address);

    Address query(Integer id);
}
