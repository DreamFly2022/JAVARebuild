# ProtocolBuffer 语法及使用

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

### 2.16 更改消息类型

  如果一个message类型，有一个字段不再需要，但是你又不想影响旧的代码，那么记住下面的规则：

  - 不要改变任何已经存在字段的编号；
  - 如果你添加新的字段，旧的序列化消息仍然能被新产生的代码解析，类似的，从新代码创建的消息也能被旧代码解析（旧代码解析会忽略新的字段）；
  - 字段可以被删除，只要这个字段编号不再被使用。更好的选择是添加“OBSOLETE_”前缀，或者添加`reserved`修饰，可以防止编号其它再次修改时使用；
  - int32、uint32、int64、uint64、boo，它们时互相兼容的，当从它们中其中一个类型变成另一个类型的时候，不用考虑向前或向后兼容问题；
  - sint32和sint64是互相兼容的，但他们和其他的数值类型不兼容；
  - stirng和bytes是互相兼容的，只要bytes是有效的UTF-8编码格式；
  - 如果bytes包含编码版本，那么它和嵌入消息也是兼容的；
  - fixed32和sfixed32互相兼容，fixed64和sfixed64互相兼容；
  - 对于string、bytes、message字段，optional和repeated是互相兼容的；
  - enum和int32、uint32、int64、uint64是兼容的，但enum类型的消息反序列化依赖语言特性；
  - 为一个字段添加oneof是安全的，但移除oneof 可能是不安全的；

### 2.17 未知字段

  未知字段是指格式良好的protocol buffer序列化数据，但解析器不认识。例如，当使用旧的二进制解析器通过新的字段对新的二进制文件进行解析时，这些新字段就是未知字段。

  最开始proto3会丢弃掉这些未知字段，从v3.5之后，还会保留，并作为序列化输出。

### 2.18 Any

  Any代表任意序列化消息，使用时需要：`import google/protobuf/any.proto`

  ```protobuf
  import "google/protobuf/any.proto"
  message ErrorStatus {
    string message = 1;
    repeated google.protobuf.Any details = 2;
  }
  ```

### 2.19 oneof

 当我们某一个字段可能出现多个不同类型，那么就可以使用oneof。例如，返回值SearchResponse的一个返回字段既可以时String类型，也可以时SubMessage类型：

  ```protobuf
  message SearchResponse {
     oneof test_oneof {
        string name = 3;
        SubMessage sub_message = 9;
     }
  }
  ```

### 2.20 Maps

  如果想要创建map作为数据定义的一部分，protocol buffers提供了一个快捷方式，语法为：

  `map<key_type, value_type> map_field = N;`

  这里的key_type可以是任意的标准量类型除了浮点类型和bytes类型，注意枚举类型不能作为key_type。vlaue_type可以是除了map类型之外的任意类型。

  map字段不能被repeated修饰。

  map的向后兼容（下面的语法等价于map）：

  ```protobuf
  message MapFieldEntry {
     key_type key = 1;
     value_type value = 2;
  }
  repeated MapFieldEntry map_field = N;
  ```

### 2.21 包

 添加包，可以避免包名称的冲突：

```protobuf
package foo.bar;
message Open {...}

message Foo {
   foo.bar.Open.open = 1;
}
```

### 2.22 定义服务

如果你想使用你的消息类型在RPC 系统中，你可以在`.proto`文件中定义rpc服务接口，protocol buffers编译器将根据你选择的语言产生服务接口和存根。例如，定义一个RPC服务，里面有一个方法接受参数为SearchRequest，返回值是SearchResponse：

```protobuf
service SearchService {
   rpc Search(SearchRequest) return (SearchResponse);
}
```

### 2.23 和JSON的对应关系

