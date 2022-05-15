package com.culinair.gui.front.transport;

import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.culinair.entities.Transport;
import com.culinair.services.TransportService;

import java.util.ArrayList;

public class DisplayAll extends Form {

    public static Transport currentTransport = null;
    Label dureeTransportLabel, typeTransportLabel, capaciteLabel;
    Container btnsContainer;

    public Component addGUIs() {
        Container compo = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        compo.setScrollableY(true);

        ArrayList<Transport> listTransports = TransportService.getInstance().getAll();
        if (listTransports.size() > 0) {
            for (Transport listTransport : listTransports) {
                compo.add(makeTransportModel(listTransport));
            }
        } else {
            compo.add(new Label("Aucune donnee"));
        }

        return compo;
    }

    private Component makeTransportModel(Transport transport) {
        Container transportModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        transportModel.setUIID("containerRounded");

        dureeTransportLabel = new Label("Duree : " + transport.getDureeTransport());
        dureeTransportLabel.setUIID("labelDefault");

        typeTransportLabel = new Label("Type : " + transport.getTypeTransport());
        typeTransportLabel.setUIID("labelDefault");

        capaciteLabel = new Label("Capacite : " + transport.getCapacite());
        capaciteLabel.setUIID("labelDefault");

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        transportModel.addAll(
                dureeTransportLabel, typeTransportLabel, capaciteLabel,
                btnsContainer
        );

        return transportModel;
    }
}