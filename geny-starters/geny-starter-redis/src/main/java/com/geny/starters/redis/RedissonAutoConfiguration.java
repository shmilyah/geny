package com.geny.starters.redis;

import com.geny.starters.redis.config.RedissonProperties;
import io.netty.channel.nio.NioEventLoopGroup;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties({RedisProperties.class, RedissonProperties.class})
public class RedissonAutoConfiguration {

    Logger logger = LoggerFactory.getLogger(RedissonAutoConfiguration.class);

    @Resource
    RedisProperties redisProperties;
    @Resource RedissonProperties redissonProperties;

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean
    RedissonClient redissonClient(){

        logger.info("RedissonAutoConfiguration enabled");

        Config config = new Config();
        if(redisProperties.getSentinel() != null){

            List<String> list = redisProperties.getSentinel().getNodes();
            String[] nodes = list.stream().map(s -> s.startsWith("redis://") ? s : "redis://" + s).toArray(size -> new String[size]);

            SentinelServersConfig serverConfig = config.useSentinelServers()
                    .addSentinelAddress(nodes)
                    .setDatabase(redisProperties.getDatabase())
                    .setMasterName(redisProperties.getSentinel().getMaster());
            if (!StringUtils.isEmpty(redisProperties.getPassword())) {
                serverConfig.setPassword(redisProperties.getPassword());
            }
        }else if(redisProperties.getCluster() != null){
            List<String> list = redisProperties.getCluster().getNodes();
            String[] nodes = list.stream().map(s -> s.startsWith("redis://") ? s : "redis://" + s).toArray(size -> new String[size]);

            ClusterServersConfig serverConfig = config.useClusterServers()
                    .addNodeAddress(nodes);
            if (!StringUtils.isEmpty(redisProperties.getPassword())) {
                serverConfig.setPassword(redisProperties.getPassword());
            }
        }else {
            String node = redisProperties.getHost() + ":" + redisProperties.getPort();
            node = node.startsWith("redis://") ? node : "redis://" + node;
            SingleServerConfig serverConfig = config.useSingleServer()
                    .setAddress(node)
                    .setDatabase(redisProperties.getDatabase());
            if (!StringUtils.isEmpty(redisProperties.getPassword())) {
                serverConfig.setPassword(redisProperties.getPassword());
            }
        }
        Codec codec = null;
        try {
            codec = (Codec) ClassUtils.forName(redissonProperties.getCodec(), ClassUtils.getDefaultClassLoader()).newInstance();
        } catch (Exception e) {
            logger.warn("无法初始化 Redission Codec 配置，使用默认的 org.redisson.codec.JsonJacksonCodec，错误原因:{}",e);
            codec = new JsonJacksonCodec();
        }
        config.setCodec(codec);
        config.setEventLoopGroup(new NioEventLoopGroup());
        return Redisson.create(config);
    }
}

