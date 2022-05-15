package com.culinair.gui.back.transport;

import com.codename1.components.InteractionDialog;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.culinair.entities.Transport;
import com.culinair.gui.back.AccueilBack;
import com.culinair.services.TransportService;

import java.util.ArrayList;

public class DisplayAll extends Form {

    public static Transport currentTransport = null;
    Button addBtn;
    Label dureeTransportLabel, typeTransportLabel, capaciteLabel;
    Button editBtn, deleteBtn;
    Container btnsContainer;

    public Component addGUIs() {
        Container compo = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        compo.setScrollableY(true);

        addBtn = new Button("Ajouter");
        addBtn.addActionListener(action -> {
            currentTransport = null;
            new Manage(AccueilBack.accueilForm, false).show();
        });

        compo.add(addBtn);

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

        editBtn = new Button("Modifier");
        editBtn.addActionListener(action -> {
            currentTransport = transport;
            new Manage(AccueilBack.accueilForm, false).show();
        });

        deleteBtn = new Button("Supprimer");
        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce transport ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = TransportService.getInstance().delete(transport.getId());

                if (responseCode == 200) {
                    currentTransport = null;
                    dlg.dispose();
                    transportModel.remove();
                    AccueilBack.accueilForm.revalidate();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du transport. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.WEST, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);

        transportModel.addAll(
                dureeTransportLabel, typeTransportLabel, capaciteLabel,
                btnsContainer
        );

        return transportModel;
    }
}