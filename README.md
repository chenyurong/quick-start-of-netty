# Netty快速入门指南

本项目是学习《Netty权威指南》的实践记录，只有实践才能更好地掌握知识。

本项目目录：

* [BIO 阻塞IO](#bioblock-io)
* [NIO 非阻塞IO](#niono-block-io)
* [AIO 异步IO](#aioasynchronized-io)

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

## Netty实现

具体代码见：[Netty默认实现时间服务器](src/main/java/com/chenshuyi/netty/basic)

使用netty实现时间服务器非常简单，分为下面几个步骤：

* 设置Bootstrap对象
* 发起连接或绑定操作
* 等待链路关闭

其中 TimeClientHandler 和 ServerHandler 都继承了 ChannelHandlerAdapter 类，非常简单。但这种方式没有处理拆包和粘包问题，在高并发访问会出现问题。

## Netty实现 —— 出现粘包拆包问题

具体代码见：[Netty解决粘包拆包问题](src/main/java/com/chenshuyi/netty/frame/fault)

上面的Netty默认实现只是一个请求，现在我们在客户端循环100个请求，会发现出现了粘包和拆包的问题。

## Netty实现 —— 解决粘包拆包问题

具体代码见：[Netty解决粘包拆包问题](src/main/java/com/chenshuyi/netty/frame/correct)

这个示例与上一个的唯一区别是加了两个处理器（handler），即加了LineBasedFrameDecoder、StringDecoder。第一个Decoder会根据换行符和回车符分割数据，而第二个Decoder会将对象转成字符串。

通过在处理链上加了这样两个Decoder，就解决了粘包和拆包问题。

## Netty解码器 —— 分隔符解码器和定长解码器

在Netty中有四种类型的解码器，分别是：

* 定长解码器。接收到指定长度消息后，直接截断。
* 回车换行作为结束标志。
* 特殊的分隔符作为结束标志。
* 在消息头定义长度字段来表示消息总长度。

这里说的是使用分隔符以及定长来进行解码，分别对应：DelimiterBasedFrameDecoder和FixedLengthFrameDecoder。

这两种方式和上面使用LineBasedFrameDecoder的方式一模一样，只不过是换了handler上的Decoder而已。

* [DelimiterBasedFrameDecoder示例](src/main/java/com/chenshuyi/netty/frame/correct)

运行EchoServer和EchoClient即可。

* [FixedLengthFrameDecoder示例](src/main/java/com/chenshuyi/netty/frame/correct)

运行EchoServer之后，启动命令行工具，telnet到对应的IP以及端口，之后输入字符串，EchoServer会截取前20个字符输出。

