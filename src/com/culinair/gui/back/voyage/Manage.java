package com.culinair.gui.back.voyage;


import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.culinair.entities.Ville;
import com.culinair.entities.Voyage;
import com.culinair.gui.back.AccueilBack;
import com.culinair.services.VoyageService;

public class Manage extends Form {


    public static Ville selectedVille;
    Voyage currentVoyage;
    Label dateDepartLabel, dateRetourLabel, nbrVoyageursLabel, prixLabel, villeLabel;
    TextField
            nbrVoyageursTF,
            prixTF,
            villeTF, elemTF;
    PickerComponent dateDepartTF;
    PickerComponent dateRetourTF;

    Label selectedVilleLabel;
    Button selectVilleButton;


    Button manageButton;

    Form previous;

    public Manage(Form previous) {
        super(new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;


        selectedVille = null;

        currentVoyage = DisplayAll.currentVoyage;

        addGUIs();
        addActions();

        super.setToolbar(new Toolbar());
        super.setTitle(com.culinair.gui.back.voyage.DisplayAll.currentVoyage == null ? "Ajouter voyage" : "Modifier voyage");
        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }


    public void refreshVille() {
        selectedVilleLabel.setText(selectedVille.getVille());
        selectVilleButton.setText("Modifier le ville");
        this.refreshTheme();
    }


    private void addGUIs() {

        dateDepartTF = PickerComponent.createDate(null).label("DateDepart");
        dateRetourTF = PickerComponent.createDate(null).label("DateRetour");

        nbrVoyageursLabel = new Label("NbrVoyageurs : ");
        nbrVoyageursLabel.setUIID("labelDefault");
        nbrVoyageursTF = new TextField();
        nbrVoyageursTF.setHint("Tapez le nbrVoyageurs");

        prixLabel = new Label("Prix : ");
        prixLabel.setUIID("labelDefault");
        prixTF = new TextField();
        prixTF.setHint("Tapez le prix");

        villeLabel = new Label("Ville : ");
        villeLabel.setUIID("labelDefault");
        villeTF = new TextField();
        villeTF.setHint("Tapez le ville");


        villeLabel = new Label("ville : ");
        villeLabel.setUIID("labelDefault");
        selectedVilleLabel = new Label("vide");
        selectVilleButton = new Button("Choisir ville");
        selectVilleButton.addActionListener(l -> new ChooseVille(this).show());


        if (currentVoyage == null) {


            manageButton = new Button("Ajouter");
        } else {


            dateDepartTF.getPicker().setDate(currentVoyage.getDateDepart());
            dateRetourTF.getPicker().setDate(currentVoyage.getDateRetour());
            nbrVoyageursTF.setText(String.valueOf(currentVoyage.getNbrVoyageurs()));
            prixTF.setText(String.valueOf(currentVoyage.getPrix()));

            selectedVille = currentVoyage.getVille();

            villeLabel = new Label("ville : ");
            villeLabel.setUIID("labelDefault");
            selectedVilleLabel = new Label("null");
            selectedVilleLabel.setText(selectedVille.getVille());
            selectVilleButton.setText("Choisir ville");


            manageButton = new Button("Modifier");
        }

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(
                dateDepartTF,
                dateRetourTF,
                nbrVoyageursLabel, nbrVoyageursTF,
                prixLabel, prixTF,
                villeLabel,
                selectedVilleLabel, selectVilleButton,
                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {

        if (currentVoyage == null) {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = VoyageService.getInstance().add(
                            new Voyage(


                                    dateDepartTF.getPicker().getDate(),
                                    dateRetourTF.getPicker().getDate(),
                                    (int) Float.parseFloat(nbrVoyageursTF.getText()),
                                    Float.parseFloat(prixTF.getText()),
                                    selectedVille
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Voyage ajouté avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur d'ajout de voyage. Code d'erreur : " + responseCode, new Command("Ok"));
                    }
                }
            });
        } else {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = VoyageService.getInstance().edit(
                            new Voyage(
                                    currentVoyage.getId(),


                                    dateDepartTF.getPicker().getDate(),
                                    dateRetourTF.getPicker().getDate(),
                                    (int) Float.parseFloat(nbrVoyageursTF.getText()),
                                    Float.parseFloat(prixTF.getText()),
                                    selectedVille

                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Voyage modifié avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur de modification de voyage. Code d'erreur : " + responseCode, new Command("Ok"));
                    }
                }
            });
        }
    }

    private void showBackAndRefresh() {
        AccueilBack.accueilForm.addComponentAndRefresh(
                new com.culinair.gui.back.voyage.DisplayAll().addGUIs(),
                "Voyage"
        );
        previous.showBack();
    }

    private boolean controleDeSaisie() {


        if (dateDepartTF.getPicker().getDate() == null) {
            Dialog.show("Avertissement", "Veuillez saisir la dateDepart", new Command("Ok"));
            return false;
        }


        if (dateRetourTF.getPicker().getDate() == null) {
            Dialog.show("Avertissement", "Veuillez saisir la dateRetour", new Command("Ok"));
            return false;
        }


        if (nbrVoyageursTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le nbrVoyageurs", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(nbrVoyageursTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", nbrVoyageursTF.getText() + " n'est pas un nombre valide", new Command("Ok"));
            return false;
        }


        if (prixTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le prix", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(prixTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", prixTF.getText() + " n'est pas un nombre valide", new Command("Ok"));
            return false;
        }


        if (selectedVille == null) {
            Dialog.show("Avertissement", "Veuillez choisir un ville", new Command("Ok"));
            return false;
        }


        return true;
    }
}