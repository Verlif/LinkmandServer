# Linkmand

Linkmand（中文名：链接指令）旨在方便地通过三方设备操作目标计算机。
为了能增加拓展性，以 [socket-command](https://github.com/Verlif/socket-command) 为框架，以支持jar指令包。

------

本项目为服务端，可直接通过jar包运行。例如：

```shell
java -jar linkmand.jar
```

客户端可以通过Java自定义（推荐，因为方便），也可以通过本项目规则，使用socket来进行操作。  
这里有 [客户端制作教程](instructions/制作Linkmand的客户端.md)  
这里有 [可用的客户端仓库](https://github.com/topics/linkmand-client)

------

## 配置说明

```json
{
  "port": 16508,
  "max": 2,
  "tied": 1,
  "paths": [
    "F:\\Code\\JAVA\\socket-command-answer\\target\\socket-command-answer-0.1.jar",
    "F:\\Code\\JAVA\\socket-command-cmd\\target\\socket-command-cmd-1.1.jar"
  ],
  "fileFilter" : {
    "exclude" : [ ".*beta.*" ],
    "include" : [ ".*command.*" ]
  },
  "allowedNames": ["Verlif"],
  "blockedNames": [],
  "allowedIps": [],
  "blockedIps": []
}
```

|    配置参数名     |  参数类型   | 参数作用                      |
|:------------:|:-------:|:--------------------------|
|     port     |   数字    | 设定当前监听的端口号                |
|     max      |   数字    | 客户端处理器的最大数量               |
|     tied     |   数字    | 每个客户端处理器管理的客户端数量          |
|    paths     |  字符串数组  | 加载的jar指令包路径列表             |
|   exclude    | 正则表达式数组 | 不加载的指令包名称正则               |
|   include    | 正则表达式数组 | 只加载的指令包名称正则，会无效化exclude参数 |
| allowedNames |  字符串数组  | 允许的客户端名称，会无效化blockedNames |
| blockedNames |  字符串数组  | 不允许的客户端名称                 |
|  allowedIps  |  字符串数组  | 允许的客户端IP，会无效化blockedIps   |
|  blockedIps  |  字符串数组  | 不允许的客户端IP                 |