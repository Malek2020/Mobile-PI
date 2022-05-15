package com.culinair.gui.front.restaurant;

import com.codename1.components.ImageViewer;
import com.codename1.components.SpanLabel;
import com.codename1.maps.Coord;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.culinair.entities.Restaurant;
import com.culinair.gui.front.AccueilFront;
import com.culinair.services.RestaurantService;
import com.culinair.utils.Statics;

import java.util.ArrayList;

public class DisplayAll {

    private static final String RESTAURANT_NAME = "culinair";
    private static final double RESTAURANT_LATITUDE = 40.7127;
    private static final double RESTAURANT_LONGITUDE = 74.0059;
    private static final Coord RESTAURANT_LOCATION = new Coord(RESTAURANT_LATITUDE, RESTAURANT_LONGITUDE);
    Resources theme = UIManager.initFirstTheme("/theme");
    Label nomLabel, typeLabel, descriptionLabel, emplacementLabel, longitudeLabel, lattitudeLabel;
    SpanLabel descriptionSpanLabel;
    ImageViewer imageIV;
    Container btnsContainer;

    public Component addGUIs() {
        Container compo = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        compo.setScrollableY(true);

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

        try {
            float longitude = Float.parseFloat(restaurant.getLongitude());
            float lattitude = Float.parseFloat(restaurant.getLattitude());

            Button mapBtn = new Button("Afficher dans maps");
            mapBtn.addActionListener(l -> {
                new MapForm(AccueilFront.accueilForm, restaurant.getNom(), longitude, lattitude).show();
            });
            restaurantModel.add(mapBtn);
        } catch (NumberFormatException | NullPointerException ignored) {

        }

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        restaurantModel.addAll(
                nomLabel, imageIV, typeLabel,
                descriptionLabel, descriptionSpanLabel,
                emplacementLabel,
                longitudeLabel, lattitudeLabel,
                btnsContainer
        );

        return restaurantModel;
    }
}
