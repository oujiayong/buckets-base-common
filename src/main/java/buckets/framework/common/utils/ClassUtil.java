package buckets.framework.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 查找指定路径下面继承父类的全部子类
 *
 * @author buckets
 * @date 2020/8/31
 */
public class ClassUtil {
    private static final Logger log = LoggerFactory.getLogger(ClassUtil.class);

    /**
     * 类包名分隔符
     */
    private static final String CLASS_PACKAGE_SEPARATOR = ".";
    /**
     * 文件路径名分隔符
     */
    private static final String FILE_PATH_SEPARATOR = "/";


    /**
     * 查询全部包路径下 继承父类的子类
     *
     * @param parentClazz 父类class对象
     * @return 子类集合
     */
    public static ArrayList<Class> getSubList(@NonNull Class<?> parentClazz) {
        return getClzFromPkg(parentClazz, "");
    }

    /**
     * 查询固定包路径下 继承父类的子类
     *
     * @param parentClazz 父类class对象
     * @param packageName 包名
     * @return 子类集合
     */
    public static ArrayList<Class> getClzFromPkg(@NonNull Class<?> parentClazz, @NonNull String packageName) {
        //第一个class类的集合
        ArrayList<Class> classes = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        try {

            // 获取包的名字 并进行替换
            String pkgDirName = packageName.replace(CLASS_PACKAGE_SEPARATOR, FILE_PATH_SEPARATOR);
            Enumeration<URL> urls = ClassUtil.class.getClassLoader().getResources(pkgDirName);
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                // 得到协议的名称
                String protocol = url.getProtocol();
                // 如果是以文件的形式保存在服务器上
                if ("file".equals(protocol)) {
                    // 获取包的物理路径
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    // 以文件的方式扫描整个包下的文件 并添加到集合中
                    findClassesByFile(packageName, filePath, parentClazz, classes);
                } else if ("jar".equals(protocol)) {
                    // 如果是jar包文件
                    // 获取jar
                    JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
                    //扫描jar包文件 并添加到集合中
                    findClassesByJar(packageName, jar, parentClazz, classes);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        log.info("在包 '{}' 下查找 {} 的子类过程耗时 {} ms", packageName, parentClazz.getTypeName(), (endTime - startTime));
        return classes;
    }

    /**
     * 从文件夹中寻找继承的子类
     *
     * @param packageName 包名
     * @param packagePath 包的物理路径
     * @param parentClazz 父类class对象
     * @param classes     子类集合
     */
    private static void findClassesByFile(String packageName, String packagePath, Class<?> parentClazz, ArrayList<Class> classes) {
        // 获取此包的目录 建立一个File
        File dir = new File(packagePath);
        // 如果不存在或者 也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }

        File[] dirFiles = dir.listFiles(pathname -> pathname.isDirectory() || pathname.getName().endsWith(".class"));

        if (dirFiles == null || dirFiles.length == 0) {
            return;
        }

        // 循环所有文件
        for (File f : dirFiles) {
            // 如果是目录 则继续扫描
            if (f.isDirectory()) {
                findClassesByFile(packageName + CLASS_PACKAGE_SEPARATOR + f.getName(), packagePath + FILE_PATH_SEPARATOR + f.getName(), parentClazz, classes);
                continue;
            }
            // 如果是java类文件 去掉后面的.class 只留下类名
            checkSubClass(packageName + CLASS_PACKAGE_SEPARATOR + f.getName(), parentClazz, classes);
        }
    }

    /**
     * 从jar包中寻找继承的子类
     *
     * @param packageName 包名
     * @param jar         jar包文件对象
     * @param parentClazz 父类class对象
     * @param classes     子类集合
     */
    private static void findClassesByJar(String packageName, JarFile jar, Class<?> parentClazz, ArrayList<Class> classes) {
        String pkgDir = packageName.replace(CLASS_PACKAGE_SEPARATOR, FILE_PATH_SEPARATOR);
        // 从此jar包 得到一个枚举类
        Enumeration<JarEntry> entry = jar.entries();

        JarEntry jarEntry;
        String name;
        // 同样的进行循环迭代
        while (entry.hasMoreElements()) {
            // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文
            jarEntry = entry.nextElement();

            name = jarEntry.getName();
            // 如果是以/开头的
            if (name.startsWith(FILE_PATH_SEPARATOR)) {
                // 获取后面的字符串
                name = name.substring(1);
            }

            if (jarEntry.isDirectory() || !name.startsWith(pkgDir) || !name.endsWith(".class")) {
                continue;
            }

            checkSubClass(name, parentClazz, classes);
        }
    }

    /**
     * 判断并添加子类到子类集合中
     *
     * @param className   判断对象类名
     * @param parentClazz 父类class对象
     * @param classes     子类集合
     */
    private static void checkSubClass(String className, Class<?> parentClazz, ArrayList<Class> classes) {

        //如果是一个.class文件 而且不是目录
        //去掉后面的".class" 获取真正的类名
        className = className.substring(0, className.length() - 6).replace(FILE_PATH_SEPARATOR, CLASS_PACKAGE_SEPARATOR);
        if (className.startsWith(CLASS_PACKAGE_SEPARATOR)) {
            className = className.replaceFirst(CLASS_PACKAGE_SEPARATOR, "");
        }
        try {
            Class<?> clazz = Class.forName(className);
            clazz.asSubclass(parentClazz);
            // 要么是子类，要么是类本身
            if (!className.equals(parentClazz.getCanonicalName())) {
                classes.add(clazz);
            }
        } catch (Throwable ignored) {
            //类名查找失败，类转换失败均不做处理
        }
    }

}
