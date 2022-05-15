package com.culinair.gui.back.evenement;

import com.codename1.components.SpanLabel;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.culinair.entities.Promotion;
import com.culinair.services.PromotionService;

import java.util.ArrayList;

public class ChoosePromotion extends Form {

    Form previousForm;
    Label tauxPromotionLabel, descriptionLabel;
    SpanLabel descriptionSpanLabel;
    Button chooseBtn;
    Container btnsContainer;

    public ChoosePromotion(Form previous) {
        super("Choisir un promotion", new BoxLayout(BoxLayout.Y_AXIS));

        previousForm = previous;
        addGUIs();

        super.setToolbar(new Toolbar());
        super.setTitle(com.culinair.gui.back.evenement.DisplayAll.currentEvenement == null ? "Ajouter evenement" : "Modifier evenement");
        super.getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    public void refresh() {
        this.removeAll();
        addGUIs();
        this.refreshTheme();
    }

    private void addGUIs() {
        ArrayList<Promotion> listPromotions = PromotionService.getInstance().getAll();
        if (listPromotions.size() > 0) {
            for (Promotion promotions : listPromotions) {
                this.add(makePromotionModel(promotions));
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private Component makePromotionModel(Promotion promotion) {
        Container promotionModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        promotionModel.setUIID("containerRounded");

        tauxPromotionLabel = new Label("Taux : " + promotion.getTauxPromotion());
        tauxPromotionLabel.setUIID("labelDefault");

        descriptionLabel = new Label("Description : ");
        descriptionLabel.setUIID("labelDefault");

        descriptionSpanLabel = new SpanLabel(promotion.getDescription());
        descriptionSpanLabel.setUIID("labelDefault");

        chooseBtn = new Button("Choisir");
        chooseBtn.addActionListener(l -> {
            Manage.selectedPromotion = promotion;
            ((Manage) previousForm).refreshPromotion();
            previousForm.showBack();
        });

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");
        btnsContainer.add(BorderLayout.CENTER, chooseBtn);

        promotionModel.addAll(
                tauxPromotionLabel, descriptionLabel, descriptionSpanLabel,
                btnsContainer
        );

        return promotionModel;
    }
}