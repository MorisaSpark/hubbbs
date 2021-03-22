package com.hubbbs.user.dao;

import com.hubbbs.user.pojo.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

/**
 * 数据访问接口
 *
 * @author Administrator
 */
public interface PostDao extends JpaRepository<Post, String>, JpaSpecificationExecutor<Post> {

    List<Post> findByIdIn(List ids);

    @Query(nativeQuery = true, value = "select title,id from post where id in (:ids)  and isBan=0")
    Page<Map<String, Object>> findTitleAndIdByIdIn(List ids, Pageable pageable);

    @Query(value = "SELECT post.id postId,post.body postBody,post.clickNum postClickNum,post.collectionNum postCollectionNum ,post.cookieNum postCookieNum,post.createTime postCreateTime,post.isBan postIsBan,post.isHot postIsHot,post.lastReplyTime postLastReplyTime,post.replyNum postReplyNum,post.title postTitle,post.userId postUserId,post.typeId postTypeId,type.id typeId,type.nickname typeNickname,type.isBan typeIsBan,type.postNum typePostNum,type.createTime typeCreateTime,`user`.attenterNum userAttenterNum,`user`.college userCollege,`user`.cookieNum userCookieNum,`user`.createTime userCreateTime,`user`.summary userSummary,`user`.fansNum userFansNum,`user`.grade userGrade,`user`.icon userIcon,`user`.id userId,`user`.isBan userIsBan,`user`.lastLoginTime userLastLoginTime,`user`.lastLoginTimeB userLastLoginTimeB,`user`.lastRenameTime userLastRenameTime,`user`.major userMajor,`user`.nickname userNickname,`user`.postNum userPostNum,`user`.sex userSex FROM post,type,`user` WHERE post.id = (:postId) AND post.typeId = type.id AND post.userId = `user`.id AND ( post.isBan = 0 OR type.isBan = 0 OR `user`.isBan = 0 ) limit 1", nativeQuery = true)
    Map<String, Object> findByIdDetail(@Param("postId") String postId);

    //
    @Query(value = "SELECT post.id postId, post.clickNum postClickNum, post.collectionNum postCollectionNum, post.cookieNum postCookieNum, post.createTime postCreateTime, post.isBan postIsBan, post.isHot postIsHot, post.lastReplyTime postLastReplyTime, post.replyNum postReplyNum, post.title postTitle, post.userId postUserId, post.typeId postTypeId, type.id typeId, type.nickname typeNickname, type.isBan typeIsBan, type.postNum typePostNum, type.createTime typeCreateTime, `user`.attenterNum userAttenterNum, `user`.college userColleage, `user`.id userId, `user`.nickname userNickname FROM post, type, `user` WHERE post.typeId = type.id  AND post.userId = `user`.id  AND ( post.isBan = 0 OR type.isBan = 0 OR `user`.isBan = 0 )  AND post.typeId = (:typeId)", nativeQuery = true)
    Page<Map<String, Object>> getListSummary(@Param("typeId") String typeId, Pageable Pageable);

    @Query(value = "SELECT post.id postId, post.clickNum postClickNum, post.collectionNum postCollectionNum, post.cookieNum postCookieNum, post.createTime postCreateTime, post.isBan postIsBan, post.isHot postIsHot, post.lastReplyTime postLastReplyTime, post.replyNum postReplyNum, post.title postTitle, post.userId postUserId, post.typeId postTypeId, type.id typeId, type.nickname typeNickname, type.isBan typeIsBan, type.postNum typePostNum, type.createTime typeCreateTime, `user`.attenterNum userAttenterNum, `user`.college userColleage, `user`.id userId, `user`.nickname userNickname FROM post, type, `user` WHERE post.typeId = type.id  AND post.userId = `user`.id  AND ( post.isBan = 0 OR type.isBan = 0 OR `user`.isBan = 0 ) ", nativeQuery = true)
    Page<Map<String, Object>> getListSummaryAll(Pageable Pageable);

    @Modifying
    @Query(nativeQuery = true, value = "update post set replyNum=replyNum+(:step) where id=(:postId)")
    public void incReplyNumByPostId(@Param("postId") String userId, @Param("step") Integer step);

    @Query(nativeQuery = true, value = "select sum(clickNum) from post where userId=(:userId)")
    public Integer findSumClickNumByUserId(@Param("userId") String userId);

    public Page<Post> findByUserId(@Param("userId") String userId, Pageable pageable);

    @Modifying
    @Query(nativeQuery = true, value = "update post set cookieNum=cookieNum+(:step) where id=(:postId)")
    public void updateCookieNumByPostId(@Param("postId") String postId, @Param("step") String step);

    @Query(nativeQuery = true, value = "select * from post where typeId=(:typeId)")
    public Page<Map<String, Object>> findTitleAndIdByTypeId(@Param("typeId") String typeId, Pageable pageable);

    @Query(nativeQuery = true, value = "select post.replyNum replyNum, post.title title, post.id id,`user`.nickname userNickname,post.createTime createTime,post.clickNum clickNum,post.cookieNum cookieNum,post.collectionNum collectionNum, post.userId from post,`user` where  post.isBan=0 and post.userId = `user`.id and post.userId=(:userId)")
    Page<Map<String, Object>> findPartByUserId(@Param("userId") String userId, Pageable pageable);

    @Modifying
    @Query(nativeQuery = true, value = "update post set collectionNum=collectionNum+(:step) where id=(:postId)")
    public void updateCollectionNumByPostId(@Param("postId") String postId, @Param("step") String step);

    @Modifying
    @Query(nativeQuery = true, value = "update post set clickNum=clickNum+(:step) where id=(:postId)")
    public void updateClickNumByPostId(@Param("postId") String postId, @Param("step") Integer step);
}
