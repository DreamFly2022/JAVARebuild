# Java并发编程

## 多线程的基础知识

### 线程和进程的区别（面试）

- 1. 操作系统基本的运行单位是进程；
- 2. CPU调度运行的单位是线程
- 3. 一个进程里面可以有多个线程
- 4. 一个进程内的资源在多个线程中是可以共享的

	- 也就是说，一个进程中如果有一百个线程，这一百个线程可以访问相同的内存，共享相同的变量等信息。

- 5. 进程与进程之间的资源是相互隔离的。进程启动，进程再销毁，其占用的资源就销毁掉了。
- 注意：Linux中一切都是文件。在Linux内核中看，线程和进程是一个东西。

### 线程的创建过程

- Java怎么创建一个新的线程
- 守护线程

	- 后台线程，为前台提供服务
	- 如果JVM发现只有一个守护线程，就不会运行

### Thread的状态

- Thread状态的转化

	- 1. 运行Running、准备Runnable状态
	- 2.（主动）本线程主动操作

		- 等待：wait()、join()
		- 超时等待：wait(long timeout)、join(long timeout) 、sleep(long timeout)

	- 3. ( 被动)阻塞

- Thread状态改变操作

	- Thread.sleep(long millis)

		- 不会释放锁，会释放CPU
		- 作用：给其它线程执行机会的最佳方式

	- Thread.yeid()

		- 将线程从运行状态变为就绪状态

	- t.join()/t.join(long millis)

		- 线程执行完毕或者millis时间到，当前线程进入就绪状态

	- obj.wait()

		- 当前线程调用对象的wait()方法，当前线程释放对象锁，释放CPU。依靠notify() 或notifyAll() 唤醒，或者wait(long millis) 时间到自动唤醒

	- obj.notify()

		- 唤醒此对象监视器上等待的单个线程，选择是任意性的。obj.notifyAll()唤醒在此对象监视器上等待的所有线程。

- 示例：

	- 示例一：Interrupted()

		- 一个标识，用于在线程外，改变线程中使用的标识。
		- 这种机制现在很少使用，因为有更好的方案

	- 示例二 ：Thread

		- 线程的“优先级” 和“是否为守护线程”这两个属性是会被继承的，而且不可以被覆盖

	- 示例三：Join

		- thead.join()， 如果thread作为锁，当执行thread.join() 的时候，会释放thread锁

	- 示例四：wait-notify

		- 不在同步代码块下使用wait-notify时，程序会报错

### 多线程并发问题

- 竞争
- 并发相关性质

	- 原子性
	- 可见性

		- JVM的机制导致：一个变量在多线程中共享时，会被复制出一个副本，这样导致这个数如果被其它线程改变，当前线程获取不到最新的数据。
		- 解决：加volatile关键字

			- 效果：会有一个指令，让该线程看到这个值在祝内存中改变；
			- 强调：volatile关键字不能保证原子性

	- 有序性

		- Java允许编译器和处理器对指令进行重排，volatile关键字能保证一定“有序性“

- synchronized/volatile/final

	- synchronized实现机理：在对象头加标识字
	- volatile

		- 每次强调从主内存刷数据
		- 适合多线程写，多线程读
		- 不能保证原子性
		- 一定程度能够组织指令重排

	- final

		- 最好将方法的参数都设置为final

## 线程池

### 线程池原理与应用

- 线程池类模型

	- Excutor, 执行者（顶层接口）
	- ExcutorService 接口API
	- ThreadFactory  线程工厂
	- Excutors 工具类、创建线程（线程池工厂）

