package com.culinair.gui.back.activite;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.culinair.entities.Activite;
import com.culinair.entities.Transport;
import com.culinair.gui.back.AccueilBack;
import com.culinair.services.ActiviteService;

public class Manage extends Form {

    public static Transport selectedTransport;
    Activite currentActivite;

    Label nomLabel, typeActiviteLabel, descriptionLabel, tempsLabel, prixActiviteLabel, nombreParticipantLabel, transportLabel, selectedTransportLabel;
    TextField nomTF, typeActiviteTF, tempsTF, prixActiviteTF, nombreParticipantTF;
    TextArea descriptionTF;
    Button selectTransportButton, manageButton;

    Form previous;

    public Manage(Form previous) {
        super(DisplayAll.currentActivite == null ? "Ajouter une activite" : "Modifier l'activite", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        selectedTransport = null;
        currentActivite = DisplayAll.currentActivite;

        addGUIs();
        addActions();

        super.setToolbar(new Toolbar());
        super.setTitle(DisplayAll.currentActivite == null ? "Ajouter une activite" : "Modifier l'activite");
        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    public void refreshTransport() {
        selectedTransportLabel.setText(selectedTransport.getTypeTransport());
        selectTransportButton.setText("Modifier le transport");
        this.refreshTheme();
    }

    private void addGUIs() {

        nomLabel = new Label("Nom : ");
        nomLabel.setUIID("labelDefault");
        nomTF = new TextField();
        nomTF.setHint("Tapez le nom de l'activite");

        typeActiviteLabel = new Label("Type : ");
        typeActiviteLabel.setUIID("labelDefault");
        typeActiviteTF = new TextField();
        typeActiviteTF.setHint("Tapez le type de l'activite");

        descriptionLabel = new Label("Description : ");
        descriptionLabel.setUIID("labelDefault");
        descriptionTF = new TextArea();
        descriptionTF.setHint("Tapez la description de l'activite");

        tempsLabel = new Label("Temps : ");
        tempsLabel.setUIID("labelDefault");
        tempsTF = new TextField();
        tempsTF.setHint("Tapez le temps de l'activite");

        prixActiviteLabel = new Label("Prix : ");
        prixActiviteLabel.setUIID("labelDefault");
        prixActiviteTF = new TextField();
        prixActiviteTF.setHint("Tapez le prix de l'activite");

        nombreParticipantLabel = new Label("Nb participants : ");
        nombreParticipantLabel.setUIID("labelDefault");
        nombreParticipantTF = new TextField();
        nombreParticipantTF.setHint("Tapez le nobre de participants");

        if (currentActivite == null) {
            manageButton = new Button("Ajouter");
        } else {

            nomTF.setText(currentActivite.getNom());
            typeActiviteTF.setText(currentActivite.getTypeActivite());
            tempsTF.setText(String.valueOf(currentActivite.getTemps()));
            prixActiviteTF.setText(String.valueOf(currentActivite.getPrixActivite()));
            nombreParticipantTF.setText(String.valueOf(currentActivite.getNombreParticipant()));
            descriptionTF.setText(currentActivite.getDescription());

            selectedTransport = currentActivite.getTransport();

            manageButton = new Button("Modifier");
        }

        transportLabel = new Label("Transport : ");
        transportLabel.setUIID("labelDefault");
        if (selectedTransport != null) {
            selectedTransportLabel = new Label(selectedTransport.getTypeTransport());
            selectTransportButton = new Button("Modifier le transport");
        } else {
            selectedTransportLabel = new Label("Aucun transport selectionne");
            selectTransportButton = new Button("Choisir un transport");
        }

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(
                nomLabel, nomTF,
                typeActiviteLabel, typeActiviteTF,
                tempsLabel, tempsTF,
                prixActiviteLabel, prixActiviteTF,
                nombreParticipantLabel, nombreParticipantTF,
                descriptionLabel, descriptionTF,
                transportLabel, selectedTransportLabel, selectTransportButton,
                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {
        selectTransportButton.addActionListener(l -> new ChooseTransport(this).show());

        if (currentActivite == null) {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = ActiviteService.getInstance().add(
                            new Activite(
                                    nomTF.getText(),
                                    typeActiviteTF.getText(),
                                    descriptionTF.getText(),
                                    (int) Float.parseFloat(tempsTF.getText()),
                                    (int) Float.parseFloat(prixActiviteTF.getText()),
                                    (int) Float.parseFloat(nombreParticipantTF.getText()),
                                    selectedTransport
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succes", "Activite ajoute avec succes", new Command("Ok"));
                    } else {
                        Dialog.show("Erreur", "Erreur d'ajout de activite. Code d'erreur : " + responseCode, new Command("Ok"));
                    }

                    showBackAndRefresh();
                }
            });
        } else {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = ActiviteService.getInstance().edit(
                            new Activite(
                                    currentActivite.getId(),
                                    nomTF.getText(),
                                    typeActiviteTF.getText(),
                                    descriptionTF.getText(),
                                    (int) Float.parseFloat(tempsTF.getText()),
                                    (int) Float.parseFloat(prixActiviteTF.getText()),
                                    (int) Float.parseFloat(nombreParticipantTF.getText()),
                                    selectedTransport
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succes", "Activite modifie avec succes", new Command("Ok"));
                    } else {
                        Dialog.show("Erreur", "Erreur de modification de activite. Code d'erreur : " + responseCode, new Command("Ok"));
                    }

                    showBackAndRefresh();
                }
            });
        }
    }

    private void showBackAndRefresh() {
        AccueilBack.accueilForm.addComponentAndRefresh(
                new com.culinair.gui.back.activite.DisplayAll().addGUIs(),
                "Activites"
        );
        previous.showBack();
    }

    private boolean controleDeSaisie() {

        if (nomTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le nom", new Command("Ok"));
            return false;
        }

        if (typeActiviteTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le type", new Command("Ok"));
            return false;
        }

        if (tempsTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le temps", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(tempsTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", tempsTF.getText() + " n'est pas un nombre valide", new Command("Ok"));
            return false;
        }

        if (prixActiviteTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le prix", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(prixActiviteTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", prixActiviteTF.getText() + " n'est pas un prix valide", new Command("Ok"));
            return false;
        }

        if (nombreParticipantTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le nombre de participants", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(nombreParticipantTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", nombreParticipantTF.getText() + " n'est pas un nombre valide", new Command("Ok"));
            return false;
        }

        if (descriptionTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir la description", new Command("Ok"));
            return false;
        }

        if (selectedTransport == null) {
            Dialog.show("Avertissement", "Veuillez choisir une transport", new Command("Ok"));
            return false;
        }

        return true;
    }
}