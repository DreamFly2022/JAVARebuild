package com.hef.java8;

/**
 * @Date 2021/5/9
 * @Author lifei
 */
public class LambdaDemo {

    public static void main(String[] args) {
        LambdaDemo lambdaDemo = new LambdaDemo();

        // 匿名函数
        MathOperation operation = new MathOperation() {

            @Override
            public int operation(int a, int b) {
                return a + b;
            }
        };

        // 使用lambda替换上面的表达式
        MathOperation operation1 = (int a, int b) -> {return a+b;};
        MathOperation addition = (a, b) -> a+b;
        MathOperation subtraction = (a, b) -> a - b;
        MathOperation multiplication = (a, b) -> a * b;
        MathOperation division = (a, b) -> a/b;


        System.out.println("10 + 5 = " + lambdaDemo.operation(10, 5, addition));
        System.out.println("10 - 5 = " + lambdaDemo.operation(10, 5, subtraction));
        System.out.println("10 * 5 = " + lambdaDemo.operation(10, 5, multiplication));
        System.out.println("10 / 5 = " + lambdaDemo.operation(10, 5, division));
        System.out.println("10 ^ 5 = " + lambdaDemo.operation(10, 5, (a, b) -> new Double(Math.pow(a, b)).intValue()));

        // 不用括号
        GreetingService greetingService = (mes)-> System.out.println(mes);
        // 方法引用
        GreetingService simple = System.out::println;
        greetingService.sayMessage("hello world");





    }

    interface MathOperation {
        int operation(int a, int b);
    }

    interface GreetingService {
        void sayMessage(String message);
    }

    private  int operation(int a, int b, MathOperation mathOperation) {
        return mathOperation.operation(a, b);
    }


}


