/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.xiaofu.demo.jmock;

import java.util.Iterator;

/**
 *
 * @author xiaofu
 */
public class UserManager {

    public AddressService addressService;

    public Address findAddress(String userName) {
        return addressService.findAddress(userName);
    }

    public Iterator<Address> findAddresses(String userName) {
        return addressService.findAddresses(userName);
    }
}
