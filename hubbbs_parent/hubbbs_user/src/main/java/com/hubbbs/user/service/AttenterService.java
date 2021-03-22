package com.hubbbs.user.service;

import com.hubbbs.user.dao.AttenterDao;
import com.hubbbs.user.dao.UserDao;
import com.hubbbs.user.pojo.Attenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务层
 *
 * @author Administrator
 */
@Service
@Transactional
public class AttenterService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private AttenterDao attenterDao;

    @Autowired
    private UserDao userDao;

    /**
     * 查询全部列表
     *
     * @return
     */
    public List<Attenter> findAll() {
        return attenterDao.findAll();
    }


    /**
     * 条件查询+分页
     *
     * @param whereMap
     * @param page
     * @param size
     * @return
     */
    public Page<Attenter> findSearch(Map whereMap, int page, int size) {
        Specification<Attenter> specification = createSpecification(whereMap);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return attenterDao.findAll(specification, pageRequest);
    }


    /**
     * 条件查询
     *
     * @param whereMap
     * @return
     */
    public List<Attenter> findSearch(Map whereMap) {
        Specification<Attenter> specification = createSpecification(whereMap);
        return attenterDao.findAll(specification);
    }

    /**
     * 根据ID查询实体
     *
     * @param id
     * @return
     */
    public Attenter findById(String id) {
        return attenterDao.findById(id).get();
    }

    /**
     * 增加
     *
     * @param attenter
     */
    public void addRedis(Attenter attenter) {
        //        用redis    attenter::add::userId::fanId, time
        redisTemplate.opsForZSet().add("attenter::user::" + attenter.getUserId(), attenter.getFanId(), Instant.now().toEpochMilli());
        redisTemplate.opsForZSet().add("attenter::fan::" + attenter.getFanId(), attenter.getUserId(), Instant.now().toEpochMilli());
    }

    public void add(Attenter attenter) {
        attenter.setId(idWorker.nextId() + "")
                .setTime("" + Instant.now().toEpochMilli());
        attenterDao.save(attenter);
        userDao.incFansNumByUserId(attenter.getUserId(), 1);
        userDao.incAttenterNumByUserId(attenter.getFanId(),1);
    }

    /**
     * 修改
     *
     * @param attenter
     */
    public void update(Attenter attenter) {
        attenterDao.save(attenter);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void deleteById(String id) {
        attenterDao.deleteById(id);
    }

    /**
     * 动态条件构建
     *
     * @param searchMap
     * @return
     */
    private Specification<Attenter> createSpecification(Map searchMap) {

        return new Specification<Attenter>() {

            @Override
            public Predicate toPredicate(Root<Attenter> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                // 
                if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
                    predicateList.add(cb.like(root.get("id").as(String.class), "%" + (String) searchMap.get("id") + "%"));
                }
                // 
                if (searchMap.get("userId") != null && !"".equals(searchMap.get("userId"))) {
                    predicateList.add(cb.like(root.get("userId").as(String.class), "%" + (String) searchMap.get("userId") + "%"));
                }
                // 
                if (searchMap.get("fanId") != null && !"".equals(searchMap.get("fanId"))) {
                    predicateList.add(cb.like(root.get("fanId").as(String.class), "%" + (String) searchMap.get("fanId") + "%"));
                }
                // 
                if (searchMap.get("time") != null && !"".equals(searchMap.get("time"))) {
                    predicateList.add(cb.like(root.get("time").as(String.class), "%" + (String) searchMap.get("time") + "%"));
                }

                return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));

            }
        };

    }

    public Boolean findIsSelf(String userId, String fanId) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("fanId", fanId);
        return findSearch(map).size() > 0;
    }

    public void delete(Map map) {
        Attenter attenter = attenterDao.findByUserIdAndFanId((String) map.get("userId"), (String) map.get("fanId"));
        userDao.decFansNumByUserId((String) map.get("userId"),1);
        userDao.decAttenterNumByUserId((String) map.get("fanId"),1);
        attenterDao.delete(attenter);
    }

    private Attenter specificationPojo(Map map) {
        Attenter attenter = new Attenter();
        if(map.get("id")!=null){
            attenter.setId((String) map.get("id"));
        }
        if(map.get("userId")!=null){
            attenter.setUserId((String) map.get("userId"));
        }
        if(map.get("fanId")!=null){
            attenter.setFanId((String) map.get("fanId"));
        }
        if(map.get("time")!=null){
            attenter.setTime((String) map.get("time"));
        }
        return attenter;
    }

    public Page<Map<String, Object>> searchByTypeSelf(String userId, String er, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.DESC, "time");
        if (er.equals("fan")) {
            return attenterDao.findByAttenterIdWithUser(userId, pageable);
        } else if (er.equals("attenter")) {
            return attenterDao.findByFanIdWithUser(userId, pageable);
        } else {
            return null;
        }
    }
}
