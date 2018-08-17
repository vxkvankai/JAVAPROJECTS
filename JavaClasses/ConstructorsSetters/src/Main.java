import java.util.Arrays;

public class Main {
    public static void main(String[] args)
    {

        // Instantiating an object of class LabCustomer and creating three customers objects
        Customer customer = new Customer("Juan",12345679);
        Customer customer1 = new Customer("ken",232454343);
        Customer customer2 = new Customer(1112223434);

        /**
         * Once you've instantiated an object and have an object variable,
         * you can use object variable to call an instance method.
         *  e.g.:
         *  object variables: cust1, cust2, cust3
         *  call the method toString() using the object variable and dot [.] in order to perform the method call.
         */
        // calling method toString() in class LabCustomer to print customer values

        System.out.println("Test" + customer);
        System.out.println(customer1.toString());
        System.out.println(customer2);

    }


}
