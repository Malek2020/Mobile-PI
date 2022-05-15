package com.culinair.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import com.culinair.entities.Activite;
import com.culinair.entities.Transport;
import com.culinair.utils.Statics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ActiviteService {

    public static ActiviteService instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<Activite> listActivites;

    private ActiviteService() {
        cr = new ConnectionRequest();
    }

    public static ActiviteService getInstance() {
        if (instance == null) {
            instance = new ActiviteService();
        }
        return instance;
    }

    public ArrayList<Activite> getAll() {
        listActivites = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/activite");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listActivites = getList();
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

        return listActivites;
    }

    private ArrayList<Activite> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                Activite activite = new Activite(
                        (int) Float.parseFloat(obj.get("id").toString()),
                        (String) obj.get("nom"),
                        (String) obj.get("typeActivite"),
                        (String) obj.get("description"),
                        (int) Float.parseFloat(obj.get("temps").toString()),
                        (int) Float.parseFloat(obj.get("prixActivite").toString()),
                        (int) Float.parseFloat(obj.get("nombreParticipant").toString()),
                        makeTransport((Map<String, Object>) obj.get("transport"))
                );

                listActivites.add(activite);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return listActivites;
    }

    public Transport makeTransport(Map<String, Object> obj) {
        if (obj == null) {
            return null;
        }

        return new Transport(
                (int) Float.parseFloat(obj.get("id").toString()),
                (int) Float.parseFloat(obj.get("dureeTransport").toString()),
                (String) obj.get("typeTransport"),
                (int) Float.parseFloat(obj.get("capacite").toString())
        );
    }

    public int add(Activite activite) {
        return manage(activite, false);
    }

    public int edit(Activite activite) {
        return manage(activite, true);
    }

    public int manage(Activite activite, boolean isEdit) {
        MultipartRequest cr = new MultipartRequest();
        cr.setFilename("file", "Activite.jpg");
        cr.setHttpMethod("POST");
        if (isEdit) {
            cr.setUrl(Statics.BASE_URL + "/activite/edit");
            cr.addArgument("id", String.valueOf(activite.getId()));
        } else {
            cr.setUrl(Statics.BASE_URL + "/activite/add");
        }

        cr.addArgument("nom", activite.getNom());
        cr.addArgument("typeActivite", activite.getTypeActivite());
        cr.addArgument("description", activite.getDescription());
        cr.addArgument("temps", String.valueOf(activite.getTemps()));
        cr.addArgument("prixActivite", String.valueOf(activite.getPrixActivite()));
        cr.addArgument("nombreParticipant", String.valueOf(activite.getNombreParticipant()));
        cr.addArgument("transport", String.valueOf(activite.getTransport().getId()));

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

    public int delete(int activiteId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/activite/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(activiteId));

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
