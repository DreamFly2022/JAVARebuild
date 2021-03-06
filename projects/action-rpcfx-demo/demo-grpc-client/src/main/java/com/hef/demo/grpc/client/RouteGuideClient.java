package com.hef.demo.grpc.client;

import com.hef.demo.grpc.api.*;
import com.hef.demo.grpc.api.RouteGuideGrpc.RouteGuideBlockingStub;
import com.hef.demo.grpc.api.RouteGuideGrpc.RouteGuideStub;
import io.grpc.*;
import io.grpc.stub.StreamObserver;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 客户端
 * @Date 2022/2/27
 * @Author lifei
 */
public class RouteGuideClient {

    private static final Logger logger = Logger.getLogger(RouteGuideClient.class.getName());


    private final RouteGuideBlockingStub blockingStub;
    private final RouteGuideStub asyncStub;
    private Random random = new Random();

    /**
     * 通过指定服务的端口和地址来创建客户端
     * @param host 地址
     * @param port 端口
     */
    public RouteGuideClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext());
    }

    /**
     * 通过管道（channel）构造器客户端
     * @param channelBuilder
     */
    public RouteGuideClient(ManagedChannelBuilder<?> channelBuilder) {
        this(channelBuilder.build());
    }

    /**
     * 使用存在的管道（channel）构建客户端，为了能访问RouteGuide 服务
     * @param channel
     */
    public RouteGuideClient(Channel channel) {
        this.blockingStub = RouteGuideGrpc.newBlockingStub(channel);
        this.asyncStub = RouteGuideGrpc.newStub(channel);
    }

