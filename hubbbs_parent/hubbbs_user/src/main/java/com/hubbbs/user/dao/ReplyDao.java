package com.hubbbs.user.dao;

import com.hubbbs.user.pojo.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 数据访问接口
 *
 * @author Administrator
 */
public interface ReplyDao extends MongoRepository<Reply, String> {


    public Page<Reply> findByPostId(@Param("postId") String postId, Pageable pageable);

    public List<Reply> findByPostIdOrderByFloorThAsc(@Param("postId") String postId);

    public Integer countByPostId(String postId);

    public Reply findTopByPostIdOrderByFloorThDesc(String postId);
//    @Query(value = "")
//    public List<Reply> findByPostIdRandom(@Param("postId") String postId, @Param("size") int size);
}
