package com.culinair.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import com.culinair.entities.Pays;
import com.culinair.entities.Ville;
import com.culinair.utils.Statics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VilleService {

    public static VilleService instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<Ville> listVilles;

    private VilleService() {
        cr = new ConnectionRequest();
    }

    public static VilleService getInstance() {
        if (instance == null) {
            instance = new VilleService();
        }
        return instance;
    }

    public ArrayList<Ville> getAll() {
        listVilles = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/ville");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listVilles = getList();
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

        return listVilles;
    }

    private ArrayList<Ville> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                Ville ville = new Ville(
                        (int) Float.parseFloat(obj.get("id").toString()),

                        (String) obj.get("ville"),
                        makePays((Map<String, Object>) obj.get("pays"))

                );

                listVilles.add(ville);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listVilles;
    }


    public Pays makePays(Map<String, Object> obj) {
        if (obj == null) {
            return null;
        }

        return new Pays(
                (int) Float.parseFloat(obj.get("id").toString()),

                (String) obj.get("nomPays")

        );
    }


    public int add(Ville ville) {
        return manage(ville, false);
    }

    public int edit(Ville ville) {
        return manage(ville, true);
    }

    public int manage(Ville ville, boolean isEdit) {

        cr = new ConnectionRequest();


        cr.setHttpMethod("POST");
        if (isEdit) {
            cr.setUrl(Statics.BASE_URL + "/ville/edit");
            cr.addArgument("id", String.valueOf(ville.getId()));
        } else {
            cr.setUrl(Statics.BASE_URL + "/ville/add");
        }
        cr.addArgument("ville", ville.getVille());
        cr.addArgument("pays", String.valueOf(ville.getPays().getId()));

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

    public int delete(int villeId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/ville/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(villeId));

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
