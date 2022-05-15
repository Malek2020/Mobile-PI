package com.culinair.gui.back.voyage;

import com.codename1.components.InteractionDialog;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.culinair.entities.Voyage;
import com.culinair.gui.back.AccueilBack;
import com.culinair.services.VoyageService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DisplayAll extends Form {


    public static Voyage currentVoyage = null;
    Button addBtn;
    Label dateDepartLabel, dateRetourLabel, nbrVoyageursLabel, prixLabel, villeLabel;
    Button editBtn, deleteBtn;
    Container btnsContainer;

    public Component addGUIs() {
        Container compo = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        compo.setScrollableY(true);

        addBtn = new Button("Ajouter");
        addBtn.addActionListener(action -> {
            currentVoyage = null;
            new Manage(AccueilBack.accueilForm).show();
        });

        compo.add(addBtn);

        ArrayList<Voyage> listVoyages = VoyageService.getInstance().getAll();
        if (listVoyages.size() > 0) {
            for (Voyage listVoyage : listVoyages) {
                compo.add(makeVoyageModel(listVoyage));
            }
        } else {
            compo.add(new Label("Aucune donnée"));
        }

        return compo;
    }

    private Component makeVoyageModel(Voyage voyage) {
        Container voyageModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        voyageModel.setUIID("containerRounded");


        dateDepartLabel = new Label("DateDepart : " + new SimpleDateFormat("dd-MM-yyyy").format(voyage.getDateDepart()));
        dateDepartLabel.setUIID("labelDefault");

        dateRetourLabel = new Label("DateRetour : " + new SimpleDateFormat("dd-MM-yyyy").format(voyage.getDateRetour()));
        dateRetourLabel.setUIID("labelDefault");

        nbrVoyageursLabel = new Label("NbrVoyageurs : " + voyage.getNbrVoyageurs());
        nbrVoyageursLabel.setUIID("labelDefault");

        prixLabel = new Label("Prix : " + voyage.getPrix());
        prixLabel.setUIID("labelDefault");


        villeLabel = new Label("Ville : " + voyage.getVille().getVille());
        villeLabel.setUIID("labelDefault");


        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        editBtn = new Button("Modifier");
        editBtn.addActionListener(action -> {
            currentVoyage = voyage;
            new Manage(AccueilBack.accueilForm).show();
        });

        deleteBtn = new Button("Supprimer");
        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce voyage ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = VoyageService.getInstance().delete(voyage.getId());

                if (responseCode == 200) {
                    currentVoyage = null;
                    dlg.dispose();
                    voyageModel.remove();
                    this.refreshTheme();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du voyage. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.WEST, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);

        voyageModel.addAll(
                dateDepartLabel, dateRetourLabel, nbrVoyageursLabel, prixLabel, villeLabel,

                btnsContainer
        );

        return voyageModel;
    }
}