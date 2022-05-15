package com.culinair.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.io.rest.Response;
import com.codename1.io.rest.Rest;
import static com.codename1.ui.events.ActionEvent.Type.Response;
import com.codename1.ui.events.ActionListener;
import com.codename1.util.Base64;
import com.culinair.entities.Reservation;
import com.culinair.entities.Guide;
import com.culinair.utils.Statics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReservationService {

    public static ReservationService instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<Reservation> listReservations;

    private ReservationService() {
        cr = new ConnectionRequest();
    }

    public static ReservationService getInstance() {
        if (instance == null) {
            instance = new ReservationService();
        }
        return instance;
    }

    public ArrayList<Reservation> getAll() {
        listReservations = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/reservation");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listReservations = getList();
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

        return listReservations;
    }

    private ArrayList<Reservation> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                Reservation reservation = new Reservation(
                        (int) Float.parseFloat(obj.get("id").toString()),
                        Float.parseFloat(obj.get("num_vol").toString()),
                        (String) obj.get("destination"),
                        (String) obj.get("date_depart"),
                        (String) obj.get("date-arrivee"),
                        Float.parseFloat(obj.get("adulte").toString()),
                        Float.parseFloat(obj.get("enfant").toString()),
                        makeGuide((Map<String, Object>) obj.get("guide"))
                );

                listReservations.add(reservation);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return listReservations;
    }

    public Guide makeGuide(Map<String, Object> obj) {
        if (obj == null) {
            return null;
        }

        try {
            return new Guide(
                         (int) Float.parseFloat(obj.get("id").toString()),
                        (String) obj.get("nom"),
                        (String) obj.get("prenom"),
                        (String) obj.get("description"),
                        (String) obj.get("image")
            );
        } catch (NullPointerException e) {
            return new Guide(
                    0,
                    "null",
                    "null",
                    "null",
                    "null"
                    
                  

            );
        }
    }

    public int add(Reservation reservation) {
        sendSms();
        return manage(reservation, false, true);
    }

    public int edit(Reservation reservation, boolean imageEdited) {
        return manage(reservation, true, imageEdited);
    }

    public int manage(Reservation reservation, boolean isEdit, boolean imageEdited) {
         cr = new MultipartRequest();
        cr.setHttpMethod("POST");

            cr.setUrl(Statics.BASE_URL + "/reservation/add");

        cr.addArgument("num_vol", String.valueOf(reservation.getnum_vol()));
        cr.addArgument("destination", reservation.getdestination());
        cr.addArgument("date_depart", reservation.getdate_depart());
        cr.addArgument("date_arrivee", reservation.getdate_arrivee());
        cr.addArgument("adulte", String.valueOf(reservation.getadulte()));
        cr.addArgument("enfant", String.valueOf(reservation.getenfant()));
        cr.addArgument("guide", String.valueOf(reservation.getGuide().getId()));


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

    public int delete(int reservationId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/reservation/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(reservationId));

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
    public void sendSms() {
     String accountSID = "AC5ae12f720f77d17914b24ca11bb66341";
String authToken = "99a7f6a3fb8bebe36fc83ba4f13b9314";
String fromPhone = "+19302123925";



 Response<Map> smsresult = Rest.post("https://api.twilio.com/2010-04-01/Accounts/" + accountSID + "/Messages.json").
                        queryParam("To", "+21644196276").
                        queryParam("From", fromPhone).
                        queryParam("Body", " resrevation effectuee avec succes ").
                        header("Authorization", "Basic " + Base64.encodeNoNewline((accountSID + ":" + authToken).getBytes())).
                        getAsJsonMap();;



}
}
