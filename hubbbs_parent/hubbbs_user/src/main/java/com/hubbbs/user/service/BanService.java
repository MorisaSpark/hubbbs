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

import com.hubbbs.user.dao.BanDao;
import com.hubbbs.user.pojo.Ban;

/**
 * 服务层
 * 
 * @author Administrator
 *
 */
@Service
@Transactional
public class BanService {

	@Autowired
	private BanDao banDao;
	
	@Autowired
	private IdWorker idWorker;

	/**
	 * 查询全部列表
	 * @return
	 */
	public List<Ban> findAll() {
		return banDao.findAll();
	}

	
	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Ban> findSearch(Map whereMap, int page, int size) {
		Specification<Ban> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return banDao.findAll(specification, pageRequest);
	}

	
	/**
	 * 条件查询
	 * @param whereMap
	 * @return
	 */
	public List<Ban> findSearch(Map whereMap) {
		Specification<Ban> specification = createSpecification(whereMap);
		return banDao.findAll(specification);
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public Ban findById(String id) {
		return banDao.findById(id).get();
	}

	/**
	 * 增加
	 * @param ban
	 */
	public void add(Ban ban) {
		ban.setId( idWorker.nextId()+"" );
		banDao.save(ban);
	}

	/**
	 * 修改
	 * @param ban
	 */
	public void update(Ban ban) {
		banDao.save(ban);
	}

	/**
	 * 删除
	 * @param id
	 */
	public void deleteById(String id) {
		banDao.deleteById(id);
	}

	/**
	 * 动态条件构建
	 * @param searchMap
	 * @return
	 */
	private Specification<Ban> createSpecification(Map searchMap) {

		return new Specification<Ban>() {

			@Override
			public Predicate toPredicate(Root<Ban> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // 
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 
                if (searchMap.get("thing")!=null && !"".equals(searchMap.get("thing"))) {
                	predicateList.add(cb.like(root.get("thing").as(String.class), "%"+(String)searchMap.get("thing")+"%"));
                }
                // 
                if (searchMap.get("thingId")!=null && !"".equals(searchMap.get("thingId"))) {
                	predicateList.add(cb.like(root.get("thingId").as(String.class), "%"+(String)searchMap.get("thingId")+"%"));
                }
                // 
                if (searchMap.get("reason")!=null && !"".equals(searchMap.get("reason"))) {
                	predicateList.add(cb.like(root.get("reason").as(String.class), "%"+(String)searchMap.get("reason")+"%"));
                }
                // 
                if (searchMap.get("time")!=null && !"".equals(searchMap.get("time"))) {
                	predicateList.add(cb.like(root.get("time").as(String.class), "%"+(String)searchMap.get("time")+"%"));
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
