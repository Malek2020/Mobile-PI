package com.culinair.gui.back.evenement;

import com.codename1.components.ImageViewer;
import com.codename1.components.InteractionDialog;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.culinair.entities.Evenement;
import com.culinair.gui.back.AccueilBack;
import com.culinair.services.EvenementService;
import com.culinair.utils.Statics;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DisplayAll extends Form {


    public static Evenement currentEvenement = null;
    Resources theme = UIManager.initFirstTheme("/theme");
    Button addBtn;
    Label nomLabel, dateLabel, lieuLabel, prixLabel, descriptionLabel, promotionLabel;
    Calendar calendar;
    ImageViewer imageIV;
    Button editBtn, deleteBtn;
    Container btnsContainer;

    public Component addGUIs() {
        Container compo = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        compo.setScrollableY(true);

        addBtn = new Button("Ajouter");
        addBtn.addActionListener(action -> {
            currentEvenement = null;
            new Manage(AccueilBack.accueilForm).show();
        });

        compo.add(addBtn);

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

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        editBtn = new Button("Modifier");
        editBtn.addActionListener(action -> {
            currentEvenement = evenement;
            new Manage(AccueilBack.accueilForm).show();
        });

        deleteBtn = new Button("Supprimer");
        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce evenement ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = EvenementService.getInstance().delete(evenement.getId());

                if (responseCode == 200) {
                    currentEvenement = null;
                    dlg.dispose();
                    evenementModel.remove();
                    this.refreshTheme();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du evenement. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.WEST, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);

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