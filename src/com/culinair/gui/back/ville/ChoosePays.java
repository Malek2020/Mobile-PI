package com.culinair.gui.back.ville;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.culinair.entities.Pays;
import com.culinair.services.PaysService;

import java.util.ArrayList;

public class ChoosePays extends Form {

    Form previousForm;
    Label nomPaysLabel;
    Button chooseBtn;
    Container btnsContainer;


    public ChoosePays(Form previous) {
        super("Choisir un pays", new BoxLayout(BoxLayout.Y_AXIS));

        previousForm = previous;
        addGUIs();

        super.getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    public void refresh() {
        this.removeAll();
        addGUIs();
        this.refreshTheme();
    }

    private void addGUIs() {
        ArrayList<Pays> listPayss = PaysService.getInstance().getAll();
        if (listPayss.size() > 0) {
            for (Pays payss : listPayss) {
                this.add(makePaysModel(payss));
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private Component makePaysModel(Pays pays) {
        Container paysModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        paysModel.setUIID("containerRounded");


        nomPaysLabel = new Label("NomPays : " + pays.getNomPays());
        nomPaysLabel.setUIID("labelDefault");


        chooseBtn = new Button("Choisir");
        chooseBtn.addActionListener(l -> {
            Manage.selectedPays = pays;
            ((Manage) previousForm).refreshPays();
            previousForm.showBack();
        });

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");
        btnsContainer.add(BorderLayout.CENTER, chooseBtn);

        paysModel.addAll(
                nomPaysLabel,

                btnsContainer
        );
        return paysModel;
    }
}