- 手动创建一个线程池

  public ThreadPoolExecutor initThreadPool(){
      int coreSize = Runtime.getRuntime().availableProcessors();
      int maxSize = Runtime.getRuntime().availableProcessors() * 2;
      BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(500);
      CustomThreadFactory customThreadFactory = new CustomThreadFactory();
      ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(coreSize, maxSize, 1,
              TimeUnit.MINUTES, queue, customThreadFactory, new ThreadPoolExecutor.AbortPolicy());
      return threadPoolExecutor;
  }
  
  class CustomThreadFactory implements ThreadFactory {
      private AtomicInteger seial = new AtomicInteger(0);
      @Override
      public Thread newThread(Runnable r) {
          Thread thread = new Thread(r);
          thread.setDaemon(false);
          thread.setName("CustomeThread-" + seial.getAndIncrement());
          return thread;
      }
  }

	- 线程池参数

		- corePoolSize  核心线程数

			- 可以设置为CPU核数：Runtime.getRuntime().availableProcessors()

		- maximumPoolSize 最大线程数

			- 可以设置为CPU核数的2倍：Runtime.getRuntime().availableProcessors() * 2

		- keepAliveTime、unit  存活时间和单位
		- workQueue 缓存队列

			- ArrayBlockingQueue

				- 先进先出，固定大小

			- LinkedBlockingQueue

				- 不指定大小

			- PriorityBlockingQueue

				- 优先级队列

			- SynchronousQueue

				- 放和取必须交替执行

		- threadFactory 线程工厂
		- RejectedExecutionHandler 拒绝策略

			- AbortPolicy（默认）

				- 丢弃任务并抛出异常

			- DiscardPolicy

				- 丢弃任务，不抛异常

			- DiscardOldestPolicy

				- 放弃队列最前面任务，然后重新提交被拒绝的任务

			- CallerRunsPolicy

				- 由调用线程（提交任务的线程）处理该任务

	- 线程池是怎么创建出来的

		- 1. 如果当前线程数小于核心线程数，创建一个线程，并标记为核心线程；
		- 2. 如果当前线程都在runnning， 并且大于核心线程数，先放到队列中缓存起来；
		- 3. 如果队列放满了，重新创建线程，标记为非核心线程；
		- 4. 如果达到最大线程，执行拒绝策略

- 使用JDK中Exectors提供的四种线程池

	- Executors.newSingleThreadExecutor();
	- Executors.newFixedThreadPool(nThreads);(坑：使用LinkedBlockQueue， 缓存队列无穷大，任务可以一直堆，最大线程数和核心线程数一样)
	- Executors.newCachedThreadPool();
（坑：线程数可以无穷大）
	- Executors.newScheduledThreadPool(10);

- 经验：常用固定线程池

	- 假设CPU核心数为N
对于CPU密集型，线程池大小设置为N或N+1；
对于IO密集型，线程池大小设置为2N或2N+2

### 线程池的代码示例

- 示例一：submit和execute

	- executorService.submit()
	和
	executorService.execute()

		- 调用submit，可以得到Future，在调用future.get(),   如果submit中抛出异常，异常能够在主线程捕获到

- 示例二：定时线程池

	- executorService.schedule(runnable, 10l, TimeUnit.SECONDS); // 10秒后再运行

- 示例三：单线程池

	- 多个任务能够顺序执行

- 示例四：固定大小到线程池
- 示例五：缓存线程池

	- 疯狂到创建线程

## 锁Lock

### synchronized与Lock的区别

- synchronized

	- 不够灵活，无法控制什么条件下锁，什么条件下解锁。

- lock

### Lock中的重要方法

- lock()

	- 获取锁，类比synchronized

- lockInterruptibly() 

	- 获取锁，允许打断

- tryLock()

	- 尝试获取锁，无等待

- unlock()

	- 解锁

- newCondition()

	- 新增绑定到当前Lock的条件

### Lock的使用

