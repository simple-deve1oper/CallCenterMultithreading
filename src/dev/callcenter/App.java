package dev.callcenter;

import dev.callcenter.action.ActionThreads;
import dev.callcenter.action.impl.CallCenter;
import dev.callcenter.entity.thread.Operator;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Класс для запуска приложения
 * @version 1.0
 */
public class App {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();

        Set<Operator> operators = Set.of(
                new Operator(1, "Иван Петров"),
                new Operator(2, "Мария Николаева")
        );
        int timeWorkingDay = 30;
        TimeUnit unit = TimeUnit.SECONDS;
        ActionThreads threads = new CallCenter(executorService, operators, timeWorkingDay, unit);
        threads.start();

        executorService.shutdown();
    }
}
