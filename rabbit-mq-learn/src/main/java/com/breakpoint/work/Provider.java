package com.breakpoint.work;

import com.breakpoint.WorkQueueTest;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * 生产者
 * 
 * @author breakpoint
 *
 */
public class Provider {

	public static void main(String[] args) throws Exception {
		// 获取到连接以及mq通道
		Connection connection = WorkQueueTest.getConnection();
		Channel channel = connection.createChannel();

		// 声明队列
		channel.queueDeclare(WorkQueueTest.QUEUE_NAME, false, false, false, null);
		
		

		for (int i = 0; i < 100; i++) {
			// 消息内容
			String message = "" + i;
			channel.basicPublish("", WorkQueueTest.QUEUE_NAME, null, message.getBytes());
			System.out.println(" [x] Sent '" + message + "'");

			Thread.sleep(i * 10);
		}

		channel.close();
		connection.close();

	}

}
