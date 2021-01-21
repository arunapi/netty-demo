# netty-demo

Channels
https://docs.oracle.com/javase/8/docs/api/java/nio/channels/package-summary.html
which represent connections to entities that are capable of performing I/O operations, such as files and sockets
- represents an open connection to an entity such as a hardware device, a file, a network socket, or a program component that is capable of performing one or more distinct I/O operations, for example reading or writing. As specified in the Channel interface, channels are either open or closed, and they are both asynchronously closeable and interruptible.
-  An appropriate channel can be constructed from an InputStream or an OutputStream, and conversely an InputStream or an OutputStream can be constructed from a channel. A Reader can be constructed that uses a given charset to decode bytes from a given readable byte channel, and conversely a Writer can be constructed that uses a given charset to encode characters into bytes and write them to a given writable byte channel.
-
selectors -  for multiplexed, non-blocking I/O operations
Callbacks
- Common way to notify an interested party that an operation has completed.
- In netty, when a callback is triggered, the event can be handled by an implementation of interface ChannelHandler
- channelActive(ChannelHandlerContext) is called when a new connection is has been established
  Futures
- Same concept as Callback, a way to notify when an operation is completed.
- This object act as the place holder for the result of an asynchronous operation, which will be completed at some point in future.
- JDK implementation of future only provide a way to check manually whether the operation has completed or to block until it does
- Netty provides ChannelFuture, that allows you to register ChannelFutureListener. The listeners callback method operationComplete() is called when the operation has completed. In the method you can check whether the operation is success or has error
  Events and Handlers
- Events notify about change of state or status of operations
- Events are categorized by their relevance to inbound or outbound data flow
- Every event can be dispatched to a user-implemented method of a handler class (event-driven paradigm)
- ChannelHandler provides basic abstraction for handlers.
- ChannelHandlers use event and futures themseleves

Netty's key design approach
- Netty's asynchronous programming model is built on the concepts of Futures and Callbacks, with the dispatching of events to handler methods happening at a deeper level
- Selectors, Events and EventLoops - Netty abstract selector away by firing events and eliminating all the hand written dispacth code.
- Under the cover an Event Loop is assigned to each channel and does not change. This eliminates the concern of synchronization in ChannelHandlers

Netty Server
- All netty servers require
    - At least one ChannelHandler
        - implement processing of data received from the client
        - recieve and react to event notifications
        - helps to keep business logic from networking code
        - server responds to incoming messages - implement ChannelInboundHandler - ChannelInboundHandlerAdappter provides default implementation
          - channelRead() - Called for each incoming message
          - channelReadComplete() - Notifies the handler that the last call made to channelRead() was the last message in the batch
          - exceptionCaught() - Called if an exception is thrown during the read operation
        - hook into the event lifecycle
    - Bootstrapping - Startup Code that configures the server
       - Bind the port - listen for and accept incoming connection requests
       - Configure Channels
    - Every Channel has an associated ChannelPipeline - holds a chain of ChannelHandler instances
    - Transport
        - In Networking protocol transport layer is the one that provides services for end to end or host to host communications.
        - Internet communications are based on TCP transport
        - NIO transport refers to a transport (identical to TCP transport) is for server-side performance enhancements provided by the java 
    