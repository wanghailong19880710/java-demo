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
public interface AddressService {
  Address   findAddress(String userName);
  Iterator<Address>       findAddresses(String userName);
}
