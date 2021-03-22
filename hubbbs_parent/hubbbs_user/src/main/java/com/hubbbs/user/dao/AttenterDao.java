package com.hubbbs.user.dao;

import com.hubbbs.user.pojo.Attenter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Map;

/**
 * 数据访问接口
 *
 * @author Administrator
 */
public interface AttenterDao extends JpaRepository<Attenter, String>, JpaSpecificationExecutor<Attenter> {
    public Attenter findByUserIdAndFanId(@Param("userId") String userId, @Param("fanId") String fanId);

    @Query(nativeQuery = true, value = "SELECT `user`.id userId,`user`.nickname userNickname,`user`.icon userIcon,`user`.summary userSummary,`user`.postNum userPostNum,`user`.fansNum userfansNum,  attenter.fanId attenterFanId, attenter.userId  attenterUserId,attenter.id attenterId FROM `user`, attenter WHERE `user`.id = attenter.fanId  AND attenter.userId = (:attenterId)")
    public Page<Map<String, Object>> findByAttenterIdWithUser(@Param("attenterId") String attenterId, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT `user`.id userId,`user`.nickname userNickname,`user`.icon userIcon,`user`.summary userSummary,`user`.postNum userPostNum,`user`.fansNum userfansNum, attenter.fanId attenterFanId, attenter.userId  attenterUserId,attenter.id attenterId FROM `user`, attenter WHERE `user`.id = attenter.userId  AND attenter.fanId = (:fanId)")
    public Page<Map<String, Object>> findByFanIdWithUser(@Param("fanId") String fanId, Pageable pageable);

}
