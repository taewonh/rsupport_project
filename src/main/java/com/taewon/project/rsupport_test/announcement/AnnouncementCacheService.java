package com.taewon.project.rsupport_test.announcement;

import com.taewon.project.rsupport_test.announcement.AnnouncementConverter.PFN;
import com.taewon.project.rsupport_test.announcement.dto.AnnouncementListRequest;
import com.taewon.project.rsupport_test.common.lucene.LuceneUpdatableIndex;
import com.taewon.project.rsupport_test.common.lucene.SearchCacheResult;
import com.taewon.project.rsupport_test.common.util.DateUtil;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AnnouncementCacheService {

    private final AnnouncementRepository announcementRepository;

    private LuceneUpdatableIndex announcementIndex;

    public AnnouncementCacheService(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }

    @PostConstruct
    void init() {
        createAnnouncementsIndex();
    }

    private void createAnnouncementsIndex() {

        List<Announcement> announcements = announcementRepository.findAll();
        announcementIndex = new LuceneUpdatableIndex();

        LocalDateTime now = LocalDateTime.now();
        announcements.forEach(announcement -> {
            Document document = AnnouncementConverter.toDocument(announcement, now);
            try {
                announcementIndex.write(document);
            } catch (IOException e) {
                log.error("Failed announcement document write to index.", e);
                throw new RuntimeException(e);
            }
        });
    }

    public SearchCacheResult search(AnnouncementListRequest request) {

        try {
            SearcherManager searcherManager = announcementIndex.getSearcherManager();
            IndexSearcher searcher = searcherManager.acquire();

            Query query = toAnnouncementSearchQuery(request);
            Sort sort = new Sort(new SortField(PFN.updated_at_sort, SortField.Type.LONG, true));
            TopDocs topDocs = searcher.search(query, Integer.MAX_VALUE, sort);

            if (topDocs.scoreDocs == null || topDocs.scoreDocs.length == 0) {
                return SearchCacheResult.builder()
                        .totalCount(0)
                        .build();
            }

            return toSearchCacheResult(request, searcher, query, topDocs);
        } catch (Exception e) {
            log.error("Failed to search announcement list.", e);
            throw new RuntimeException(e);
        }
    }

    private Query toAnnouncementSearchQuery(AnnouncementListRequest request) throws ParseException {

        BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder()
                .add(new TermQuery(new Term(PFN.deleted, "F")), BooleanClause.Occur.MUST);

        Long startMillis = DateUtil.toEpochMillis(request.getStart_date());
        Long endMillis = DateUtil.toEpochMillis(request.getEnd_date());
        if (startMillis != null && endMillis != null) {
            Query createdAtRange = LongPoint.newRangeQuery(PFN.created_at_search, startMillis, endMillis);
            queryBuilder.add(createdAtRange, BooleanClause.Occur.MUST);
        }

        if (StringUtils.isNotEmpty(request.getKeyword())) {

            // 제목에 키워드 포함 여부
            QueryParser titleParser = new QueryParser(PFN.title, announcementIndex.getAnalyzer());
            Query titleQuery = titleParser.parse(request.getKeyword());

            // 공개된(description) 필드에서 키워드 포함 여부
            BooleanQuery.Builder descriptionQueryBuilder = new BooleanQuery.Builder()
                    .add(new TermQuery(new Term(AnnouncementConverter.PFN.expose, "T")), BooleanClause.Occur.MUST);
            QueryParser descriptionParser = new QueryParser(AnnouncementConverter.PFN.description, announcementIndex.getAnalyzer());
            Query descriptionContentQuery = descriptionParser.parse(request.getKeyword());
            descriptionQueryBuilder.add(descriptionContentQuery, BooleanClause.Occur.MUST);
            Query descriptionQuery = descriptionQueryBuilder.build();

            // title 또는 description 중 하나는 반드시 포함되도록 묶음
            BooleanQuery.Builder mustContainKeywordQuery = new BooleanQuery.Builder();
            mustContainKeywordQuery.add(titleQuery, BooleanClause.Occur.SHOULD);
            mustContainKeywordQuery.add(descriptionQuery, BooleanClause.Occur.SHOULD);

            // 최소 하나는 반드시 포함되도록 MUST
            queryBuilder.add(mustContainKeywordQuery.build(), BooleanClause.Occur.MUST);
        }

        return queryBuilder.build();
    }

    private SearchCacheResult toSearchCacheResult(AnnouncementListRequest request, IndexSearcher searcher, Query query, TopDocs topDocs) throws IOException {

        int totalCount = searcher.count(query);
        int start = request.getOffset() * request.getCount();
        int end = (int) Math.min(topDocs.totalHits.value, start + request.getCount());

        List<Document> documents = new ArrayList<>();
        for (int index = start; index < end; index++) {
            documents.add(searcher.doc(topDocs.scoreDocs[index].doc));
        }

        return SearchCacheResult.builder()
                .documents(documents)
                .totalCount(totalCount)
                .build();
    }

    public void writeToIndex(Announcement announcement) {

        Document doc = AnnouncementConverter.toDocument(announcement, LocalDateTime.now());
        try {
            announcementIndex.write(doc);
        } catch (IOException e) {
            log.error("Failed to write document to index.", e);
            throw new RuntimeException(e);
        }
    }
}
