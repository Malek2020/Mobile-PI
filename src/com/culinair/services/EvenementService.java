package com.culinair.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import com.culinair.entities.Evenement;
import com.culinair.entities.Promotion;
import com.culinair.utils.Statics;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EvenementService {

    public static EvenementService instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<Evenement> listEvenements;

    private EvenementService() {
        cr = new ConnectionRequest();
    }

    public static EvenementService getInstance() {
        if (instance == null) {
            instance = new EvenementService();
        }
        return instance;
    }

    public ArrayList<Evenement> getAll() {
        listEvenements = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/evenement");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listEvenements = getList();
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

        return listEvenements;
    }

    private ArrayList<Evenement> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                Evenement evenement = new Evenement(
                        (int) Float.parseFloat(obj.get("id").toString()),
                        (String) obj.get("nom"),
                        new SimpleDateFormat("dd-MM-yyyy").parse((String) obj.get("dateDebut")),
                        new SimpleDateFormat("dd-MM-yyyy").parse((String) obj.get("dateFin")),
                        (String) obj.get("lieu"),
                        (String) obj.get("image"),
                        Float.parseFloat(obj.get("prix").toString()),
                        (String) obj.get("description"),
                        makePromotion((Map<String, Object>) obj.get("promotion"))

                );

                listEvenements.add(evenement);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return listEvenements;
    }


    public Promotion makePromotion(Map<String, Object> obj) {
        if (obj == null) {
            return null;
        }

        return new Promotion(
                (int) Float.parseFloat(obj.get("id").toString()),
                (int) Float.parseFloat(obj.get("tauxPromotion").toString()),
                (String) obj.get("description")
        );
    }


    public int add(Evenement evenement) {
        return manage(evenement, false, true);
    }

    public int edit(Evenement evenement, boolean imageEdited) {
        return manage(evenement, true, imageEdited);
    }

    public int manage(Evenement evenement, boolean isEdit, boolean imageEdited) {

        MultipartRequest cr = new MultipartRequest();
        cr.setFilename("file", "Evenement.jpg");


        cr.setHttpMethod("POST");
        if (isEdit) {
            cr.setUrl(Statics.BASE_URL + "/evenement/edit");
            cr.addArgumentNoEncoding("id", String.valueOf(evenement.getId()));
        } else {
            cr.setUrl(Statics.BASE_URL + "/evenement/add");
        }

        if (imageEdited) {
            try {
                cr.addData("file", evenement.getImage(), "image/jpeg");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            cr.addArgumentNoEncoding("image", evenement.getImage());
        }

        cr.addArgumentNoEncoding("nom", evenement.getNom());
        cr.addArgumentNoEncoding("dateDebut", new SimpleDateFormat("dd-MM-yyyy").format(evenement.getDateDebut()));
        cr.addArgumentNoEncoding("dateFin", new SimpleDateFormat("dd-MM-yyyy").format(evenement.getDateFin()));
        cr.addArgumentNoEncoding("lieu", evenement.getLieu());
        cr.addArgumentNoEncoding("image", evenement.getImage());
        cr.addArgumentNoEncoding("prix", String.valueOf(evenement.getPrix()));
        cr.addArgumentNoEncoding("description", evenement.getDescription());
        cr.addArgumentNoEncoding("promotion", String.valueOf(evenement.getPromotion().getId()));

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

    public int delete(int evenementId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/evenement/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(evenementId));

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
