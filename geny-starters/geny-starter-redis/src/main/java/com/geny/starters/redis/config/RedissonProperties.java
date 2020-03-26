package com.geny.starters.redis.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description: Redisson Cluster 配置文件
 */
@ConfigurationProperties(prefix = "spring.redis")
public class RedissonProperties {

    private String codec = "org.redisson.codec.JsonJacksonCodec";

    public String getCodec() {
        return codec;
    }

    public void setCodec(String codec) {
        this.codec = codec;
    }

    public static class ClusterServer {

        private String[] nodes;

        public String[] getNodes() {
            return nodes;
        }

        public void setNodes(String[] nodes) {
            this.nodes = nodes;
        }
    }
}
