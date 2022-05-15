package com.culinair.gui.back.restaurant;

import com.codename1.components.ImageViewer;
import com.codename1.components.InteractionDialog;
import com.codename1.components.SpanLabel;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.culinair.entities.Restaurant;
import com.culinair.gui.back.AccueilBack;
import com.culinair.services.RestaurantService;
import com.culinair.utils.Statics;

import java.util.ArrayList;

public class DisplayAll {

    public static Restaurant currentRestaurant = null;
    Resources theme = UIManager.initFirstTheme("/theme");
    Button addBtn;
    Label nomLabel, typeLabel, descriptionLabel, emplacementLabel, longitudeLabel, lattitudeLabel;
    SpanLabel descriptionSpanLabel;
    ImageViewer imageIV;
    Button editBtn, deleteBtn;
    Container btnsContainer;

    public Component addGUIs() {
        Container compo = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        compo.setScrollableY(true);

        addBtn = new Button("Ajouter");
        addBtn.addActionListener(action -> {
            currentRestaurant = null;
            new Manage(AccueilBack.accueilForm, false).show();
        });
        compo.add(addBtn);

        ArrayList<Restaurant> listRestaurants = RestaurantService.getInstance().getAll();
        if (listRestaurants.size() > 0) {
            for (Restaurant listRestaurant : listRestaurants) {
                compo.add(makeRestaurantModel(listRestaurant));
            }
        } else {
            compo.add(new Label("Aucune donnee"));
        }

        return compo;
    }

    private Component makeRestaurantModel(Restaurant restaurant) {
        Container restaurantModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        restaurantModel.setUIID("containerRounded");

        nomLabel = new Label(restaurant.getNom());
        nomLabel.setUIID("labelCenter");

        typeLabel = new Label("Type : " + restaurant.getType());
        typeLabel.setUIID("labelDefault");

        descriptionLabel = new Label("Description : ");
        descriptionLabel.setUIID("labelDefault");

        descriptionSpanLabel = new SpanLabel(restaurant.getDescription());
        descriptionSpanLabel.setUIID("labelDefault");

        emplacementLabel = new Label("Emplacement : " + restaurant.getEmplacement());
        emplacementLabel.setUIID("labelDefault");


        longitudeLabel = new Label("Longitude : " + restaurant.getLongitude());
        longitudeLabel.setUIID("labelDefault");

        lattitudeLabel = new Label("Lattitude : " + restaurant.getLattitude());
        lattitudeLabel.setUIID("labelDefault");

        if (restaurant.getImage() != null) {
            String url = Statics.RESTAURANT_IMAGE_URL + restaurant.getImage();
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

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        editBtn = new Button("Modifier");
        editBtn.addActionListener(action -> {
            currentRestaurant = restaurant;
            new Manage(AccueilBack.accueilForm, false).show();
        });

        deleteBtn = new Button("Supprimer");
        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce restaurant ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = RestaurantService.getInstance().delete(restaurant.getId());

                if (responseCode == 200) {
                    currentRestaurant = null;
                    dlg.dispose();
                    restaurantModel.remove();
                    AccueilBack.accueilForm.revalidate();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du restaurant. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.WEST, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);


        restaurantModel.addAll(
                nomLabel, imageIV, typeLabel, descriptionLabel, descriptionSpanLabel, emplacementLabel, longitudeLabel, lattitudeLabel,
                btnsContainer
        );

        return restaurantModel;
    }
}