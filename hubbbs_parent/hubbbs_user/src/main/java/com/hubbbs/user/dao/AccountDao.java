package com.hubbbs.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.hubbbs.user.pojo.Account;
/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface AccountDao extends JpaRepository<Account,String>,JpaSpecificationExecutor<Account>{
	public Account findByUsername(String username);

	public Account findByUserId(String userId);
}