Proto3支持用JSON的方式进行编码，对应关系参考：[JSON Mapping](https://developers.google.com/protocol-buffers/docs/proto3#json)。

### 2.24 操作

操作分为文件级别（定义在文件顶部）、消息级别（定义在消息内部）、字段级别（定义在字段上面）。

常见的操作有：

- `java_package` （文件级别）：用于定于Java/Kotlin的类路径，如果没有定义java_package，默认使用`package`的定义

  ```protobuf
  option java_package = "com.example.foo";
  ```

- `java_out_classname`（文件级别）：包装Java类的类名，如果没有通过`java_outer_classname`进行明确，那个类名将使用` .proto`的文件名

  ```protobuf
  option java_out_classname = "Ponycopter";
  ```

- `java_multiple_files`（文件级别）：如果是false，那么单个Java文件将产生。如果为true，将会产生多个java文件，默认是false。

  如果不生成java代码，这个操作是无效的。

  ```protobuf
  option javaz_mutiple_files = true;
  ```

- `optimize_for` （文件级别）：可以设置的值为`SPEED`、`CODE_SIZE`、`LITE_RUNTIME` ,对产生C++和Java代码会有影响：

  - `SPEED`（默认）：protocol buffer编译器将生成序列化、解析、和其他常见操作的代码，代码都是高效优化的；

  - `CODE_SIZE`：将产生较小的类文件，根据依赖共享和反射来实现序列化、解析等操作。产生的代码比SPEED模式下的小，也实现了SPEED模式下的所有方法，但是执行比较慢。适合`.proto`文件特别多，对性能要求不高的应用。

  - `LITE_RUNTIME`：产生的代码依赖库用`libprotobuf-lite`代替`libprotobuf`，这个库运行时比完整的库要小，但是它忽略了一些确定特性，例如描述符和反射。这个是特别有用的对于需要运行在比较局限的设备上，比如手机。编译器将仍然产生比较快的代码并且实现了`SPEED`模式下的所有方法。产生的代码仅仅实现了`MessageLite`接口下的方法，`MessageLite`接口是`Message` 接口的子集。

    ```protobuf
    option optimize_for = CODE_SIZE;
    ```

- `cc_enable_arenas`：对产生C++代码有用；

- `objc_class_prefix`：对产生Objective-C代码有用；

- `deprecated`（字段级别）：如果为true，表示在产生新的代码时该文件会被忽略。在大多数语言中这个事没有用的。在Java中，这将变成`@Deprecated`注解。

  ```protobuf
  int32 old_field = 6 [deprecated=true]
  ```

### 2.25 自定义操作

protocol buffer 也允许自定义操作，大多数人都用不到，详情看[Custom Options](https://developers.google.com/protocol-buffers/docs/proto3#customoptions)。

## 三、产生类文件

为了根据`.proto`文件生成Java、Kotlin、Python、C++、Go、Ruby、Objective-C，或C#代码，你需要在protocol buffer编译器上运行`protoc`命令，如果没有安装编译器，需要[下载安装](https://developers.google.com/protocol-buffers/docs/downloads)。

下载安装包后，注意阅读里面的readme文件，进行安装。

对于Go语言，参考：[golang/protobuf](https://github.com/golang/protobuf/)

执行命令如下：

```protobuf
protoc --proto_path=IMPORT_PATH --cpp_out=DST_DIR --java_out=DST_DIR --python_out=DST_DIR --go_out=DST_DIR --ruby_out=DST_DIR --objc_out=DST_DIR --csharp_out=DST_DIR path/to/file.proto
```

- `IMPORT_PATH`：指定要查找`.proto`文件的目录，如果省略，将使用当前目录。可以指定多个`.proto`文件目录，通过设置多次`--proto_path`参数，它们将按照顺序被查询。`-I=_IMPORT_PATH_`是`--proto_path`的缩写。
- 可以提供一个或多个输出指令：
  - `--cpp_out`：产生C++代码，并放到DST_DIR下；
  - `--java_out`：产生Java代码；
  - `--kotlin_out`：产生Kotlin代码
  - `--python_out`：产生python代码
  - `--go_out`：产生go代码
  - `--ruby_out` ：产生ruby代码
  - `--objc_out`：铲射扔Objective-C代码
  - `--csharp_out` ：产生C#代码
  - `--php_out`：产生PHP代码
- 有一个极其方便的操作，如果DST_DIR的结尾是`.zip`或`.jar`，那么编译器将写一个ZIP格式或`.jar`的文件作为输出。如果压缩文件已将存在，将会覆盖，编译器还没有足够聪明到能把内容追加到已经存在的压缩文件中。
- 必须提供一个或多个`.proto`文件作为输入，可以一次指定多个`.proto` 文件。尽管文件是相对于当前目录命名的，每个文件必须能在IMPORT_PATH中找到，以便编译器能确定绝对名称。







  

  
