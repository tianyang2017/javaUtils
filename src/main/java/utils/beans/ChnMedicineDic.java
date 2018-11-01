package utils.beans;

import java.io.Serializable;
import java.util.Date;

import utils.excel.Excel;

/**
 * @author : tianyang
 * @description :
 * @date :2018年10月30日
 */
public class ChnMedicineDic implements Serializable {


	@Excel(name = "qytbdm",outName = "药品区域统编代码")
	private String qytbdm;//药品区域统编代码

	@Excel(name = "ypybdm",outName = "药品医保代码")
	private String ypybdm;//药品医保代码

	@Excel(name = "yptym",outName = "药品通用名")
	private String yptym;//药品通用名

	@Excel(name = "ypbc",outName = "药品别称")
	private String ypbc;//药品别称

	@Excel(name = "pzff",outName = "炮制方法")
	private String pzff;//炮制方法

	@Excel(name = "jldw",outName = "计量单位")
	private String jldw;//计量单位


	@Excel(name = "bz",outName = "备注")
	private String bz;//备注


	public String getQytbdm() {
		return qytbdm;
	}


	public void setQytbdm(String qytbdm) {
		this.qytbdm = qytbdm;
	}


	public String getYpybdm() {
		return ypybdm;
	}


	public void setYpybdm(String ypybdm) {
		this.ypybdm = ypybdm;
	}


	public String getYptym() {
		return yptym;
	}


	public void setYptym(String yptym) {
		this.yptym = yptym;
	}


	public String getYpbc() {
		return ypbc;
	}


	public void setYpbc(String ypbc) {
		this.ypbc = ypbc;
	}


	public String getPzff() {
		return pzff;
	}


	public void setPzff(String pzff) {
		this.pzff = pzff;
	}


	public String getJldw() {
		return jldw;
	}


	public void setJldw(String jldw) {
		this.jldw = jldw;
	}


	public String getBz() {
		return bz;
	}


	public void setBz(String bz) {
		this.bz = bz;
	}


	@Override
	public String toString() {
		return "ChnMedicineDic [qytbdm=" + qytbdm + ", ypybdm=" + ypybdm + ", yptym=" + yptym + ", ypbc=" + ypbc
				+ ", pzff=" + pzff + ", jldw=" + jldw + ", bz=" + bz + "]";
	}

	
}
