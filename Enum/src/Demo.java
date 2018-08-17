public class Demo {


    public static void main(String[] args)
    {
        Learning learn = Learning.COREJAVA;

        switch (learn)
        {
            case COLLECTIONS:
                System.out.println("Step2: learning collecitons framework");
                break;
            case COREJAVA:
                System.out.println("learning corejava");
                break;
            case GENERICS:
                System.out.println("learning generics");
                break;
            case JSPANDSERVLETS:
                System.out.println("learning jspand servlets");
                break;
            case MULTITHREADING:
                System.out.println("learning multithreading");
                break;
            default:
                break;

        }

        for (Learning temp : Learning.values())
        {
            System.out.println(temp.ordinal());
        }
    }
}
