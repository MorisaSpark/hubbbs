package com.hubbbs.user.dao;

import com.hubbbs.user.pojo.RelTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 数据访问接口
 *
 * @author Administrator
 */
public interface RelTagDao extends JpaRepository<RelTag, String>, JpaSpecificationExecutor<RelTag> {
    public void deleteByPostIdAndSummary(@Param("postId") String postId, @Param("summary") String summary);

    public List<RelTag> findBySummaryIn(List summaries);

}
