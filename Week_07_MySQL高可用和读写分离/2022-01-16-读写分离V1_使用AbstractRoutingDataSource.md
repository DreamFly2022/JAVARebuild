# 读写分离V1：使用AbstractRoutingDataSource

[toc]

## 一、AbstractRoutingDataSource 介绍

根据定义的规则选择对应的数据源

完整的代码：[]()

## 二、pom依赖

```xml
<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
			<exclusions>
				<exclusion>
					<groupId>org.springframework.boot</groupId>
					<artifactId>spring-boot-starter-logging</artifactId>
				</exclusion>
			</exclusions>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-log4j2</artifactId>
		</dependency>
		<dependency>
			<groupId>org.mybatis.spring.boot</groupId>
			<artifactId>mybatis-spring-boot-starter</artifactId>
			<version>${mybatis-spring-boot-starter.version}</version>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-aop</artifactId>
		</dependency>
		<dependency>
			<groupId>com.alibaba</groupId>
			<artifactId>druid-spring-boot-starter</artifactId>
			<version>${druid-spring-boot-starter.version}</version>
		</dependency>
		<dependency>
			<groupId>org.apache.commons</groupId>
			<artifactId>commons-lang3</artifactId>
			<version>${commons-lang3.version}</version>
		</dependency>
		<dependency>
			<groupId>org.apache.commons</groupId>
			<artifactId>commons-collections4</artifactId>
			<version>${commons-collections4.version}</version>
		</dependency>

		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>junit</groupId>
			<artifactId>junit</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>
```

## 三、配置读写分离的数据源

```java
/**
 * @Date 2022/1/16
 * @Author lifei
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicDataSource.class);

    /**
     * ThreadLocal 用于提供线程局部变量，在多线程环境可以保证各个线程里的变量独立于其它线程里的变量。
     * 也就是说 ThreadLocal 可以为每个线程创建一个【单独的变量副本】，相当于线程的 private static 类型变量。
     */
    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();

    public DynamicDataSource(DataSource defaultDataSource, Map<Object, Object> targetDataSourceMap) {
        super.setDefaultTargetDataSource(defaultDataSource);
        super.setTargetDataSources(targetDataSourceMap);
        super.afterPropertiesSet();
    }
    
    /**
     * 根据规则选择当前当前的数据源
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        String dataSourceKey = getDataSourceKey();
        LOGGER.info("当前的dataSourceKey为： " + dataSourceKey);
        return dataSourceKey;
    }

    public static void setDataSource(String dataSourceKey) {
        CONTEXT_HOLDER.set(dataSourceKey);
    }

    public static String getDataSourceKey() {
        return CONTEXT_HOLDER.get();
    }

    public static void clearDataSource() {
        CONTEXT_HOLDER.remove();
    }
}

```

```java
/**
 * @Date 2022/1/16
 * @Author lifei
 */
@Configuration
public class DataSourceConf {

    @Bean(name = "dataSourceWrite")
    @ConfigurationProperties(prefix = "spring.datasource.druid.write")
    public DataSource dataSourceWrite() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "dataSourceRead01")
    @ConfigurationProperties(prefix = "spring.datasource.druid.read01")
    public DataSource dataSourceRead01() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "dataSourceRead02")
    @ConfigurationProperties(prefix = "spring.datasource.druid.read02")
    public DataSource dataSourceRead02() {
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * 动态切换的数据源
     * @param dataSourceWrite
     * @param dataSourceRead01
     * @param dataSourceRead02
     * @return
     */
    @Bean
    public DynamicDataSource dynamicDataSource(@Qualifier("dataSourceWrite") DataSource dataSourceWrite,
                                               @Qualifier("dataSourceRead01") DataSource dataSourceRead01,
                                               @Qualifier("dataSourceRead02") DataSource dataSourceRead02) {
        Map<Object, Object> targetDataSourceMap = new HashMap<>();
        targetDataSourceMap.put(DataSourceKey.READ01_KEY, dataSourceRead01);
        targetDataSourceMap.put(DataSourceKey.READ02_KEY, dataSourceRead02);
        DynamicDataSource dynamicDataSource = new DynamicDataSource(dataSourceWrite, targetDataSourceMap);
        return dynamicDataSource;
    }
}
```

## 四、配置mybatis

```java
/**
 * @Date 2022/1/16
 * @Author lifei
 */
@MapperScan(basePackages = {"com.hef.myreadwritesepabstractrountingv1.dao"},
        sqlSessionFactoryRef = "sqlSessionFactory", sqlSessionTemplateRef = "sqlSessionTemplate")
@Configuration
public class RepositoryConf {

    private static final String[] LOCAL_MAPPERS = {"classpath:mybatis/**/*.xml"};

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(DynamicDataSource dynamicDataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dynamicDataSource);
        List<Resource> resourceList = new ArrayList<>();
        for (String localMapper : LOCAL_MAPPERS) {
            Resource[] resources = new PathMatchingResourcePatternResolver().getResources(localMapper);
            resourceList.addAll(Arrays.asList(resources));
        }
        sqlSessionFactoryBean.setMapperLocations(resourceList.toArray(new Resource[0]));

        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "sqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}

```

## 五、定义注解，并配置AOP，运行时切换数据源

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurDataSource {
    String name() default "";
}
```

```
/**
 * @Date 2022/1/16
 * @Author lifei
 */
@Aspect
@Order(1)
@Component
public class DataSourceAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceAspect.class);


    /**
     * 切点
     */
    @Pointcut("@annotation(com.hef.myreadwritesepabstractrountingv1.conf.CurDataSource)")
    public void dataSourcePointCut() {
    }

    /**
     * 增强
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();
        CurDataSource annotation = method.getAnnotation(CurDataSource.class);
        if (annotation==null) {
            DynamicDataSource.setDataSource(DataSourceKey.WRITE_KEY);
            LOGGER.info("使用默认数据库连接： " + DataSourceKey.WRITE_KEY);
        }else {
            DynamicDataSource.setDataSource(annotation.name());
            LOGGER.info("使用指定数据库连接： " + annotation.name());
        }
        try {
            return point.proceed();
        } finally {
            DynamicDataSource.clearDataSource();
        }
    }
}
```

定义一个常量池，表示不同的数据源类型：

```
/**
 * @Date 2022/1/16
 * @Author lifei
 */
public final class DataSourceKey {

    public static final String WRITE_KEY = "write";
    public static final String READ01_KEY = "read01";
    public static final String READ02_KEY = "read02";
}
```

## 六、在service层使用注解，动态切换数据源

```java
@Service
public class ModelInfoServiceImpl implements ModelInfoService {


    @Resource
    private ModelInfoDao modelInfoDao;

    @CurDataSource(name = DataSourceKey.READ01_KEY)
    @Override
    public List<ModelInfo> findModelInfoList() {
        List<ModelInfo> result = new ArrayList<>();
        List<ModelInfo> modelInfoList = modelInfoDao.findModelInfoList();
        if (CollectionUtils.isNotEmpty(modelInfoList)) {
            result.addAll(modelInfoList);
        }
        return result;
    }

    @CurDataSource(name = DataSourceKey.READ02_KEY)
    @Override
    public ModelInfo findModelInfoByModelType(String modelType) {
        if (StringUtils.isBlank(modelType)) return null;
        ModelInfo modelInfo = modelInfoDao.findModelInfoByModelType(modelType);
        if (modelInfo==null) return null;
        return modelInfo.clone();
    }

}

```

## 七、参考

[spring boot使用AbstractRoutingDataSource实现动态数据源切换](https://blog.csdn.net/qq_37502106/article/details/91044952)。

