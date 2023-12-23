package com.searchblog.api.application.port.out.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@ToString
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoClientResponse extends ClientResponse {
    private List<Document> documents;
    private Meta meta;

    @Getter
    @ToString
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Document {
        private String blogname;
        private String contents;
        private LocalDateTime datetime;
        private String thumbnail;
        private String title;
        private String url;
    }

    @Getter
    @ToString
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Meta {
        @JsonProperty("total_count")
        private Integer totalCount;
        @JsonProperty("pageable_count")
        private Integer pageableCount;
        private Boolean isEnd;

    }
}
