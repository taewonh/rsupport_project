package com.taewon.project.rsupport_test.announcement;

import com.taewon.project.rsupport_test.announcement.dto.AnnouncementListRequest;
import com.taewon.project.rsupport_test.common.lucene.LuceneUpdatableIndex;
import com.taewon.project.rsupport_test.common.lucene.SearchCacheResult;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
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

    public SearchCacheResult search(AnnouncementListRequest request) throws Exception {

        SearcherManager searcherManager = announcementIndex.getSearcherManager();
        IndexSearcher searcher = searcherManager.acquire();

        BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder()
                .add(new TermQuery(new Term(AnnouncementConverter.PFN.deleted, "F")), BooleanClause.Occur.MUST);

        Query query = queryBuilder.build();
        TopDocs topDocs = searcher.search(query, Integer.MAX_VALUE);

        if (topDocs.scoreDocs == null || topDocs.scoreDocs.length == 0) {
            return SearchCacheResult.builder()
                    .totalCount(0)
                    .build();
        }

        int totalCount = searcher.count(query);
        int start = request.getOffset() * request.getCount();
        int end = (int) Math.min(topDocs.totalHits.value, start + request.getCount());

        List<Document> documents = new ArrayList<>();
        for (int index = start ; index < end ; index++) {
            documents.add(searcher.doc(topDocs.scoreDocs[index].doc));
        }

        return SearchCacheResult.builder()
                .documents(documents)
                .totalCount(totalCount)
                .build();
    }
}
