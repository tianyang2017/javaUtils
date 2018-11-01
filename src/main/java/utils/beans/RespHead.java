package utils.beans;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
/**
 * 
 * @author 张俊
 * @date 2016年5月11日
 */
public class RespHead {
	private Date jssj;// 接收时间 JSSJ 字符串 16 消息返回时提供，格式：日期格式A
	private String ztcljg;// 消息主体处理结果 ZTCLJG 字符串 5 消息返回时提供，详见4.1.11消息主体处理结果
	private String cwxx;// 错误提示内容 CWXX 字符串 200
						// 消息返回时，若消息主体处理结果不为“00000”时，该字段的内容为对出错情况的说明
	private String bzxx;// 备注信息 BZXX 字符串 200 对发送消息头中填写的内容原值返回

	/**
	 * 交易是否成功 --发票和配送明细重复验收时，返回22007错误
	 * 
	 * @return
	 */
	public boolean isOk() {
		if (StringUtils.isEmpty(ztcljg))
			return false;
		boolean isOk = ztcljg.startsWith("0") || ztcljg.startsWith("3")
				|| ("22007".equals(ztcljg) && cwxx.contains("已验收"));
		return isOk;
	}

	/**
	 * 用户名和密码是否正确
	 * @return
	 */
	public boolean isAuthenticated() {
		//11003	用户名密码校验失败	上传的用户名加密码校验失败，用户不存在或密码错误
		return !StringUtils.equals(ztcljg, "11003");
	}

	/**
	 * @return the jssj
	 */
	public Date getJssj() {
		return jssj;
	}

	/**
	 * @param jssj
	 *            the jssj to set
	 */
	public void setJssj(Date jssj) {
		this.jssj = jssj;
	}

	/**
	 * @return the ztcljg
	 */
	public String getZtcljg() {
		return ztcljg;
	}

	/**
	 * @param ztcljg
	 *            the ztcljg to set
	 */
	public void setZtcljg(String ztcljg) {
		this.ztcljg = ztcljg;
	}

	/**
	 * @return the cwxx
	 */
	public String getCwxx() {
		return cwxx;
	}

	/**
	 * @param cwxx
	 *            the cwxx to set
	 */
	public void setCwxx(String cwxx) {
		this.cwxx = cwxx;
	}

	/**
	 * @return the bzxx
	 */
	public String getBzxx() {
		return bzxx;
	}

	/**
	 * @param bzxx
	 *            the bzxx to set
	 */
	public void setBzxx(String bzxx) {
		this.bzxx = bzxx;
	}

}
