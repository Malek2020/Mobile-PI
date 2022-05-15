package com.culinair.gui.back.transport;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.culinair.entities.Transport;
import com.culinair.gui.back.AccueilBack;
import com.culinair.gui.back.activite.ChooseTransport;
import com.culinair.services.TransportService;

public class Manage extends Form {

    Transport currentTransport;
    boolean isChoose;

    Label dureeTransportLabel, typeTransportLabel, capaciteLabel;
    TextField dureeTransportTF, typeTransportTF, capaciteTF;
    Button manageButton;

    Form previous;

    public Manage(Form previous, boolean isChoose) {
        super(DisplayAll.currentTransport == null ? "Ajouter un transport" : "Modifier le transport", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;
        this.isChoose = isChoose;

        currentTransport = DisplayAll.currentTransport;

        addGUIs();
        addActions();

        super.setToolbar(new Toolbar());
        super.setTitle(DisplayAll.currentTransport == null ? "Ajouter un transport" : "Modifier le transport");
        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    private void addGUIs() {

        dureeTransportLabel = new Label("Duree : ");
        dureeTransportLabel.setUIID("labelDefault");
        dureeTransportTF = new TextField();
        dureeTransportTF.setHint("Tapez la duree de transport");

        typeTransportLabel = new Label("Type : ");
        typeTransportLabel.setUIID("labelDefault");
        typeTransportTF = new TextField();
        typeTransportTF.setHint("Tapez le type de transport");

        capaciteLabel = new Label("Capacite : ");
        capaciteLabel.setUIID("labelDefault");
        capaciteTF = new TextField();
        capaciteTF.setHint("Tapez la capacite de transport");

        if (currentTransport == null) {
            manageButton = new Button("Ajouter");
        } else {
            dureeTransportTF.setText(String.valueOf(currentTransport.getDureeTransport()));
            typeTransportTF.setText(currentTransport.getTypeTransport());
            capaciteTF.setText(String.valueOf(currentTransport.getCapacite()));

            manageButton = new Button("Modifier");
        }

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(
                dureeTransportLabel, dureeTransportTF,
                typeTransportLabel, typeTransportTF,
                capaciteLabel, capaciteTF,
                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {
        if (currentTransport == null) {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = TransportService.getInstance().add(
                            new Transport(
                                    (int) Float.parseFloat(dureeTransportTF.getText()),
                                    typeTransportTF.getText(),
                                    (int) Float.parseFloat(capaciteTF.getText())
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succes", "Transport ajoute avec succes", new Command("Ok"));
                    } else {
                        Dialog.show("Erreur", "Erreur d'ajout de transport. Code d'erreur : " + responseCode, new Command("Ok"));
                    }

                    showBackAndRefresh();
                }
            });
        } else {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = TransportService.getInstance().edit(
                            new Transport(
                                    currentTransport.getId(),
                                    (int) Float.parseFloat(dureeTransportTF.getText()),
                                    typeTransportTF.getText(),
                                    (int) Float.parseFloat(capaciteTF.getText())
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succes", "Transport modifie avec succes", new Command("Ok"));
                    } else {
                        Dialog.show("Erreur", "Erreur de modification de transport. Code d'erreur : " + responseCode, new Command("Ok"));
                    }

                    showBackAndRefresh();
                }
            });
        }
    }

    private void showBackAndRefresh() {
        if (isChoose) {
            ((ChooseTransport) previous).refresh();
        } else {
            AccueilBack.accueilForm.addComponentAndRefresh(
                    new com.culinair.gui.back.transport.DisplayAll().addGUIs(),
                    "Transport"
            );
        }
        previous.showBack();
    }

    private boolean controleDeSaisie() {

        if (dureeTransportTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir la duree", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(dureeTransportTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", dureeTransportTF.getText() + " n'est pas un nombre valide", new Command("Ok"));
            return false;
        }

        if (typeTransportTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le type", new Command("Ok"));
            return false;
        }

        if (capaciteTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir la capacite", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(capaciteTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", capaciteTF.getText() + " n'est pas un nombre valide", new Command("Ok"));
            return false;
        }

        return true;
    }
}