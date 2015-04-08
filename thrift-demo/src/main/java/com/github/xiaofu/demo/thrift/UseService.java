package com.github.xiaofu.demo.thrift;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TMemoryBuffer;

public class UseService {
	public static void main(String[] args) {
		TMemoryBuffer transport = new TMemoryBuffer(1000);
		TProtocol protocol = new TCompactProtocol(transport);
		 
		 for (int i = 0; i < 1000; i++)
         {
			 Info item = new Info  ();
			 item.setEnable(true);
			 item.setReload(false);
			 item.setKey("fasdfas");
             try {
				item.write(protocol);
			} catch (TException e) {
				 
				e.printStackTrace();
			}
         }
		 System.out.println( transport.length());
		
	}
}
