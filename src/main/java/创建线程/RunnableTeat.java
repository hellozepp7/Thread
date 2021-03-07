package 创建线程;

/**
 * <pre>
 * Description:
 *      创建多线程之实现Runnable接口，重写该类的run()方法
 * @author Zepp Deng
 * @date 2021/3/6
 * </pre>
 */
public class RunnableTeat {

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            System.out.println(Thread.currentThread().getName() + " " + i);
            if (i == 30) {
                // 创建一个Runnable实现类的对象
                Runnable myRunnable = new MyRunnable();
                // 将myRunnable作为Thread的target创建新的线程
                //该Thread对象才是真正的线程对象
                Thread thread1 = new Thread(myRunnable);
                Thread thread2 = new Thread(myRunnable);
                // 调用start()方法使得线程进入就绪状态
                thread1.start();
                thread2.start();
            }
        }
    }

    static class MyRunnable implements Runnable {
        @Override
        public void run() {
            int i = 0;
            for (i = 0; i < 100; i++) {
                System.out.println(Thread.currentThread().getName() + " " + i);
            }
        }
    }
}
