package com.github.xiaofu.demo.jetty;


import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * <p>
 * </p>
 * 
 * @author fulaihua 2014-5-15 上午10:32:37
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2014-5-15
 * @modify by reason:{方法名}:{原因}
 */
@WebService(name = "BehavioralAnalysisService", serviceName = "BehavioralAnalysisService", targetNamespace = "http://vipcloud.cqvip.com/VipCloud/Service/BehaviorAlanalysis")
public interface BehavioralAnalysisService
{
	 
	@WebMethod
	public String logRecord(@WebParam(name = "data") String data,
			@WebParam(name = "tableName") String tableName);

	@WebMethod
	public String query(@WebParam(name = "selectSql") String selectSql);
	
	 
 
}
