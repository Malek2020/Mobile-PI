package com.culinair.gui.front.ville;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.culinair.entities.Ville;
import com.culinair.services.VilleService;

import java.util.ArrayList;

public class DisplayAll extends Form {


    public static Ville currentVille = null;

    Label villeLabel, paysLabel;
    Container btnsContainer;

    public Component addGUIs() {
        Container compo = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        compo.setScrollableY(true);

        ArrayList<Ville> listVilles = VilleService.getInstance().getAll();

        if (listVilles.size() > 0) {
            for (Ville ville : listVilles) {
                Component model = makeVilleModel(ville);
                compo.add(model);
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

        villeModel.addAll(
                villeLabel, paysLabel,
                btnsContainer
        );

        return villeModel;
    }
}