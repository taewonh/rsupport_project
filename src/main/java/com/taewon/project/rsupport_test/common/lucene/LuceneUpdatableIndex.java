package com.taewon.project.rsupport_test.common.lucene;

import lombok.Getter;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.ngram.NGramTokenFilter;
import org.apache.lucene.analysis.ngram.NGramTokenizer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.SearcherFactory;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;

import java.io.IOException;

@Getter
public class LuceneUpdatableIndex implements AutoCloseable {

    private Analyzer analyzer;

    private Directory directory = null;

    private IndexWriter indexWriter = null;

    private SearcherManager searcherManager;

    public LuceneUpdatableIndex() {

        analyzer = new Analyzer() {
            @Override
            protected TokenStreamComponents createComponents(String s) {
                NGramTokenizer tokenizer = new NGramTokenizer(1, 1);

                TokenStream filter = new LowerCaseFilter(tokenizer);
                filter = new NGramTokenFilter(filter, 1);
                return new TokenStreamComponents(tokenizer, filter);
            }
        };

        directory = new ByteBuffersDirectory();
    }

    private IndexWriter getIndexWriter() {
        if ( indexWriter == null ) {
            synchronized ( this ) {
                if ( indexWriter == null ) {
                    try {
                        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
                        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
                        indexWriter = new IndexWriter(directory, iwc);
                    }
                    catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return indexWriter;
    }

    public void write(Document doc) throws IOException {

        IndexWriter _writer = getIndexWriter();
        _writer.addDocument(doc);
        getSearcherManager().maybeRefresh();
    }

    public SearcherManager getSearcherManager() {
        if (searcherManager == null) {
            synchronized ( this ) {
                try {
                    if (indexWriter == null) {
                        searcherManager = new SearcherManager(directory, new SearcherFactory());
                    } else {
                        searcherManager = new SearcherManager(indexWriter, true, false, null);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return searcherManager;
    }

    @Override
    public void close() throws Exception {

        if (searcherManager != null) {
            searcherManager.close();
            searcherManager = null;
        }

        if (indexWriter != null) {
            indexWriter.close();
            indexWriter = null;
        }

        if (directory != null) {
            directory.close();
            directory = null;
        }

        if (analyzer != null) {
            analyzer.close();
            analyzer = null;
        }
    }
}