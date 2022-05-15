package com.culinair.gui.back.plat;

import com.codename1.components.ImageViewer;
import com.codename1.components.SpanLabel;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.culinair.entities.Restaurant;
import com.culinair.services.RestaurantService;
import com.culinair.utils.Statics;

import java.util.ArrayList;

public class ChooseRestaurant extends Form {

    Resources theme = UIManager.initFirstTheme("/theme");
    Form previousForm;
    Button addBtn;
    Label nomLabel, typeLabel, descriptionLabel, emplacementLabel;
    SpanLabel descriptionSpanLabel;
    ImageViewer imageIV;
    Button chooseBtn;
    Container btnsContainer;

    public ChooseRestaurant(Form previous) {
        super("Choisir un restaurant", new BoxLayout(BoxLayout.Y_AXIS));

        previousForm = previous;
        addGUIs();
        addActions();

        super.setToolbar(new Toolbar());
        super.setTitle("Choisir un restaurant");
        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    public void refresh() {
        this.removeAll();
        addGUIs();
        addActions();
        this.refreshTheme();
    }

    private void addGUIs() {
        addBtn = new Button("Ajouter restaurant");
        this.add(addBtn);

        ArrayList<Restaurant> listRestaurants = RestaurantService.getInstance().getAll();
        if (listRestaurants.size() > 0) {
            for (Restaurant restaurants : listRestaurants) {
                this.add(makeRestaurantModel(restaurants));
            }
        } else {
            this.add(new Label("Aucune donnee"));
        }
    }

    private void addActions() {
        addBtn.addActionListener(action -> new com.culinair.gui.back.restaurant.Manage(this, true).show());
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

        chooseBtn = new Button("Choisir");
        chooseBtn.addActionListener(l -> {
            Manage.selectedRestaurant = restaurant;
            ((Manage) previousForm).refreshRestaurant();
            previousForm.showBack();
        });

        btnsContainer.add(BorderLayout.CENTER, chooseBtn);

        restaurantModel.addAll(
                nomLabel, imageIV, typeLabel, descriptionLabel, descriptionSpanLabel, emplacementLabel,
                btnsContainer
        );

        return restaurantModel;
    }
}