package dev.callcenter.pool;

import dev.callcenter.entity.Client;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Класс-пул клиентов для работы с очередью
 * @param <T> - тип клиентов системы
 * @version 1.0
 */
public class ClientPool<T extends Client> {
    private BlockingQueue<T> blockingQueue; // блокирующая очередь

    /**
     * Конструктор для создания нового объекта типа ClientPool
     * @param poolSize - размер пула
     */
    public ClientPool(int poolSize) {
        blockingQueue = new LinkedBlockingQueue<>(poolSize);
    }

    /**
     * Метод для добавления клиента в очередь
     * @param client - клиент
     * @param seconds - количество секунд
     * @return - логический результат добавления клиента в очередь за запрашиваемое время
     * @throws InterruptedException - исключение потока исполнения
     */
    public boolean addInQueue(T client, int seconds) throws InterruptedException {
        boolean flag = blockingQueue.offer(client, seconds, TimeUnit.SECONDS);
        if (flag) {
            System.out.printf("%d %s добавлен в очередь%n", client.getId(), client.getName());
        } else {
            System.out.printf("%d %s не дождался ответа и перезвонит позже%n",
                    client.getId(), client.getName());
        }
        System.out.printf("Статус добавления %d %s в очередь: %b%n", client.getId(), client.getName(), flag);
        return flag;
    }
    /**
     * Метод для удаления клиента из очереди
     * @return - удаленного клиент из очереди
     * @throws InterruptedException
     */
    public T takeFromQueue() throws InterruptedException {
        T client = blockingQueue.take();
        System.out.printf("%d %s удалён из очереди%n", client.getId(), client.getName());
        return client;
    }
}
