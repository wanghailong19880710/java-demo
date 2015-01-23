/**
 * @ProjectName: demo
 * @Copyright: 版权所有 Copyright © 2001-2012 cqvip.com Inc. All rights reserved. 
 * @address: http://www.cqvip.com
 * @date: 2014-9-26 下午7:40:42
 * @Description: 本内容仅限于维普公司内部使用，禁止转发.
 */
package com.github.xiaofu.demo.reflect;

/**
 * <p></p>
 * @author fulaihua 2014-9-26 下午7:40:42
 * @version V1.0   
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2014-9-26
 * @modify by reason:{方法名}:{原因}
 */
public interface BaseDao<T> {  
      T get(String id);  
  
}  
