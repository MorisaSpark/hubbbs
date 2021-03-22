package com.hubbbs.user.dao;

import com.hubbbs.user.pojo.ReplyMy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 数据访问接口
 *
 * @author Administrator
 */
public interface ReplyMyDao extends JpaRepository<ReplyMy, String>, JpaSpecificationExecutor<ReplyMy> {


}
