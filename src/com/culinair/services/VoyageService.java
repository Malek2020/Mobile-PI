package com.culinair.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import com.culinair.entities.Ville;
import com.culinair.entities.Voyage;
import com.culinair.utils.Statics;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VoyageService {

    public static VoyageService instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<Voyage> listVoyages;

    private VoyageService() {
        cr = new ConnectionRequest();
    }

    public static VoyageService getInstance() {
        if (instance == null) {
            instance = new VoyageService();
        }
        return instance;
    }

    public ArrayList<Voyage> getAll() {
        listVoyages = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/voyage");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listVoyages = getList();
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

        return listVoyages;
    }

    private ArrayList<Voyage> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                Voyage voyage = new Voyage(
                        (int) Float.parseFloat(obj.get("id").toString()),

                        new SimpleDateFormat("dd-MM-yyyy").parse((String) obj.get("dateDepart")),
                        new SimpleDateFormat("dd-MM-yyyy").parse((String) obj.get("dateRetour")),
                        (int) Float.parseFloat(obj.get("nbrVoyageurs").toString()),
                        Float.parseFloat(obj.get("prix").toString()),
                        makeVille((Map<String, Object>) obj.get("ville"))

                );

                listVoyages.add(voyage);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return listVoyages;
    }


    public Ville makeVille(Map<String, Object> obj) {
        if (obj == null) {
            return null;
        }

        return new Ville(
                (int) Float.parseFloat(obj.get("id").toString()),
                (String) obj.get("ville"),
                null
        );
    }


    public int add(Voyage voyage) {
        return manage(voyage, false);
    }

    public int edit(Voyage voyage) {
        return manage(voyage, true);
    }

    public int manage(Voyage voyage, boolean isEdit) {

        cr = new ConnectionRequest();


        cr.setHttpMethod("POST");
        if (isEdit) {
            cr.setUrl(Statics.BASE_URL + "/voyage/edit");
            cr.addArgument("id", String.valueOf(voyage.getId()));
        } else {
            cr.setUrl(Statics.BASE_URL + "/voyage/add");
        }
        cr.addArgument("dateDepart", new SimpleDateFormat("dd-MM-yyyy").format(voyage.getDateDepart()));
        cr.addArgument("dateRetour", new SimpleDateFormat("dd-MM-yyyy").format(voyage.getDateRetour()));
        cr.addArgument("nbrVoyageurs", String.valueOf(voyage.getNbrVoyageurs()));
        cr.addArgument("prix", String.valueOf(voyage.getPrix()));
        cr.addArgument("ville", String.valueOf(voyage.getVille().getId()));

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

    public int delete(int voyageId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/voyage/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(voyageId));

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
