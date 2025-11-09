package model;

public class Student extends Person {
    private int age;

    public Student() {
        super();
    }

    public Student(int id, String name, int age) {
        super(id, name);
        this.age = age;
    }

    public int getAge() { return age; }

    public void setAge(int age) { this.age = age; }

    @Override
    public String toString() {
        return String.format("%d | %s | %d", getId(), getName(), age);
    }
}