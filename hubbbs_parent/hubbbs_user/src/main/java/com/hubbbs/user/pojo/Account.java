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
@Table(name="account")
public class Account implements Serializable{

	@Id
	private String id;//accountId


	
	private String username;//账号名
	private String password;//密码
	private String role;//权限
	private Integer isBan;//状态
	private String userId;//用户Id

	
//实体类公有方法.txt
	
}
