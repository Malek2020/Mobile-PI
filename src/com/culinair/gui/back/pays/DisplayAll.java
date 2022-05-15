package com.culinair.gui.back.pays;

import com.codename1.components.InteractionDialog;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.culinair.entities.Pays;
import com.culinair.gui.back.AccueilBack;
import com.culinair.services.PaysService;

import java.util.ArrayList;

public class DisplayAll extends Form {


    public static Pays currentPays = null;
    Button addBtn;
    Label nomPaysLabel;
    Button editBtn, deleteBtn;
    Container btnsContainer;

    public Component addGUIs() {
        Container compo = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        compo.setScrollableY(true);

        addBtn = new Button("Ajouter");
        addBtn.addActionListener(action -> {
            currentPays = null;
            new Manage(AccueilBack.accueilForm).show();
        });

        compo.add(addBtn);

        ArrayList<Pays> listPayss = PaysService.getInstance().getAll();
        if (listPayss.size() > 0) {
            for (Pays listPays : listPayss) {
                compo.add(makePaysModel(listPays));
            }
        } else {
            compo.add(new Label("Aucune donnée"));
        }

        return compo;
    }

    private Component makePaysModel(Pays pays) {
        Container paysModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        paysModel.setUIID("containerRounded");


        nomPaysLabel = new Label("NomPays : " + pays.getNomPays());
        nomPaysLabel.setUIID("labelDefault");


        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        editBtn = new Button("Modifier");
        editBtn.addActionListener(action -> {
            currentPays = pays;
            new Manage(AccueilBack.accueilForm).show();
        });

        deleteBtn = new Button("Supprimer");
        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce pays ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = PaysService.getInstance().delete(pays.getId());

                if (responseCode == 200) {
                    currentPays = null;
                    dlg.dispose();
                    paysModel.remove();
                    this.refreshTheme();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du pays. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.WEST, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);

        paysModel.addAll(
                nomPaysLabel,

                btnsContainer
        );

        return paysModel;
    }
}