package com.culinair.gui.back.ville;


import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.culinair.entities.Pays;
import com.culinair.entities.Ville;
import com.culinair.gui.back.AccueilBack;
import com.culinair.services.VilleService;

public class Manage extends Form {


    public static Pays selectedPays;
    Ville currentVille;
    Label villeLabel, paysLabel;
    TextField
            villeTF,
            paysTF, elemTF;


    Label selectedPaysLabel;
    Button selectPaysButton;


    Button manageButton;

    Form previous;

    public Manage(Form previous) {
        super(new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;


        selectedPays = null;

        currentVille = DisplayAll.currentVille;

        addGUIs();
        addActions();

        super.setToolbar(new Toolbar());
        super.setTitle(com.culinair.gui.back.ville.DisplayAll.currentVille == null ? "Ajouter ville" : "Modifier ville");
        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }


    public void refreshPays() {
        selectedPaysLabel.setText(selectedPays.getNomPays());
        selectPaysButton.setText("Modifier le pays");
        this.refreshTheme();
    }


    private void addGUIs() {


        villeLabel = new Label("Ville : ");
        villeLabel.setUIID("labelDefault");
        villeTF = new TextField();
        villeTF.setHint("Tapez le ville");

        paysLabel = new Label("Pays : ");
        paysLabel.setUIID("labelDefault");
        paysTF = new TextField();
        paysTF.setHint("Tapez le pays");


        paysLabel = new Label("pays : ");
        paysLabel.setUIID("labelDefault");
        selectedPaysLabel = new Label("vide");
        selectPaysButton = new Button("Choisir pays");
        selectPaysButton.addActionListener(l -> new ChoosePays(this).show());


        if (currentVille == null) {


            manageButton = new Button("Ajouter");
        } else {


            villeTF.setText(currentVille.getVille());

            selectedPays = currentVille.getPays();

            paysLabel = new Label("pays : ");
            paysLabel.setUIID("labelDefault");
            selectedPaysLabel = new Label("null");
            selectedPaysLabel.setText(selectedPays.getNomPays());
            selectPaysButton.setText("Choisir pays");


            manageButton = new Button("Modifier");
        }

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(
                villeLabel, villeTF,
                paysLabel,
                selectedPaysLabel, selectPaysButton,
                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {

        if (currentVille == null) {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = VilleService.getInstance().add(
                            new Ville(


                                    villeTF.getText(),
                                    selectedPays
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Ville ajouté avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur d'ajout de ville. Code d'erreur : " + responseCode, new Command("Ok"));
                    }
                }
            });
        } else {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = VilleService.getInstance().edit(
                            new Ville(
                                    currentVille.getId(),


                                    villeTF.getText(),
                                    selectedPays

                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Ville modifié avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur de modification de ville. Code d'erreur : " + responseCode, new Command("Ok"));
                    }
                }
            });
        }
    }

    private void showBackAndRefresh() {
        AccueilBack.accueilForm.addComponentAndRefresh(
                new com.culinair.gui.back.ville.DisplayAll().addGUIs(),
                "Ville"
        );
        previous.showBack();
    }

    private boolean controleDeSaisie() {


        if (villeTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le ville", new Command("Ok"));
            return false;
        }


        if (selectedPays == null) {
            Dialog.show("Avertissement", "Veuillez choisir un pays", new Command("Ok"));
            return false;
        }


        return true;
    }
}