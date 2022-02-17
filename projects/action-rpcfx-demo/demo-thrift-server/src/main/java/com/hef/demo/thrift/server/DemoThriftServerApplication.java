package com.hef.demo.thrift.server;

import com.hef.demo.thrift.api.OrderService;
import com.hef.demo.thrift.api.ServerConfig;
import com.hef.demo.thrift.api.UserService;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.transport.TTransportFactory;

/**
 * @Date 2022/2/17
 * @Author lifei
 */
public class DemoThriftServerApplication {

    public static void main(String[] args) {
        try {
            TServerSocket tServerSocket = new TServerSocket(ServerConfig.SERVER_PORT);

            // 只注册一个服务
//            UserService.Processor<UserService.Iface> processor = new UserService.Processor<>(new UserServiceImpl());
            // 注册多个服务
            TMultiplexedProcessor processor = new TMultiplexedProcessor();
            processor.registerProcessor("userService", new UserService.Processor<>(new UserServiceImpl()));
            processor.registerProcessor("orderService", new OrderService.Processor<>(new OrderServiceImpl()));

            TThreadPoolServer.Args tArgs = new TThreadPoolServer.Args(tServerSocket);
            tArgs.processor(processor);
            tArgs.protocolFactory(new TBinaryProtocol.Factory());
            tArgs.transportFactory(new TTransportFactory());

            TSimpleServer server = new TSimpleServer(tArgs);
            System.out.println("Running Simple server");
            server.serve();

        } catch (TTransportException e) {
            throw new RuntimeException(e);
        }
    }
}
