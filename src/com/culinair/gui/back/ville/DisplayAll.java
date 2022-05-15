package com.culinair.gui.back.ville;

import com.codename1.components.InteractionDialog;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.culinair.entities.Ville;
import com.culinair.gui.back.AccueilBack;
import com.culinair.services.VilleService;

import java.util.ArrayList;

public class DisplayAll extends Form {


    public static Ville currentVille = null;
    Button addBtn;
    Label villeLabel, paysLabel;
    Button editBtn, deleteBtn;
    Container btnsContainer;

    public Component addGUIs() {
        Container compo = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        compo.setScrollableY(true);

        addBtn = new Button("Ajouter");
        addBtn.addActionListener(action -> {
            currentVille = null;
            new Manage(AccueilBack.accueilForm).show();
        });

        compo.add(addBtn);

        ArrayList<Ville> listVilles = VilleService.getInstance().getAll();
        if (listVilles.size() > 0) {
            for (Ville listVille : listVilles) {
                compo.add(makeVilleModel(listVille));
            }
        } else {
            compo.add(new Label("Aucune donnée"));
        }

        return compo;
    }

    private Component makeVilleModel(Ville ville) {
        Container villeModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        villeModel.setUIID("containerRounded");

        villeLabel = new Label("Ville : " + ville.getVille());
        villeLabel.setUIID("labelDefault");

        paysLabel = new Label("Pays : " + ville.getPays().getNomPays());
        paysLabel.setUIID("labelDefault");

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        editBtn = new Button("Modifier");
        editBtn.addActionListener(action -> {
            currentVille = ville;
            new Manage(AccueilBack.accueilForm).show();
        });

        deleteBtn = new Button("Supprimer");
        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce ville ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = VilleService.getInstance().delete(ville.getId());

                if (responseCode == 200) {
                    currentVille = null;
                    dlg.dispose();
                    villeModel.remove();
                    this.refreshTheme();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du ville. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.WEST, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);

        villeModel.addAll(
                villeLabel, paysLabel,
                btnsContainer
        );

        return villeModel;
    }
}