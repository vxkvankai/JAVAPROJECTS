import java.io.FileNotFoundException;


public class UserDefineException extends Throwable {

    public static void main(String[] args)
    {
        try
        {
            someMethod();
        } catch (FileNotFoundException e)
        {
            System.out.println("Catch block of the main method(FileNotFoundException)");
            e.printStackTrace();
        } catch (UserDefineException e)
        {
            e.printStackTrace();
        } catch (Exception e)
        {
            System.out.println("Catch block of main method (Exception)");
        }
    }

    public static void someMethod() throws Exception, FileNotFoundException, UserDefineException
    {
        int x = 1;
        switch (x)
        {
            case 1:
                throw new FileNotFoundException();
            case 2:
                throw new Exception();
            default:
                throw new UserDefineException();
        }
    }


}
