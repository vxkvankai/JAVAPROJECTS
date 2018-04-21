/**
 *   vamsi
 *   @author vamsik
 */


public class Person {

    private static int personCount = 1000;

    /**
     *
     */
    static {
        personCount = 20;
    }

    static {
        personCount = 40;
    }

    {
        personCount = 10;
    }

    {
        personCount = 30;
    }

    public static void main(String[] args) {
        System.out.println(Person.personCount);
        Person p = new Person();
        System.out.println(Person.personCount);
        Person.personCount = 100;
        p = new Person();
        System.out.println(Person.personCount);

        // customize frame: newColor = "blue"   newType = "convertible"
    }
}
