package com.culinair.gui.front.promotion;

import com.codename1.components.SpanLabel;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.culinair.entities.Promotion;
import com.culinair.services.PromotionService;

import java.util.ArrayList;

public class DisplayAll extends Form {

    Label tauxPromotionLabel, descriptionLabel;
    SpanLabel descriptionSpanLabel;
    Container btnsContainer;

    public Component addGUIs() {
        Container compo = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        compo.setScrollableY(true);

        ArrayList<Promotion> listPromotions = PromotionService.getInstance().getAll();
        if (listPromotions.size() > 0) {
            for (Promotion listPromotion : listPromotions) {
                compo.add(makePromotionModel(listPromotion));
            }
        } else {
            compo.add(new Label("Aucune donnee"));
        }

        return compo;
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

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        promotionModel.addAll(
                tauxPromotionLabel, descriptionLabel, descriptionSpanLabel,
                btnsContainer
        );

        return promotionModel;
    }
}