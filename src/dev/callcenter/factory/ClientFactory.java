package dev.callcenter.factory;

import dev.callcenter.entity.Client;

import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Класс-фабрика для создания клиентов
 * @version 1.0
 */
public class ClientFactory {
    private Lock lock;  // лок для синхронизированного доступа к данным
    private static long countForId = 1; // счетчик для идентификатора клиента
    // список с именами для создания клиента
    private static List<String> names = List.of(
            "Алиса", "Геннадий", "Виктор", "Павел", "Ксения", "Александр", "Григорий", "Василиса", "Андрей",
            "Ян", "Юрий", "Мария", "Николай", "Тимур"
    );
    // список с вопросами для создания клиента
    private static List<String> questions = List.of(
            "Не загружается компьютер", "Не работает интернет", "Сколько стоит билет в кино",
            "Где найти самое вкусное мороженое", "Ближайшие концерты", "Выгодный тариф"
    );

    /**
     * Конструктор для создания нового объекта типа ClientFactory
     */
    public ClientFactory() {
        lock = new ReentrantLock();
    }

    /**
     * Метод для создания клиента
     * @return - созданный клиент
     */
    public Client createClient() {
        try {
            lock.lock();
            long id = getIdAndIncrement();
            String name = getRandomName();
            String question = getRandomQuestion();
            Client client = new Client(id, name, question);
            return client;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Метод для получения идентификатора и инкремента счётчика
     * @return - идентификатор
     */
    private long getIdAndIncrement() {
        long id = countForId++;
        return id;
    }
    /**
     * Метод для получения случайного имени из списка
     * @return - имя
     */
    private String getRandomName() {
        int index = getRandomIndex(names);
        String name = names.get(index);
        return name;
    }
    /**
     * Метод для получения случайного вопроса из списка
     * @return - вопрос
     */
    private String getRandomQuestion() {
        int index = getRandomIndex(questions);
        String question = questions.get(index);
        return question;
    }
    /**
     * Метод для получения случайного индекса элемента из доступных в списке
     * @param list - список строк
     * @return - индекс
     */
    private int getRandomIndex(List<String> list) {
        int size = list.size();
        final Random random = new Random();
        int index = random.nextInt(size);
        return index;
    }
}
