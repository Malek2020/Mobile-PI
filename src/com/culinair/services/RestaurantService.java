package com.culinair.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import com.culinair.entities.Restaurant;
import com.culinair.utils.Statics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RestaurantService {

    public static RestaurantService instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<Restaurant> listRestaurants;

    private RestaurantService() {
        cr = new ConnectionRequest();
    }

    public static RestaurantService getInstance() {
        if (instance == null) {
            instance = new RestaurantService();
        }
        return instance;
    }

    public ArrayList<Restaurant> getAll() {
        listRestaurants = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/restaurant");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listRestaurants = getList();
                }

                cr.removeResponseListener(this);
            }
        });

        try {
            cr.setDisposeOnCompletion(new InfiniteProgress().showInfiniteBlocking());
            NetworkManager.getInstance().addToQueueAndWait(cr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listRestaurants;
    }

    private ArrayList<Restaurant> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                Restaurant restaurant = new Restaurant(
                        (int) Float.parseFloat(obj.get("id").toString()),
                        (String) obj.get("nom"),
                        (String) obj.get("type"),
                        (String) obj.get("description"),
                        (String) obj.get("emplacement"),
                        (String) obj.get("image"),
                        (String) obj.get("longitude"),
                        (String) obj.get("lattitude")
                );

                listRestaurants.add(restaurant);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return listRestaurants;
    }

    public int add(Restaurant restaurant) {
        return manage(restaurant, false, true);
    }

    public int edit(Restaurant restaurant, boolean imageEdited) {
        return manage(restaurant, true, imageEdited);
    }

    public int manage(Restaurant restaurant, boolean isEdit, boolean imageEdited) {
        MultipartRequest cr = new MultipartRequest();
        cr.setHttpMethod("POST");
        cr.setFilename("file", "Restaurant.jpg");
        if (isEdit) {
            cr.setUrl(Statics.BASE_URL + "/restaurant/edit");
            cr.addArgumentNoEncoding("id", String.valueOf(restaurant.getId()));
        } else {
            cr.setUrl(Statics.BASE_URL + "/restaurant/add");
        }

        cr.addArgumentNoEncoding("nom", restaurant.getNom());
        cr.addArgumentNoEncoding("type", restaurant.getType());
        cr.addArgumentNoEncoding("description", restaurant.getDescription());
        cr.addArgumentNoEncoding("emplacement", restaurant.getEmplacement());
        cr.addArgumentNoEncoding("longitude", restaurant.getLongitude());
        cr.addArgumentNoEncoding("lattitude", restaurant.getLattitude());

        if (imageEdited) {
            try {
                cr.addData("file", restaurant.getImage(), "image/jpeg");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            cr.addArgument("image", restaurant.getImage());
        }

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultCode = cr.getResponseCode();
                cr.removeResponseListener(this);
            }
        });
        try {
            cr.setDisposeOnCompletion(new InfiniteProgress().showInfiniteBlocking());
            NetworkManager.getInstance().addToQueueAndWait(cr);
        } catch (Exception ignored) {

        }
        return resultCode;
    }

    public int delete(int restaurantId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/restaurant/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(restaurantId));

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                cr.removeResponseListener(this);
            }
        });

        try {
            cr.setDisposeOnCompletion(new InfiniteProgress().showInfiniteBlocking());
            NetworkManager.getInstance().addToQueueAndWait(cr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cr.getResponseCode();
    }
}
