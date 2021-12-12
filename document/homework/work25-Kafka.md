# 第13周25课作业实践：分布式消息Kafka

[toc]

## 一、（必做）搭建3节点kafka

> 搭建一个3节点Kafka集群，测试功能和性能；实现spring kafka下对kafka集群 的操作，将代码提交到github。

[搭建文档](https://github.com/hefrankeleyn/JAVARebuild/blob/main/Week_13_%E5%88%86%E5%B8%83%E5%BC%8F%E6%B6%88%E6%81%AF/2021-12-08-%E5%88%86%E5%B8%83%E5%BC%8F%E6%B6%88%E6%81%AF-Kafka%E6%B6%88%E6%81%AF%E4%B8%AD%E9%97%B4%E4%BB%B6.md)

### 1.1 简单的kafka生产者和消费者

[简单生产者]()：

```java
    public static void main(String[] args) {
        MySimpleKafkaProducer producerMain = new MySimpleKafkaProducer();
        producerMain.producerSend();
    }

    public void producerSend() {
        Properties props = new Properties();
        props.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.setProperty("bootstrap.servers", "localhost:9001,localhost:9002,localhost:9003");
        String topic = "test32";

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        // 发送消息
        for (int i = 0; i < 10; i++) {
            Order order = new Order();
            order.setAmount(new BigDecimal(1000+i));
            order.setId(i);
            order.setType(System.currentTimeMillis());
            String value = JSON.toJSONString(order);
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, value);
            producer.send(record);
        }
        producer.close();
    }
```

消费者：

```java
    public static void main(String[] args) {
        MySimpleKafkaConsumer consumer = new MySimpleKafkaConsumer();
        consumer.consumerPoll();
    }


    public void consumerPoll() {
        Properties props = new Properties();
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("bootstrap.servers", "localhost:9001,localhost:9002,localhost:9003");
        // kafka group id 的配置
        props.setProperty("group.id", "group1");

        String topic = "test32";
        //  构建一个Consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(topic));
        while (true) {
            ConsumerRecords<String, String> poll = consumer.poll(Duration.ofMillis(1000));
            poll.forEach(item->{
                ConsumerRecord<String, String> record = (ConsumerRecord) item;
                Order order = JSON.parseObject(record.value(), Order.class);
                System.out.println(order);
            });
            try {
                Thread.sleep(1000 * 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
```



## 二、（选做）安装kafka-manager

> 安装kafka-manager工具，监控kafka集群状态。



## 三、（挑战）演练本课提及的各种生产者和消费者特性

> 演练本课提及的各种生产者和消费者特性。



## 四、（挑战）Kafka金融领域实战

> Kafka金融领域实战：在证券或者外汇、数字货币类金融核心交易系统里， 对于订单的处理，大概可以分为收单、定序、撮合、清算等步骤。其中我们一般可以用mq来 实现订单定序，然后将订单发送给撮合模块。
>
> 1）收单：请实现一个订单的rest接口，能够接收一个订单Order对象；
>
> 2）定序：将Order对象写入到kafka集群的order.usd2cny队列，要求数据有序并且不丢失； 
>
> 3）撮合：模拟撮合程序（不需要实现撮合逻辑），从kafka获取order数据，并打印订单信息， 要求可重放, 顺序消费, 消息仅处理一次。

