package com.culinair.gui.front.plat;

import com.codename1.components.ImageViewer;
import com.codename1.components.SpanLabel;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.culinair.MainApp;
import com.culinair.entities.Plat;
import com.culinair.gui.front.AccueilFront;
import com.culinair.services.PlatService;
import com.culinair.utils.Statics;

import java.util.ArrayList;

public class DisplayAll {

    public static Plat currentPlat = null;
    Button showFavButton;

    Resources theme = UIManager.initFirstTheme("/theme");
    Label nomLabel, typeLabel, descriptionLabel, prixLabel, resttaurantLabel;
    SpanLabel descriptionSpanLabel;
    ImageViewer imageIV;
    Container btnsContainer;

    public Component addGUIs() {


        Container compo = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        compo.setScrollableY(true);

        showFavButton = new Button("Afficher mes favoris");
        showFavButton.addActionListener(action -> {
            new DisplayFavorites(AccueilFront.accueilForm).show();
        });
        compo.add(showFavButton);

        ArrayList<Plat> listPlats = PlatService.getInstance().getAll();

        if (listPlats.size() > 0) {
            for (Plat listPlat : listPlats) {
                Component model = makePlatModel(listPlat);
                compo.add(model);
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

        typeLabel = new Label("Type de cuisine : " + plat.getTypeCuisine());
        typeLabel.setUIID("labelDefault");

        descriptionLabel = new Label("Description : ");
        descriptionLabel.setUIID("labelDefault");

        descriptionSpanLabel = new SpanLabel(plat.getDescription());
        descriptionSpanLabel.setUIID("labelDefault");

        prixLabel = new Label(String.valueOf(plat.getPrix()));
        prixLabel.setUIID("labelDefault");

        if (plat.getRestaurant() != null) {
            resttaurantLabel = new Label("Restaurant : " + plat.getRestaurant().getNom());
            resttaurantLabel.setUIID("labelDefault");
        } else {
            resttaurantLabel = new Label("Restaurant : null");
        }

        if (plat.getImage() != null) {
            String url = Statics.RESTAURANT_IMAGE_URL + plat.getImage();
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

        Button addToFavButton;
        ArrayList<Plat> listPlatFavoris = MainApp.listPlatFavoris;
        currentPlat = plat;
        if (listPlatFavoris.contains(plat)) {
            addToFavButton = new Button(FontImage.MATERIAL_FAVORITE);
        } else {
            addToFavButton = new Button(FontImage.MATERIAL_FAVORITE_OUTLINE);

            addToFavButton.addActionListener(action -> {
                if (listPlatFavoris.contains(plat)) {
                    addToFavButton.setMaterialIcon(FontImage.MATERIAL_FAVORITE_OUTLINE);
                    MainApp.listPlatFavoris.remove(plat);
                } else {
                    addToFavButton.setMaterialIcon(FontImage.MATERIAL_FAVORITE);
                    MainApp.listPlatFavoris.add(plat);
                }
                platModel.revalidate();
            });
        }

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.add(BorderLayout.CENTER, addToFavButton);
        btnsContainer.setUIID("containerButtons");

        platModel.addAll(
                nomLabel, imageIV, typeLabel, descriptionLabel, descriptionSpanLabel, prixLabel, resttaurantLabel,
                btnsContainer
        );

        return platModel;
    }
}
