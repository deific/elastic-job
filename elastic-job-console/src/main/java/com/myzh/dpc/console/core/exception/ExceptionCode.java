package com.myzh.dpc.console.core.exception;

public enum ExceptionCode {
    // 用户相关
    User_Login_UserName_Password_Required("common.user.token.required",101),
    User_Login_Code_Error("user.login.code.error",102),
    User_Login_UserName_Password_Error("user.login.username.password.error",103),
    User_Login_Not_Administrator_Error("user.login.not.administrator.error",103),
    // 公共
    Unknown_Exception("unknown.exception", 100),
    Common_User_Token_Required("common.user.token.required",101),
    Common_User_Token_Authority_Error("common.user.token.authority.error",102),
    Common_Parameter_Required("common.parameter.required",103),
    Common_Channel_Is_Null("common.channel.is.null",104),
    Common_Channel_Not_Exist("common.channel.not.exist",105),

    // 用户积分相关
    User_Point_Is_Not_Enough("user.point.is.not.enough", 201),
    UserId_Or_ClinicCode_Required("user.userid.or.clinicCode.required", 202),
    User_Point_Type_Not_Exist("user.point.type.not.exist",203),
    User_Point_Not_Exist("user.point.not.exist",204),
    User_Not_Exist("user.not.exist",205),
    ;

    // message code
    private String msgCode;
    // exception code
    private int expCode;

    private ExceptionCode(String msgCode, int expCode) {
        this.msgCode = msgCode;
        this.expCode = expCode;
    }

    public String getMsgCode() {
        return msgCode;
    }
    public int getExpCode() {
        return expCode;
    }

    /**
     * 根据信息编码获取异常编码
     * @param msgCode
     * @return
     */
    public static int valueOfExpCode(String msgCode) {
        int rtnCode = 100;
        ExceptionCode[] exceptionCodes = ExceptionCode.values();
        for(ExceptionCode exceptionCode:exceptionCodes) {
            if(exceptionCode.msgCode.equals(msgCode)) {
                rtnCode = exceptionCode.expCode;
                break;
            }
        }
        return rtnCode;
    }
}
