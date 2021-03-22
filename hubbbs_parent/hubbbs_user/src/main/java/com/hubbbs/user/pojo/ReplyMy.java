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

/**
 * Created by IntelliJ IDEA.
 * User: HB
 * Date: 2019/4/17
 *
 * @author HB
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Data
@SuppressWarnings("unused")
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@DynamicInsert
@DynamicUpdate
@Table(name = "reply")
public class ReplyMy {
    @Id
    private String id;//



    private String toId;//
    private String createTime;//
    private String body;//
    private Integer thumbUpNum;//
    private Integer isBan;//
    private Integer floorTh;//
    private String userId;//
    private String postId;//


//实体类公有方法.txt
}
