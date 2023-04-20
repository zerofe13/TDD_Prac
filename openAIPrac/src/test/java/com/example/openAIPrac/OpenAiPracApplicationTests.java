package com.example.openAIPrac;

import com.example.openAIPrac.service.ChatService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest
class OpenAiPracApplicationTests {

	@Autowired
	ChatService service;

	@Test
	void contextLoads() {

	}

	@Test
	void getAnswer(){
		String temp = "hi gpt";
		String gptAnswer = service.getGptAnswer(temp);
		Assertions.assertThat(gptAnswer).isNotEmpty();
		System.out.println("gptAnswer = " + gptAnswer);
	}

}
