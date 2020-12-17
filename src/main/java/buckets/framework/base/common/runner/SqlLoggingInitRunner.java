package buckets.framework.base.common.runner;

import buckets.framework.base.common.utils.ClassUtil;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.*;

/**
 * SQL语句日志输出等级设定初始化
 * @author buckets
 * @date 2020/11/25
 */
@Component
public class SqlLoggingInitRunner implements ApplicationRunner {
    private List<String> showSql = Arrays.asList("true","1");

    @Value("${buckets.sql.show:false}")
    private String sqlShow;

    @Override
    public void run(ApplicationArguments args) {
        //当日志等级为info时，SQL语句将不会输出到日志中
        Level level = Level.INFO;
        //判断是否显示输出sql，默认为不输出sql
        if(showSql.contains(sqlShow)){
            //配置输出SQL语句，修改日志等级为debug
            level = Level.DEBUG;
        }

        //获取所有dao的类
        List<Class> classes = ClassUtil.getSubClassFromPackage(Mapper.class, "buckets.framework");
        Set<String> packages = new HashSet<>();
        //获取所有的包名并去重
        classes.forEach(clazz -> packages.add(clazz.getPackage().getName()));
        //获取日志输出上下文
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        //修改包内对应日志输出等级
        for (String packageName : packages) {
            loggerContext.getLogger(packageName).setLevel(level);
        }

    }
}
