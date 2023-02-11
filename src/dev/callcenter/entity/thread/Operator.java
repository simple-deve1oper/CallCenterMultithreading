package dev.callcenter.entity.thread;


import dev.callcenter.entity.Person;
import dev.callcenter.entity.component.Status;
import dev.callcenter.pool.ClientPool;
import dev.callcenter.thread.daemon.WorkingDay;
import dev.callcenter.util.NumberGenerator;
import dev.callcenter.entity.Client;

import java.util.concurrent.TimeUnit;

/**
 * Класс-сущность-поток для описания оператора и его работы
 * @version 1.0
 */
public class Operator extends Person implements Runnable {
    private Status status;  // статус
    private ClientPool clientPool;  // пул клиентов
    private WorkingDay workingDay;  // рабочий день

    /**
     * Конструктор для создания нового объекта типа Operator
     * @param id - идентификатор
     * @param name - имя
     */
    public Operator(long id, String name) {
        super(id, name);
        this.status = Status.OFFLINE;
    }

    /**
     * Метод для инициализации пула клиентов
     * @param clientPool - пул клиентов
     */
    public void initClientPool(ClientPool clientPool) {
        this.clientPool = clientPool;
    }
    /**
     * Метод для инициализации рабочего дня
     * @param workingDay - рабочий день
     */
    public void initWorkingDay(WorkingDay workingDay) {
        this.workingDay = workingDay;
    }

    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public void run() {
        try {
            status = Status.ONLINE;
            System.out.println("Рабочий день оператора начался: " + this);
            TimeUnit.SECONDS.sleep(2);
            while (workingDay.isActive()) {
                status = Status.CALL;
                Client client = clientPool.takeFromQueue();
                System.out.println(this + " - " + client + ": begin");
                int seconds = NumberGenerator.getRandomNumberInRange(1, 10);
                TimeUnit.SECONDS.sleep(seconds);
                System.out.println(this + " - " + client + ": end");
                status = Status.ONLINE;
            }
            TimeUnit.SECONDS.sleep(1);
            status = Status.OFFLINE;
            System.out.println("Рабочий день оператора окончен: " + this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object obj) {
        boolean flag = false;
        if (super.equals(obj)) {
            Operator operator = (Operator) obj;
            flag = (status == operator.status);
        }
        return flag;
    }

    @Override
    public int hashCode() {
        return super.hashCode() + (status == null ? 0 : status.hashCode());
    }

    @Override
    public String toString() {
        return super.toString().replace("}", ",status=" + status + "}");
    }
}
