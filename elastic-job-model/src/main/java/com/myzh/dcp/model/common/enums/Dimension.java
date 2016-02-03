package com.myzh.dcp.model.common.enums;

/**
 * 行为维度
 * @author chensg
 *
 */
public enum Dimension {
	/**
	 * 诊疗软件
	 */
	YDB("001","诊疗软件"), 
	/**
	 * 学术园地
	 */
	XSYD("002","学术园地"),
	/**
	 * 医疗直通车
	 */
	YLZTC("003","医疗直通车"),
	/**
	 * 明医商城
	 */
	MYSC("004","明医商城"),
	/**
	 * 积分商城
	 */
	JFSC("005","积分商城"),
	/**
	 * 微信
	 */
	WX("006","微信"),
	/**
	 * 媒体
	 */
	MT("007","媒体"),
	/**
	 * 金融
	 */
	JR("008","金融");
	
	String code;
	String desc;
	Dimension(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	
	public static Dimension getBehaviorDimension(String code) {
		Dimension[] dimensions = values();
		for (Dimension dimension:dimensions) {
			if (dimension.code.equals(code)) {
				return dimension;
			}
		}
		return null;
	}
	
	public static boolean isExist(String code) {
		return getBehaviorDimension(code) != null;
	}
	
	public String getCode() {
		return this.code;
	}
	
	public String getDesc() {
		return this.desc;
	}
}
