# API说明文档

## 目录 DOC

### [1. 说明](#c1)
- [1.1 API协议](#c1.1)

### [2. 用户积分](#c2)
- [2.1 积分查询](#c2.1)
- [2.2 积分明细查询](#c2.2)
- [2.3 积分消费](#c2.3)
- [2.4 产生积分](#c2.4)

## <h3 id="c1">1. 说明</h3>

### <h3 id="c1.1">1.1 API协议</h3>
客户端与服务器之间通过HTTP/HTTPS协议进行通信，其中：
- **Request:** 采用标准的Form参数形式， POST方式提交
```javascript

http://112.126.72.237/dpc/api/v1/point/getUserPoint?clinicCode=81000000008

```
- **Response:** 采用JSON数据格式进行返回，基本形式：

```javascript
{
  rtnCode: '',         //错误代码，数字类型。客户端可以根据错误代码进行异常的处理流程。若请求成功处理，则为0；否则则为错误代码, 错误代码参见1.2 错误代码。
  rtnMessage: '',      //错误消息，文本类型。若请求成功处理，则为空字符串；否则则为业务错误信息, 如'用户还没有登录。'。
  data:             //具体的业务数据。
    {
    }
}
```

- 正常示例：

```javascript
{
    "rtnMessage": "处理成功",
    "rtnCode": 0,
    "data": {
        "clinicCode": "81000000008",
        "userType": "superAdmin",
        "userId": "8a7b427850c2a9990150c30a99c700ee",
        "allPoint": 105,
        "year": "2016"
    }
}
```

- 异常示例：

```javascript
{
    "rtnMessage": "用户积分不足",
    "rtnCode": 201,
    "data": {}
}
```

在具体的API中，返回值将只包含data里面的内容。
- **server_url:**
 - 测试地址server_url:http://112.126.72.237/dpc
 - 发布地址:

- **异常处理:**
- **说明:** 服务器端业务处理结果编码和信息，服务端处理有错误异常时，只返回rtnCode和rtnMessage信息，data为空

| rtnCode |        rtnMessage        |           备注           |
|---------|--------------------------|--------------------------|
|       0 |                          | 成功                     |
|     100 | 未知服务器异常。         | 未知服务器异常           |
|     104 | 渠道编码不能空位         | 渠道编码不能空位         |
|     201 | 用户积分不足             | 用户积分不足             |
|     202 | 用户id或诊所编码不能为空 | 用户id或诊所编码不能为空 |
|     203 | 用户积分类型不存在       | 用户积分类型不存在       |

- **说明:**
 - 错误编码后续根据开发进度不断补充完善，原则上只增不减。

- **错误编码完善记录:**

## <h3 id="c2">2. 用户积分</h3>
### <h3 id="c2.1">2.1 积分查询</h3>
- **说明:** 查询指定用户当前时间为止的积分余额
- **URL:**  http://{server_url}/api/v1/point/getUserPoint
- **请求参数:**

|    名称    | 数据类型 | 必须 |   说明   |
|------------|----------|------|----------|
| userId     | String   | 否   | 用户id   |
| clinicCode | String   | 否   | 诊所编码 |

- **返回值:**

|    名称    | 数据类型 | 必须 |    说明    |
|------------|----------|------|------------|
| clinicCode | String   | 否   | 诊所编码   |
| userType   | String   | 否   | 用户类型   |
| userId     | String   | 否   | 用户id     |
| allPoint   | Integer  | 否   | 积分余额   |
| year       | String   | 否   | 积分所属年 |


**示例:**

```javascript
{
    "clinicCode": "81000000008",
    "userType": "superAdmin",
    "userId": "8a7b427850c2a9990150c30a99c700ee",
    "allPoint": 105,
    "year": "2016"
}
```

### <h3 id="c2.2">2.2 积分明细查询</h3>
- **说明:** 查询用户的明细积分
- 如果指定日期，则查询指定日期用户的积分明细；
- 如果未指定日期，则查询所有日期的用户积分明细，倒序排序。
- **URL:** https://{server_url}/api/v1/point/getAllUserPointDetails
- **请求参数:**

|    名称    | 数据类型 | 必须 |                  说明                 |
|------------|----------|------|---------------------------------------|
| userId     | String   | 否   | 用户id                                |
| clinicCode | String   | 否   | 诊所编码                              |
| pointTyoe  | String   | 否   | 积分类型，取值：0 产生积分 1 消费积分 |
| pointDate  | String   | 否   | 积分日期，格式：yyyy-MM-dd            |

- **返回值**

|      名称      | 数据类型 | 必须 |                说明                |
|----------------|----------|------|------------------------------------|
| data           | Array    | 是   | 明细列表                           |
|----------------|----------|------|------------------------------------|
| id             | int      | 是   | 明细id                             |
| userId         | String   | 是   | 用户id                             |
| clinicCode     | String   | 是   | 诊所编码                           |
| pointType      | String   | 是   | 积分类型                           |
| pointDimension | String   | 是   | 积分维度                           |
| point          | Integer  | 是   | 积分值                             |
| desc           | String   | 是   | 描述                               |
| pointTime      | String   | 是   | 积分时间,格式：yyyy-MM-dd hh:mm:ss |
| createTime     | String   | 是   | 创建时间,格式：yyyy-MM-dd hh:mm:ss |

```javascript
[
    {
        "id": 5,
        "userId": "8a7b427850c2a9990150c30a99c700ee",
        "clinicCode": "81000000008",
        "pointType": "0",
        "pointDimension": "001",
        "point": 5,
        "desc": "您登录了系统产生积分5",
        "pointTime": "2016-01-11 12:48:52",
        "createTime": "2016-01-15 11:37:02"
    },
    {
        "id": 6,
        "userId": "8a7b427850c2a9990150c30a99c700ee",
        "clinicCode": "81000000008",
        "pointType": "0",
        "pointDimension": "001",
        "point": 100,
        "desc": "您累计1.0天签到了，奖励积分100",
        "pointTime": "2016-01-11 12:48:52",
        "createTime": "2016-01-15 11:37:03"
    }
]
```

- 异常

| rtnCode |        rtnMessage        |           备注           |
|---------|--------------------------|--------------------------|
|     203 | 用户积分类型不存在       | 用户积分类型不存在       |


### <h3 id="c2.3">2.3 消费积分</h3>
- **说明:** 指定用户或诊所消费积分
- **URL:** http://{server_url}/api/v1/point/consume
- **请求参数:**

|    名称    | 数据类型 | 必须 |                   说明                   |
|------------|----------|------|------------------------------------------|
| userId     | String   | 否   | 用户id                                   |
| clinicCode | String   | 否   | 诊所编码                                 |
| channel    | String   | 是   | 渠道，积分消费的来源，取值：参见渠道取值 |
| point      | Integer  | 是   | 积分数                                   |

- 渠道编码

| 编码 |    含义    |
|------|------------|
|  001 | 诊疗软件   |
|  002 | 学术园地   |
|  003 | 医疗直通车 |
|  004 | 明医商城   |
|  005 | 积分商城   |
|  006 | 微信       |
|  007 | 媒体       |
|  008 | 金融       |
    

- **返回值**
- 积分消费后的用户积分情况

|    名称    | 数据类型 | 必须 |    说明    |
|------------|----------|------|------------|
| clinicCode | String   | 是   | 诊所编码   |
| userType   | String   | 是   | 用户类型   |
| userId     | String   | 是   | 用户id     |
| allPoint   | Integer  | 是   | 积分余额   |
| year       | String   | 是   | 积分所属年 |

json格式：
```javascript
{
    "clinicCode": "81000000008",
    "userType": "superAdmin",
    "userId": "8a7b427850c2a9990150c30a99c700ee",
    "allPoint": 105,
    "year": "2016"
}
```

- **异常**

| rtnCode |        rtnMessage        |           备注           |
|---------|--------------------------|--------------------------|
|     104 | 渠道编码不能空位         | 渠道编码不能空位         |
|     201 | 用户积分不足             | 用户积分不足             |
|     202 | 用户id或诊所编码不能为空 | 用户id或诊所编码不能为空 |


### <h3 id="c2.4">2.4 产生积分</h3>
- **说明:** 直接给指定用户产生积分
- **URL:** http://{server_url}/api/v1/point/create
- **请求参数:**
  
|    名称    | 数据类型 | 必须 |                   说明                   |
|------------|----------|------|------------------------------------------|
| userId     | String   | 否   | 用户id                                   |
| clinicCode | String   | 否   | 诊所编码                                 |
| channel    | String   | 是   | 渠道，积分消费的来源，取值：参见渠道取值 |
| point      | Integer  | 是   | 积分数                                   |

- **返回值**
- 产生积分后的用户积分情况

|    名称    | 数据类型 | 必须 |    说明    |
|------------|----------|------|------------|
| clinicCode | String   | 是   | 诊所编码   |
| userType   | String   | 是   | 用户类型   |
| userId     | String   | 是   | 用户id     |
| allPoint   | String   | 是   | 积分余额   |
| year       | String   | 是   | 积分所属年 |

json格式：
```javascript
{
    "clinicCode": "81000000008",
    "userType": "superAdmin",
    "userId": "8a7b427850c2a9990150c30a99c700ee",
    "allPoint": 105,
    "year": "2016"
}
```
