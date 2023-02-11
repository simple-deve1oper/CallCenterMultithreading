package dev.callcenter.thread.daemon;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * Класс-поток для отслеживания рабочего дня
 * @version 1.0
 */
public class WorkingDay implements Runnable {
    private int time;   // время рабочего дня
    private TimeUnit unit;  // единица времени
    private boolean active; // статус активности потока

    /**
     * Конструктор для создания нового объекта типа WorkingDay
     * @param time - время рабочего дня
     * @param unit - единица времени
     */
    public WorkingDay(int time, TimeUnit unit) {
        this.time = time;
        this.unit = unit;
        this.active = false;
    }

    @Override
    public void run() {
        active = true;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        LocalDateTime dateTimeNow = LocalDateTime.now();
        String stringDateTimeFormatter = dateTimeNow.format(dateTimeFormatter);
        System.out.println("Начало рабочего дня: " + stringDateTimeFormatter);
        try {
            unit.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        dateTimeNow = LocalDateTime.now();
        stringDateTimeFormatter = dateTimeNow.format(dateTimeFormatter);
        System.out.println("Конец рабочего дня: " + stringDateTimeFormatter);
        active = false;
    }

    public int getTime() {
        return time;
    }
    public TimeUnit getUnit() {
        return unit;
    }
    public boolean isActive() {
        return active;
    }
}
