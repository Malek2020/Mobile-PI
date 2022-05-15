package com.culinair.gui.front.user;

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

import java.text.SimpleDateFormat;

public class Profil {

    Resources theme = UIManager.initFirstTheme("/theme");
    Label emailLabel, rolesLabel, passwordLabel, nomLabel, prenomLabel, naissanceLabel;
    ImageViewer imageIV;
    Button editProfileBTN;


    public Component addGUIs() {
        Container compo = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        compo.setScrollableY(true);

        Container userModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        userModel.setUIID("containerRounded");

        User user = UserService.getInstance().getUserById(MainApp.getSession().getId());

        emailLabel = new Label("Email : " + user.getEmail());
        emailLabel.setUIID("labelDefault");

        rolesLabel = new Label("Roles : " + user.getRoles());
        rolesLabel.setUIID("labelDefault");

        passwordLabel = new Label("Password : " + user.getPassword());
        passwordLabel.setUIID("labelDefault");

        nomLabel = new Label("Nom : " + user.getNom());
        nomLabel.setUIID("labelDefault");

        prenomLabel = new Label("Prenom : " + user.getPrenom());
        prenomLabel.setUIID("labelDefault");

        naissanceLabel = new Label("Naissance : " + new SimpleDateFormat("dd-MM-yyyy").format(user.getNaissance()));
        naissanceLabel.setUIID("labelDefault");

        if (user.getImage() != null) {
            String url = Statics.USER_IMAGE_URL + user.getImage();
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

        compo.addAll(
                imageIV, emailLabel,
                rolesLabel, passwordLabel,
                nomLabel, prenomLabel,
                naissanceLabel
        );

        editProfileBTN = new Button("Modifier mon profil");
        editProfileBTN.addActionListener(action -> new ModifierProfil(AccueilFront.accueilForm).show());

        compo.add(editProfileBTN);
        return compo;
    }
}