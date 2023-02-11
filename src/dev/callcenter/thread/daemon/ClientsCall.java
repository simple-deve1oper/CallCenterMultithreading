package dev.callcenter.thread.daemon;

import dev.callcenter.factory.ClientFactory;
import dev.callcenter.pool.ClientPool;
import dev.callcenter.util.NumberGenerator;
import dev.callcenter.entity.Client;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

/**
 * Класс-поток для добавления клиентов в пул клиентов в течение рабочего дня
 * @version 1.0
 */
public class ClientsCall implements Runnable {
    private ClientPool<Client> clientPool;  // пул клиентов
    private WorkingDay workingDay;  // рабочий день
    private Queue<Client> waitingQueue; // очередь из клиентов, которые не смогли дозвониться до операторов с первого раза

    /**
     * Конструктор для создания нового объекта типа ClientsCall
     * @param clientPool - пул клиентов
     * @param workingDay - рабочий день
     */
    public ClientsCall(ClientPool<Client> clientPool, WorkingDay workingDay) {
        this.clientPool = clientPool;
        this.workingDay = workingDay;
        this.waitingQueue = new LinkedList<>();
    }

    @Override
    public void run() {
        ClientFactory factory = new ClientFactory();
        int unsuccessful = 0;
        while (workingDay.isActive()) {
            Client client = factory.createClient();
            try {
                int seconds = NumberGenerator.getRandomNumberInRange(1, 2);
                TimeUnit.SECONDS.sleep(seconds);
                boolean resultAdding = clientPool.addInQueue(client, seconds);
                if (!resultAdding) {
                    waitingQueue.offer(client);
                } else {
                    if (!waitingQueue.isEmpty()) {
                        client = waitingQueue.remove();
                        //TimeUnit.SECONDS.sleep(seconds);
                        System.out.println(client + " пробует дозвониться ещё раз");
                        resultAdding = clientPool.addInQueue(client, seconds);
                        if (!resultAdding) {
                            System.out.println(client + ": не получилось дозвониться снова");
                            unsuccessful++;
                        }
                    }
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        unsuccessful += waitingQueue.size();
        System.out.println("Количество людей, которые не смогли дозвониться за рабочий день: " + unsuccessful);
    }
}