//    private final ManagedChannel channel;
//    public RouteGuideClient(ManagedChannel channel) {
//        this.channel = channel;
//        this.blockingStub = RouteGuideGrpc.newBlockingStub(channel);
//        this.asyncStub = RouteGuideGrpc.newStub(channel);
//    }

    private void info(String msg, Object... params) {
        logger.log(Level.INFO, msg, params);
    }

    private void warning(String msg, Object... params) {
        logger.log(Level.WARNING, msg, params);
    }

    /**
     * 调用简单RPC的方法
     * @param lat 纬度
     * @param lon 经度
     */
    public void getFeature(int lat, int lon) {
        logger.info(String.format("*** GetFeature: lat=%d lon=%d", lat, lon));
        Point point = Point.newBuilder().setLatitude(lat).setLongitude(lon).build();
        Feature feature;
        try {
            feature = blockingStub.getFeature(point);
        }catch (StatusRuntimeException e) {
            warning("RPC 调用出错： {0}", e.getStatus());
            return;
        }

        if (RouteGuideUtil.exists(feature)) {
            info("发现了 feature 通过调用 \"{0}\" 参数为 {1}, {2}",
                    feature.getName(),
                    RouteGuideUtil.getLatitude(feature.getLocation()),
                    RouteGuideUtil.getLongitude(feature.getLocation()));
        }else {
            info(String.format("在 {0}, {1}上没有发现 feature",
                    RouteGuideUtil.getLatitude(feature.getLocation()),
                    RouteGuideUtil.getLongitude(feature.getLocation())));
        }
    }

    /**
     * 服务端流形式的RPC方法调用
     * @param lowLat
     * @param lowLon
     * @param hiLat
     * @param hiLon
     */
    public void listFeatures(int lowLat, int lowLon, int hiLat, int hiLon) {
        info("*** ListFeatures: {0}, {1}, {2}, {3}", lowLat, lowLon, hiLat, hiLon);

        Rectangle rectangle = Rectangle.newBuilder()
                .setLo(Point.newBuilder().setLatitude(lowLat).setLongitude(lowLon).build())
                .setHi(Point.newBuilder().setLatitude(hiLat).setLongitude(hiLon).build())
                .build();
        Iterator<Feature> features;
        try {
            features = blockingStub.listFeatures(rectangle);
            for (int i=1; features.hasNext(); i++) {
                Feature feature = features.next();
                info("Result #{0}: {1}", i, feature);
            }
        }catch (StatusRuntimeException e) {
            warning("RPC调用出错：{0} ", e.getStatus());
        }
    }

    /**
     * 客户端流形式的RPC方法调用
     * @param features
     * @param numPoints
     */
    public void recordRoute(List<Feature> features, int numPoints) throws InterruptedException {
        info("*** RecordRoute");
        final CountDownLatch finishLatch = new CountDownLatch(1);
        StreamObserver<RouteSummary> responseObserver = new StreamObserver<RouteSummary>() {
            @Override
            public void onNext(RouteSummary routeSummary) {
                info("完成了本次旅行，通过了{0}个points, 通过了{1}个features, 行走了{2}米。花费了{3}秒。",
                        routeSummary.getPointCount(),
                        routeSummary.getFeatureCount(),
                        routeSummary.getDistance(),
                        routeSummary.getElapsedTime());
            }

            @Override
            public void onError(Throwable throwable) {
                warning("RecordRoute 出错：{0}", Status.fromThrowable(throwable));
                finishLatch.countDown();
            }

            @Override
            public void onCompleted() {
                info("Finished RecordRoute");
                finishLatch.countDown();
            }
        };

        StreamObserver<Point> requestObserver = asyncStub.recordRoute(responseObserver);
        try {
            for (int i = 0; i < numPoints; i++) {
                int index = random.nextInt(features.size());
                Point point = features.get(index).getLocation();
                info("正在参观：{0}, {1}", RouteGuideUtil.getLatitude(point),
                        RouteGuideUtil.getLongitude(point));
                requestObserver.onNext(point);
                // 在发送下一个之前睡一会儿
                Thread.sleep(random.nextInt(1000) + 500);
                if (finishLatch.getCount()==0) {
                    // 在我们完成发送之前，RPC调用完成或者发生错误
                    // 发送更多的请求不会出现错误，但是它们会被抛弃
                    return;
                }
            }
            // 标记请求完成
            requestObserver.onCompleted();

            // 接受异步的请求
            if (!finishLatch.await(1, TimeUnit.MINUTES)) {
                warning("recordRoute 在1分钟之内没有完成。");
            }
        }catch (RuntimeException e) {
            requestObserver.onError(e);
            throw e;
        }
    }

    /**
     * 双向流RPC方法的调用
     * @return
     */
    public CountDownLatch routeChat() {
        info("*** RouteChar");
        final CountDownLatch finishLatch = new CountDownLatch(1);
        StreamObserver<RouteNote> responseObserver = new StreamObserver<RouteNote>() {
            @Override
            public void onNext(RouteNote routeNote) {
                info("在{0} {1} 得到信息 \"{2}\"",
                        routeNote.getLocation().getLatitude(),
                        routeNote.getLocation().getLongitude(),
                        routeNote.getMessage()
                        );
            }

            @Override
            public void onError(Throwable throwable) {
                warning("routeChat发生错误：{0}", Status.fromThrowable(throwable));
                finishLatch.countDown();
            }

            @Override
            public void onCompleted() {
                info("完成了routeChat 调用");
                finishLatch.countDown();
            }
        };
        StreamObserver<RouteNote> requestObserver = asyncStub.routeChat(responseObserver);
        try {
            RouteNote[] requests = {
                    newNote("第一个消息", 0, 0),
                    newNote("第二个消息", 0, 10_000_000),
                    newNote("第三个消息", 10_000_000, 0),
                    newNote("第四个消息", 10_000_000, 10_000_000)
            };
            for (RouteNote request : requests) {
                info("在{0}, {1}上 发送消息 \"{2}\"",
                        request.getLocation().getLatitude(),
                        request.getLocation().getLongitude(),
                        request.getMessage());
                requestObserver.onNext(request);
            }
        }catch (StatusRuntimeException e) {
            // 取消RPC
            requestObserver.onError(e);
        }
        // 标记所有的请求完成
        requestObserver.onCompleted();
        // 在异步接受的过程中，返回finishLatch
        return finishLatch;
    }

    private RouteNote newNote(String message, int lat, int lon) {
        return RouteNote.newBuilder().setMessage(message)
                .setLocation(Point.newBuilder().setLatitude(lat).setLongitude(lon).build())
                .build();
    }

    /**
     * 客户端运行入口
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        String host = "localhost";
        int port = 8980;

        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext().build();
        try {
            RouteGuideClient client = new RouteGuideClient(channel);
            // 调用简单RPC方法
            client.getFeature(409146138, -746188906);
            // 调用服务端流形式的RPC方法
            client.listFeatures(400000000, -750000000, 420000000, -730000000);
            // 调用客户端流形式的RPC方法
            List<Feature> features = RouteGuideUtil.parseFeatures(RouteGuideUtil.getDefaultFeaturesFile());
            // 从features列表中记录一些随机选择的点
            client.recordRoute(features, 10);

            // 调用双向流RPC方法： 发送并返回一些notes
            CountDownLatch finishLatch = client.routeChat();
            if (!finishLatch.await(1, TimeUnit.MINUTES)) {
                client.warning("routeChat 在一分钟之内 没有完成");
            }
        }finally {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }

}
