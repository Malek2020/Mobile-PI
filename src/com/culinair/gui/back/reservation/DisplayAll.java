package com.culinair.gui.back.reservation;

import com.codename1.components.ImageViewer;
import com.codename1.components.SpanLabel;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.culinair.entities.Plat;
import com.culinair.entities.Reservation;
import com.culinair.services.ReservationService;
import com.culinair.utils.Statics;

import java.util.ArrayList;

public class DisplayAll {

    Resources theme = UIManager.initFirstTheme("/theme");
    TextField searchTF;
    ArrayList<Component> componentModels;
    public static Reservation currentReservation = null;

    public Component addGUIs() {
        Container compo = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        compo.setScrollableY(true);

        ArrayList<Reservation> listReservations = ReservationService.getInstance().getAll();
        componentModels = new ArrayList<>();

        searchTF = new TextField("", "Chercher un Reservation");
        searchTF.addDataChangedListener((d, t) -> {
            if (componentModels.size() > 0) {
                for (int i = 0; i < componentModels.size(); i++) {
                    compo.removeComponent(componentModels.get(i));
                    componentModels.remove(i);
                }
            }
            for (int i = 0; i < listReservations.size(); i++) {
                if (listReservations.get(i).getdestination().startsWith(searchTF.getText())) {
                    Component model = makeReservationModel(listReservations.get(i));
                    compo.add(model);
                    componentModels.add(model);
                }
            }
            compo.revalidate();
        });
        compo.add(searchTF);

        if (listReservations.size() > 0) {
            for (Reservation listReservation : listReservations) {
                Component model = makeReservationModel(listReservation);
                compo.add(model);
                componentModels.add(model);
            }
        } else {
            compo.add(new Label("Aucune donnee"));
        }

        return compo;
    }

    Label  num_volLabel, destinationLabel, date_departLabel, date_arriveeLabel, GuideLabel,  adulteLabel,enfantLabel;
    Container btnsContainer;

    private Component makeReservationModel(Reservation Reservation) {
        Container ReservationModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        ReservationModel.setUIID("containerRounded");
        
        num_volLabel = new Label(String.valueOf(Reservation.getnum_vol()));
        num_volLabel.setUIID("labelDefault");
        
        destinationLabel = new Label("Type de cuisine : " + Reservation.getdestination());
        destinationLabel.setUIID("labelDefault");

        date_departLabel = new Label("Type de cuisine : " + Reservation.getdate_depart());
        date_departLabel.setUIID("labelDefault");
        
        date_arriveeLabel = new Label("Type de cuisine : " + Reservation.getdate_arrivee());
        date_arriveeLabel.setUIID("labelDefault");
        
        adulteLabel = new Label(String.valueOf(Reservation.getadulte()));
        adulteLabel.setUIID("labelDefault");

        enfantLabel = new Label(String.valueOf(Reservation.getenfant()));
        enfantLabel.setUIID("labelDefault");

        if (Reservation.getGuide() != null) {
            GuideLabel = new Label("Guide : " + Reservation.getGuide().getNom());
            GuideLabel.setUIID("labelDefault");
        } else {
            GuideLabel = new Label("Restaurant : null");
        }



        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        ReservationModel.addAll(
                num_volLabel, destinationLabel, date_departLabel, date_arriveeLabel, GuideLabel,  adulteLabel,enfantLabel,
                btnsContainer
        );

        return ReservationModel;
    }
}
