import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.List;


/**
 * 第一周课程作业：
 * @author lifei
 * @since 2020/10/21
 */
public class MyLFClassLoader extends ClassLoader{

    private String filePath;
    public MyLFClassLoader(String filePath){
        this.filePath = filePath;
    }

    /**
     * 读取文件的字节信息
     * @param filePath
     * @return
     */
    private byte[] readFileBytes(String filePath) throws IOException {
        InputStream in = new FileInputStream(filePath);
        List<Byte> bytes = new ArrayList<>();
        byte[] tbs = new byte[1024];
        int len = 0;
        while ((len=in.read(tbs, 0, tbs.length))!=-1){
            for (int i = 0; i < len; i++) {
                bytes.add(tbs[i]);
            }
        }
        byte[] rbs = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); i++) {
            rbs[i] = bytes.get(i);
        }
        return rbs;
    }

    /**
     * 解密字节码
     * @param source
     * @return
     */
    private byte[] changeSourceBytes(byte[] source){
        if (source==null || source.length==0){
            return null;
        }
        byte[] target = new byte[source.length];
        for (int i = 0; i < source.length; i++) {
            target[i] = (byte) (255 - source[i]);
        }
        return target;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            byte[] bytes = readFileBytes(filePath);
            byte[] targetSource = changeSourceBytes(bytes);
            return defineClass(name, targetSource, 0, targetSource.length);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {
        String filePath = "Hello.xlass";
        MyLFClassLoader myLFClassLoader = new MyLFClassLoader(filePath);
        try {
            Class<?> aClass = myLFClassLoader.findClass("Hello");
            Object target = aClass.getConstructor().newInstance();
            aClass.getMethod("hello").invoke(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

