package dev.callcenter.action.impl;

import dev.callcenter.entity.Client;
import dev.callcenter.entity.thread.Operator;
import dev.callcenter.pool.ClientPool;
import dev.callcenter.thread.daemon.ClientsCall;
import dev.callcenter.thread.daemon.WorkingDay;
import dev.callcenter.action.ActionThreads;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Класс для описания работы Call Center
 * @version 1.0
 */
public class CallCenter implements ActionThreads {
    private ExecutorService executorService;    // сервис задач
    private Set<Operator> operators;    // операторы
    private ClientPool<Client> clientPool;  // пул клиентов
    private ClientsCall clientsCall;    // задача добавления клиентов в очередь
    private WorkingDay workingDay;  // задача для отслеживания рабочего дня

    /**
     * Конструктор для создания нового объекта типа CallCenter
     * @param executorService   - сервис задач
     * @param operators - операторы
     * @param timeWorkingDay - время
     * @param unit - единица времени
     */
    public CallCenter(ExecutorService executorService, Set<Operator> operators, int timeWorkingDay, TimeUnit unit) {
        initGeneral(executorService, operators, timeWorkingDay, unit);
    }

    /**
     * Метод для инициализации всех объектов
     * @param executorService - сервис задач
     * @param operators - операторы
     * @param timeWorkingDay - время
     * @param unit - единица времени
     */
    private void initGeneral(ExecutorService executorService, Set<Operator> operators, int timeWorkingDay, TimeUnit unit) {
        this.executorService = executorService;
        this.operators = operators;
        int poolSize = operators.size();
        this.clientPool = new ClientPool(poolSize);
        this.workingDay = new WorkingDay(timeWorkingDay, unit);
        this.clientsCall = new ClientsCall(clientPool, workingDay);
        initClientPoolAndWorkingDayForOperators(this.operators, this.clientPool, this.workingDay);
    }
    /**
     * Метод для инициализации объектов операторов пулом клиентов и задачей отслеживания рабочего дня
     * @param operators - операторы
     * @param clientPool - пул клиентов
     * @param workingDay - задача для отследивания рабочего дня
     */
    private void initClientPoolAndWorkingDayForOperators(Set<Operator> operators, ClientPool<Client> clientPool,
                                                         WorkingDay workingDay) {
        for (Operator operator : operators) {
            operator.initClientPool(clientPool);
            operator.initWorkingDay(workingDay);
        }
    }

    /**
     * Метод для создания потока демона из задачи
     * @param task - задача
     * @return - поток-демон
     */
    private Thread getDaemonThread(Runnable task) {
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        return thread;
    }

    @Override
    public void start() {
        try {
            Thread threadWorkingDayDaemon = getDaemonThread(workingDay);
            executorService.submit(threadWorkingDayDaemon);
            Thread threadClientsCallDaemon = getDaemonThread(clientsCall);
            executorService.submit(threadClientsCallDaemon);
            TimeUnit.MILLISECONDS.timedJoin(threadClientsCallDaemon, 500);

            for (Operator operator : operators) {
                executorService.submit(operator);
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
