package com.culinair.gui.back.plat;

import com.codename1.components.ImageViewer;
import com.codename1.components.InteractionDialog;
import com.codename1.components.SpanLabel;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.culinair.entities.Plat;
import com.culinair.gui.back.AccueilBack;
import com.culinair.services.PlatService;
import com.culinair.utils.Statics;

import java.util.ArrayList;

public class DisplayAll {

    public static Plat currentPlat = null;
    Resources theme = UIManager.initFirstTheme("/theme");
    Button addBtn;
    Label nomLabel, typeCuisineLabel, descriptionLabel, prixLabel, restaurantLabel;
    SpanLabel descriptionSpanLabel;
    ImageViewer imageIV;
    Button editBtn, deleteBtn;
    Container btnsContainer;

    public Component addGUIs() {
        Container compo = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        compo.setScrollableY(true);

        addBtn = new Button("Ajouter");
        addBtn.addActionListener(action -> {
            currentPlat = null;
            new Manage(AccueilBack.accueilForm).show();
        });

        compo.add(addBtn);

        ArrayList<Plat> listPlats = PlatService.getInstance().getAll();
        if (listPlats.size() > 0) {
            for (Plat listPlat : listPlats) {
                compo.add(makePlatModel(listPlat));
            }
        } else {
            compo.add(new Label("Aucune donnee"));
        }

        return compo;
    }

    private Component makePlatModel(Plat plat) {
        Container platModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        platModel.setUIID("containerRounded");

        nomLabel = new Label(plat.getNom());
        nomLabel.setUIID("labelCenter");

        typeCuisineLabel = new Label("Type : " + plat.getTypeCuisine());
        typeCuisineLabel.setUIID("labelDefault");

        descriptionLabel = new Label("Description : " + plat.getDescription());
        descriptionLabel.setUIID("labelDefault");

        descriptionSpanLabel = new SpanLabel(plat.getDescription());
        descriptionSpanLabel.setUIID("labelDefault");

        prixLabel = new Label("Prix : " + plat.getPrix());
        prixLabel.setUIID("labelDefault");

        if (plat.getImage() != null) {
            String url = Statics.PLAT_IMAGE_URL + plat.getImage();
            Image image = URLImage.createToStorage(
                    EncodedImage.createFromImage(theme.getImage("default.jpg").fill(1100, 500), false),
                    url,
                    url,
                    URLImage.RESIZE_SCALE
            );
            imageIV = new ImageViewer(image);
        } else {
            imageIV = new ImageViewer(theme.getImage("default.jpg").fill(1100, 500));
        }
        imageIV.setFocusable(false);

        if (plat.getRestaurant() != null) {
            restaurantLabel = new Label("Restaurant : " + plat.getRestaurant().getNom());
            restaurantLabel.setUIID("labelDefault");
        } else {
            restaurantLabel = new Label("Restaurant : null");
        }

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        editBtn = new Button("Modifier");
        editBtn.addActionListener(action -> {
            currentPlat = plat;
            new Manage(AccueilBack.accueilForm).show();
        });

        deleteBtn = new Button("Supprimer");
        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce plat ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                System.out.println(plat);
                int responseCode = PlatService.getInstance().delete(plat.getId());

                if (responseCode == 200) {
                    currentPlat = null;
                    dlg.dispose();
                    platModel.remove();
                    AccueilBack.accueilForm.revalidate();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du plat. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.WEST, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);

        platModel.addAll(
                nomLabel, imageIV, typeCuisineLabel, descriptionLabel, descriptionSpanLabel, prixLabel, restaurantLabel,
                btnsContainer
        );

        return platModel;
    }
}
