# HttpServer 性能对比

[toc]

## 一、单线程的HttpServer

```java
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8801);
            while (true) {
                Socket socket = serverSocket.accept();
                service(socket);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
```

对请求的处理：

```
    private static void service(Socket socket){
        try (OutputStream outputStream = socket.getOutputStream();
              PrintWriter printWriter = new PrintWriter(outputStream, true)){
            String contentStr="hello,nio\r\n";
            Thread.sleep(20);
            printWriter.println("HTTP/1.1 200 OK");
            printWriter.println("Accept-Ranges: bytes");
            printWriter.println("Cache-Control: private, no-cache, no-store, proxy-revalidate, no-transform");
            printWriter.println("Connection: keep-alive");
            printWriter.println("Content-length:"+contentStr.length());
            printWriter.println("Content-Type: text/html;charset=utf-8");
            printWriter.println();
            printWriter.write(contentStr);
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }
```

### httpServer01 压测

```
$ wrk -c 20 -d 60 --latency http://localhost:8801/
Running 1m test @ http://localhost:8801/
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   462.11ms   32.59ms 520.86ms   97.76%
    Req/Sec    12.31      9.91    50.00     77.76%
  Latency Distribution
     50%  465.60ms
     75%  473.85ms
     90%  481.17ms
     99%  492.95ms
  1026 requests in 1.00m, 278.67KB read
  Socket errors: connect 0, read 2563, write 23, timeout 0
Requests/sec:     17.07
Transfer/sec:      4.64KB
```



## 二、多线程的HttpServer

```
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8801);
            while (true) {
                Socket socket = serverSocket.accept();
                service(socket);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
```

### httpServer02 压测

```
$ wrk -c 20 -d 60 --latency http://localhost:8802/
Running 1m test @ http://localhost:8802/
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    23.67ms    6.37ms  96.50ms   98.29%
    Req/Sec    21.09     19.21   111.00     86.90%
  Latency Distribution
     50%   22.74ms
     75%   24.83ms
     90%   26.07ms
     99%   33.42ms
  1929 requests in 1.00m, 1.60MB read
  Socket errors: connect 0, read 48541, write 28, timeout 0
Requests/sec:     32.10
Transfer/sec:     27.28KB
```

## 三、线程池的HttpServer

```
public static void main(String[] args) {
    ExecutorService executorService = Executors.newFixedThreadPool(38);
    try {
        ServerSocket serverSocket = new ServerSocket(8803);
        while (true){
            Socket socket = serverSocket.accept();
            executorService.execute(()->{service(socket);});
        }
    } catch (IOException e) {
        e.printStackTrace();
        throw new RuntimeException(e);
    }
}
```

### httpServer03  压测

```
$ wrk -c 20 -d 60 --latency http://localhost:8803/
Running 1m test @ http://localhost:8803/
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    23.43ms    9.44ms 109.56ms   98.18%
    Req/Sec    24.85     28.29   150.00     87.36%
  Latency Distribution
     50%   21.86ms
     75%   23.76ms
     90%   25.08ms
     99%  100.40ms
  1538 requests in 1.00m, 1.37MB read
  Socket errors: connect 0, read 48970, write 35, timeout 0
Requests/sec:     25.61
Transfer/sec:     23.41KB
```

## 四、使用netty使用

代码在github上: [NettyServerApplication](https://github.com/hefrankeleyn/JAVA-000/projects/MyJavaPractice/com/hef/nio/netty/NettyServerApplication)

### 压测的性能：

```
$ wrk -c 20 -d 60 http://localhost:8804/test
Running 1m test @ http://localhost:8804/test
  2 threads and 20 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   225.04us    0.95ms  64.88ms   99.43%
    Req/Sec    47.12k     7.24k   54.66k    88.02%
  5635223 requests in 1.00m, 575.04MB read
Requests/sec:  93764.49
Transfer/sec:      9.57MB
```

