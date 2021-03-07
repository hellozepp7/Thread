package 线程池;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * <pre>
 * Description:
 *          ThreadPoolTaskExecutor类中维护了一个ThreadPoolExecutor成员变量
 *          使用ThreadPoolTaskExecutor线程池时，我们需要将他交给spring进行管理
 *          在使用时直接注入到组件中使用即可，这样就保证不会滥用线程池
 * @author Zepp Deng
 * @date 2021/3/7
 * </pre>
 */
@Configuration
public class ThreadPoolTaskExecutorTest {

    //使用时直接使用@AutoWired注解注入使用
    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    void threadPoolTaskExecutor(){
        threadPoolTaskExecutor.execute(() -> System.out.println("执行了线程工作"));
    }

    /**
     * @Description:
     *          AbortPolicy：用于被拒绝任务的处理程序，它将抛出RejectedExecutionException
     *          CallerRunsPolicy：用于被拒绝任务的处理程序，它直接在execute方法的调用线程中运行被拒绝的任务。
     *          DiscardOldestPolicy：用于被拒绝任务的处理程序，它放弃最旧的未处理请求，然后重试execute。
     *          DiscardPolicy：用于被拒绝任务的处理程序，默认情况下它将丢弃被拒绝的任务。
     * @Author Zepp Deng [2021-03-07 17:47]
     * @Return ThreadPoolTaskExecutor
     */
    @Bean(name = "threadPoolTaskExecutor")
    ThreadPoolTaskExecutor getThreadPoolTaskExecutor(){
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        int i = Runtime.getRuntime().availableProcessors();//获取到服务器的cpu内核
        threadPoolTaskExecutor.setCorePoolSize(i);//核心池大小
        threadPoolTaskExecutor.setMaxPoolSize(10);//最大线程数
        threadPoolTaskExecutor.setQueueCapacity(200);//队列长度
        threadPoolTaskExecutor.setKeepAliveSeconds(1000);//线程空闲时间
        threadPoolTaskExecutor.setThreadNamePrefix("task-concurrent-work");//线程前缀名称
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());//配置拒绝策略
        // 这一步是不需要的，下面会讲解到
        return threadPoolTaskExecutor;
    }

}
