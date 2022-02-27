package com.hef.demo.grpc.api;


import com.google.protobuf.util.JsonFormat;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @Date 2022/2/26
 * @Author lifei
 */
public class RouteGuideUtil {

    // 坐标因子
    private static final double COORD_FACTOR = 1e7;


    /**
     * 确定一个feature是否存在
     * @param feature
     * @return
     */
    public static boolean exists(Feature feature) {
        return feature!=null && StringUtils.isNoneEmpty(feature.getName());
    }

    /**
     * 从给定的点获取经度
     * @param location
     * @return
     */
    public static double getLatitude(Point location) {
        return location.getLatitude()/COORD_FACTOR;
    }

    /**
     * 从给定的点获取纬度
     * @param location
     * @return
     */
    public static double getLongitude(Point location) {
        return location.getLongitude()/COORD_FACTOR;
    }

    /**
     * 从class路径获取默认的Features列表
     * @return
     */
    public static URL getDefaultFeaturesFile() {
        return RouteGuideUtil.class.getResource("route_guide_db.json");
    }

    /**
     * 解析包含Feature列表的JSON文件
     * @param file
     * @return
     */
    public static List<Feature> parseFeatures(URL file) {
        try (InputStream inputStream = file.openStream();
             Reader reader = new InputStreamReader(inputStream, Charset.forName("UTF-8"))) {
            FeatureDatabase.Builder database = FeatureDatabase.newBuilder();
            JsonFormat.parser().merge(reader, database);
            return database.getFeatureList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        System.out.println(1e7==10_000_000);
    }
}
