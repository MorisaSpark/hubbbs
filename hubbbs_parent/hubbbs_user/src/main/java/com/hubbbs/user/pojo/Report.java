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
@Table(name="report")
public class Report implements Serializable{

	@Id
	private String id;//reportId


	
	private String thing;//举报数据类型
	private String thingId;//举报数据Id
	private String reason;//举报理由
	private String type;//处理情况
	private String time;//举报时间
	private Integer isBan;//状态
	private String userId;//发送者
	private String dealerId;//处理者

	
//实体类公有方法.txt
	
}
