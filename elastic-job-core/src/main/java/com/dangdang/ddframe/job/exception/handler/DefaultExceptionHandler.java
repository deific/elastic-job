package com.dangdang.ddframe.job.exception.handler;

import com.dangdang.ddframe.job.exception.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultExceptionHandler implements ExceptionHandler {

	@Override
	public void handler(Step step, Exception e) {
		log.error("发生异常,步骤：{},异常信息：", step, e);
	}
}
