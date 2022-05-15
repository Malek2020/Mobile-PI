package com.culinair.gui.front.user;

import com.codename1.capture.Capture;
import com.codename1.components.ImageViewer;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.culinair.MainApp;
import com.culinair.entities.User;
import com.culinair.gui.front.AccueilFront;
import com.culinair.services.UserService;
import com.culinair.utils.Statics;

import java.io.IOException;

public class ModifierProfil extends Form {

    Resources theme = UIManager.initFirstTheme("/theme");
    String selectedImage;
    User currentUser;
    boolean imageEdited = false;

    Label emailLabel, rolesLabel, passwordLabel, nomLabel, prenomLabel, naissanceLabel, imageLabel;
    TextField emailTF, rolesTF, passwordTF, nomTF, prenomTF;
    PickerComponent naissanceTF;

    ImageViewer imageIV;
    Button selectImageButton;

    Button manageButton;

    Form previous;

    public ModifierProfil(Form previous) {
        super("Modifier mon profil", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;
        currentUser = MainApp.getSession();

        addGUIs();
        addActions();

        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
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

        emailTF.setText(currentUser.getEmail());
        rolesTF.setText(currentUser.getRoles());
        nomTF.setText(currentUser.getNom());
        prenomTF.setText(currentUser.getPrenom());
        naissanceTF.getPicker().setDate(currentUser.getNaissance());

        if (currentUser.getImage() != null) {
            selectedImage = currentUser.getImage();

            String url = Statics.USER_IMAGE_URL + currentUser.getImage();
            Image image = URLImage.createToStorage(
                    EncodedImage.createFromImage(theme.getImage("default.jpg").fill(500, 500), false),
                    url,
                    url,
                    URLImage.RESIZE_SCALE
            );
            imageIV = new ImageViewer(image);
        } else {
            imageIV = new ImageViewer(theme.getImage("default.jpg").fill(500, 500));
        }
        imageIV.setFocusable(false);


        manageButton = new Button("Modifier");

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(
                imageLabel, imageIV,
                selectImageButton,
                emailLabel, emailTF,
                rolesLabel, rolesTF,
                passwordLabel, passwordTF,
                nomLabel, nomTF,
                prenomLabel, prenomTF,
                naissanceTF,
                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {
        selectImageButton.addActionListener(a -> {
            selectedImage = Capture.capturePhoto(500, -1);
            imageEdited = true;
            try {
                imageIV.setImage(Image.createImage(selectedImage));
            } catch (IOException e) {
                e.printStackTrace();
            }
            selectImageButton.setText("Modifier l'image");
        });
        manageButton.addActionListener(action -> {
            if (controleDeSaisie()) {
                int responseCode = UserService.getInstance().edit(
                        new User(
                                currentUser.getId(),
                                emailTF.getText(),
                                rolesTF.getText(),
                                passwordTF.getText(),
                                nomTF.getText(),
                                prenomTF.getText(),
                                naissanceTF.getPicker().getDate(),
                                selectedImage

                        ), imageEdited
                );
                if (responseCode == 200) {
                    Dialog.show("Succés", "User modifié avec succes", new Command("Ok"));
                    showBackAndRefresh();
                } else {
                    Dialog.show("Erreur", "Erreur de modification de user. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            }
        });
    }

    private void showBackAndRefresh() {
        AccueilFront.accueilForm.addComponentAndRefresh(
                new com.culinair.gui.front.user.Profil().addGUIs(),
                "Mon profil"
        );
        previous.showBack();
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


        if (passwordTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le password", new Command("Ok"));
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


        return true;
    }
}