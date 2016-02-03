package com.dangdang.ddframe.job.exception;

/**
 * 异常处理
 * @author chensg
 *
 */
public interface ExceptionHandler {
	/**
	 * 异常处理
	 * @param e
	 */
	public void handler(Step step, Exception e);
	
	static enum Step {
			fetch,process
		}
}
