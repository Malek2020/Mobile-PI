package com.culinair.gui.back.user;

import com.codename1.components.ImageViewer;
import com.codename1.components.InteractionDialog;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.culinair.entities.User;
import com.culinair.gui.back.AccueilBack;
import com.culinair.services.UserService;
import com.culinair.utils.Statics;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DisplayAll extends Form {

    public static User currentUser = null;
    Resources theme = UIManager.initFirstTheme("/theme");
    Button addBtn;
    Label emailLabel, rolesLabel, passwordLabel, nomLabel, prenomLabel, naissanceLabel;
    ImageViewer imageIV;
    Button editBtn, deleteBtn;
    Container btnsContainer;

    public Component addGUIs() {
        Container compo = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        compo.setScrollableY(true);

        addBtn = new Button("Ajouter");
        addBtn.addActionListener(action -> {
            currentUser = null;
            new Manage(AccueilBack.accueilForm).show();
        });

        compo.add(addBtn);

        ArrayList<User> listUsers = UserService.getInstance().getAll();
        if (listUsers.size() > 0) {
            for (User listUser : listUsers) {
                compo.add(makeUserModel(listUser));
            }
        } else {
            compo.add(new Label("Aucune donnée"));
        }

        return compo;
    }

    private Component makeUserModel(User user) {
        Container userModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        userModel.setUIID("containerRounded");

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

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        editBtn = new Button("Modifier");
        editBtn.addActionListener(action -> {
            currentUser = user;
            new Manage(AccueilBack.accueilForm).show();
        });

        deleteBtn = new Button("Supprimer");
        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce user ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = UserService.getInstance().delete(user.getId());

                if (responseCode == 200) {
                    currentUser = null;
                    dlg.dispose();
                    userModel.remove();
                    this.refreshTheme();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du user. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.WEST, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);

        userModel.addAll(
                emailLabel, rolesLabel, passwordLabel, nomLabel, prenomLabel, naissanceLabel,

                imageIV,

                btnsContainer
        );

        return userModel;
    }
}