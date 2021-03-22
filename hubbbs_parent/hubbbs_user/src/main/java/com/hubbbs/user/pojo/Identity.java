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
@Table(name="identity")
public class Identity implements Serializable{

	@Id
	private String id;//identityId


	
	private String net;//网址
	private String username;//用户名
	private String password;//用户密码
	private String userId;//用户Id

	
//实体类公有方法.txt
	
}
