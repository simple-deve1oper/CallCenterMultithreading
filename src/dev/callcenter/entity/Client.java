package dev.callcenter.entity;

/**
 * Класс-сущность для описания клиента
 * @version 1.0
 */
public class Client extends Person {
    private String question;    // вопрос

    /**
     * Конструктор для создания нового объекта типа Client
     * @param id - идентификатор
     * @param name - имя
     * @param question - вопрос
     */
    public Client(long id, String name, String question) {
        super(id, name);
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }
    public void setQuestion(String question) {
        this.question = question;
    }

    @Override
    public boolean equals(Object obj) {
        boolean flag = false;
        if (super.equals(obj)) {
            Client client = (Client) obj;
            flag = ((this.question == client.question) || ((question != null) && question.equals(client.question)));
        }
        return flag;
    }

    @Override
    public int hashCode() {
        return super.hashCode() + (question == null ? 0 : question.hashCode());
    }

    @Override
    public String toString() {
        return super.toString().replace("}", ",question=" + question + "}");
    }
}
