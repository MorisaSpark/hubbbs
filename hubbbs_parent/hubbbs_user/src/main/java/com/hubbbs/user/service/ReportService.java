package com.hubbbs.user.service;

import com.hubbbs.user.dao.ReportDao;
import com.hubbbs.user.pojo.Report;
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
public class ReportService {

    @Autowired
    private ReportDao reportDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 查询全部列表
     *
     * @return
     */
    public List<Report> findAll() {
        return reportDao.findAll();
    }


    /**
     * 条件查询+分页
     *
     * @param whereMap
     * @param page
     * @param size
     * @return
     */
    public Page<Report> findSearch(Map whereMap, int page, int size) {
        Specification<Report> specification = createSpecification(whereMap);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return reportDao.findAll(specification, pageRequest);
    }


    /**
     * 条件查询
     *
     * @param whereMap
     * @return
     */
    public List<Report> findSearch(Map whereMap) {
        Specification<Report> specification = createSpecification(whereMap);
        return reportDao.findAll(specification);
    }

    /**
     * 根据ID查询实体
     *
     * @param id
     * @return
     */
    public Report findById(String id) {
        return reportDao.findById(id).get();
    }

    /**
     * 增加
     *
     * @param report
     */
    public void add(Report report) {
        report.setId(idWorker.nextId() + "")
                .setType("0")
                .setTime("" + Instant.now().toEpochMilli())
                .setDealerId("0");
        reportDao.save(report);
    }

    /**
     * 修改
     *
     * @param report
     */
    public void update(Report report) {
        reportDao.save(report);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void deleteById(String id) {
        reportDao.deleteById(id);
    }

    /**
     * 动态条件构建
     *
     * @param searchMap
     * @return
     */
    private Specification<Report> createSpecification(Map searchMap) {

        return new Specification<Report>() {

            @Override
            public Predicate toPredicate(Root<Report> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                // 
                if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
                    predicateList.add(cb.like(root.get("id").as(String.class), "%" + (String) searchMap.get("id") + "%"));
                }
                // 
                if (searchMap.get("thing") != null && !"".equals(searchMap.get("thing"))) {
                    predicateList.add(cb.like(root.get("thing").as(String.class), "%" + (String) searchMap.get("thing") + "%"));
                }
                // 
                if (searchMap.get("thingId") != null && !"".equals(searchMap.get("thingId"))) {
                    predicateList.add(cb.like(root.get("thingId").as(String.class), "%" + (String) searchMap.get("thingId") + "%"));
                }
                // 
                if (searchMap.get("reason") != null && !"".equals(searchMap.get("reason"))) {
                    predicateList.add(cb.like(root.get("reason").as(String.class), "%" + (String) searchMap.get("reason") + "%"));
                }
                // 
                if (searchMap.get("type") != null && !"".equals(searchMap.get("type"))) {
                    predicateList.add(cb.like(root.get("type").as(String.class), "%" + (String) searchMap.get("type") + "%"));
                }
                // 
                if (searchMap.get("time") != null && !"".equals(searchMap.get("time"))) {
                    predicateList.add(cb.like(root.get("time").as(String.class), "%" + (String) searchMap.get("time") + "%"));
                }
                // 
                if (searchMap.get("userId") != null && !"".equals(searchMap.get("userId"))) {
                    predicateList.add(cb.like(root.get("userId").as(String.class), "%" + (String) searchMap.get("userId") + "%"));
                }
                // 处理人
                if (searchMap.get("dealerId") != null && !"".equals(searchMap.get("dealerId"))) {
                    predicateList.add(cb.like(root.get("dealerId").as(String.class), "%" + (String) searchMap.get("dealerId") + "%"));
                }

                return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));

            }
        };

    }

}
