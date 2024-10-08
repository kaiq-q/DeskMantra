package com.example.application.configurationComponents;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import org.vaadin.lineawesome.LineAwesomeIcon;

public class ButtonConfiguration {


    // Getters
    public Button getSaveButton() {
        return getSaveButton("Save"); // default text
    }

    public Button getSaveButton(String textButton) {

        Button button = new Button();
        button.setWidth("125px");
        button.addClickShortcut(Key.ENTER);
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.setIcon(LineAwesomeIcon.CHECK_SOLID.create());

        if (textButton != null && !textButton.trim().isEmpty()) {
            button.setText(textButton);
        }
        return button;
    }

    public Button getCloseButton() {
        return getCloseButton("Close");
    }

    public Button getCloseButton(String textButton) {

        Button button = new Button();
        button.setWidth("125px");
        button.addClickShortcut(Key.ESCAPE);
        button.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        button.setIcon(LineAwesomeIcon.TIMES_SOLID.create());

        if (textButton != null && !textButton.trim().isEmpty()) {
            button.setText(textButton);
        }
        return button;
    }

    public Button getDeleteButton() {
        return getDeleteButton("Delete");
    }


    public Button getDeleteButton(String textButton){
        Button button = new Button();
        button.setWidth("125px");
        button.addThemeVariants(ButtonVariant.LUMO_ERROR);
        button.setIcon(LineAwesomeIcon.TRASH_SOLID.create());

        if (textButton != null && !textButton.trim().isEmpty()){
            button.setText(textButton);
        }
        return button;
    }


}
