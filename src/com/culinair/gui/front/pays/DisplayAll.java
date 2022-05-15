package com.culinair.gui.front.pays;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.culinair.entities.Pays;
import com.culinair.services.PaysService;

import java.util.ArrayList;

public class DisplayAll extends Form {


    public static Pays currentPays = null;

    Label nomPaysLabel;
    Container btnsContainer;

    public Component addGUIs() {
        Container compo = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        compo.setScrollableY(true);

        ArrayList<Pays> listPayss = PaysService.getInstance().getAll();

        if (listPayss.size() > 0) {
            for (Pays pays : listPayss) {
                Component model = makePaysModel(pays);
                compo.add(model);
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

        paysModel.addAll(
                nomPaysLabel,


                btnsContainer
        );

        return paysModel;
    }
}