- 可重入锁

	- 可重入的公平锁：
  Lock lock = new ReentrantLock(true);

		- ReentrantLock 就是可重入锁。
  当前线程拿到锁，进入代码，执行过程中遇到同一把锁，能继续进去，还是锁在这里继续等待。
  可重入的意思是可以继续进去。
		- true， 就是公平锁。
  谁等的时间长，谁先获得锁
		- 非公平锁比公平锁都效率高

			- 公平锁要维护一个队列，后来的线程要加锁，即使锁空闲，也要先检查有没有其他线程在 wait，如果有自己要挂起，加到队列后面，然后唤醒队列最前面的线程。这种情况下相比较非公平锁多了一次挂起和唤醒
     线程切换的开销，其实就是非公平锁效率高于公平锁的原因，因为非公平锁减少了线程挂起的几率，后来的线程有一定几率逃离被挂起的开销

- 可重入的读写锁

	- // 可重入+读写锁+公平锁
ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

		- 读操作可以并发，写操作不能并发
		- 通过读写锁，可以对受保护的共享资源进行并发读取和独占写入。读写锁是可以在读取或写入模式下锁定的单一实体。要修改资源，线程必须首先获取互斥写锁。必须释放所有读锁之后，才允许使用互斥写锁。

	- 有明确的应用场景

- 优化锁的基础原理

	- 第一：减少每把锁锁的范围；锁粒度
	- 第二：锁分离，将锁应用在特定的场景下；

- 加锁的条件：
Condition condition = Lock.newCondition();

### lock的工具类LockSupport。
里面都是静态方法

- LockSupport.park(); 无限期的暂停当前线程
- LockSupport.unpark(thread); 唤醒暂停线程

### 用锁的最佳实践

- 1. 永远只在更新对象的成员变量时加锁；局部变量不需要枷锁
- 2. 永远只在访问可变的成员变量时加锁；
- 3.永远不在调用其它对象的方法时加锁。锁写在方法里，不对外暴露。

### LockSupport的代码示例

- thread.interrupt()也能够让ThreadSupport.park()的线程释放锁

### 加锁需要考虑的问题

- 粒度
- 性能
- 重入
- 公平
- 自旋锁
- 场景：脱离业务场景考虑性能都是耍流氓

## 并发原子类和并发工具类

### 并发原子类

- 为什么有并发原子类

	- 像计数这样的场景，多线程下，会不准确
	- java.util.concurrent.atomic

- 实现原理

	- 1. volatile in value

		- 这个value值，每次操作的时候都不会缓存，拿到的都是主内存中真实的值；可见行，并不能保证原子性

	- 2. Compare-And-Swap， CAS指令。JDK中C++的原生实现。不加锁，不断自旋（乐观锁），判断原来的值是否被修改了。

- LongAdder对AtomicLong的改进

	- 分段思想，多路归并：使用分段策略，有多少个线程，就让线程自己去加。最后将多个线程的结果汇总在一块。

- 加锁好还是不加锁好？

	- 并发第的情况下，无锁好；并发特别高的时候，加锁好。

### 并发工具类

- 为什么需要并发工具类

	- 线程间协作信号量
	- 更复杂的场景

		- 1. 我们需要控制实际并发访问资源的并发数量；
		- 2. 我们需要多个线程在某个时间同时开始运行；new 出多线程，希望这多个线程在某个时间点同时start；
		- 3. 例如，启动100个线程，执行任务，只要50个以上的线程完成，就可以进行下一步骤；

	- 为了达到这样的目录，Java并发包中设计了一个类：AQS，AbstractQueuedSynchronizer

- Semaphore , 信号量

	- 控制并发执行的数量
	- 1. 准入数量N；
2. N=1则等价于独占锁；
	- N=1时变成独占锁，因此Semaphore 可以看作是一个增强版的信号量或同步块
	- // 获取多个许可
	semaphore.acquire(3);
	// 释放多个许可
	semaphore.release(3);

- CountDownLatch

	- 可以看作对线程对计数器，当一个线程完成，做减法
	- 当前线程任务执行完之后，再调用countDown
	- CountDownLatch是如何用的

		- 1. 创建多线程，将CountDownLatch对象传递到线程中；
		- 2. 线程在执行完任务的时候，调用countDown() 方法
		- 3. 为了收集状态，在主线程调用await() 方法

