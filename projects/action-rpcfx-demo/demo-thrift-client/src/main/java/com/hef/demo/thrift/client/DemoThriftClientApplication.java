package com.hef.demo.thrift.client;

import com.hef.demo.thrift.api.*;
import org.apache.thrift.TConfiguration;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

/**
 * @Date 2022/2/17
 * @Author lifei
 */
public class DemoThriftClientApplication {

    public static void main(String[] args) {
        TTransport transport = null;
        try {
            transport = new TSocket(new TConfiguration(), ServerConfig.SERVER_HOST,
                    ServerConfig.SERVER_PORT, ServerConfig.TIMEOUT);
            transport.open();

            TBinaryProtocol protocol = new TBinaryProtocol(transport);
            // 当注册了一个服务当时候，获取方法
            /*UserService.Client userService = new UserService.Client(protocol);
            User user = userService.findUser(2);
            System.out.println(user);*/

            // 当注册多个服务当时候，获取方法
            UserService.Client userService = new UserService.Client(new TMultiplexedProtocol(protocol, "userService"));
            User user = userService.findUser(2);
            System.out.println(user);

            OrderService.Client orderService = new OrderService.Client(new TMultiplexedProtocol(protocol, "orderService"));
            Order order = orderService.findOrder(3);
            System.out.println(order);


        } catch (TException e) {
            throw new RuntimeException(e);
        } finally {
            if (transport!=null) {
                transport.close();
            }
        }
    }
}
