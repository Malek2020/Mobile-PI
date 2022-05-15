package com.culinair.gui;

import com.codename1.capture.Capture;
import com.codename1.components.ImageViewer;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.culinair.entities.User;
import com.culinair.services.UserService;

import java.io.IOException;

public class RegisterForm extends Form {

    Resources theme = UIManager.initFirstTheme("/theme");
    String selectedImage;

    Label emailLabel, rolesLabel, passwordLabel, nomLabel, prenomLabel, naissanceLabel, imageLabel;
    TextField emailTF, rolesTF, passwordTF, nomTF, prenomTF;
    PickerComponent naissanceTF;
    Label passwordConfirmationLabel;
    TextField passwordConfirmationTF;
    Label loginLabel;
    ImageViewer imageIV;
    Button selectImageButton;
    Button registerButton, loginButton;

    public RegisterForm() {
        super("Inscription", new BoxLayout(BoxLayout.Y_AXIS));
        addGUIs();
        addActions();
    }

    private void addGUIs() {

        emailLabel = new Label("Email : ");
        emailLabel.setUIID("labelDefault");
        emailTF = new TextField();
        emailTF.setHint("Tapez le email");

        rolesLabel = new Label("Roles : ");
        rolesLabel.setUIID("labelDefault");
        rolesTF = new TextField();
        rolesTF.setHint("Tapez le roles");

        passwordLabel = new Label("Password : ");
        passwordLabel.setUIID("labelDefault");
        passwordTF = new TextField();
        passwordTF.setHint("Tapez le password");

        nomLabel = new Label("Nom : ");
        nomLabel.setUIID("labelDefault");
        nomTF = new TextField();
        nomTF.setHint("Tapez le nom");

        prenomLabel = new Label("Prenom : ");
        prenomLabel.setUIID("labelDefault");
        prenomTF = new TextField();
        prenomTF.setHint("Tapez le prenom");
        naissanceTF = PickerComponent.createDate(null).label("Naissance");


        imageLabel = new Label("Image : ");
        imageLabel.setUIID("labelDefault");
        selectImageButton = new Button("Ajouter une image");
        passwordConfirmationLabel = new Label("Mot de passe : ");
        passwordConfirmationLabel.setUIID("labelDefault");
        passwordConfirmationTF = new TextField("", "Retapez votre mot de passe", 20, TextField.PASSWORD);

        registerButton = new Button("S'inscrire");

        loginLabel = new Label("Vous avez deja un compte ?");
        loginButton = new Button("Connexion");

        imageIV = new ImageViewer(theme.getImage("default.jpg").fill(500, 500));

        this.addAll(
                imageLabel, imageIV,
                selectImageButton,
                emailLabel, emailTF,
                rolesLabel, rolesTF,
                passwordLabel, passwordTF,
                passwordConfirmationLabel, passwordConfirmationTF,
                nomLabel, nomTF,
                prenomLabel, prenomTF,
                naissanceTF,
                registerButton,
                loginLabel, loginButton
        );
    }

    private void addActions() {
        selectImageButton.addActionListener(a -> {
            selectedImage = Capture.capturePhoto(900, -1);
            try {
                imageIV.setImage(Image.createImage(selectedImage));
            } catch (IOException e) {
                e.printStackTrace();
            }
            selectImageButton.setText("Modifier l'image");
        });

        registerButton.addActionListener(l -> {
            if (controleDeSaisie()) {
                int responseCode = UserService.getInstance().add(
                        new User(
                                emailTF.getText(),
                                rolesTF.getText(),
                                passwordTF.getText(),
                                nomTF.getText(),
                                prenomTF.getText(),
                                naissanceTF.getPicker().getDate(),
                                selectedImage

                        )
                );
                if (responseCode == 200) {
                    Dialog.show("Succés", "User ajouté avec succes", new Command("Ok"));
                } else if (responseCode == 203) {
                    Dialog.show("Erreur", "Email already exist", new Command("Ok"));
                } else {
                    Dialog.show("Erreur", "Erreur d'ajout" + responseCode, new Command("Ok"));
                }
            }
        });

        loginButton.addActionListener(l -> LoginForm.loginForm.showBack());
    }

    private boolean controleDeSaisie() {

        if (emailTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le email", new Command("Ok"));
            return false;
        }


        if (rolesTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le roles", new Command("Ok"));
            return false;
        }

        if (nomTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le nom", new Command("Ok"));
            return false;
        }


        if (prenomTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le prenom", new Command("Ok"));
            return false;
        }


        if (naissanceTF.getPicker().getDate() == null) {
            Dialog.show("Avertissement", "Veuillez saisir la naissance", new Command("Ok"));
            return false;
        }


        if (selectedImage == null) {
            Dialog.show("Avertissement", "Veuillez choisir une image", new Command("Ok"));
            return false;
        }

        if (passwordTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le mot de passe", new Command("Ok"));
            return false;
        }

        if (passwordConfirmationTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir la confirmation", new Command("Ok"));
            return false;
        }

        if (!passwordConfirmationTF.getText().equals(passwordTF.getText())) {
            Dialog.show("Avertissement", "Mot de passe et confirmation doivent etre identiques", new Command("Ok"));
            return false;
        }

        return true;
    }
}
