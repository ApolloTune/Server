package com.apollotune.server;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.output.Response;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}
	@Bean
	public CommandLineRunner commandLineRunner(){
		return runner -> {
			// langChainTest();
		};
	}
	/*
	private void langChainTest(){
		PromptTemplate promptTemplate = PromptTemplate
				.from("Tell me {{adjective}} joke about {{content}}..");
		Map<String, Object> variables = new HashMap<>();
		variables.put("adjective","funny");
		variables.put("content","computers");
		Prompt prompt = promptTemplate.apply(variables);
		ChatLanguageModel model;

	}
	*/

}
