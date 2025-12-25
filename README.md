# 前端配置修改（digital-product-vue）

**高德地图接入**

参考文档：https://lbs.amap.com/api/javascript-api-v2/guide/abc/amap-vue

项目文件/src/components/MapContainer.vue中修改securityJsCode: '「你申请的安全密钥」'以及key: '「你申请的应用Key」'

```
onMounted(() => {
  window._AMapSecurityConfig = {
    securityJsCode: '「你申请的安全密钥」',
  }
  AMapLoader.load({
    key: '「你申请的应用Key」', // 申请好的Web端开发者Key，首次调用 load 时必填
    version: '2.0', // 指定要加载的 JSAPI 的版本，缺省时默认为 1.4.15
    plugins: ['AMap.Scale', 'AMap.DistrictSearch'], //需要使用的的插件列表，如比例尺'AMap.Scale'，支持添加多个如：['...','...']
  })
    .then((AMap) => {
      map = new AMap.Map('container', {
        // 设置地图容器id
        viewMode: '3D', // 是否为3D地图模式
        zoom: 13, // 初始化地图级别
      })

      //比例尺
      let scale = new AMap.Scale()
      map.addControl(scale)

      //根据props.code进行定位
      const adcode = getTargetAdcode(props.code)
      let districtSearch = new AMap.DistrictSearch({
        level: 'district',
        subdistrict: 0,
        extensions: 'all',
      })
      districtSearch.search(adcode, (status, result) => {
        if (status === 'complete' && result.districtList && result.districtList.length > 0) {
          const district = result.districtList[0]
          // 定位到行政区中心
          if (district && district.center) {
            map.setCenter(district.center)
          }
        } else {
          // fallback: 定位到中国
          map.setCity('中国')
        }
      })
    })
    .catch((e) => {
      console.log(e)
    })
})
```



**MaxKB智能体接入**

参考文档：https://maxkb.cn/docs/v2/

项目文件index.html中修改src="你的MaxKB嵌入第三方的url"

```
<!doctype html>
<html lang="">
  <head>
    <meta charset="UTF-8" />
    <link rel="icon" href="/favicon.svg" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>宠物电商</title>
  </head>
  <body>
    <div id="app"></div>
    <script type="module" src="/src/main.js"></script>
    <script
      async
      defer
      src="你的MaxKB嵌入第三方的url"
    ></script>
  </body>
</html>
```

# 后端配置修改（digital-product-java）
## 注：替换application.yml文件中的密码相关信息

```
server:
  port: 9090

spring:
  application:
    name: digital-product-java
  servlet:
    multipart:
      max-request-size: 100MB
      max-file-size: 100MB
  #数据库配置
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    url: jdbc:mysql://你的主机名:3306/digital_product?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
    username: # 请配置数据库用户名
    password: # 请配置数据库密码
    druid:
      # 连接池核心参数
      initialSize: 5      # 初始连接数
      minIdle: 5          # 最小空闲连接数
      maxActive: 20       # 最大连接数（根据并发量调整）
      maxWait: 5000       # 获取连接超时时间（毫秒）
      # 连接有效性检测
      validationQuery: SELECT 1
      testWhileIdle: true  # 空闲时检测连接
      testOnBorrow: false  # 获取连接时不检测（性能更好）
      testOnReturn: false  # 归还连接时不检测
      # 空闲连接管理
      timeBetweenEvictionRunsMillis: 60000  # 检测间隔（60秒）
      minEvictableIdleTimeMillis: 300000    # 连接最小空闲时间（5分钟）
  data:
    redis:
      host: server
      port: 6379
      password: # 请配置Redis密码
      database: 0
      timeout: 3000
      lettuce:
        pool:
          max-active: 20
          max-idle: 10
          min-idle: 5
          max-wait: 3000

#mybatis配置
mybatis:
  configuration:
    map-underscore-to-camel-case: true
  mapper-locations: classpath:mapper/*.xml

#配置日志信息
logging:
  level:
    com.cdwater.digitalproduct: debug
    dev.langchain4j: debug

#jwt配置
jwt:
  secret: thisIsCdwaterSecretForDigitalProductProject # 请配置安全的JWT密钥
  admin-expiration: 14400000  # 4小时的毫秒数
  shop-expiration: 86400000  # 1天的毫秒数
  user-expiration: 604800000  # 7天的毫秒数
  token-name: Authorization

# 文件上传配置
dromara:
  x-file-storage: #文件存储配置
    default-platform: aliyun-oss-1 #默认使用的存储平台
    thumbnail-suffix: ".min.jpg" #缩略图后缀，例如【.min.jpg】【.png】
    #对应平台的配置写在这里，注意缩进要对齐
    aliyun-oss:
      - platform: aliyun-oss-1 # 存储平台标识
        enable-storage: true  # 启用存储
        access-key: # 请配置阿里云OSS访问密钥
        secret-key: # 请配置阿里云OSS密钥
        end-point: oss-cn-beijing.aliyuncs.com # 访问域名
        bucket-name: digital-product # 存储桶名称
        # 访问域名，注意"/"结尾，例如：https://abc.oss-cn-shanghai.aliyuncs.com/
        domain: https://digital-product.oss-cn-beijing.aliyuncs.com/
        base-path: images/ # 基础路径

langchain4j:
  open-ai:
    chat-model:
      base-url: https://api.deepseek.com/v1
      api-key: # 请配置DeepSeek API密钥
      model-name: deepseek-chat
      log-requests: true
      log-responses: true
    streaming-chat-model:
      base-url: https://api.deepseek.com/v1
      api-key: # 请配置DeepSeek API密钥
      model-name: deepseek-chat
      log-requests: true
      log-responses: true
    embedding-model:
      base-url: https://dashscope.aliyuncs.com/compatible-mode/v1
      api-key: # 请配置DashScope API密钥
      model-name: text-embedding-v4
      log-requests: true
      log-responses: true
      max-segments-per-batch: 10
```
