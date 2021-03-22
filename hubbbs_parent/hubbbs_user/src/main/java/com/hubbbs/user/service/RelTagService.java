package com.hubbbs.user.service;

import com.hubbbs.user.dao.RelTagDao;
import com.hubbbs.user.pojo.RelTag;
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
public class RelTagService {

    @Autowired
    private RelTagDao relTagDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 查询全部列表
     *
     * @return
     */
    public List<RelTag> findAll() {
        return relTagDao.findAll();
    }


    /**
     * 条件查询+分页
     *
     * @param whereMap
     * @param page
     * @param size
     * @return
     */
    public Page<RelTag> findSearch(Map whereMap, int page, int size) {
        Specification<RelTag> specification = createSpecification(whereMap);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return relTagDao.findAll(specification, pageRequest);
    }


    /**
     * 条件查询
     *
     * @param whereMap
     * @return
     */
    public List<RelTag> findSearch(Map whereMap) {
        Specification<RelTag> specification = createSpecification(whereMap);
        return relTagDao.findAll(specification);
    }

    /**
     * 根据ID查询实体
     *
     * @param id
     * @return
     */
    public RelTag findById(String id) {
        return relTagDao.findById(id).get();
    }

    /**
     * 增加
     *
     * @param reltag
     */
    public void add(RelTag reltag) {
        reltag.setId(idWorker.nextId() + "")
                .setIsBan(0);
        relTagDao.save(reltag);
    }

    public void deleteByPostIdAndSummary(String postId, String summary){
        relTagDao.deleteByPostIdAndSummary(postId, summary);
    }

    /**
     * 修改
     *
     * @param reltag
     */
    public void update(RelTag reltag) {
        relTagDao.save(reltag);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void deleteById(String id) {
        relTagDao.deleteById(id);
    }

    /**
     * 动态条件构建
     *
     * @param searchMap
     * @return
     */
    private Specification<RelTag> createSpecification(Map searchMap) {

        return new Specification<RelTag>() {

            @Override
            public Predicate toPredicate(Root<RelTag> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                // 
                if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
                    predicateList.add(cb.like(root.get("id").as(String.class), "%" + (String) searchMap.get("id") + "%"));
                }
                //
                if (searchMap.get("postId") != null && !"".equals(searchMap.get("postId"))) {
                    predicateList.add(cb.like(root.get("postId").as(String.class), "%" + (String) searchMap.get("postId") + "%"));
                }
                if (searchMap.get("summary") != null && !"".equals(searchMap.get("summary"))) {
                    predicateList.add(cb.like(root.get("summary").as(String.class), "%" + (String) searchMap.get("summary") + "%"));
                }

                return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));

            }
        };

    }

    public List<String> findSummaryByPostId(String postId){
        HashMap whereMap = new HashMap();
        whereMap.put("postId",postId);
        List<RelTag> search = findSearch(whereMap);
        ArrayList<String> summaries = new ArrayList<>();
        for (RelTag aSearch : search) {
            summaries.add(aSearch.getSummary());
        }

        return summaries;
    }
    public List<RelTag> findBySummaryIn(List<String> summries){
        return relTagDao.findBySummaryIn(summries);
    }
}
