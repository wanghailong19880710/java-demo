package com.github.xiaofu.demo;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect
public class Ref {

	private String Id = "";
	private String ReferText = "";
	private String strTitle = "";
	private String strType = "";
	private String strName = "";
	private String strPubWriter = "";
	private String strWriter1 = "";
	private String strWriter2 = "";
	private String strYearVolNum = "";
	private String strPages = "";
	private String other = "";
	private String lngSourceID = "";
	private String sBookID = "";
	private String lngID = "";
	private String GCH = "";
	private String zz;
	
	@JsonProperty(value="Id")
	public String getId() {
		return Id;
	}
	
	public void setId(String id) {
		Id = id;
	}
	@JsonProperty(value="ReferText")
	public String getReferText() {
		return ReferText;
	}

	public void setReferText(String referText) {
		ReferText = referText;
	}

	public String getStrTitle() {
		return strTitle;
	}

	public void setStrTitle(String strTitle) {
		this.strTitle = strTitle;
	}

	public String getStrType() {
		return strType;
	}

	public void setStrType(String strType) {
		this.strType = strType;
	}

	public String getStrName() {
		return strName;
	}

	public void setStrName(String strName) {
		this.strName = strName;
	}

	public String getStrPubWriter() {
		return strPubWriter;
	}

	public void setStrPubWriter(String strPubWriter) {
		this.strPubWriter = strPubWriter;
	}

	public String getStrWriter1() {
		return strWriter1;
	}

	public void setStrWriter1(String strWriter1) {
		this.strWriter1 = strWriter1;
	}

	public String getStrWriter2() {
		return strWriter2;
	}

	public void setStrWriter2(String strWriter2) {
		this.strWriter2 = strWriter2;
	}

	public String getStrYearVolNum() {
		return strYearVolNum;
	}

	public void setStrYearVolNum(String strYearVolNum) {
		this.strYearVolNum = strYearVolNum;
	}

	public String getStrPages() {
		return strPages;
	}

	public void setStrPages(String strPages) {
		this.strPages = strPages;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public String getLngSourceID() {
		return lngSourceID;
	}

	public void setLngSourceID(String lngSourceID) {
		this.lngSourceID = lngSourceID;
	}

	public String getsBookID() {
		return sBookID;
	}

	public void setsBookID(String sBookID) {
		this.sBookID = sBookID;
	}

	public String getLngID() {
		return lngID;
	}

	public void setLngID(String lngID) {
		this.lngID = lngID;
	}
	@JsonProperty(value="GCH")
	public String getGCH() {
		return GCH;
	}

	public void setGCH(String gCH) {
		GCH = gCH;
	}

}
