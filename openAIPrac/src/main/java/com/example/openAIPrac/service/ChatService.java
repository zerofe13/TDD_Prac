package com.example.openAIPrac.service;

import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
public class ChatService {

    /*
      @Value 는 인젝션이 모두 마친후 불러오기때문에
      npe가 발생
     */
    @Value("${my_token}")
    private String MY_OPEN_AI_KEY;
    @PostConstruct
    public void init(){
        openAiService = new OpenAiService(MY_OPEN_AI_KEY);
    }
    private OpenAiService openAiService;
    private ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), "You are gpt with simple answer.");
    private ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(), "");

    public String getGptAnswer(String question){
        ArrayList<String> result = new ArrayList<>();

        ChatCompletionRequest chatCompletionRequest = createCompletionRequest(question);
        openAiService.streamChatCompletion(chatCompletionRequest)
                .doOnError(Throwable::printStackTrace)
                .blockingForEach(chunk -> {
                    result.add(chunk.getChoices().get(0).getMessage().getContent());
                } );

        return result.stream().filter(Objects::nonNull).collect(Collectors.joining(""));

    }

    public ChatCompletionRequest createCompletionRequest(String question){
        userMessage.setContent(question);
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(systemMessage);
        messages.add(userMessage);
        return ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(messages)
                .n(1)
                .maxTokens(1000)
                .build();
    }
}
