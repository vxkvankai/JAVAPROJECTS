public class Contact {
    //private ArrayList<String> contactsList = new ArrayList<>();

    private String name;
    private String phoneNumber;

    public Contact(String name, String phoneNumber)
    {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public static Contact createContact(String name, String phoneNumber)
    {
        return new Contact(name, phoneNumber);

    }

    public String getName()
    {
        return name;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }


    //    public void addContact(String name, String phoneNumber)
//    {
//        contactsList.add();
//
//    }
//
//    public void printContacts()
//    {
//        System.out.println("You have " + contactsList.size() + " items in your grocery List");
//        for (int i = 0; i < contactsList.size(); i++)
//        {
//            System.out.println((i + 1) + ". " + contactsList.get(i));
//        }
//    }
//
//
//    public void modifyContact(int position, String newItem)
//    {
//        contactsList.set(position, newItem);
//        System.out.println("Grocery item " + (position + 1) + " has been modified. ");
//
//    }
//
//    public void removeContact(int position)
//    {
//        String theItem = contactsList.get(position);
//        contactsList.remove(position);
//
//    }
//
//    public String findContacts(String searchItem)
//    {
//        //boolean exists = groceryList.contains(searchItem);
//        int position = contactsList.indexOf(searchItem);
//        if (position >= 0)
//        {
//            return contactsList.get(position);
//
//        }
//        return null;
//    }
}
