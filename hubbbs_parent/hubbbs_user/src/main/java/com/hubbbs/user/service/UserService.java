package com.hubbbs.user.service;

import com.hubbbs.user.dao.CookieDao;
import com.hubbbs.user.dao.UserDao;
import com.hubbbs.user.pojo.Cookie;
import com.hubbbs.user.pojo.User;
import com.hubbbs.user.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
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
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private CookieDao cookieDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 查询全部列表
     *
     * @return
     */
    public List<User> findAll() {
        return userDao.findAll();
    }


    /**
     * 条件查询+分页
     *
     * @param whereMap
     * @param page
     * @param size
     * @return
     */
    public Page<User> findSearch(Map whereMap, int page, int size) {
        Specification<User> specification = createSpecification(whereMap);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return userDao.findAll(specification, pageRequest);
    }


    /**
     * 条件查询
     *
     * @param whereMap
     * @return
     */
    public List<User> findSearch(Map whereMap) {
        Specification<User> specification = createSpecification(whereMap);
        return userDao.findAll(specification);
    }

    /**
     * 根据ID查询实体
     *
     * @param id
     * @return
     */
    public User findById(String id) {
        return userDao.findById(id).get();
    }

    /**
     * 增加
     *
     * @param user
     */
    public void add(User user) {
        user.setId(idWorker.nextId() + "");
        userDao.save(user);
    }

    /**
     * 修改
     *
     * @param user
     */
    public void update(User user) {
        userDao.save(user);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void deleteById(String id) {
        userDao.deleteById(id);
    }

    /**
     * 动态条件构建
     *
     * @param searchMap
     * @return
     */
    private Specification<User> createSpecification(Map searchMap) {

        return new Specification<User>() {

            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                // 
                if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
                    predicateList.add(cb.like(root.get("id").as(String.class), "%" + (String) searchMap.get("id") + "%"));
                }
                // 
                if (searchMap.get("nickname") != null && !"".equals(searchMap.get("nickname"))) {
                    predicateList.add(cb.like(root.get("nickname").as(String.class), "%" + (String) searchMap.get("nickname") + "%"));
                }
                // 
                if (searchMap.get("sex") != null && !"".equals(searchMap.get("sex"))) {
                    predicateList.add(cb.like(root.get("sex").as(String.class), "%" + (String) searchMap.get("sex") + "%"));
                }
                //
                if (searchMap.get("grade") != null && !"".equals(searchMap.get("grade"))) {
                    predicateList.add(cb.like(root.get("grade").as(String.class), "%" + (String) searchMap.get("grade") + "%"));
                }
                //
                if (searchMap.get("createTime") != null && !"".equals(searchMap.get("createTime"))) {
                    predicateList.add(cb.like(root.get("createTime").as(String.class), "%" + (String) searchMap.get("createTime") + "%"));
                }
                // 
                if (searchMap.get("lastLoginTime") != null && !"".equals(searchMap.get("lastLoginTime"))) {
                    predicateList.add(cb.like(root.get("lastLoginTime").as(String.class), "%" + (String) searchMap.get("lastLoginTime") + "%"));
                }
                // 
                if (searchMap.get("lastLoginTimeB") != null && !"".equals(searchMap.get("lastLoginTimeB"))) {
                    predicateList.add(cb.like(root.get("lastLoginTimeB").as(String.class), "%" + (String) searchMap.get("lastLoginTimeB") + "%"));
                }
                // 
                if (searchMap.get("lastRenameTime") != null && !"".equals(searchMap.get("lastRenameTime"))) {
                    predicateList.add(cb.like(root.get("lastRenameTime").as(String.class), "%" + (String) searchMap.get("lastRenameTime") + "%"));
                }
                // 
                if (searchMap.get("icon") != null && !"".equals(searchMap.get("icon"))) {
                    predicateList.add(cb.like(root.get("icon").as(String.class), "%" + (String) searchMap.get("icon") + "%"));
                }
                // 
                if (searchMap.get("college") != null && !"".equals(searchMap.get("college"))) {
                    predicateList.add(cb.like(root.get("college").as(String.class), "%" + (String) searchMap.get("college") + "%"));
                }
                // 
                if (searchMap.get("major") != null && !"".equals(searchMap.get("major"))) {
                    predicateList.add(cb.like(root.get("major").as(String.class), "%" + (String) searchMap.get("major") + "%"));
                }
                // 
                if (searchMap.get("summary") != null && !"".equals(searchMap.get("summary"))) {
                    predicateList.add(cb.like(root.get("summary").as(String.class), "%" + (String) searchMap.get("summary") + "%"));
                }
                //
                if (searchMap.get("isBan") != null && !"".equals(searchMap.get("isBan"))) {
                    predicateList.add(cb.like(root.get("isBan").as(String.class), "%" + (String) searchMap.get("isBan") + "%"));
                }
                //
                if (searchMap.get("cookieNum") != null && !"".equals(searchMap.get("cookieNum"))) {
                    predicateList.add(cb.like(root.get("cookieNum").as(String.class), "%" + (String) searchMap.get("cookieNum") + "%"));
                }
                //
                if (searchMap.get("postNum") != null && !"".equals(searchMap.get("postNum"))) {
                    predicateList.add(cb.like(root.get("postNum").as(String.class), "%" + (String) searchMap.get("postNum") + "%"));
                }
                //
                if (searchMap.get("fansNum") != null && !"".equals(searchMap.get("fansNum"))) {
                    predicateList.add(cb.like(root.get("fansNum").as(String.class), "%" + (String) searchMap.get("fansNum") + "%"));
                }
                //
                if (searchMap.get("attenterNum") != null && !"".equals(searchMap.get("attenterNum"))) {
                    predicateList.add(cb.like(root.get("attenterNum").as(String.class), "%" + (String) searchMap.get("attenterNum") + "%"));
                }

                return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));

            }
        };

    }

    public Map<String, Object> findInfoById(String userId) {
        return userDao.findInfoById(userId);
    }

    public void loginRecord(User user) {
        // 更新最近登入时间
        user.setLastLoginTimeB(user.getLastLoginTime());
        user.setLastLoginTime("" + Instant.now().toEpochMilli());
        // 判断是否是当日签到
        if (StringUtils.isCheckInLogin(user.getLastLoginTime(), user.getLastLoginTimeB())) {
            user.setCookieNum(user.getCookieNum() + 1);
            user.setGrade((int) (user.getCookieNum() / 10));
            //顺带更新cookie表
            Cookie cookie = new Cookie()
                    .setId(idWorker.nextId()+"")
                    .setTime(StringUtils.toEpochMilli())
                    .setUserId(user.getId())
                    .setPostId("-1")
                    .setSummary("每日登录增加饼干")
                    .setVariance(1);
            cookieDao.save(cookie);
        }
        userDao.save(user);
    }
}
