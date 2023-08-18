package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accService;
    MessageService msgService;

    public SocialMediaController() {
        this.accService = new AccountService();
        this.msgService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);

        // POST localhost:8080/register
        app.post("/register", this::postAccountHandler);

        // POST localhost:8080/login
        app.post("/login", this::loginAccountHandler);

        // POST localhost:8080/messages 
        app.post("/messages", this::postMessageHandler);

        // GET localhost:8080/messages
        app.get("/messages", this::getAllMessagesHandler);

        // GET localhost:8080/messages/{message_id} 
        app.get("/messages/{message_id}", this::getMessageByIdHandler);

        // DELETE localhost:8080/messages/{message_id}
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);

        // PATCH localhost:8080/messages/{message_id}
        app.patch("/messages/{message_id}", this::updateMessageByIdHandler);

        // GET localhost:8080/accounts/{account_id}/messages
        app.get("/accounts/{account_id}/messages", this::getAllMessageByAccountHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void postAccountHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account acc = mapper.readValue(context.body(), Account.class);
        Account newAcc = accService.register(acc);
        if(newAcc == null) {
            context.status(400);
        }else {
            context.json(mapper.writeValueAsString(newAcc));
        }
    }

    private void loginAccountHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account acc = mapper.readValue(context.body(), Account.class);
        Account loginAcc = accService.login(acc);
        if(loginAcc == null) {
            context.status(401);
        }else {
            context.json(mapper.writeValueAsString(loginAcc));
        }
    }

    private void postMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Message newMsg = msgService.addMessage(message);
        if(newMsg == null) {
            context.status(400);
        }else {
            context.json(mapper.writeValueAsString(newMsg));
        }
    }

    private void getAllMessagesHandler(Context context) throws JsonProcessingException {
        context.json(msgService.getAllMessages());
    }

    private void getMessageByIdHandler(Context context) throws JsonProcessingException {
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message message = msgService.getMessageById(message_id);
        if(message == null) {
            context.json("");
        }else {
            context.json(message);
        }
    }

    private void deleteMessageByIdHandler(Context context) throws JsonProcessingException {
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message message = msgService.deletMessageById(message_id);
        if(message == null) {
            context.json("");
        }else {
            context.json(message);
        }
    }

    private void updateMessageByIdHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        // String text = mapper.readValue(context.body(), String.class);
        Message msg = mapper.readValue(context.body(), Message.class);
        String text = msg.getMessage_text();
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message message = msgService.updateMessageById(message_id, text);
        if(message == null) {
            context.status(400);
        }else {
            context.json(mapper.writeValueAsString(message));
        }
    }

    private void getAllMessageByAccountHandler(Context context) throws JsonProcessingException {
        int account_id = Integer.parseInt(context.pathParam("account_id"));
        context.json(msgService.getAllMessagesByAccount(account_id));
    }

}