package com.breakpoint.routing;

import com.breakpoint.WorkQueueTest;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * 路由模式
 * 
 * 可以根据 交换机 队列 以及 其他的方式 发送对应的信息
 * 
 * @author breakpoint
 *
 */
public class Send {

	private final static String EXCHANGE_NAME = "test_direct_exchange";

	public static void main(String[] args) throws Exception {

		Connection connection = WorkQueueTest.getConnection();
		
		Channel channel = connection.createChannel();
		
		// 不同的订阅类型
		channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
		
		// 消息内容，
        String message = "注册成功！请短信回复[T]退订";
        // 发送消息，并且指定routing key 为：sms，只有短信服务能接收到消息
        channel.basicPublish(EXCHANGE_NAME, "sms", null, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");
 
        channel.close();
        connection.close();	

	}

}
