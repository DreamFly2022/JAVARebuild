package com.hef.demo.grpc.server;

import com.hef.demo.grpc.api.*;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 一个简单的服务实现
 * @Date 2022/2/26
 * @Author lifei
 */
public class RouteGuideService extends RouteGuideGrpc.RouteGuideImplBase{

    private static final Logger logger = Logger.getLogger(RouteGuideService.class.getName());

    private final Collection<Feature> features;
    private final ConcurrentMap<Point, List<RouteNote>> routeNotes = new ConcurrentHashMap<>();


    public RouteGuideService(Collection<Feature> features) {
        this.features = features;
    }

    /**
     * 简单RPC
     * 从客户端获取Point，并从Feature数据库返回相应的Feature信息
     * @param request 请求参数
     * @param responseObserver 一个响应的观察者，它是一个特定的接口为了让服务端去调用传递它的响应
     */
    @Override
    public void getFeature(Point request, StreamObserver<Feature> responseObserver) {
        responseObserver.onNext(checkFeature(request));
        responseObserver.onCompleted();
    }

    private Feature checkFeature(Point location) {
        for (Feature feature : features) {
            if (feature.getLocation().getLatitude() == location.getLatitude()
                    && feature.getLocation().getLongitude()==location.getLongitude()) {
                return feature;
            }
        }
        return Feature.newBuilder().setName("").setLocation(location).build();
    }

    /**
     * 服务端流形式端RPC
     * 通过遍历features集合，判断是否在请求的范围内，获取多个Feature对象返回给客户端。并且使用观察者
     * @param request 请求对象
     * @param responseObserver 响应的观察者
     */
    @Override
    public void listFeatures(Rectangle request, StreamObserver<Feature> responseObserver) {
        int left = Math.min(request.getLo().getLongitude(), request.getHi().getLongitude());
        int right = Math.max(request.getLo().getLongitude(), request.getHi().getLongitude());
        int top = Math.max(request.getLo().getLatitude(), request.getHi().getLatitude());
        int bottom = Math.min(request.getLo().getLatitude(), request.getHi().getLatitude());

        for (Feature feature : features) {
            if (!RouteGuideUtil.exists(feature)) {
                continue;
            }
            int latitude = feature.getLocation().getLatitude();
            int longitude = feature.getLocation().getLongitude();
            if (longitude>=left && longitude<=right && latitude>=bottom && latitude<=top) {
                responseObserver.onNext(feature);
            }
        }
        responseObserver.onCompleted();
    }

    /**
     * 客户端流形式的RPC
     * @param responseObserver
     * @return
     */
    @Override
    public StreamObserver<Point> recordRoute(final StreamObserver<RouteSummary> responseObserver) {
        return new StreamObserver<Point>() {
            int pointCount;
            int featureCount;
            int distance;
            Point previous;
            final long startTime = System.nanoTime();
            @Override
            public void onNext(Point point) {
                pointCount++;
                if (RouteGuideUtil.exists(checkFeature(point))) {
                    featureCount++;
                }
                if (previous!=null) {
                    distance = calcDistance(previous, point);
                }
                previous = point;
            }

            @Override
            public void onError(Throwable t) {
                logger.log(Level.WARNING, "在recordRoute()方法中遇到一个错误", t);
            }

            @Override
            public void onCompleted() {
                long seconds = TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - startTime);
                responseObserver.onNext(RouteSummary.newBuilder().setPointCount(pointCount)
                        .setFeatureCount(featureCount).setDistance(distance)
                        .setElapsedTime((int)seconds).build());
                responseObserver.onCompleted();
            }
        };
    }

    /**
     * 根据"正半矢"公式来计算两点之间的距离
     * 半正矢公式是一种根据两点的经度和纬度来确定大圆上两点之间距离的计算方法，在导航有着重要地位。
     * @param start
     * @param end
     * @return
     */
    private static int calcDistance(Point start, Point end) {
        // 地球的半径，单位是米
        int r = 6371000;
        // 将角度转化为弧度
        double lat1 = Math.toRadians(RouteGuideUtil.getLatitude(start));
        double lat2 = Math.toRadians(RouteGuideUtil.getLatitude(end));
        double lon1 = Math.toRadians(RouteGuideUtil.getLongitude(start));
        double lon2 = Math.toRadians(RouteGuideUtil.getLongitude(end));
        double deltaLat = lat2 - lat1;
        double deltaLon = lon2 - lon1;
        double a = Math.sin(deltaLat/2) * Math.sin(deltaLat/2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.sin(deltaLon/2) * Math.sin(deltaLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return (int) (r * c);
    }

    /**
     * 双向流的RPC
     * @param responseObserver
     * @return
     */
    @Override
    public StreamObserver<RouteNote> routeChat(StreamObserver<RouteNote> responseObserver) {
        return new StreamObserver<RouteNote>() {
            @Override
            public void onNext(RouteNote routeNote) {
                List<RouteNote> notes = getOrCreateNotes(routeNote.getLocation());
                // 返回一个点之前所有点信息
                for (RouteNote prevNote : notes.toArray(new RouteNote[0])) {
                    responseObserver.onNext(prevNote);
                }
                // 添加新的note到列表中
                notes.add(routeNote);
            }

            @Override
            public void onError(Throwable t) {
                logger.log(Level.WARNING, "在routeChat()方法上发生错误", t);
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }

    /**
     * 根据传入的Point信息获取RouteNote列表，如果不存在就创建
     * @param location
     * @return
     */
    private List<RouteNote> getOrCreateNotes(Point location) {
        List<RouteNote> notes = Collections.synchronizedList(new ArrayList<>());
        List<RouteNote> prevNotes = routeNotes.putIfAbsent(location, notes);
        return prevNotes!=null?prevNotes:notes;
    }
}
