package day3;

class Student{
    int rno;
    String name;
    String dept;
    public void display(){
        System.out.println("RNo: "+rno+"\nName: "+name+"\nDept: "+dept);
    }
}
public class Class {
    public static void main(String[] args) {
        Student s1=new Student();
        s1.name="Arsath";
        s1.rno=21;
        s1.dept="IT";
        s1.display();
        System.out.println("---------------------------------");
        Student s2=new Student();
        s2.name="Aathif";
        s2.rno=26;
        s2.dept="cse";
        s2.display();

    }
}