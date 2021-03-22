package com.hubbbs.user.service;

import com.hubbbs.user.dao.CookieDao;
import com.hubbbs.user.dao.PostDao;
import com.hubbbs.user.dao.UserDao;
import com.hubbbs.user.pojo.Cookie;
import com.hubbbs.user.pojo.Post;
import com.hubbbs.user.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
public class CookieService {

    @Autowired
    private CookieDao cookieDao;

    @Autowired
    private PostDao postDao;
    @Autowired
    private UserDao userDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 查询全部列表
     *
     * @return
     */
    public List<Cookie> findAll() {
        return cookieDao.findAll();
    }


    /**
     * 条件查询+分页
     *
     * @param whereMap
     * @param page
     * @param size
     * @return
     */
    public Page<Cookie> findSearch(Map whereMap, int page, int size) {
        Specification<Cookie> specification = createSpecification(whereMap);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return cookieDao.findAll(specification, pageRequest);
    }


    /**
     * 条件查询
     *
     * @param whereMap
     * @return
     */
    public List<Cookie> findSearch(Map whereMap) {
        Specification<Cookie> specification = createSpecification(whereMap);
        return cookieDao.findAll(specification);
    }

    /**
     * 根据ID查询实体
     *
     * @param id
     * @return
     */
    public Cookie findById(String id) {
        return cookieDao.findById(id).get();
    }

    /**
     * 增加
     *
     * @param cookie
     */
    public void add(Cookie cookie) {
        //喂食逻辑
        cookie.setId(idWorker.nextId() + "");
        cookie.setTime(StringUtils.toEpochMilli());
        cookieDao.save(cookie);
        //1.文章饼干数增加
        postDao.updateCookieNumByPostId(cookie.getPostId(), "1");
        Post post = postDao.findById(cookie.getPostId()).get();
        //2.作者饼干数增加
        userDao.updateCookieNumByUserId(post.getUserId(), "1");
        //3.用户饼干数减少
        userDao.updateCookieNumByUserId(cookie.getUserId(), "-1");
        redisTemplate.delete("post::" + cookie.getPostId());
    }

    /**
     * 修改
     *
     * @param cookie
     */
    public void update(Cookie cookie) {
        cookieDao.save(cookie);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void deleteById(String id) {
        cookieDao.deleteById(id);
    }

    /**
     * 动态条件构建
     *
     * @param searchMap
     * @return
     */
    private Specification<Cookie> createSpecification(Map searchMap) {

        return new Specification<Cookie>() {

            @Override
            public Predicate toPredicate(Root<Cookie> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                // 
                if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
                    predicateList.add(cb.like(root.get("id").as(String.class), "%" + (String) searchMap.get("id") + "%"));
                }
                // 
                if (searchMap.get("summary") != null && !"".equals(searchMap.get("summary"))) {
                    predicateList.add(cb.like(root.get("summary").as(String.class), "%" + (String) searchMap.get("summary") + "%"));
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

}
