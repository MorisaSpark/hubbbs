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
@Table(name="user")
public class User implements Serializable{

	@Id
	private String id;//userId


	
	private String nickname;//用户昵称
	private Integer sex;//性别（0：女，1：男）
	private Integer grade;//年级
	private String createTime;//创号时间
	private String lastLoginTime;//最近登录时间
	private String lastLoginTimeB;//上一次登录时间
	private String lastRenameTime;//最后改名时间
	private String icon;//头像地址
	private String college;//大学名称
	private String major;//专业名称
	private String summary;//简介
	private Integer isBan;//状态
	private Double cookieNum;//饼干个数
	private Integer postNum;//发表文章量
	private Integer fansNum;//粉丝量
	private Integer attenterNum;//关注量

	
//实体类公有方法.txt
	
}
