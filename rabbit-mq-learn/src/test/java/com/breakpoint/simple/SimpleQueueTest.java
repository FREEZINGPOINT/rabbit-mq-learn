package com.breakpoint.simple;

import org.junit.Test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

/**
 * 简单队列的基本基本操作
 * 
 * 生产者 消费者
 * 
 * 简单队列的特点：
 *  只有一个生产者 以及 一个 消费者
 *  
 *  简单队列耦合性高，一个消费者对应于一个生产者，无法处理多个消费者同时调用消息队列的情况。
 *  要变换队列名称时，需要同时该表消费者和生产者中队列的名称，麻烦并且容易遗漏。
 * 	
 * 
 * @author breakpoint
 *
 */
public class SimpleQueueTest {

	// 队列的名称
	private final static String QUEUE_NAME = "simple_queue_test";

	public static Connection getConnection() throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("127.0.0.1");
		factory.setPort(5672);
		factory.setVirtualHost("/");
		factory.setUsername("admin");
		factory.setPassword("admin");
		Connection newConnection = factory.newConnection();
		return newConnection;
	}

	/**
	 * 消息的生产者
	 */
	@Test
	public void provider() throws Exception {

		// 获取到链接
		Connection newConnection = getConnection();
		// 从链接里面创建通道
		Channel channel = newConnection.createChannel();
		// 声明或者创建队列
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);

		String message = "你是谁";
		
		channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
		
		
		System.out.println(" [x] Sent '" + message + "'");

		// 关闭通道和连接
		channel.close();
		newConnection.close();

	}

	/**
	 * 消息的消费者
	 * 
	 */
	@Test
	public void consumer() throws Exception {
		// 获取到连接以及mq通道
		Connection connection = getConnection();
		// 从连接中创建通道
		Channel channel = connection.createChannel();
		// 声明队列
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);

		// 定义队列的消费者
		QueueingConsumer consumer = new QueueingConsumer(channel);

		// 监听队列
		channel.basicConsume(QUEUE_NAME, true, consumer);

		// 获取消息
		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String message = new String(delivery.getBody());
			System.out.println(" [x] Received '" + message + "'");
		}
	}

}
