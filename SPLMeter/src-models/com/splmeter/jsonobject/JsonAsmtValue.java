package com.splmeter.jsonobject;

import java.util.ArrayList;
import java.util.List;

import com.splmeter.entities.AsmtValue;
import com.splmeter.entities.SPLValue;

/**
 * Entity mapped to table "ASMT_VALUE".
 */
public class JsonAsmtValue {

	private int asmt_id;
	private String imei;
	private String modeltype;
	private String mode;
	private String source;
	private String asmt;
	private Integer gender;
	private Integer age;
	private Float mF;
	private Float mLpa;
	private Float l10;
	private Float l50;
	private Float l90;
	private Float laeq;
	private Float calb;
	private String lstTime;
	private String timeZone;
	private String utc;
	private List<JsonSPLValue> splValueList;
	private String token;

	public JsonAsmtValue() {
	}

	public JsonAsmtValue(AsmtValue asmtValue) {
		try {
			this.asmt_id = asmtValue.getId().intValue();
			this.imei = asmtValue.getImei();
			this.modeltype = asmtValue.getModeltype();
			this.mode = asmtValue.getMode();
			this.source = asmtValue.getSource();
			this.asmt = asmtValue.getAsmt();
			this.gender = asmtValue.getGender();
			this.age = asmtValue.getAge();
			this.mF = asmtValue.getMF();
			this.mLpa = asmtValue.getMLpa();
			this.l10 = asmtValue.getL10();
			this.l50 = asmtValue.getL50();
			this.l90 = asmtValue.getL90();
			this.laeq = asmtValue.getLaeq();
			this.calb = asmtValue.getCalb();
			this.lstTime = asmtValue.getLstTime();
			this.timeZone = asmtValue.getTimeZone();
			this.utc = asmtValue.getUtc();
			this.token = "dq3421bswikwb52jp0sa34hmdltwq1fb";
			List<SPLValue> sList = asmtValue.getSplValueList();
			this.splValueList = new ArrayList<>();
			for (SPLValue splValue : sList) {
				this.splValueList.add(new JsonSPLValue(splValue));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public int getAsmt_id() {
		return asmt_id;
	}

	public void setAsmt_id(int asmt_id) {
		this.asmt_id = asmt_id;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getModeltype() {
		return modeltype;
	}

	public void setModeltype(String modeltype) {
		this.modeltype = modeltype;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getAsmt() {
		return asmt;
	}

	public void setAsmt(String asmt) {
		this.asmt = asmt;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Float getMF() {
		return mF;
	}

	public void setMF(Float mF) {
		this.mF = mF;
	}

	public Float getMLpa() {
		return mLpa;
	}

	public void setMLpa(Float mLpa) {
		this.mLpa = mLpa;
	}

	public Float getL10() {
		return l10;
	}

	public void setL10(Float l10) {
		this.l10 = l10;
	}

	public Float getL50() {
		return l50;
	}

	public void setL50(Float l50) {
		this.l50 = l50;
	}

	public Float getL90() {
		return l90;
	}

	public void setL90(Float l90) {
		this.l90 = l90;
	}

	public Float getLaeq() {
		return laeq;
	}

	public void setLaeq(Float laeq) {
		this.laeq = laeq;
	}

	public Float getCalb() {
		return calb;
	}

	public void setCalb(Float calb) {
		this.calb = calb;
	}

	public List<JsonSPLValue> getSplValueList() {
		return splValueList;
	}

	public void setSplValueList(List<JsonSPLValue> splValueList) {
		this.splValueList = splValueList;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getLstTime() {
		return lstTime;
	}

	public void setLstTime(String lstTime) {
		this.lstTime = lstTime;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getUtc() {
		return utc;
	}

	public void setUtc(String utc) {
		this.utc = utc;
	}

}
