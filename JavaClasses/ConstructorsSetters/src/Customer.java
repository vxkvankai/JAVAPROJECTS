public class Customer {

    private String name;
    private int ssn;

  public Customer(String name, int ssn){
        this.name = name;
        this.ssn = ssn;
  }

  public Customer(int ssn){
      this("John Doe",ssn); //default name is printed if only the SSN is provided

  }

  }








