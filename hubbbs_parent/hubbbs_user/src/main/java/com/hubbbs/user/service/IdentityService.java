package com.hubbbs.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import com.hubbbs.user.dao.IdentityDao;
import com.hubbbs.user.pojo.Identity;

/**
 * 服务层
 * 
 * @author Administrator
 *
 */
@Service
@Transactional
public class IdentityService {

	@Autowired
	private IdentityDao identityDao;
	
	@Autowired
	private IdWorker idWorker;

	/**
	 * 查询全部列表
	 * @return
	 */
	public List<Identity> findAll() {
		return identityDao.findAll();
	}

	
	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Identity> findSearch(Map whereMap, int page, int size) {
		Specification<Identity> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return identityDao.findAll(specification, pageRequest);
	}

	
	/**
	 * 条件查询
	 * @param whereMap
	 * @return
	 */
	public List<Identity> findSearch(Map whereMap) {
		Specification<Identity> specification = createSpecification(whereMap);
		return identityDao.findAll(specification);
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public Identity findById(String id) {
		return identityDao.findById(id).get();
	}

	/**
	 * 增加
	 * @param identity
	 */
	public void add(Identity identity) {
		identity.setId( idWorker.nextId()+"" );
		identityDao.save(identity);
	}

	/**
	 * 修改
	 * @param identity
	 */
	public void update(Identity identity) {
		identityDao.save(identity);
	}

	/**
	 * 删除
	 * @param id
	 */
	public void deleteById(String id) {
		identityDao.deleteById(id);
	}

	/**
	 * 动态条件构建
	 * @param searchMap
	 * @return
	 */
	private Specification<Identity> createSpecification(Map searchMap) {

		return new Specification<Identity>() {

			@Override
			public Predicate toPredicate(Root<Identity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // 
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 
                if (searchMap.get("net")!=null && !"".equals(searchMap.get("net"))) {
                	predicateList.add(cb.like(root.get("net").as(String.class), "%"+(String)searchMap.get("net")+"%"));
                }
                // 
                if (searchMap.get("username")!=null && !"".equals(searchMap.get("username"))) {
                	predicateList.add(cb.like(root.get("username").as(String.class), "%"+(String)searchMap.get("username")+"%"));
                }
                // 
                if (searchMap.get("password")!=null && !"".equals(searchMap.get("password"))) {
                	predicateList.add(cb.like(root.get("password").as(String.class), "%"+(String)searchMap.get("password")+"%"));
                }
                // 
                if (searchMap.get("userId")!=null && !"".equals(searchMap.get("userId"))) {
                	predicateList.add(cb.like(root.get("userId").as(String.class), "%"+(String)searchMap.get("userId")+"%"));
                }
				
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

			}
		};

	}

}
