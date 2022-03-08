# 实践常见的RPC框架Hessian

[toc]

## 一、Hessian

[Hessian](http://hessian.caucho.com/doc/) 是一个binary 协议的rpc框架，轻量级，使用起来比较简单。

hessian的maven依赖：

```xml
<dependency>
    <groupId>com.caucho</groupId>
    <artifactId>hessian</artifactId>
    <version>4.0.63</version>
</dependency>
```

## 二、Hessian实践项目

[action-rpcfx-demo](https://github.com/hefrankeleyn/JAVARebuild/tree/main/projects/action-rpcfx-demo)项目关于Hessian实践涉及其中三个模块：[`demo-api`](https://github.com/hefrankeleyn/JAVARebuild/tree/main/projects/action-rpcfx-demo/demo-api)、[`demo-hessian-client`](https://github.com/hefrankeleyn/JAVARebuild/tree/main/projects/action-rpcfx-demo/demo-hessian-client)、[`demo-hessian-server`](https://github.com/hefrankeleyn/JAVARebuild/tree/main/projects/action-rpcfx-demo/demo-hessian-server)。

### 2.1 `demo-api` 定义Bean和服务接口

**注意：实体类Bean应当实现Serializable接口。**

- `User`、`UserService`
- `Order`、`OrderService`

### 2.2 `demo-hessian-client` 模拟客户端

- [`BeanConfig`](https://github.com/hefrankeleyn/JAVARebuild/blob/main/projects/action-rpcfx-demo/demo-hessian-client/src/main/java/com/hef/demo/hessian/client/BeanConfig.java)： 创建服务代理对象，并放入Spring容器中

创建服务的代理对象：

```java
    private final String USER_SERVICE_URL = "http://localhost:8088/userService";
    @Bean
    public UserService userService() throws MalformedURLException {
        HessianProxyFactory hessianProxyFactory = new HessianProxyFactory();
        return (UserService) hessianProxyFactory.create(UserService.class, USER_SERVICE_URL);
    }
```

- [`DemoHessianClientApplication`](https://github.com/hefrankeleyn/JAVARebuild/blob/main/projects/action-rpcfx-demo/demo-hessian-client/src/main/java/com/hef/demo/hessian/client/DemoHessianClientApplication.java) 像访问本地服务一样访问远程服务。

```java
    @Resource
    private UserService userService;
    
    @RequestMapping(value = "/userTest", method = RequestMethod.GET)
    public User userTest() {
        System.out.println(userService);
        User user = userService.findUser(23);
        System.out.println(user);
        return user;
    }
```

### 2.3 `demo-hessian-server` 服务端，实现了服务接口

- [BeanConfig](https://github.com/hefrankeleyn/JAVARebuild/blob/main/projects/action-rpcfx-demo/demo-hessian-server/src/main/java/com/hef/demo/hessian/server/BeanConfig.java) ：创建本地服务的服务发现

```java
    @Bean
    public UserService userService() {
        return new UserServiceImpl();
    }

    @Bean("/userService")
    public HessianServiceExporter iniHessianService(UserService userService) {
        HessianServiceExporter exporter = new HessianServiceExporter();
        exporter.setServiceInterface(UserService.class);
        exporter.setService(userService);
        return exporter;
    }
```

- [UserServiceImpl](https://github.com/hefrankeleyn/JAVARebuild/blob/main/projects/action-rpcfx-demo/demo-hessian-server/src/main/java/com/hef/demo/hessian/server/UserServiceImpl.java) 本地真实的服务实现

```java
public class UserServiceImpl implements UserService {

    @Override
    public User findUser(int id) {
        return new User(id, "hessian "+id);
    }
}
```

## 三、测试访问

浏览器上输入：`http://localhost:8188/userTest`

页面上会显示如下内容：

> {"id":23,"name":"hessian 23"}



