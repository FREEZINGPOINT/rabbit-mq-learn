package com.breakpoint.ps;

import com.breakpoint.WorkQueueTest;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * 消息 的生产者
 * 
 * 业务场景：  发布订阅的模式  一个服务 的信息 有可能有双重的用途 
 * 这个时候，我们可以采用消息队列 分别发给不同的消费者 也就是 广播的模式
 * 
 * @author breakpoint
 *
 */
public class Provider {

	// fanout 为发布订阅的模式 广播的模式
	private final static String EXCHANGE_NAME = "test_fanout_exchange";

	public static void main(String[] args) throws Exception {

		// 获取到操作的链接
		Connection connection = WorkQueueTest.getConnection();

		// 创建通道
		Channel channel = connection.createChannel();
		// 声明exchange，指定类型为fanout
		channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

		// 消息内容
		String message = "注册成功！！";
		// 发布消息到Exchange
		channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
		System.out.println(" [生产者] Sent '" + message + "'");
		// 关闭连结
		channel.close();
		connection.close();
	}

}
