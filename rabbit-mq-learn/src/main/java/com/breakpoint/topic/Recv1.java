package com.breakpoint.topic;

import java.io.IOException;

import com.breakpoint.WorkQueueTest;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Recv1 {

	private final static String QUEUE_NAME = "topic_exchange_queue_Q1";
	private final static String EXCHANGE_NAME = "test_topic_exchange";

	public static void main(String[] args) throws Exception {

		Connection connection = WorkQueueTest.getConnection();

		Channel channel = connection.createChannel();

		channel.queueDeclare(QUEUE_NAME, false, false, false, null);

		channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "*.orange.*");

		// 定义队列的消费者
		DefaultConsumer consumer = new DefaultConsumer(channel) {
			// 获取消息，并且处理，这个方法类似事件监听，如果有消息的时候，会被自动调用
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				// body 即消息体
				String msg = new String(body);
				System.out.println(" [消费者1] received : " + msg + "!");
			}
		};
		// 监听队列，自动ACK
		channel.basicConsume(QUEUE_NAME, true, consumer);

	}

}
