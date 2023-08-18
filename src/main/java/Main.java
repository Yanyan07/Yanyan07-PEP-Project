import Controller.SocialMediaController;
import DAO.MessageDAO;
import DAO.MessageDAOImpl;
import Model.Message;
import io.javalin.Javalin;

/**
 * This class is provided with a main method to allow you to manually run and test your application. This class will not
 * affect your program in any way and you may write whatever code you like here.
 */
public class Main {
    public static void main(String[] args) {
        SocialMediaController controller = new SocialMediaController();
        Javalin app = controller.startAPI();
        app.start(8080);

        // public Message getMessageById(int id) {
        //     return messageDAO.getMessageById(id);
        // }
        MessageDAO msgDAO = new MessageDAOImpl();
        Message message = msgDAO.getMessageById(1);
        System.out.println("message id=1 " + message);
        
        System.out.println();
        Message message2 = msgDAO.getMessageById(100);
        System.out.println("message id=100 " + message2);

    }
}
