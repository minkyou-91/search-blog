package com.searchblog.api.adpater.out.persistence;

import com.searchblog.global.entity.BaseDateTimeEntity;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table
@Entity
@Getter
public class QueryEntity extends BaseDateTimeEntity {
    @Id
    private Long id;
    private String queryText;
    private Long searchCnt;
}
