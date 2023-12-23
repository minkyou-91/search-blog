package com.searchblog.global.factory;

import com.searchblog.api.application.port.out.external.ExternalPort;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ExternalPortFactory {
    @Getter private final Map<Integer, Object> externalPortMapper = new HashMap<>();

    public ExternalPortFactory(List<ExternalPort> externalPorts) {
        if (CollectionUtils.isEmpty(externalPorts)) {
            log.warn("ExternalPort Interface가 구현된 Bean이 없습니다.");
            return;
        }

        log.info("======== REGISTED EXTERNAL PORT ===========");
        for (ExternalPort extPort : externalPorts) {
            final Integer key = mapperKey(extPort.getPriority());
            this.externalPortMapper.put(key, extPort);

            log.info("key : {} , extPort : {} ", key, extPort);

        }
    }

    private Integer mapperKey(@NonNull Integer priority){
        return priority;
    }


}
