package com.hubbbs.user.service;

import com.hubbbs.user.dao.AccountDao;
import com.hubbbs.user.dao.UserDao;
import com.hubbbs.user.pojo.Account;
import com.hubbbs.user.pojo.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 服务层
 *
 * @author Administrator
 */
@Service
@Transactional
public class AccountService {

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private Environment environment;

    /**
     * 查询全部列表
     *
     * @return
     */
    public List<Account> findAll() {
        return accountDao.findAll();
    }


    /**
     * 条件查询+分页
     *
     * @param whereMap
     * @param page
     * @param size
     * @return
     */
    public Page<Account> findSearch(Map whereMap, int page, int size) {
        Specification<Account> specification = createSpecification(whereMap);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return accountDao.findAll(specification, pageRequest);
    }


    /**
     * 条件查询
     *
     * @param whereMap
     * @return
     */
    public List<Account> findSearch(Map whereMap) {
        Specification<Account> specification = createSpecification(whereMap);
        return accountDao.findAll(specification);
    }

    /**
     * 根据ID查询实体
     *
     * @param id
     * @return
     */
    public Account findById(String id) {
        return accountDao.findById(id).get();
    }

    /**
     * 增加
     *
     * @param account
     */
    public void add(Account account) {
        String now = "" + Instant.now().toEpochMilli();
        String userId = idWorker.nextId() + "";
        account.setId(idWorker.nextId() + "")
                .setPassword(encoder.encode(account.getPassword()))
                .setRole("user")
                .setUserId(userId)
                .setIsBan(0);
        accountDao.save(account);
        User user = new User();
        user.setId(userId)
                .setAttenterNum(0)
                .setCollege(environment.getProperty("pojo.user.college"))
                .setCookieNum(0.0)
                .setCreateTime(now)
                .setSummary(environment.getProperty("pojo.user.Summary"))
                .setFansNum(0)
                .setGrade(0)
                .setIcon(environment.getProperty("pojo.user.icon"))
                .setIsBan(0)
                .setLastLoginTime(now)
                .setLastLoginTimeB(now)
                .setMajor(environment.getProperty("pojo.user.major"))
                .setNickname(environment.getProperty("pojo.user.nickname"))
                .setPostNum(0)
                .setSex(0)
                .setLastRenameTime(now);
        userDao.save(user);
    }

    /**
     * 修改
     *
     * @param account
     */
    public void update(Account account) {
        if (account.getPassword() != null) {
            account.setPassword(encoder.encode(account.getPassword()));
        }
        accountDao.save(account);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void deleteById(String id) {
        accountDao.deleteById(id);
    }

    /**
     * 动态条件构建
     *
     * @param searchMap
     * @return
     */
    private Specification<Account> createSpecification(Map searchMap) {

        return new Specification<Account>() {

            @Override
            public Predicate toPredicate(Root<Account> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                //
                if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
                    predicateList.add(cb.like(root.get("id").as(String.class), "%" + (String) searchMap.get("id") + "%"));
                }
                //
                if (searchMap.get("username") != null && !"".equals(searchMap.get("username"))) {
                    predicateList.add(cb.like(root.get("username").as(String.class), "%" + (String) searchMap.get("username") + "%"));
                }
                //
                if (searchMap.get("password") != null && !"".equals(searchMap.get("password"))) {
                    predicateList.add(cb.like(root.get("password").as(String.class), "%" + (String) searchMap.get("password") + "%"));
                }
                //
                if (searchMap.get("role") != null && !"".equals(searchMap.get("role"))) {
                    predicateList.add(cb.like(root.get("role").as(String.class), "%" + (String) searchMap.get("role") + "%"));
                }
                //
                if (searchMap.get("userId") != null && !"".equals(searchMap.get("userId"))) {
                    predicateList.add(cb.like(root.get("userId").as(String.class), "%" + (String) searchMap.get("userId") + "%"));
                }

                return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));

            }
        };

    }

    public void sendSms(String mobile) {
        //只有缓存中没有的时候才生成
        String checkCode = redisTemplate.opsForValue().get("check_" + mobile);
        if (checkCode == null) {
            //1.生成六位随机数
            checkCode = RandomStringUtils.randomNumeric(6);
            //2.缓存一份
            redisTemplate.opsForValue().set("check_" + mobile, checkCode, 5, TimeUnit.MINUTES);
        }
        //3.用户一份
        HashMap<String, String> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("checkCode", checkCode);
        rabbitTemplate.convertAndSend("sms", map);
        //4.输出一份
        System.out.println("checkCode===" + checkCode);
    }

    public Account login(Account account) {
        Account accountMobile = accountDao.findByUsername(account.getUsername());
        System.out.println(encoder.encode(account.getPassword()));
        if (accountMobile != null && encoder.matches(account.getPassword(), accountMobile.getPassword())) {
            return accountMobile;
        }
        return null;
    }

    public Account findByIdForChange(String userId) {
        return accountDao.findByUserId(userId);
    }

    public Boolean updatePassword(String accountUsername, Map<String, String> map) {
        Account account = accountDao.findByUsername(accountUsername);
        if (account != null && encoder.matches(map.get("accountFormOldPassword"), account.getPassword())) {
            account.setPassword(map.get("accountFormNewPassword"));
            update(account);
            return true;
        } else {
            return false;
        }
    }

    public Boolean updateUsername(String oldUsername, String newUsername) {
        Account account = accountDao.findByUsername(oldUsername);
        account.setUsername(newUsername);
        accountDao.save(account);
        return true;
    }

    public Account findByUsername(String username) {
        return accountDao.findByUsername(username);
    }
}
