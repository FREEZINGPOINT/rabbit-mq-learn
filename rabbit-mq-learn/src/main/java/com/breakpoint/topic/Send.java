package com.breakpoint.topic;

import com.breakpoint.WorkQueueTest;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * 主题模式
 * 
 * @author breakpoint
 *
 */
public class Send {

	private final static String EXCHANGE_NAME = "test_topic_exchange";

	public static void main(String[] args) throws Exception {
		// 获取到连结
		Connection connection = WorkQueueTest.getConnection();
		// 获取到 channel
		Channel channel = connection.createChannel();

		channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

		// 消息内容
		String message = "这是一只行动迅速的橙色的兔子";
		// 发送消息，并且指定routing key为：quick.orange.rabbit
		channel.basicPublish(EXCHANGE_NAME, "quick.orange.rabbit", null, message.getBytes());
		System.out.println(" [动物描述：] Sent '" + message + "'");

		channel.close();
		connection.close();

	}

}
