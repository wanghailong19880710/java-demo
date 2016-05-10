/**
 * 
 */
package com.github.xiaofu.demo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

 

 


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.healthmarketscience.jackcess.Database.FileFormat;

import net.ucanaccess.jdbc.UcanaccessDriver;

/**
 * @author fulaihua
 *
 */
public class AccessDemo {
	static Map<String,Set<String>> typeFields=new HashMap<String,Set<String>>();
	static Map<String,BufferedWriter> writerMaps=new  HashMap<String,BufferedWriter>();
	private static void init()
	{
		
			Set<String> qkFields= new LinkedHashSet<String>();
			Collections.addAll(qkFields,StringUtils.split("lngid,titletype,mediaid,media_c,media_e,years,vol,num,volumn,specialnum,subjectnum,gch,title_c,title_e,keyword_c,keyword_e,remark_c,remark_e,class,beginpage,endpage,jumppage,pagecount,showwriter,showorgan,imburse,author_e,medias_qk,refercount,referids,intpdf,isqwkz,intgby,isinclude,range,fstorgan,fstwriter,muinfo,language,issn,type",","));
			typeFields.put("1",qkFields);
			Set<String> xwFields=new  LinkedHashSet<String>();
			Collections.addAll(xwFields,StringUtils.split("lngid,title_c,title_e,bstitlename_pair,showwriter,author_e,bssubjectcode,bsspeciality,bsdegree,showorgan,bstutorsname,years,bsstudydirection,language,class,class,keyword_c,bsmarkskeywords,keyword_e,remark_c,remark_e,bsdigestlanguages,imburse,strreftext,bsdatabsename,pagecount,bsthesissize,bscontributionpeople,bscontributiontime,fulltextaddress,netfulltextaddr,type",","));
			typeFields.put("2",xwFields);
			Set<String> hyFields=new  LinkedHashSet<String>();
			Collections.addAll(hyFields,StringUtils.split("lngid,title_c,title_e,years,hymeetingrecordname,hyenmeetingrecordname,showwriter,author_e,showorgan,media_c,media_e,hymeetingdate,hymeetingplace,num,hyhostorganization,hypressorganization,hypressdate,hypressplace,hysocietyname,hychiefeditor,beginpage,endpage,jumppage,class,keyword_c,keyword_e,remark_c,remark_e,imburse,pagecount,hythesissize,language,strreftext,fulltextpath,netfulltextaddr,type",","));
			typeFields.put("3",hyFields);
			Set<String> zlFields=new  LinkedHashSet<String>();
			Collections.addAll(zlFields,StringUtils.split("lngid,title_c,zlmaintype,years,showwriter,showorgan,zlprovincecode,zlapplicationnum,zlapplicationdata,media_c,zlopendata,zlpriority,zlmainclassnum,zlclassnum,zlinternationalpub,zlinternationalapp,remark_c,keyword_c,zlsovereignty,zlagents,zlagency,zllegalstatus,pagecount,zlthesissize,fulltextaddress,netfulltextaddr,type",","));
			typeFields.put("4",zlFields);
			Set<String> bzFields=new  LinkedHashSet<String>();
			Collections.addAll(bzFields,StringUtils.split("lngid,title_c,title_e,bzmaintype,years,media_c,bznum2,bzpubdate,bzimpdate,bzstatus,bzcountry,bzissued,showorgan,showwriter,bzcommittee,bzapproved,bzlevel,class,keyword_c,keyword_e,bzintclassnum,remark_c,bzpagenum,language,bzrelationship,bzreplacedbz,bzsubsbz,bzrefbz,pagecount,bzthesissize,fulltextaddress,netfulltextaddr,netfulltextaddr,type",","));
			typeFields.put("5",bzFields);
			Set<String> cgFields=new  LinkedHashSet<String>();
			Collections.addAll(cgFields,StringUtils.split("lngid,title_c,cgitemnumber,years,cglimituse,cgprovinces,class,cgcgtypes,keyword_c,remark_c,cgprojectname,cgappunit,cgappdate,cgawards,cgdecunit,cgdecdate,cgrecomunit,cgrecomdate,cgrecomnum,cgrecomcode,cgregunit,media_c,cgregdate,cgplanname,cgplandate,cgprocesstimes,showorgan,showwriter,cgindustry,cginduscode,cgpatents,cgpatentnum,cgpatentsauthno,cgtransnotes,cgtransrange,cgtranscond,cgtranscontent,cgtransmode,cgtranscost,cginvestment,cgbuildtimes,cginvestexplain,cgoutputvalue,cgtaxes,cgforeiexch,cgsavemoney,cgpromoteways,cgpromoterange,cgpromotetrack,cgpromoteexp,cgcontactunit,cgcontactaddr,cgcontacted,cgcontactphone,cgcontactfax,cgzipcode,cgcontactemail,netfulltextaddr,fulltextaddress,type",","));
			typeFields.put("6",cgFields);
	}

