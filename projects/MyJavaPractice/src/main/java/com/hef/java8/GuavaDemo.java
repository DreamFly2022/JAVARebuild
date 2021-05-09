package com.hef.java8;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

/**
 * @Date 2021/5/9
 * @Author lifei
 */
public class GuavaDemo {

    /*
     1. 注册到 eventBus 的类， 会被guava搜索， 这个类里面有没有 添加了@Subscribe 注解的方法；
     2. 然后在任何地方，调用 eventBus.post(new AEvent()) 方法， 里面的事件，就会交给特定的 handler处理
     */

    static EventBus eventBus = new EventBus();
    static {
        eventBus.register(new GuavaDemo());
    }

    public static void main(String[] args) {

        eventBus.post(new AEvent());

    }


    public static class AEvent {
        String name = "aEvent";
    }

    @Subscribe
    public void handle(AEvent ae) {
        System.out.println("handle run: " + ae.name);
    }
}
