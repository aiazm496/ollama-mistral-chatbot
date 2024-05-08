package com.learngerman.helper.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Configuration
public class RagConfiguration {

    private static final Logger log = LoggerFactory.getLogger(RagConfiguration.class);

    @Value("vector-store.json")
    private String vectorStoreName;


    @Bean
    public SimpleVectorStore simpleVectorStore(EmbeddingClient embeddingClient){
        SimpleVectorStore simpleVectorStore = new SimpleVectorStore(embeddingClient);
        File vectorStoreFile = getVectorStoreFile();
        if(vectorStoreFile.exists()){
            simpleVectorStore.load(vectorStoreFile);
        }else {
            log.info("Vector Store File Does Not Exist, loading web page into the vector store");
            TikaDocumentReader reader = new TikaDocumentReader("https://www.space.com/20881-wormholes.html");
            List<Document> documents = reader.get();
            TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
            List<Document> splitDocuments = tokenTextSplitter.apply(documents);
            simpleVectorStore.add(splitDocuments);
            simpleVectorStore.save(vectorStoreFile);
        }
        return simpleVectorStore;
    }

    private File getVectorStoreFile() {
        Path path = Paths.get("src", "main", "resources", "data");
        String absolutePath = path.toFile().getAbsolutePath() + "/" + vectorStoreName;
        return new File(absolutePath);
    }
}
