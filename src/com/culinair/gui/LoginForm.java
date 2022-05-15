package com.culinair.gui;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.util.Resources;
import com.culinair.MainApp;
import com.culinair.entities.User;
import com.culinair.services.UserService;

import java.util.List;

public class LoginForm extends Form {

    public static Form loginForm;

    private final Resources theme;

    TextField tfEmail;
    TextField tfPassword;
    Button btnConnexion, btnInscription;
    Button backendBtn;

    public LoginForm(Resources theme) {
        super("Connexion", new BoxLayout(BoxLayout.Y_AXIS));
        this.theme = theme;
        loginForm = this;
        addGUIs();
        addActions();
    }

    private void addGUIs() {

        tfEmail = new TextField("", "Entrez votre email");
        tfPassword = new TextField("", "Tapez votre mot de passe", 20, TextField.PASSWORD);
        btnConnexion = new Button("Connexion");
        btnInscription = new Button("Inscription");

        backendBtn = new Button("Backend");
        backendBtn.setUIID("button");

        this.addAll(tfEmail, tfPassword, btnConnexion, new Label("Besoin d'un compte ?"), btnInscription, backendBtn);
    }

    private void addActions() {
        btnConnexion.addActionListener(action -> {
            User user = UserService.getInstance().verifierMotDePasse(tfEmail.getText(), tfPassword.getText());
            if (user != null) {
                login(user);
            } else {
                Dialog.show("Erreur", "Identifiants invalides", new Command("Ok"));
            }
        });

        btnInscription.addActionListener(action -> new RegisterForm().show());
        backendBtn.addActionListener(l -> new com.culinair.gui.back.AccueilBack(theme).show());
    }

    private void login(User user) {
        MainApp.setSession(user);
        if (user.getRoles().equals("ROLE_ADMIN")) {
            new com.culinair.gui.back.AccueilBack(theme).show();
        } else {
            new com.culinair.gui.front.AccueilFront(theme).show();
        }
    }
}
