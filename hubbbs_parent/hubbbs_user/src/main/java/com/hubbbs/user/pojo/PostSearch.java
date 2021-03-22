package com.hubbbs.user.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import javax.persistence.Id;

/**
 * Created by IntelliJ IDEA.
 * User: HB
 * Date: 2019/4/3
 *
 * @author HB
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("unused")
@Accessors(chain = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "postindex", type = "postsearch")
public class PostSearch {
    @Id
    private String id;
    @Field(index = true, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String title;

    @Field(index = true, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String body;

    private Integer isban;
}
