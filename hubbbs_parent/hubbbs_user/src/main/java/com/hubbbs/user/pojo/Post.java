package com.hubbbs.user.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
@Table(name="post")
public class Post implements Serializable{

	@Id
	private String id;//postId


	
	private String title;//标题
	private String body;//内容
	private String createTime;//创建时间
	private String lastReplyTime;//最新评论时间
	private Integer replyNum;//评论量
	private Integer clickNum;//点击量
	private Integer cookieNum;//喂食量
	private Integer collectionNum;//收藏量
	private Integer isHot;//是否热门
	private Integer isBan;//状态
	private String userId;//作者Id
	private String typeId;//类型Id

	
//实体类公有方法.txt
	
}