	private static void importData() throws IOException, SQLException, ClassNotFoundException
	{
		 init();
		String mdbFileName =  "hydx.mdb";
		File mdbFile = new File("/tmp/hydx/", mdbFileName);
		Connection ucanaccessConnection = getConn(mdbFile);
		ucanaccessConnection.setAutoCommit(false);
		Statement statement = ucanaccessConnection.createStatement() ;
		 File file=new File("d:/_user_flh_HYDXDataExport_4109-r-00000");
		 BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		 String  line =reader.readLine();
		 while(line!=null)
		 {
			 line = line.replace("'", "''");
			 String[] fieldValues=line.split(Pattern.quote("|") );
			 String type=fieldValues[fieldValues.length-1];
			 StringBuilder builderTotal=new StringBuilder();
			 builderTotal.append("insert into table_"+type+"(");
			 builderTotal.append(StringUtils.join(typeFields.get(type), ","));
			 builderTotal.append(") values (");
			int len= typeFields.get(type).size();
			StringBuilder builder=new StringBuilder();
			  for (int i = 0; i <len ; i++)
			  {
				  builder.append("'");
                  builder.append(fieldValues[i]);
                  builder.append("'");
                  if (i == len-1)
                      continue;
                  builder.append(",");
			  }
			 builderTotal.append(builder);
			 builderTotal.append(")");
			// System.out.println(builderTotal);
			 statement.execute(builderTotal.toString());
			 line =reader.readLine();
		 }
		 /*for (Writer writer : writerMaps.values()) {
			 writer.close();
		}*/
		 reader.close();
			ucanaccessConnection.commit();
		ucanaccessConnection.close();
	}
	private static BufferedWriter getWriter(String fileName) throws FileNotFoundException
	{
		File file=new File("d:/"+fileName+".txt");
		if(file.exists())
			return writerMaps.get(fileName);
		BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
		 writerMaps.put(fileName, writer);
		 return writer;
	}
	private static Connection getConn(File mdbFile) throws SQLException, ClassNotFoundException
	{
		if (!mdbFile.getParentFile().exists())
			mdbFile.getParentFile().mkdirs();
		Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
		String url = UcanaccessDriver.URL_PREFIX
				+ mdbFile.getAbsolutePath() + ";newdatabaseversion="
				+ FileFormat.V2003.name();
		Connection ucanaccessConnection = DriverManager.getConnection(url, "", "");
		return ucanaccessConnection;
	}
	private static void createTable() throws ClassNotFoundException, SQLException
	{
		String mdbFileName =  "hydx.mdb";
		File mdbFile = new File("/tmp/hydx/", mdbFileName);
		Connection ucanaccessConnection = getConn(mdbFile);
		ucanaccessConnection.setAutoCommit(false);
		Statement statement = ucanaccessConnection.createStatement();
		 init();
		for (Map.Entry<String, Set<String>> entry : typeFields.entrySet()) {
			StringBuilder builder=new StringBuilder();
			builder.append("CREATE TABLE table_"+entry.getKey()+" ( ");
			int i=0;
			for (String field : entry.getValue()) {
				builder.append(field);
				builder.append(" memo");
				i++;
				if(i==entry.getValue().size())
					continue;
				else
					builder.append(",");
			}
			builder.append(" )");
			System.out.println(builder);
			statement.execute(builder.toString().substring(0,builder.toString().length()));
		
			ucanaccessConnection.commit();
		}
		ucanaccessConnection.close();
	}
	/**
	 * @param args
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
		//createTable();
	   importData();
	}

}
