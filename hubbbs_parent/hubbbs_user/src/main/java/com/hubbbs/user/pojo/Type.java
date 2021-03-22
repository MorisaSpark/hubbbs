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
@Table(name="type")
public class Type implements Serializable{

	@Id
	private String id;//typeId


	
	private String nickname;//类型名
	private Integer postNum;//文章量
	private String createTime;//创建时间
	private Integer isBan;//状态

	
//实体类公有方法.txt
	
}
