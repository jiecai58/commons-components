# 🥤 commons-components-cache

公共缓存组件

##目标
  将缓存逻辑从业务代码中抽离出来，通过AOP代理彻底实现缓存逻辑通业务解耦。
  
##功能
   ###1、将请求参数缓存
     例如:@CachePut(configName = RenaultConstant.PromotionCache, prefix = RedisKeyConstant.TRIAL_RESULTS_KEY,timeout = RedisKeyConstant.TRIAL_RESULTS_KEY_TTL, key = "#trialResultsDO.flowNo")

   ###2、读缓存，缓存内不存在调用接口，并更新缓存
      例如：@Cacheable(configName = RenaultConstant.PromotionCache, prefix = RedisKeyConstant.TRIAL_RESULTS_KEY,
            timeout = RedisKeyConstant.TRIAL_RESULTS_KEY_TTL, key = "#flowNo")  

   ###3、缓存清理
       例如：UnCacheable(configName = RenaultConstant.PromotionCache, prefix = RedisKeyConstant.TRIAL_RESULTS_KEY,key = "#flowNo")

   ###3、根据条件增加、删除、查询缓存
        @Cacheable(configName = xx, prefix =xx,key =#user.age, condition = "#user.age < 35" )

   ###4、排除指定key不走缓存
         在配置文件中增加cache.exclude.key的配置，多个key用逗号隔开。推荐在apollo默认增加此配置，可有效减少读本地配置文件带来的时间开销，配置后立马生效，不需要重启服务。排除某个可以，即将注解中prefix属性的值配置到cache.exclude.key上。

