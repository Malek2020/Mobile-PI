package com.culinair.gui.front.guide;

import com.codename1.components.ImageViewer;
import com.codename1.components.SpanLabel;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.culinair.entities.Guide;
import com.culinair.services.GuideService;
import com.culinair.utils.Statics;

import java.util.ArrayList;

public class DisplayAll {

    Resources theme = UIManager.initFirstTheme("/theme");
    TextField searchTF;
    ArrayList<Component> componentModels;

    public Component addGUIs() {
        Container compo = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        compo.setScrollableY(true);

        ArrayList<Guide> listGuides = GuideService.getInstance().getAll();
        componentModels = new ArrayList<>();

        searchTF = new TextField("", "Chercher un guide");
        searchTF.addDataChangedListener((d, t) -> {
            if (componentModels.size() > 0) {
                for (int i = 0; i < componentModels.size(); i++) {
                    compo.removeComponent(componentModels.get(i));
                    componentModels.remove(i);
                }
            }
            for (int i = 0; i < listGuides.size(); i++) {
                if (listGuides.get(i).getNom().startsWith(searchTF.getText())) {
                    Component model = makeGuideModel(listGuides.get(i));
                    compo.add(model);
                    componentModels.add(model);
                }
            }
            compo.revalidate();
        });
        compo.add(searchTF);

        if (listGuides.size() > 0) {
            for (Guide listGuide : listGuides) {
                Component model = makeGuideModel(listGuide);
                compo.add(model);
                componentModels.add(model);
            }
        } else {
            compo.add(new Label("Aucune donnee"));
        }

        return compo;
    }

    Label nomLabel, prenomLabel, descriptionLabel;
    SpanLabel descriptionSpanLabel;
    ImageViewer imageIV;
    Container btnsContainer;

    private Component makeGuideModel(Guide guide) {
        Container guideModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        guideModel.setUIID("containerRounded");

        nomLabel = new Label(guide.getNom());
        nomLabel.setUIID("labelCenter");

        prenomLabel = new Label("prenom du guide : " + guide.getPrenom());
        prenomLabel.setUIID("labelDefault");

        descriptionLabel = new Label("Description : ");
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

        guideModel.addAll(
                nomLabel, imageIV, prenomLabel, descriptionLabel, descriptionSpanLabel,
                btnsContainer
        );

        return guideModel;
    }
}
