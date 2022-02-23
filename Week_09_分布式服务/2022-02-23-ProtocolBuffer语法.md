# #ProtocolBuffer 语法

[toc]

## 一、ProtocolBuffer

> 在学习gRPC的过程中，发现里面用到了Protocol Buffer。于是开始了解Protocol Buffer。

Protocol Buffer既可以作为接口定义语言（IDL），又可以做为底层消息交换的格式。

参考：

- [官网proto3语法](https://developers.google.com/protocol-buffers/docs/proto3)
- [proto3语法的实践](https://developers.google.com/protocol-buffers/docs/tutorials)

## 二、Proto3的语法

### 2.1 定义消息类型

假设你要定义一个请求消息体，一个查询请求包含：一个字符串类型的query、数值类型的页码数page_number、数值类型的一页条数result_per_page。那么在一个`.proto`文件中可以定义如下：

```protobuf
syntax = "proto3"

message SearchRequest {
   string query = 1;
   int32 page_number = 2;
   int32 result_per_page = 3;
}
```

- 第一行指定要使用`proto3` 的语法，如果不写第一行，默认使用`proto2`语法解析器。该行必须放在非空、非注释行的第一行；
- `SearchRequest`中定义了三个字段；

### 2.2 声明字段的编号

- 每一个字段都有唯一的编号；

  这些编号将在消息的二进制格式中使用，一旦这些字段的编号确定之后就不应该改变。

- 编号从1到15占用一个字节，包含了字段类型和编号，编号从16到2047占用两个字节；

  因此应当预留1到15点编号对常用的消息类型。

- 最小的编号为1，最大的为2^29-1（536,870,911），19000到19999是Protocol Buffer自己使用的，我们不应该使用。

  类似的，你不应该使用任何自己预留的编号。

### 2.3 指定字段的规则*

> 翻译的官方文档，这里自己没读懂。

- `singular`：是proto3默认的字段规则，上面只能定义零个或多个字段；
- `repeated`： 这样的字段可以重复定义任何次数（包括零次），重复的顺序值也将会保存；

### 2.4 添加多个消息类型

一个`.proto`文件可以定义多个消息体：

```protobuf
syntax = "proto3"

message SearchRequest {
   string query = 1;
   int32 page_number = 2; // 你想看哪个页码
   int32 result_per_page = 3; // 一页展示多少条数据
}

message SearchResponse {
   int32 status = 1;
}
```

### 2.5 添加注释

可以向`.proto`文件中添加注释，使用像`C/C++`的注释语法：`//`或`/* ... */`。

### 2.6 定义预留字段

如果你更新信息体，通过删除字段或者将字段注释掉，那么将来其他用户在更新这个消息类型的使用可能重新使用这些字段编号。如果随后再加载老版本的`.proto`文件，就会造成一些问题。

可以使用`reserved`修饰哪些要删除的字段编号或者名称（那些在json序列化时可能出问题的名称），这样当重新使用这些被reserved修饰的编号或字段，编译器就会出问题：

```protobuf
message Foo {
   reserved 2, 15, 9 to 11;
   reserved "foo", "bar";
}
```

**注意：你不能在一个reserved生命中混合字段编号和名称。**

### 2.7 从`.proto`文件可以生成什么

当在`.proto`文件上运行protocol buffer 编译器的时候，编译器将根据你选择的语言产生对应的代码，这个代码包含了：字段的getter和setter方法、将你的消息序列化成输出流、将输入流序列化成消息。

对于Java语法，编译器将根据`.proto`文件生成表示每个消息体的`.java`文件，并且为每个消息类，创建了Builder类；

还支持的语言有：C++,Kotlin,Python,Go,Ruby,Objective-C,C#,Dart,PHP

### 2.8 标量值类型

[一个标量类型的字段和实际类型的对应关系](https://developers.google.com/protocol-buffers/docs/proto3#scalar)。

### 2.9 默认值

如果一个消息体中不包含`singular`元素，相应的字段在解析成对象时会设置默认值：

- 字符串类型，默认值为空字符串；
- 字节类型，默认值是空字节；
- 布尔类型，默认值是false；
- 数值类型，默认值是0；
- 枚举类型，默认值是第一个枚举值（第一个枚举编号必须为0）；

对于`repeated`类型的默认值是空。

### 2.10 枚举

有些时候，我们想定义一个字段的值是来自一个提前定义好的列表，这个时候我们可以使用枚举：

```protobuf
message SearchRequest {
   string query = 1;
   int32 page_number = 2; 
   int32 result_per_page = 3; 
   enum Corpus {
      UNIVERSAL = 0;
      WEB = 1;
      IMAGES = 2;
   }
   Corpus corpus = 4;
}
```

- 每个枚举定义都必须包含一个映射到零作为其第一个元素的常量。

  为了兼容proto2

- 能够定义别名将相同的值映射到不同的枚举常量上，但这个时候要设置`allow_alias=true`,  例如：

```protobuf
message MyMessage1 {
   enum EnumAllowingAlias {
    option allow_alias = true;
    UNKNOWN = 0;
    STARTED = 1;
    RUNNING = 1;
  }
}
```

- 定义枚举常量的个数应当是一个int32大小能放得下的；
- 既可以在一个`message`中定义枚举，也可以在`message`外定义枚举。还可以在一个`message`中使用另一个message中的枚举，格式为`_MessageType_._EnumType_`
- 在反序列化过程中，不认识的值还会留在消息中。

### 2.11  为枚举类型添加预留值

可以为枚举类型编号范围指定到  `max`:

```protobuf
enum Foo {
   reserved 2, 15, 9 to 11, 40 to max;
   reserved 'FOO', 'BAR';
}
```

**注意：在一个reserved声明中，不能混合编号和名称。**

### 2.12 使用其他的消息体

```protobuf
message SearchRequest {
  repeated Result result = 1;
}

message Result {
   string url = 1;
   string title = 2;
   repeated string snippets = 3;
}
```

### 2.13 导入定义

在上面2.12 中，看到了一个消息体使用同一个`.proto`文件中的其他消息体。如果要使用的消息体在其他文件中，可以通过导入定义的方式来进行：

```protobuf
import "myproject/other_protos.proto"
```

除Java语言之外的其他语言可以使用`public import`。

### 2.14 使用proto2

在proto3中可以使用proto2的定义，但是proto2不能使用proto3的定义。

### 2.15 内嵌消息

- 可以内嵌消息定义：

  ```protobuf
  message SearchResponse {
    message Result {
      string url = 1;
      string title = 2;
      repeated string snippets = 3;
    }
    repeated Result results = 1;
  }
  ```

- 可以使用其他的内嵌消息定义：

  ```protobuf
  message SomeOtherMessage {
    SearchResponse.Result result = 1;
  }
  ```

- 可以嵌套多层的消息定义：

  ```protobuf
  message Outer {                  // Level 0
    message MiddleAA {  // Level 1
      message Inner {   // Level 2
        int64 ival = 1;
        bool  booly = 2;
      }
    }
    message MiddleBB {  // Level 1
      message Inner {   // Level 2
        int32 ival = 1;
        bool  booly = 2;
      }
    }
  }
  ```

  

