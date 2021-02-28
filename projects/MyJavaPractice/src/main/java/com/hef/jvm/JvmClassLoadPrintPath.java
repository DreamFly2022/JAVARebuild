package com.hef.jvm;

import sun.misc.Launcher;

import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

/**
 * @author lifei
 * @since 2021/2/21
 */
public class JvmClassLoadPrintPath {

    /**
     * 发现该示例 可以使用jdk 1.8 进行演示，如果使用 jdk11 就会有问题
     * @param args
     */
    public static void main(String[] args) {
        // 启动类加载器
        URL[] urLs = Launcher.getBootstrapClassPath().getURLs();
        System.out.println("启动类加载器");
        for (URL urL : urLs) {
            System.out.println(" ==> " + urL.toExternalForm());
        }

        // 拓展类加载器
        ClassLoader extClassLoader = JvmClassLoadPrintPath.class.getClassLoader().getParent();
        printClassLoad("拓展类加载器", extClassLoader);


        //  应用类加载器
        ClassLoader appClassLoader = JvmClassLoadPrintPath.class.getClassLoader();
        printClassLoad("应用类加载器", appClassLoader);
    }

    private static void printClassLoad(String name, ClassLoader CL) {
        if (CL != null) {
            System.out.println(name + "ClassLoader -> " + CL.toString());
            printURLForClassLoader(CL);
        } else {
            System.out.println(name + "ClassLoader -> null");
        }
    }

    private static void printURLForClassLoader(ClassLoader CL) {
        Object ucp = insightField(CL, "ucp");
        Object path = insightField(ucp, "path");
        ArrayList ps = (ArrayList) path;
        for (Object p : ps) {
            System.out.println(" ==> " + p.toString());
        }

    }

    private static Object insightField(Object obj, String fName) {
        try {
            Field f = null;
            if (obj instanceof ClassLoader) {
                f = URLClassLoader.class.getDeclaredField(fName);
            }else {
                f = obj.getClass().getDeclaredField(fName);
            }
            f.setAccessible(true);
            return f.get(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
