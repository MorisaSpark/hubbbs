package com.hubbbs.user.config;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Created by IntelliJ IDEA.
 * User: HB
 * Date: 2019/4/3
 *
 * @author HB
 * To change this template use File | Settings | File Templates.
 */
@Configuration
public class ElasticSearchConfig {
    /**
     * 防止netty的bug
     * java.lang.IllegalStateException: availableProcessors is already set to [4], rejecting [4]
     */
    @PostConstruct
    void init() {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }
}
