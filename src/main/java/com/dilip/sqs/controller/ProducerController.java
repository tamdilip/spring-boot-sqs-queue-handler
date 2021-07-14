package com.dilip.sqs.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dilip.sqs.dto.Message;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class ProducerController {

	private static final Logger logger = LoggerFactory.getLogger(ProducerController.class);

	@Autowired
	private QueueMessagingTemplate queueMessagingTemplate;

	@Value("${cloud.aws.end-point.uri}")
	private String endPoint;

	@PostMapping("/message")
	public Message sendMessage(@RequestBody Message message) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			String jsonString = mapper.writeValueAsString(message);
			queueMessagingTemplate.send(endPoint, MessageBuilder.withPayload(jsonString).build());
			logger.info("Message sent :: {} ", jsonString);
		} catch (Exception e) {
			logger.info("Error sending message :: {} ", e.getMessage());
		}
		return message;
	}

}
