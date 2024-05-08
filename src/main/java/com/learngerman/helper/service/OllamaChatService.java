package com.learngerman.helper.service;

import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OllamaChatService {

    private final OllamaChatClient ollamaChatClient;
    private final VectorStore simpleVectorStore;

    @Autowired
    public OllamaChatService(OllamaChatClient ollamaChatClient, VectorStore simpleVectorStore) {
        this.ollamaChatClient = ollamaChatClient;
        this.simpleVectorStore = simpleVectorStore;
    }

    @Value("classpath:/prompts/rag-prompt-template.st")
    private Resource ragPromptTemplate;


    public String call(String message){
        List<Document> similarDocuments = simpleVectorStore.similaritySearch(SearchRequest.query(message).withTopK(2));
        List<String> contentList = similarDocuments.stream().map(Document::getContent).toList();
        PromptTemplate promptTemplate = new PromptTemplate(ragPromptTemplate);
        Map<String, Object> promptParameters = new HashMap<>();
        promptParameters.put("input", message);
        promptParameters.put("documents", String.join("\n", contentList));
        Prompt prompt = promptTemplate.create(promptParameters);
        return ollamaChatClient.call(prompt).getResult().getOutput().getContent();
    }

    public String callWithoutContext(String message){
        return ollamaChatClient.call(message);
    }


}
