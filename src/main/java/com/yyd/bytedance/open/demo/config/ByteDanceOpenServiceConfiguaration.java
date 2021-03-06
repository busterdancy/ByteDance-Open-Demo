package com.yyd.bytedance.open.demo.config;

import com.github.yydzxz.common.redis.RedissonByteDanceRedisOps;
import com.github.yydzxz.common.service.impl.RestTemplateByteDanceHttpRequestServiceImpl;
import com.github.yydzxz.open.api.IByteDanceOpenService;
import com.github.yydzxz.open.api.impl.ByteDanceOpenComponentServiceImpl;
import com.github.yydzxz.open.api.impl.ByteDanceOpenInRedisConfigStorage;
import com.github.yydzxz.open.api.impl.ByteDanceOpenServiceImpl;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * ByteDanceOpenServiceConfiguaration
 *
 * @author Clevo
 * @date 2020/7/12
 */
@Configuration
public class ByteDanceOpenServiceConfiguaration {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ByteDanceOpenProperties byteDanceOpenProperties;

    @Bean
    public IByteDanceOpenService getIByteDanceOpenService(){
        ByteDanceOpenServiceImpl byteDanceOpenService = new ByteDanceOpenServiceImpl();
        byteDanceOpenService.setByteDanceHttpRequestService(new RestTemplateByteDanceHttpRequestServiceImpl(restTemplate));
        RedissonByteDanceRedisOps redissonByteDanceRedisOps = new RedissonByteDanceRedisOps(redissonClient);
        byteDanceOpenService.setRedissonByteDanceRedisOps(redissonByteDanceRedisOps);
        byteDanceOpenService.setByteDanceOpenComponentService(new ByteDanceOpenComponentServiceImpl(byteDanceOpenService));
        ByteDanceOpenInRedisConfigStorage inRedisConfigStorage = new ByteDanceOpenInRedisConfigStorage(redissonByteDanceRedisOps, "yourprefix");
        inRedisConfigStorage.setComponentAppId(byteDanceOpenProperties.getComponentAppId());
        inRedisConfigStorage.setComponentAppSecret(byteDanceOpenProperties.getComponentSecret());
        inRedisConfigStorage.setComponentToken(byteDanceOpenProperties.getComponentToken());
        inRedisConfigStorage.setComponentAesKey(byteDanceOpenProperties.getComponentAesKey());
        byteDanceOpenService.setByteDanceOpenConfigStorage(inRedisConfigStorage);
        return byteDanceOpenService;
    }

}
