# 制作一个客户端

`Linkmand`是基于 [socket-command](https://github.com/socket-command) 制作的，旨在构建一个开放式连接、结构化的socket指令框架。  
以下内容基于`Linkmand 0.1`版本。

## 基于Java制作

因为`Linkmand`本身就是使用的Java语言开发，所以内置了`LinkClient`，开发者可以以此为依赖，直接使用`LinkClient`来进行连接。

```java
ClientConfig config = new ClientConfig()
        .handler(new ReceiveHandler() {
            @Override
            public void receive(Client client, String s) {
                System.out.println(s);
            }
        })
        .connectedListener(new ConnectedListener() {
            @Override
            public void onConnected(Client client) {
                System.out.println("已连接");
            }
        })
        .closedListener(new ClosedListener() {
            @Override
            public void onClosed() {
                System.out.println("closed!");
            }
        });
config.ip("12.12.12.12").port(7792);
LinkClient client = new LinkClient("Verlif", config);
client.connect();
```

或者也可以使用通用的Linkmand连接规则来自定义使用。

## 通用连接规范

因为`Linkmand`对连接有用户校验，所以需要在信息传递时带上必要信息，否则连接会被服务端直接关闭。  
只要实现了连接规范，即可完成`Linkmand`的连接。  
连接规范中包括了两个内容：

* 指令规范格式
* 识别内置指令，并能回传正确的信息

### 指令规范

由于权限限制，需要客户端每次在传输信息时，附加上客户端名称。 例如：

目标：`cmd ping 127.0.0.1`  
构建：`cmd Verlif ping 127.0.0.1`

其中`Verlif`就是当前客户端的名称。

指令规范在任何情况下生效，包括传输内置指令时。

### 内置指令

* `__NAME`客户端名称
  * 由服务端发出。
  * 此指令用于校验当前的客户端名称，当客户端名称不可用时，服务端会断开连接。
  * 回传例：`__NAME Verlif Verlif` *(这里使用了指令规范)*。
* `__CLOSE`关闭连接
  * 由服务端发出、由客户端发出。
  * 当接收到此指令时，此socket会被关闭。
  * 不需要回传。
* `__CHAT`客户端间信息传输
  * 由客户端发出。
  * 此指令用于向其他的在线的客户端发送信息。
  * 客户端接收的指令中会带有来源信息，格式为`__CHAT [来源客户端名称] [信息]`，例如`__CHAT Xiaoming 你好！`
  * 回传格式：`__CHAT Verlif [目标客户端名称] [消息]`，例如`__CHAT Verlif Xiaoming 你也好呀！` *(这里使用了指令规范)*。

## 分享客户端

如果希望将客户端分享出去的话，请在仓库的`topic`中加上`linkmand-client`。  
同样， 这里有 [可用的客户端仓库](https://github.com/topics/linkmand-client)。

## 最后

通用连接规范只针对此文档的使用版本，若 __版本不同__ 或是`Linkmand`的 __修改版__ ，则可能导致连接规范不一致。
