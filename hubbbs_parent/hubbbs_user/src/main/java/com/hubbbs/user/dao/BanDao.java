package com.hubbbs.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.hubbbs.user.pojo.Ban;
/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface BanDao extends JpaRepository<Ban,String>,JpaSpecificationExecutor<Ban>{
	
}
