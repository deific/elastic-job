package com.myzh.dpc.console.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myzh.dpc.console.core.controller.support.ResponseWrapper;
import com.myzh.dpc.console.core.exception.ExceptionCode;
import com.myzh.dpc.console.core.exception.ExceptionFactory;

@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
    ExceptionFactory exceptionFactory;

    @RequestMapping
    public String index(){
        return "login";
    }

    @RequestMapping(value = "test")
    @ResponseBody
    public ResponseWrapper test(){
        ResponseWrapper rw = new ResponseWrapper();
        rw.add("test","OK");
        rw.add("testMessage","hahah");
        return rw;
    }

    @RequestMapping(value = "/403", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String error403(){
        throw exceptionFactory.create(ExceptionCode.Common_User_Token_Authority_Error);
    }
}
