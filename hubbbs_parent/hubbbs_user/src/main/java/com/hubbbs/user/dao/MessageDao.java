package com.hubbbs.user.dao;

import com.hubbbs.user.pojo.Message;
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
public interface MessageDao extends JpaRepository<Message, String>, JpaSpecificationExecutor<Message> {

    @Query(nativeQuery = true, value = "SELECT `user`.id userId,`user`.nickname userNickname,`user`.icon userIcon,message.id messageId, message.body messageBody,message.sendTime messageSendTime, message.senderId messageSenderId,message.receiverId messageReceiverId FROM `user`, message WHERE `user`.id = message.senderId and message.isBan!=1 AND message.receiverId = (:senderId)")
    public Page<Map<String, Object>> findBySenderIdWithUser(@Param("senderId") String senderId, Pageable pageable);

    @Query(nativeQuery = true, value = "select `user`.id userId,`user`.nickname userNickname,`user`.icon userIcon,message.id messageId, message.body messageBody,message.sendTime messageSendTime, message.senderId messageSenderId,message.receiverId messageReceiverId from `user`,message where`user`.id = message.receiverId and message.isBan!=1 AND message.senderId = (:receiverId)")
    public Page<Map<String, Object>> findByReceiverIdWithUser(@Param("receiverId") String receiverId, Pageable pageable);
}
