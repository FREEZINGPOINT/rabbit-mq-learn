package com.breakpoint;



import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 工作队列的模式测试
 * 
 * 
 * 
 * 一个生产者、2个消费者。
 * 
 * 一个消息只能被一个消费者获取。
 * 
 * 消费者1和消费者2获取到的消息内容是不同的，同一个消息只能被一个消费者获取。
 * 消费者1和消费者2获取到的消息的数量是相同的，一个是消费奇数号消息，一个是偶数。
 * 
 * 
 * 其实，这样是不合理的，因为消费者1线程停顿的时间短。应该是消费者1要比消费者2获取到的消息多才对。
RabbitMQ 默认将消息顺序发送给下一个消费者，这样，每个消费者会得到相同数量的消息。
即轮询（round-robin）分发消息。
怎样才能做到按照每个消费者的能力分配消息呢？
联合使用 Qos 和 Acknowledge 就可以做到。
basicQos 方法设置了当前信道最大预获取（prefetch）消息数量为1。消息从队列异步推送给消费者，消费者的 ack 也是异步发送给队列，
从队列的视角去看，总是会有一批消息已推送但尚未获得 ack 确认，Qos 的 prefetchCount 参数就是用来限制这批未确认消息数量的。
设为1时，队列只有在收到消费者发回的上一条消息 ack 确认后，才会向该消费者发送下一条消息。
prefetchCount 的默认值为0，即没有限制，队列会将所有消息尽快发给消费者。

轮询分发 ：使用任务队列的优点之一就是可以轻易的并行工作。如果我们积压了好多工作，我们可以通过增加工作者（消费者）来解决这一问题，使得系统的伸缩性更加容易。在默认情况下，RabbitMQ将逐个发送消息到在序列中的下一个消费者(而不考虑每个任务的时长等等，且是提前一次性分配，并非一个一个分配)。平均每个消费者获得相同数量的消息。这种方式分发消息机制称为Round-Robin（轮询）






 * 
 * @author breakpoint
 *
 */
public class WorkQueueTest {

	// 队列的名称
	public final static String QUEUE_NAME = "work_queue_test";

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
}
