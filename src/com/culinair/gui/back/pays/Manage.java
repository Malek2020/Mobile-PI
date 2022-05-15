package com.culinair.gui.back.pays;


import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.culinair.entities.Pays;
import com.culinair.gui.back.AccueilBack;
import com.culinair.services.PaysService;

public class Manage extends Form {


    Pays currentPays;


    Label nomPaysLabel;
    TextField
            nomPaysTF, elemTF;


    Button manageButton;

    Form previous;

    public Manage(Form previous) {
        super(new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;


        currentPays = DisplayAll.currentPays;

        addGUIs();
        addActions();

        super.setToolbar(new Toolbar());
        super.setTitle(com.culinair.gui.back.pays.DisplayAll.currentPays == null ? "Ajouter pays" : "Modifier pays");
        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }


    private void addGUIs() {


        nomPaysLabel = new Label("NomPays : ");
        nomPaysLabel.setUIID("labelDefault");
        nomPaysTF = new TextField();
        nomPaysTF.setHint("Tapez le nomPays");


        if (currentPays == null) {


            manageButton = new Button("Ajouter");
        } else {


            nomPaysTF.setText(currentPays.getNomPays());


            manageButton = new Button("Modifier");
        }

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(
                nomPaysLabel, nomPaysTF,

                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {

        if (currentPays == null) {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = PaysService.getInstance().add(
                            new Pays(


                                    nomPaysTF.getText()
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Pays ajouté avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur d'ajout de pays. Code d'erreur : " + responseCode, new Command("Ok"));
                    }
                }
            });
        } else {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = PaysService.getInstance().edit(
                            new Pays(
                                    currentPays.getId(),


                                    nomPaysTF.getText()

                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Pays modifié avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur de modification de pays. Code d'erreur : " + responseCode, new Command("Ok"));
                    }
                }
            });
        }
    }

    private void showBackAndRefresh() {
        AccueilBack.accueilForm.addComponentAndRefresh(
                new com.culinair.gui.back.pays.DisplayAll().addGUIs(),
                "Pays"
        );
        previous.showBack();
    }

    private boolean controleDeSaisie() {


        if (nomPaysTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le nomPays", new Command("Ok"));
            return false;
        }


        return true;
    }
}