# Netty快速入门指南

本项目是学习《Netty权威指南》的实践记录，只有实践才能更好地掌握知识。

本项目目录：

* [BIO 阻塞IO](#bioblock-io)
* [NIO 非阻塞IO](#niono-block-io)
* [NIO 异步IO](#aioasynchronized-io)

## BIO（Block IO）

BIO是最原始的IO通信方式，在这一小节通过使用 JDK BIO 提供的 API 实现了一个简单的时间服务器。

详细代码可以查看：[BIO方式实现时间服务器](src/main/java/com/chenshuyi/netty/bio)

使用这个例子，首先运行TimeServer.main方法，之后运行TimeClient.main方法即可。

## NIO（No-Block IO）

NIO是实现了异步通信的通信方式，在这一小节通过 JDK NIO 的 API 实现了一个简单的时间服务器。

详细代码可以查看：[NIO方式实现时间服务器](src/main/java/com/chenshuyi/netty/nio)

这个例子很简单，分为两大部分：

* TimeServer 和 TimeServerHandler
* TimeClient 和 TimeClientHandle

其中 TimeServer 与 TimeClient 都只是启动类，真正的逻辑都在另一个类中处理。

在 NIO 中最为重要的是 SocketChannel 和 ServerSocketChannel，前者是客户端的，后者是服务端的。但无论是客户端的，还是服务端的，它们的使用流程都类似：

* 首先，通过 Selector.open() 获取 Selector 对象。
* 接着，调用 XXXChannel.open() 方法获取 Channel。
* 然后，调用设置 Channel 属性，这里是设置 configureBlocking 属性。
* 然后，服务器设置监听客户端连接或客户端连接服务器。
* 然后，将 Channel 注册到 Selector 对象中，并监听对应的状态位。
* 最后，调用 select.select(1000) 方法不断监听状态的变化。

简单地说，就是获取Selector对象，获取Channel对象，之后将Channel对象注册到Selector对象中，最后让Selector对象不断监听状态变化。

运行这个例子，直接运行 TimeServer 和 TimeClient 的 main 方法即可。

##  AIO（Asynchronized IO）

AIO相对于NIO，实现了真正的异步。因为NIO虽然可以不需调用线程等待，但是还是需要一个Selector线程一直在工作，并且在数据准备就绪之后，还是需要线程将数据从系统区间读取到用户区间，这段时间线程是阻塞的。

AIO这部分代码相对来说比较难懂。TODO



