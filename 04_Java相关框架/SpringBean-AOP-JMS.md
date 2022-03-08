# SpringAOP/Bean/JMS

## Spring框架的设计

### 什么框架

- 不直接解决业务问题。有了框架，我们可以更专注的解决业务问题。

### Spring框架的六个模块

- Spring Core： Bean/Context/AOP
- Testing: Mock/TestContext
- DataAccess: Tx/JDBC/ORM
- Spring MVC/Web Flux: web
- Integration: remoting/JMS/WS
- Language:(jvm多语言编程) ： Kotlin/Groovy

### 引入Spring意味着引入了一种开发协作的模式

## Spring AOP

### 实现原理

- 接口类型：JDK的动态代理， JDKProxy
- 非接口类型： CGlib (字节码增强)

### 几种增强

- 前置增强
- 后置增强
- 环绕增强
- 异常增强

### AOP的配置

- XML配置方式
- 注解配置方式

### 字节码增强

- CGlib字节码增强基于 ASM
- ByteBuddy，新的字节码增强工具，提供了更友好的API

## Spring Bean 生命周期

### 从Bean工厂到应用上下文

### Bean的加载过程
（因为Spring要做一个通用的框架，所以非常复杂）

- 1. 启动Spring容器 ApplicationContent
- 2. 构造函数
- 3. 依赖注入（是否依赖其它到bean），属性赋值
- 4. 判断是否实现了BeanNameAware
- 5. 判断是否实现了BeanFactoryAware
- 6. 判断是否实现了ApplicationContextAware
- 7. BeanPostProcessor
前置方法
- 8. InitializingBean是否实现这个接口
- 9. 自定义init方法
- 10. BeanPostProcessor后置方法
- 11 使用
- 12 DisposiableBean
- 13 自定义destroy方法

## Spring XML配置原理

### Spring的XML文件

- 命名空间<beans ..... >

	- 里面有小的命名空间 xmlns=
	- 每个小的命名空间对应一个url和一个xml文件

- xml规范

	- 一种xsd
	- 一种dtd

### 自己定义的命名空间

- 第一步：META-INF/spring.schemas

	- 里面写着url对应的xsd文件所在的位置

- 第二步：xsd文件

	- 里面配置这对自定义标签的使用规则
	- 通过这个文件，可以校验我们写的xml配置是否正确；也可以让IDEA给我们提示配置哪些属性

- 第三步：META-INF/spring.handlers

	- 配置的那段XML文件，丢给哪个类来处理
	- 将配置的XML变成Bean定义，传给Spring
	- 有了bean定义，就能将bean定义翻译成对象

### 自动的XML配置工具：XmlBeans-->spring-xbean

- 只需要写 xbean和spring.schemas

## Spring Messaging

### Message与JMS

- 点对点
- 消息订阅

*XMind - Trial Version*