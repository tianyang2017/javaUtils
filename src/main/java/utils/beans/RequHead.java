package utils.beans;

import java.util.Date;

/**
 * 
 * @author 张俊
 * @date 2016年5月11日
 */
public class RequHead {
	private Date fssj;//发送时间
	private String ip;//IP地址;业务操作终端的IP地址，格式：XXX.XXX.XXX.XXX
	private String mac;//MAC地址;业务操作终端的MAC地址，格式XXXXXXXXXXXX
	private String bzxx;//备注信息;自行填写内容，不做任何处理，返回时原值返回
	
	
	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	/**
	 * @return the mac
	 */
	public String getMac() {
		return mac;
	}
	/**
	 * @param mac the mac to set
	 */
	public void setMac(String mac) {
		this.mac = mac;
	}
	/**
	 * @return the bzxx
	 */
	public String getBzxx() {
		return bzxx;
	}
	/**
	 * @param bzxx the bzxx to set
	 */
	public void setBzxx(String bzxx) {
		this.bzxx = bzxx;
	}
	public Date getFssj() {
		return fssj;
	}
	public void setFssj(Date fssj) {
		this.fssj = fssj;
	}
}
