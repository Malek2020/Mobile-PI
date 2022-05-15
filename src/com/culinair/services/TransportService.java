package com.culinair.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import com.culinair.entities.Transport;
import com.culinair.utils.Statics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TransportService {

    public static TransportService instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<Transport> listTransports;

    private TransportService() {
        cr = new ConnectionRequest();
    }

    public static TransportService getInstance() {
        if (instance == null) {
            instance = new TransportService();
        }
        return instance;
    }

    public ArrayList<Transport> getAll() {
        listTransports = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/transport");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listTransports = getList();
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

        return listTransports;
    }

    private ArrayList<Transport> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                Transport transport = new Transport(
                        (int) Float.parseFloat(obj.get("id").toString()),
                        (int) Float.parseFloat(obj.get("dureeTransport").toString()),
                        (String) obj.get("typeTransport"),
                        (int) Float.parseFloat(obj.get("capacite").toString())
                );

                listTransports.add(transport);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return listTransports;
    }

    public int add(Transport transport) {
        return manage(transport, false);
    }

    public int edit(Transport transport) {
        return manage(transport, true);
    }

    public int manage(Transport transport, boolean isEdit) {
        cr = new ConnectionRequest();
        cr.setHttpMethod("POST");

        if (isEdit) {
            cr.setUrl(Statics.BASE_URL + "/transport/edit");
            cr.addArgument("id", String.valueOf(transport.getId()));
        } else {
            cr.setUrl(Statics.BASE_URL + "/transport/add");
        }

        cr.addArgument("dureeTransport", String.valueOf(transport.getDureeTransport()));
        cr.addArgument("typeTransport", transport.getTypeTransport());
        cr.addArgument("capacite", String.valueOf(transport.getCapacite()));

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

    public int delete(int transportId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/transport/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(transportId));

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
