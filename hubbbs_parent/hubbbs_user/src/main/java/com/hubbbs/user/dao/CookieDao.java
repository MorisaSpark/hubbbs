package com.hubbbs.user.dao;

import com.hubbbs.user.pojo.Cookie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;

/**
 * 数据访问接口
 *
 * @author Administrator
 */
public interface CookieDao extends JpaRepository<Cookie, String>, JpaSpecificationExecutor<Cookie> {
    public Cookie findByPostIdAndUserId(@Param("postId") String postId, @Param("userId") String userId);
}
