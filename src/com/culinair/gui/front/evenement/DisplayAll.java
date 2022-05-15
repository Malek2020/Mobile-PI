package com.culinair.gui.front.evenement;

import com.codename1.components.ImageViewer;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.culinair.MainApp;
import com.culinair.entities.Evenement;
import com.culinair.gui.front.AccueilFront;
import com.culinair.services.EvenementService;
import com.culinair.utils.Statics;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DisplayAll extends Form {


    public static Evenement currentEvenement = null;
    Resources theme = UIManager.initFirstTheme("/theme");
    Label nomLabel, dateLabel, lieuLabel, prixLabel, descriptionLabel, promotionLabel;
    Calendar calendar;
    ImageViewer imageIV;
    Container btnsContainer;
    Button showEventsParticipe;

    public Component addGUIs() {


        Container compo = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        compo.setScrollableY(true);

        showEventsParticipe = new Button("Afficher mes participations");
        showEventsParticipe.addActionListener(action -> new DisplayEvenementParticipe(AccueilFront.accueilForm).show());
        compo.add(showEventsParticipe);

        ArrayList<Evenement> listEvenements = EvenementService.getInstance().getAll();
        if (listEvenements.size() > 0) {
            for (Evenement listEvenement : listEvenements) {
                compo.add(makeEvenementModel(listEvenement));
            }
        } else {
            compo.add(new Label("Aucune donnée"));
        }

        return compo;
    }

    private Component makeEvenementModel(Evenement evenement) {
        Container evenementModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        evenementModel.setUIID("containerRounded");

        nomLabel = new Label("Nom : " + evenement.getNom());
        nomLabel.setUIID("labelDefault");

        lieuLabel = new Label("Lieu : " + evenement.getLieu());
        lieuLabel.setUIID("labelDefault");

        prixLabel = new Label("Prix : " + evenement.getPrix());
        prixLabel.setUIID("labelDefault");

        descriptionLabel = new Label("Description : " + evenement.getDescription());
        descriptionLabel.setUIID("labelDefault");

        dateLabel = new Label("Date de debut & fin : " + new SimpleDateFormat("dd-MM-yyyy").format(evenement.getDateDebut()));

        if (evenement.getDateDebut() != null && evenement.getDateFin() != null) {
            calendar = new Calendar();
            calendar.setFocusable(false);
            calendar.highlightDate(evenement.getDateDebut(), "dateStart");
            calendar.highlightDate(evenement.getDateFin(), "dateEnd");
        }

        if (evenement.getPromotion() != null) {
            promotionLabel = new Label("Promotion : " + evenement.getPromotion().getTauxPromotion());
        } else {
            promotionLabel = new Label("Promotion : Vide");
        }
        promotionLabel.setUIID("labelDefault");

        if (evenement.getImage() != null) {
            String url = Statics.EVENEMENT_IMAGE_URL + evenement.getImage();
            Image image = URLImage.createToStorage(
                    EncodedImage.createFromImage(theme.getImage("default.jpg").fill(1100, 500), false),
                    url,
                    url,
                    URLImage.RESIZE_SCALE
            );
            imageIV = new ImageViewer(image);
        } else {
            imageIV = new ImageViewer(theme.getImage("default.jpg").fill(1100, 500));
        }
        imageIV.setFocusable(false);

        Button participateButton;
        ArrayList<Evenement> listEvenementFavoris = MainApp.listEvenementsParticipe;
        currentEvenement = evenement;
        if (listEvenementFavoris.contains(evenement)) {
            participateButton = new Button("Participé");
            participateButton.setEnabled(false);
        } else {
            participateButton = new Button("Participer");

            participateButton.addActionListener(action -> {
                participateButton.setText("Participé");
                participateButton.setEnabled(false);
                MainApp.listEvenementsParticipe.add(evenement);

                evenementModel.revalidate();
            });
        }

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.add(BorderLayout.CENTER, participateButton);
        btnsContainer.setUIID("containerButtons");

        evenementModel.addAll(
                nomLabel, lieuLabel, prixLabel, descriptionLabel, promotionLabel,
                imageIV,
                dateLabel
        );
        if (evenement.getDateDebut() != null && evenement.getDateDebut() != null) {
            evenementModel.add(calendar);
        }
        evenementModel.add(btnsContainer);

        return evenementModel;
    }
}