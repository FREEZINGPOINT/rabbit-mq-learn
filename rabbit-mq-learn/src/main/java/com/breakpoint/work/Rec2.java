package com.breakpoint.work;

import java.io.IOException;

import com.breakpoint.WorkQueueTest;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * 第一个消费者
 * 
 * @author breakpoint
 *
 */
public class Rec2 {

	public static void main(String[] args) throws Exception {
		// 获取到连接以及mq通道
		Connection connection = WorkQueueTest.getConnection();
		Channel channel = connection.createChannel();

		// 声明队列
		channel.queueDeclare(WorkQueueTest.QUEUE_NAME, false, false, false, null);

		// 同一时刻服务器只会发一条消息给消费者
		 channel.basicQos(1);
		// 定义队列的消费者
		DefaultConsumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				// body 即消息体
				String msg = new String(body);
				System.out.println(" [Rec2] received : " + msg + "!");
				channel.basicAck(envelope.getDeliveryTag(), false);
			}
		};
		// 监听队列，false表示手动返回完成状态，true表示自动
		channel.basicConsume(WorkQueueTest.QUEUE_NAME, false, consumer);
	}

}
