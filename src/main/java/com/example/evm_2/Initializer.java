package com.example.evm_2;

import com.amazonaws.services.sqs.model.Message;
import com.example.evm_2.commons.DbOperations;
import com.example.evm_2.services.SqsService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.List;

@WebListener
public class Initializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        SqsService.getInstance().createQueue("admin");

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Nothing to do when the context is destroyed

        while (true) {
            List<Message> Msg = SqsService.getInstance().getAllMessages("admin");

            if (Msg.isEmpty()) {
                System.out.println("Context is being destroyed No more Messages");
                break;
            }
            for (Message msg : Msg) {
                SqsService.getInstance().deleteMessage("admin", msg.getReceiptHandle());

            }
        }
        SqsService.getInstance().close();
    }
}
