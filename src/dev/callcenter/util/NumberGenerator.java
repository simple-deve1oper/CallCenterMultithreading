package dev.callcenter.util;

import java.util.Random;

/**
 * Класс для генерации чисел
 * @version 1.0
 */
public class NumberGenerator {
    /**
     * Метод для получения случайного числа в диапазоне
     * @param from - число "от"
     * @param to - число "до"
     * @return - случайное число
     */
    public static int getRandomNumberInRange(int from, int to) {
        final Random random = new Random();
        int randomNumber = random.nextInt(to - from) + from;
        return randomNumber;
    }
}
