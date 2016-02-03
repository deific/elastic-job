package com.myzh.dpc.console.core.controller.support;

import java.util.HashMap;
import java.util.Map;

public class ResponseWrapper {
    private String rtnMessage = "Ok";
    private int rtnCode = 0;
    private Object data = new HashMap<String, Object>();
    
    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void add(String key, Object value) {
        ((Map<String, Object>)data).put(key, value);
    }

	public String getRtnMessage() {
		return rtnMessage;
	}

	public void setRtnMessage(String rtnMessage) {
		this.rtnMessage = rtnMessage;
	}

	public int getRtnCode() {
		return rtnCode;
	}

	public void setRtnCode(int rtnCode) {
		this.rtnCode = rtnCode;
	}

	@Override
	public String toString() {
		return super.toString();
	}

}