- CyclicBarrier

	- 和CountDownLatch很像，不过是做加法，可以重复利用；

## future

### Future

- 做为线程池submit()  返回值

### FutureTask

- 第一种方式： 创建一个FutureTask，传递给new Thread(futureTask).start();
- 第二种方式：创建一个FutureTask，传递给线程池的submit(futureTask) 方法去执行

### CompletableFuture

- 有非常多的花样
- 任务链
- 消费
- 二种组合方式
- 竞争
- 补偿异常

## 集合类

### 集合类的体系结构

- List

	- ArrayList

		- 基于数组，便于按照index访问，超过数组需要扩容，扩容成本较高
		- 默认大小为10，扩容x1.5
		- 序列化的时候不会序列化底层的数组。
transient Object[] elementData;
		- 安全问题

			- 1. 写会发生冲突；
			- 2. 读写冲突；

	- LinkedList

		- 使用链表实现，无需扩容
		- 安全问题

			- 1. 写冲突
			- 2. 读写冲突

	- 解决List线程安全的问题

		- 1. 将ArrayList上所有方法加上synchronized，就是Vector
		- 2. 使用工具类Collections.synchronizedList(), 强制上List上的操作加同步；
		- 3. Arrays.asList(), 不允许添加删除，但允许set替换元素；
		- 4. Collections.unmodifiableList,  不允许修改内容，包括添加删除和set

	- Vector

		- ArrayList上所有方法添加synchronized，就是Vector
		- vector 中，读是线程安全的，写是线程安全的，但读和写加在一起就不是线程安全的了

	- Stack(类）

		- 父类是Vector

- Set

	- HashSet
	- LinkedHashSet
	- TreeSet

- Queue

	- Deque(接口)

		- ArrayDeque
		- LinkedList

- Map

	- HashMap

		- 原理

			- 空间换时间
			- 初始容量为16，扩容X2，负载因子0.75
			- 元素的个数/容量=负载
			- jdk8 优化，当链表长度为8并且数组长度为64时，使用红黑树

		- 安全问题

			- 1. 写冲突
			- 2. 读写问题，可能会死循环
			- 3. keys无序问题

	- LinkedHashMap

		- 原理

			- 继承HashMap
			- 加了一个双链表
			- 提供两种顺序：插入顺序和访问删除

		- 安全问题同HashMap

	- TreeMap

		- 按照元素本身的顺序

	- HashTable

		- 在JDK8 中，HashTable已经实现了Map接口

- Dictionary

	- HashTable

		- Properties

			- getProperty(String key) 是一个比较坑的方法：如果不是String类型，返回一个Null

### 并发包中的集合类

- CopyOnWriteArrayList

	- 核心原理

		- 1. 写一定加锁；
		- 2. 写在一个copy副本上，而不是在原始数据上；
		- 读操作不需要加锁

			- 读的是原始数据的副本快照

- ConcurrentHashMap

	- JDK7

		- 原理

			- 16个Segment， 数组，并发节点。每个segment是一个hashMap。降低锁粒度。和分库分表的原理一样

	- JDK8

		- 原理

			- 使用CAS， 直接使用一个大数组。使用红黑树

## 其它

### ThreadLocal

- 原理

	- 线程本地的变量
	- 可以看作Context模式，减少显示传递参数
	- 每个线程一个副本

- 关键

	- 及时进行清理

### Stream中的parallel

- 多线程执行，只需要加上parallel()即可

### 线程间的协作和共享

- 线程间共享

	- static/实例变量（堆内存）
	- Lock
	- synchronized

- 线程间的协作

	- Thread#join()
	- Object#wait(),notify(),notifyAll()
	- Future/Callable
	- CountDownLatch
	- CyclicBarrier

*XMind - Trial Version*