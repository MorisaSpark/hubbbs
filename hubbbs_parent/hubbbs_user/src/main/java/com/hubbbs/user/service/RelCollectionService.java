package com.hubbbs.user.service;

import com.hubbbs.user.dao.PostDao;
import com.hubbbs.user.dao.RelCollectionDao;
import com.hubbbs.user.pojo.RelCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 服务层
 *
 * @author Administrator
 */
@Service
@Transactional
public class RelCollectionService {

    @Autowired
    private RelCollectionDao relCollectionDao;

    @Autowired
    private PostDao postDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 查询全部列表
     *
     * @return
     */
    public List<RelCollection> findAll() {
        return relCollectionDao.findAll();
    }


    /**
     * 条件查询+分页
     *
     * @param whereMap
     * @param page
     * @param size
     * @return
     */
    public Page<RelCollection> findSearch(Map whereMap, int page, int size) {
        Specification<RelCollection> specification = createSpecification(whereMap);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return relCollectionDao.findAll(specification, pageRequest);
    }


    /**
     * 条件查询
     *
     * @param whereMap
     * @return
     */
    public List<RelCollection> findSearch(Map whereMap) {
        Specification<RelCollection> specification = createSpecification(whereMap);
        return relCollectionDao.findAll(specification);
    }

    /**
     * 根据ID查询实体
     *
     * @param id
     * @return
     */
    public RelCollection findById(String id) {
        return relCollectionDao.findById(id).get();
    }

    /**
     * 增加
     *
     * @param relCollection
     */
    public void add(RelCollection relCollection) {
        relCollection.setId(idWorker.nextId() + "")
                .setTime("" + Instant.now().toEpochMilli());
//        redisTemplate.opsForValue().set("RelCollection::" + relCollection.getPostId() + "::" + relCollection.getUserId(), StringUtils.toEpochMilli());
        relCollectionDao.save(relCollection);
        postDao.updateCollectionNumByPostId(relCollection.getPostId(), "1");
        redisTemplate.delete("post::" + relCollection.getPostId());
    }

    /**
     * 修改
     *
     * @param relcollection
     */
    public void update(RelCollection relcollection) {
        relCollectionDao.save(relcollection);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void deleteById(String id) {
        relCollectionDao.deleteById(id);
    }

    /**
     * 动态条件构建
     *
     * @param searchMap
     * @return
     */
    private Specification<RelCollection> createSpecification(Map searchMap) {

        return new Specification<RelCollection>() {

            @Override
            public Predicate toPredicate(Root<RelCollection> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                // 
                if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
                    predicateList.add(cb.like(root.get("id").as(String.class), "%" + (String) searchMap.get("id") + "%"));
                }
                // 
                if (searchMap.get("time") != null && !"".equals(searchMap.get("time"))) {
                    predicateList.add(cb.like(root.get("time").as(String.class), "%" + (String) searchMap.get("time") + "%"));
                }
                // 
                if (searchMap.get("userId") != null && !"".equals(searchMap.get("userId"))) {
                    predicateList.add(cb.like(root.get("userId").as(String.class), "%" + (String) searchMap.get("userId") + "%"));
                }
                // 
                if (searchMap.get("postId") != null && !"".equals(searchMap.get("postId"))) {
                    predicateList.add(cb.like(root.get("postId").as(String.class), "%" + (String) searchMap.get("postId") + "%"));
                }

                return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));

            }
        };

    }

    public void delete(RelCollection relCollection) {
//        redisTemplate.delete("RelCollection::" + relCollection.getPostId() + "::" + relCollection.getUserId());
        relCollectionDao.deleteByPostIdAndUserId(relCollection.getPostId(), relCollection.getUserId());
        postDao.updateCollectionNumByPostId(relCollection.getPostId(), "-1");
        redisTemplate.delete("post::" + relCollection.getPostId());
    }

    public Page<Map<String, Object>> findByUserIdWithPost(String userId, Integer page, Integer size, String orderBy) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.DESC, orderBy.equals("createTime") ? "time" : orderBy);
        return relCollectionDao.findByUserIdWithPost(userId, pageable);
    }
}
