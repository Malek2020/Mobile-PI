package com.culinair.gui.front.reservation;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.culinair.entities.Reservation;
import com.culinair.entities.Guide;
import com.culinair.gui.front.AccueilFront;
import com.culinair.services.ReservationService;

public class Manage {

    Resources theme = UIManager.initFirstTheme("/theme");

    boolean date_arriveeEdited = false;

    public static Guide selectedGuide;
    Reservation currentReservation;

    Label num_volLabel, destinationLabel, date_departLabel, date_arriveeLabel, GuideLabel, selectedGuideLabel, adulteLabel, enfantLabel;
    TextField num_volTF, date_departTF, destinationTF, date_arriveeTF, adulteTF, enfantTF;
    Button selectGuideButton, manageButton;

    Form previous;

    public Container addGUIs() {

        Container compo = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        compo.setScrollableY(true);

        num_volLabel = new Label("num_vol : ");
        num_volLabel.setUIID("labelDefault");
        num_volTF = new TextField();
        num_volTF.setHint("Tapez le type du Reservation");

        destinationLabel = new Label("destination : ");
        destinationLabel.setUIID("labelDefault");
        destinationTF = new TextField();
        destinationTF.setHint("Tapez la destination du Reservation");

        date_departLabel = new Label("date_depart : ");
        date_departLabel.setUIID("labelDefault");
        date_departTF = new TextField();
        date_departTF.setHint("Tapez le date_depart du Reservation");

        date_arriveeLabel = new Label("date_arrivee : ");
        date_arriveeLabel.setUIID("labelDefault");
        date_arriveeTF = new TextField();
        date_arriveeTF.setHint("Tapez la date_arrivee du Reservation");

        adulteLabel = new Label("adulte : ");
        adulteLabel.setUIID("labelDefault");
        adulteTF = new TextField();
        adulteTF.setHint("Tapez l adulte du Reservation");

        enfantLabel = new Label("enfant : ");
        enfantLabel.setUIID("labelDefault");
        enfantTF = new TextField();
        enfantTF.setHint("Tapez l enafant du Reservation");

        GuideLabel = new Label("Guide : ");
        GuideLabel.setUIID("labelDefault");
        if (selectedGuide != null) {
            selectedGuideLabel = new Label(selectedGuide.getNom());
            selectGuideButton = new Button("Modifier le Guide");
        } else {
            selectedGuideLabel = new Label("Aucun Guide selectionne");
            selectGuideButton = new Button("Choisir un Guide");
        }

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        manageButton = new Button("Ajouter reservation");

         selectGuideButton.addActionListener(l -> new ChooseGuide(AccueilFront.accueilForm).show());

        manageButton.addActionListener(action -> {
            if (controleDeSaisie()) {
                int responseCode = ReservationService.getInstance().add(
                        new Reservation(
                                Float.parseFloat(num_volTF.getText()),
                                destinationTF.getText(),
                                date_departTF.getText(),
                                date_arriveeTF.getText(),
                                Float.parseFloat(adulteTF.getText()),
                                Float.parseFloat(enfantTF.getText()),
                                selectedGuide
                        )
                );
                if (responseCode == 200) {
                    Dialog.show("Succes", "Reservation effectuee avec succes", new Command("Ok"));
                } else {
                    Dialog.show("Erreur", "Erreur d'ajout de Reservation. Code d'erreur : " + responseCode, new Command("Ok"));
                }

                showBackAndRefresh();
            }
        });
        
        container.addAll(
                num_volLabel, num_volTF,
                destinationLabel, destinationTF,
                date_departLabel, date_departTF,
                date_arriveeLabel, date_arriveeTF,
                enfantLabel, enfantTF,
                adulteLabel, adulteTF,
                GuideLabel, selectedGuideLabel, selectGuideButton,
                manageButton
        );

        compo.addAll(container);

        return compo;
    }
   

    private void showBackAndRefresh() {
        AccueilFront.accueilForm.addComponentAndRefresh(
                new com.culinair.gui.back.reservation.DisplayAll().addGUIs(),
                "Reservation"
        );
        previous.showBack();
    }

    private boolean controleDeSaisie() {

        if (num_volTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le date_depart", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(num_volTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", num_volTF.getText() + " n'est pas un guide_id valide", new Command("Ok"));
            return false;
        }

        if (destinationTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir la destination", new Command("Ok"));
            return false;
        }

        if (date_departTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir la destination", new Command("Ok"));
            return false;
        }

        if (date_arriveeTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir la destination", new Command("Ok"));
            return false;
        }

        if (adulteTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le date_depart", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(adulteTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", adulteTF.getText() + " n'est pas un guide_id valide", new Command("Ok"));
            return false;
        }

        if (enfantTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le date_depart", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(enfantTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", enfantTF.getText() + " n'est pas un guide_id valide", new Command("Ok"));
            return false;
        }

        if (selectedGuide == null) {
            Dialog.show("Avertissement", "Veuillez choisir une Guide", new Command("Ok"));
            return false;
        }

        return true;
    }

}
