package com.hubbbs.user.dao;

import com.hubbbs.user.pojo.RelCollection;
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
public interface RelCollectionDao extends JpaRepository<RelCollection, String>, JpaSpecificationExecutor<RelCollection> {
    public RelCollection findByPostIdAndUserId(@Param("postId") String postId, @Param("userId") String userId);

    public void deleteByPostIdAndUserId(@Param("postId") String postId, @Param("userId") String userId);

    @Query(nativeQuery = true, value = "select post.replyNum replyNum, post.title title, post.id id,`user`.nickname userNickname,post.createTime createTime,post.clickNum clickNum,post.cookieNum cookieNum,post.collectionNum collectionNum, post.userId from post,relcollection,`user` where relcollection.postId=post.id and post.isBan=0 and post.userId = `user`.id and relcollection.userId=(:userId)")
    public Page<Map<String, Object>> findByUserIdWithPost(@Param("userId") String userId, Pageable pageable);
}
