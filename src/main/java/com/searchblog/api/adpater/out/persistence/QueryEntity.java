package com.searchblog.api.adpater.out.persistence;

import com.searchblog.global.entity.BaseDateTimeEntity;
import lombok.*;
import org.springframework.data.domain.Persistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "POPULAR_QUERY")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class QueryEntity extends BaseDateTimeEntity implements Persistable {
    @Id
    @Column(name = "QUERY_TEXT")
    private String queryText;
    @Column(name = "SEARCH_COUNT")
    private Long searchCount;

    public void updateSearchCount() {
        this.searchCount++;
    }

    @Override
    public Object getId() {
        return queryText;
    }

    @Override
    public boolean isNew() {
        return true;
    }
}
