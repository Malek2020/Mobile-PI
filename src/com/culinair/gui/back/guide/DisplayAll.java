package com.culinair.gui.back.guide;

import com.codename1.components.ImageViewer;
import com.codename1.components.InteractionDialog;
import com.codename1.components.SpanLabel;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.culinair.entities.Guide;
import com.culinair.entities.Guide;
import com.culinair.gui.back.AccueilBack;
import com.culinair.services.GuideService;
import com.culinair.services.GuideService;
import com.culinair.utils.Statics;
import java.util.ArrayList;

public class DisplayAll {

    Resources theme = UIManager.initFirstTheme("/theme");
    public static Guide currentGuide = null;
    Button addBtn;

    public Component addGUIs() {
        Container compo = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        compo.setScrollableY(true);

        addBtn = new Button("Ajouter");
        addBtn.addActionListener(action -> {
            currentGuide = null;
            new Manage(AccueilBack.accueilForm, false).show();
        });

        compo.add(addBtn);

        ArrayList<Guide> listGuides = GuideService.getInstance().getAll();
        if (listGuides.size() > 0) {
            for (Guide listGuide : listGuides) {
                compo.add(makeGuideModel(listGuide));
            }
        } else {
            compo.add(new Label("Aucune donnee"));
        }

        return compo;
    }

    Label nomLabel, prenomLabel, descriptionLabel, prixLabel, restaurantLabel;
    SpanLabel descriptionSpanLabel;
    ImageViewer imageIV;
    Button editBtn, deleteBtn;
    Container btnsContainer;

    private Component makeGuideModel(Guide guide) {
        Container guideModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        guideModel.setUIID("containerRounded");

        nomLabel = new Label(guide.getNom());
        nomLabel.setUIID("labelCenter");

        prenomLabel = new Label("Prenom : " + guide.getPrenom());
        prenomLabel.setUIID("labelDefault");

        descriptionLabel = new Label("Description : " + guide.getDescription());
        descriptionLabel.setUIID("labelDefault");

        descriptionSpanLabel = new SpanLabel(guide.getDescription());
        descriptionSpanLabel.setUIID("labelDefault");

       

        if (guide.getImage() != null) {
            String url = Statics.Guide_IMAGE_URL + guide.getImage();
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
            currentGuide = guide;
            new Manage(AccueilBack.accueilForm, false).show();
        });

        deleteBtn = new Button("Supprimer");
        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce guide ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                System.out.println(guide);
                int responseCode = GuideService.getInstance().delete(guide.getId());

                if (responseCode == 200) {
                    currentGuide = null;
                    dlg.dispose();
                    guideModel.remove();
                    AccueilBack.accueilForm.revalidate();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du guide. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.WEST, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);

        guideModel.addAll(
                nomLabel, imageIV, prenomLabel, descriptionLabel, descriptionSpanLabel,
                btnsContainer
        );

        return guideModel;
    }
}
