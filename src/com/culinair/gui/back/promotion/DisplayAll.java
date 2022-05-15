package com.culinair.gui.back.promotion;

import com.codename1.components.InteractionDialog;
import com.codename1.components.SpanLabel;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.culinair.entities.Promotion;
import com.culinair.gui.back.AccueilBack;
import com.culinair.services.PromotionService;

import java.util.ArrayList;

public class DisplayAll extends Form {

    public static Promotion currentPromotion = null;
    Button addBtn;
    Label tauxPromotionLabel, descriptionLabel;
    SpanLabel descriptionSpanLabel;
    Button editBtn, deleteBtn;
    Container btnsContainer;

    public Component addGUIs() {
        Container compo = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        compo.setScrollableY(true);

        addBtn = new Button("Ajouter");
        addBtn.addActionListener(action -> {
            currentPromotion = null;
            new Manage(AccueilBack.accueilForm, false).show();
        });

        compo.add(addBtn);

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

        editBtn = new Button("Modifier");
        editBtn.addActionListener(action -> {
            currentPromotion = promotion;
            new Manage(AccueilBack.accueilForm, false).show();
        });

        deleteBtn = new Button("Supprimer");
        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce promotion ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = PromotionService.getInstance().delete(promotion.getId());

                if (responseCode == 200) {
                    currentPromotion = null;
                    dlg.dispose();
                    promotionModel.remove();
                    AccueilBack.accueilForm.revalidate();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du promotion. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.WEST, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);

        promotionModel.addAll(
                tauxPromotionLabel, descriptionLabel, descriptionSpanLabel,
                btnsContainer
        );

        return promotionModel;
    }
}