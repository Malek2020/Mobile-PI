package com.culinair.gui.back.activite;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.culinair.entities.Transport;
import com.culinair.services.TransportService;

import java.util.ArrayList;

public class ChooseTransport extends Form {

    Form previousForm;
    Button addBtn;
    Label dureeTransportLabel, typeTransportLabel, capaciteLabel;
    Button chooseBtn;
    Container btnsContainer;

    public ChooseTransport(Form previous) {
        super("Choisir un transport", new BoxLayout(BoxLayout.Y_AXIS));

        previousForm = previous;
        addGUIs();
        addActions();

        super.setToolbar(new Toolbar());
        super.setTitle("Choisir un transport");
        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    public void refresh() {
        this.removeAll();
        addGUIs();
        addActions();
        this.refreshTheme();
    }

    private void addGUIs() {
        addBtn = new Button("Ajouter transport");
        this.add(addBtn);

        ArrayList<Transport> listTransports = TransportService.getInstance().getAll();
        if (listTransports.size() > 0) {
            for (Transport transports : listTransports) {
                this.add(makeTransportModel(transports));
            }
        } else {
            this.add(new Label("Aucune donnee"));
        }
    }

    private void addActions() {
        addBtn.addActionListener(action -> new com.culinair.gui.back.transport.Manage(this, true).show());
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

        chooseBtn = new Button("Choisir");
        chooseBtn.addActionListener(l -> {
            Manage.selectedTransport = transport;
            ((Manage) previousForm).refreshTransport();
            previousForm.showBack();
        });

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");
        btnsContainer.add(BorderLayout.CENTER, chooseBtn);

        transportModel.addAll(dureeTransportLabel, typeTransportLabel, capaciteLabel, btnsContainer);

        return transportModel;
    }
}