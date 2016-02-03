package com.myzh.dpc.console.core.mybatis.pagination;

import java.util.List;

public interface Page<T> extends List<T> {
	
	public int getPage();
	
	public int getTotalPage();

	public void setPage(int page);
	
	public long getTotal();
	
	public int getPageSize();
}
