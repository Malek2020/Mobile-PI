package com.culinair.gui.back.promotion;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.culinair.entities.Promotion;
import com.culinair.gui.back.AccueilBack;
import com.culinair.gui.back.evenement.ChoosePromotion;
import com.culinair.services.PromotionService;

public class Manage extends Form {

    Promotion currentPromotion;
    boolean isChoose;

    Label tauxPromotionLabel, descriptionLabel;
    TextField tauxPromotionTF;
    TextArea descriptionTF;
    Button manageButton;

    Form previous;

    public Manage(Form previous, boolean isChoose) {
        super(DisplayAll.currentPromotion == null ? "Ajouter une promotion" : "Modifier la promotion", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;
        this.isChoose = isChoose;

        currentPromotion = DisplayAll.currentPromotion;

        addGUIs();
        addActions();

        super.setToolbar(new Toolbar());
        super.setTitle(DisplayAll.currentPromotion == null ? "Ajouter une promotion" : "Modifier la promotion");
        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    private void addGUIs() {

        tauxPromotionLabel = new Label("Taux : ");
        tauxPromotionLabel.setUIID("labelDefault");
        tauxPromotionTF = new TextField();
        tauxPromotionTF.setHint("Tapez le taux de la promotion");

        descriptionLabel = new Label("Description : ");
        descriptionLabel.setUIID("labelDefault");
        descriptionTF = new TextArea();
        descriptionTF.setHint("Tapez la description de la promotion");


        if (currentPromotion == null) {
            manageButton = new Button("Ajouter");
        } else {
            tauxPromotionTF.setText(String.valueOf(currentPromotion.getTauxPromotion()));
            descriptionTF.setText(currentPromotion.getDescription());

            manageButton = new Button("Modifier");
        }

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(
                tauxPromotionLabel, tauxPromotionTF,
                descriptionLabel, descriptionTF,
                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {

        if (currentPromotion == null) {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = PromotionService.getInstance().add(
                            new Promotion(
                                    (int) Float.parseFloat(tauxPromotionTF.getText()),
                                    descriptionTF.getText()
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succes", "Promotion ajoute avec succes", new Command("Ok"));
                    } else {
                        Dialog.show("Erreur", "Erreur d'ajout de promotion. Code d'erreur : " + responseCode, new Command("Ok"));
                    }

                    showBackAndRefresh();
                }
            });
        } else {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = PromotionService.getInstance().edit(
                            new Promotion(
                                    currentPromotion.getId(),
                                    (int) Float.parseFloat(tauxPromotionTF.getText()),
                                    descriptionTF.getText()
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succes", "Promotion modifie avec succes", new Command("Ok"));
                    } else {
                        Dialog.show("Erreur", "Erreur de modification de promotion. Code d'erreur : " + responseCode, new Command("Ok"));
                    }

                    showBackAndRefresh();
                }
            });
        }
    }

    private void showBackAndRefresh() {
        if (isChoose) {
            ((ChoosePromotion) previous).refresh();
        } else {
            AccueilBack.accueilForm.addComponentAndRefresh(
                    new com.culinair.gui.back.promotion.DisplayAll().addGUIs(),
                    "Promotions"
            );
        }
        previous.showBack();
    }

    private boolean controleDeSaisie() {

        if (tauxPromotionTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le taux", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(tauxPromotionTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", tauxPromotionTF.getText() + " n'est pas un nombre valide", new Command("Ok"));
            return false;
        }


        if (descriptionTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir la description", new Command("Ok"));
            return false;
        }

        return true;
    }
}