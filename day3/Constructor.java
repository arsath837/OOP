package day3;

class Students{
    String name;
    int rno;
    String dept;
    static String college;

    public Students(String name,int rno,String dept,String college){
        this.name=name;
        this.rno=rno;
        this.dept=dept;
        this.college=college;
    }
    //    No-args Constructor
    public Students(){

    }
    public void display(){
        System.out.println("Name: "+name+"\nRno: "+rno+"\nDept: "+dept+"\nCollege Name: "+college);
    }
}
public class Constructor {
    public static void main(String[] args) {
        Students.college="MSAJCE";
        Students s1=new Students("Arsath",21,"IT","MSAJCE");
        s1.display();
        System.out.println("--------------------------------------");
        Students s2=new Students();
        s2.name="Askar";
        s2.rno=76;
        s2.dept="EEE";
        s2.display();
        Students s3=new Students();
        s3.name="Aathif";
        s3.rno=14;
        s3.dept="CSE";
        s3.display();
        Students s4=new Students();
        s4.name="Rizwan";
        s4.rno=35;
        s4.dept="ECE";
        s4.display();


    }
}