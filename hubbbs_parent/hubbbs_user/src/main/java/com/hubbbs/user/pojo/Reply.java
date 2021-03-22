package com.hubbbs.user.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
/**
 * 实体类
 * @author Administrator
 *
 */
@Entity
@Data
@SuppressWarnings("unused")
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@DynamicInsert
@DynamicUpdate
@Document(collection = "reply")
public class Reply implements Serializable{

	@Id
	private String id;//replyId



	private String toId;//回复的评论id
	private String createTime;//评论时间
	private String body;//评论内容
	private Integer thumbUpNum;//置顶级别
	private Integer isBan;//状态
	private Integer floorTh;//楼层
	private String userId;//评论者
	private String postId;//所评论的文章Id

	
//实体类公有方法.txt
	
}
