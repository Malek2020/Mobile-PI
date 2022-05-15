package com.culinair.gui.back.activite;

import com.codename1.components.InteractionDialog;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.culinair.entities.Activite;
import com.culinair.gui.back.AccueilBack;
import com.culinair.services.ActiviteService;

import java.util.ArrayList;

public class DisplayAll extends Form {

    public static Activite currentActivite = null;
    Button addBtn;
    Label nomLabel, typeActiviteLabel, descriptionLabel, tempsLabel, prixActiviteLabel, nombreParticipantLabel, transportLabel;
    Button editBtn, deleteBtn;
    Container btnsContainer;

    public Component addGUIs() {
        Container compo = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        compo.setScrollableY(true);

        addBtn = new Button("Ajouter");
        addBtn.addActionListener(action -> {
            currentActivite = null;
            new Manage(AccueilBack.accueilForm).show();
        });

        compo.add(addBtn);

        ArrayList<Activite> listActivites = ActiviteService.getInstance().getAll();
        if (listActivites.size() > 0) {
            for (Activite listActivite : listActivites) {
                compo.add(makeActiviteModel(listActivite));
            }
        } else {
            compo.add(new Label("Aucune donnee"));
        }

        return compo;
    }

    private Component makeActiviteModel(Activite activite) {
        Container activiteModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        activiteModel.setUIID("containerRounded");

        nomLabel = new Label(activite.getNom());
        nomLabel.setUIID("labelCenter");

        typeActiviteLabel = new Label("Type : " + activite.getTypeActivite());
        typeActiviteLabel.setUIID("labelDefault");

        descriptionLabel = new Label("Description : " + activite.getDescription());
        descriptionLabel.setUIID("labelDefault");

        tempsLabel = new Label("Temps : " + activite.getTemps());
        tempsLabel.setUIID("labelDefault");

        prixActiviteLabel = new Label("Prix : " + activite.getPrixActivite());
        prixActiviteLabel.setUIID("labelDefault");

        nombreParticipantLabel = new Label("Nb de participants : " + activite.getNombreParticipant());
        nombreParticipantLabel.setUIID("labelDefault");

        if (activite.getTransport() != null) {
            transportLabel = new Label("Transport : " + activite.getTransport().getTypeTransport());
            transportLabel.setUIID("labelDefault");
        } else {
            transportLabel = new Label("Transport : null");
            transportLabel.setUIID("labelDefault");
        }

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        editBtn = new Button("Modifier");
        editBtn.addActionListener(action -> {
            currentActivite = activite;
            new Manage(AccueilBack.accueilForm).show();
        });

        deleteBtn = new Button("Supprimer");
        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce activite ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = ActiviteService.getInstance().delete(activite.getId());

                if (responseCode == 200) {
                    currentActivite = null;
                    dlg.dispose();
                    activiteModel.remove();
                    AccueilBack.accueilForm.revalidate();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du activite. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.WEST, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);

        activiteModel.addAll(
                nomLabel, typeActiviteLabel, descriptionLabel, tempsLabel, prixActiviteLabel, nombreParticipantLabel, transportLabel,
                btnsContainer
        );

        return activiteModel;
    }
}
