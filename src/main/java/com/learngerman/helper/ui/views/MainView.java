package com.learngerman.helper.ui.views;

import com.learngerman.helper.service.OllamaChatService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "")
@PageTitle("Ollama mistral chat bot")
public class MainView extends VerticalLayout {

    @Autowired
    private OllamaChatService chatService;
    private final TextField message = new TextField();
    private final TextField chat = new TextField();
    private final TextArea answer = new TextArea();
    private final Button button = new Button("Generate Response");

    public MainView(){
        H1 heading = new H1("How can I help you today?");

        configureInput();
        configureAnswer();
        configureButton();
        add(heading,message,chat,button,answer);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    }

    private void configureInput(){
        message.setPlaceholder("Enter your message here to get answers to questions specific to wormholes");
        message.setSizeFull();
        message.setValueChangeMode(ValueChangeMode.LAZY);
        message.setClearButtonVisible(true);

        chat.setPlaceholder("Enter your message here to talk to the LLM on general topics");
        chat.setSizeFull();
        chat.setValueChangeMode(ValueChangeMode.LAZY);
        chat.setClearButtonVisible(true);
    }

    private void configureButton() {
        button.addClickListener(evt-> {
            if(!message.getValue().isEmpty()) {
                answer.setVisible(true);
                answer.setValue(chatService.call(message.getValue()));
            } else if (!chat.getValue().isEmpty()) {
                answer.setVisible(true);
                answer.setValue(chatService.callWithoutContext(chat.getValue()));
            }else {
                answer.setVisible(false);
                Notification.show("No question was asked to the LLM",3000, Notification.Position.TOP_CENTER);
            }
        });
    }

    private void configureAnswer(){
        answer.setVisible(false);
        answer.setSizeFull();
    }

}
