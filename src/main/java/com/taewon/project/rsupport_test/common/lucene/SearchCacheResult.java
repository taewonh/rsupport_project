package com.taewon.project.rsupport_test.common.lucene;

import lombok.Builder;
import lombok.Getter;
import org.apache.lucene.document.Document;

import java.util.List;

@Builder
@Getter
public class SearchCacheResult {

    private Integer totalCount;

    private List<Document> documents;
}
