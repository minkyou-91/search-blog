package com.searchblog.api.adpater.out.persistence;

import com.searchblog.api.application.port.out.persistence.SaveQueryPort;
import com.searchblog.api.application.port.out.persistence.SearchPopQueryPort;
import com.searchblog.api.domain.Query;
import com.searchblog.global.enums.ErrorCode;
import com.searchblog.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class QueryPersistenceAdapter implements SaveQueryPort, SearchPopQueryPort {
    private final QueryRepository queryRepository;
    private final QueryMapper queryMapper;

    /**
     * queryText로 선 조회 하여 값이 있으면 +1 update , 없으면 new Insert
     * Entity 객체 상태 변경을 Entity에서 진행함.
     *
     * @param queryText
     */
    @Override
    @Transactional
    public void saveQuery(String queryText) {
        QueryEntity queryEntity = queryRepository.findByQueryText(queryText)
                .orElse((QueryEntity.builder().queryText(queryText).searchCount(0L).build()));
        queryEntity.updateSearchCount();
        queryRepository.save(queryEntity);
    }

    @Override
    public List<Query> searchPopQuery() {
        List<QueryEntity> queryEntityList = queryRepository.findTop10ByOrderBySearchCountDesc();

        if (CollectionUtils.isEmpty(queryEntityList)) {
            throw new CustomException(ErrorCode.HTTP_STATUS_404, "서버에서 요청받은 리소스를 찾을 수 없습니다.");
        }

        return queryEntityList.stream().map(queryEntity ->
                queryMapper.toDomain(
                        QueryEntity.builder()
                                .queryText(queryEntity.getQueryText())
                                .searchCount(queryEntity.getSearchCount())
                                .build()
                )).collect(Collectors.toList());
    }
}
