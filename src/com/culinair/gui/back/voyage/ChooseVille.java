package com.culinair.gui.back.voyage;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.culinair.entities.Ville;
import com.culinair.services.VilleService;

import java.util.ArrayList;

public class ChooseVille extends Form {

    Form previousForm;
    Label villeLabel, paysLabel;
    Button chooseBtn;
    Container btnsContainer;

    public ChooseVille(Form previous) {
        super("Choisir un ville", new BoxLayout(BoxLayout.Y_AXIS));

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
        ArrayList<Ville> listVilles = VilleService.getInstance().getAll();
        if (listVilles.size() > 0) {
            for (Ville villes : listVilles) {
                this.add(makeVilleModel(villes));
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private Component makeVilleModel(Ville ville) {
        Container villeModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        villeModel.setUIID("containerRounded");

        villeLabel = new Label("Ville : " + ville.getVille());
        villeLabel.setUIID("labelDefault");

        paysLabel = new Label("Pays : " + ville.getPays().getNomPays());
        paysLabel.setUIID("labelDefault");

        chooseBtn = new Button("Choisir");
        chooseBtn.addActionListener(l -> {
            Manage.selectedVille = ville;
            ((Manage) previousForm).refreshVille();
            previousForm.showBack();
        });

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");
        btnsContainer.add(BorderLayout.CENTER, chooseBtn);

        villeModel.addAll(
                villeLabel, paysLabel,
                btnsContainer
        );

        return villeModel;
    }
}