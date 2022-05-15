package com.culinair.gui.front.reservation;

import com.codename1.components.ImageViewer;
import com.codename1.components.SpanLabel;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.culinair.entities.Guide;
import com.culinair.gui.front.AccueilFront;
import com.culinair.services.GuideService;
import com.culinair.utils.Statics;

import java.util.ArrayList;

public class ChooseGuide extends Form {

    Resources theme = UIManager.initFirstTheme("/theme");
    Form previousForm;

    public ChooseGuide(Form previous) {
        super("Choisir un Guide", new BoxLayout(BoxLayout.Y_AXIS));

        previousForm = previous;
        addGUIs();
        addActions();

        super.setToolbar(new Toolbar());
        super.setTitle("Choisir un Guide");
        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    public void refresh() {
        this.removeAll();
        addGUIs();
        addActions();
        this.refreshTheme();
    }

    Button addBtn;

    private void addGUIs() {
        addBtn = new Button("Ajouter Guide");
        this.add(addBtn);

        ArrayList<Guide> listGuides = GuideService.getInstance().getAll();
        if (listGuides.size() > 0) {
            for (Guide Guides : listGuides) {
                this.add(makeGuideModel(Guides));
            }
        } else {
            this.add(new Label("Aucune donnee"));
        }
    }

    private void addActions() {
        addBtn.addActionListener(action -> new com.culinair.gui.back.guide.Manage(this, true).show());
    }

    Label nomLabel, prenomLabel, descriptionLabel, imageLabel;
    SpanLabel descriptionSpanLabel;
    ImageViewer imageIV;
    Button chooseBtn;
    Container btnsContainer;

    private Component makeGuideModel(Guide Guide) {
        Container GuideModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        GuideModel.setUIID("containerRounded");

        nomLabel = new Label(Guide.getNom());
        nomLabel.setUIID("labelCenter");

        prenomLabel = new Label("Prenom : " + Guide.getPrenom());
        prenomLabel.setUIID("labelDefault");

        descriptionLabel = new Label("Description : ");
        descriptionLabel.setUIID("labelDefault");

        descriptionSpanLabel = new SpanLabel(Guide.getDescription());
        descriptionSpanLabel.setUIID("labelDefault");

        if (Guide.getImage() != null) {
            String url = Statics.Guide_IMAGE_URL + Guide.getImage();
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

        chooseBtn = new Button("Choisir");
        chooseBtn.addActionListener(l -> {
            previousForm.showBack();
            Manage.selectedGuide = Guide;
            
        });

        btnsContainer.add(BorderLayout.CENTER, chooseBtn);

        GuideModel.addAll(
                nomLabel, imageIV, prenomLabel, descriptionLabel, descriptionSpanLabel,
                btnsContainer
        );

        return GuideModel;
    }
}
