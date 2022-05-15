package com.culinair.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import com.culinair.entities.Promotion;
import com.culinair.utils.Statics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PromotionService {

    public static PromotionService instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<Promotion> listPromotions;

    private PromotionService() {
        cr = new ConnectionRequest();
    }

    public static PromotionService getInstance() {
        if (instance == null) {
            instance = new PromotionService();
        }
        return instance;
    }

    public ArrayList<Promotion> getAll() {
        listPromotions = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/promotion");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listPromotions = getList();
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

        return listPromotions;
    }

    private ArrayList<Promotion> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                Promotion promotion = new Promotion(
                        (int) Float.parseFloat(obj.get("id").toString()),
                        (int) Float.parseFloat(obj.get("tauxPromotion").toString()),
                        (String) obj.get("description")
                );

                listPromotions.add(promotion);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return listPromotions;
    }

    public int add(Promotion promotion) {
        return manage(promotion, false);
    }

    public int edit(Promotion promotion) {
        return manage(promotion, true);
    }

    public int manage(Promotion promotion, boolean isEdit) {
        cr = new ConnectionRequest();
        cr.setHttpMethod("POST");

        if (isEdit) {
            cr.setUrl(Statics.BASE_URL + "/promotion/edit");
            cr.addArgument("id", String.valueOf(promotion.getId()));
        } else {
            cr.setUrl(Statics.BASE_URL + "/promotion/add");
        }

        cr.addArgument("tauxPromotion", String.valueOf(promotion.getTauxPromotion()));
        cr.addArgument("description", promotion.getDescription());

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

    public int delete(int promotionId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/promotion/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(promotionId));

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
