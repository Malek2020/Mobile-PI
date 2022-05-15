package com.culinair.gui.back.evenement;


import com.codename1.capture.Capture;
import com.codename1.components.ImageViewer;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.culinair.entities.Evenement;
import com.culinair.entities.Promotion;
import com.culinair.gui.back.AccueilBack;
import com.culinair.services.EvenementService;
import com.culinair.utils.Statics;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Manage extends Form {


    public static Promotion selectedPromotion;
    Resources theme = UIManager.initFirstTheme("/theme");
    String selectedImage;
    Calendar calendar;
    boolean imageEdited = false;
    Evenement currentEvenement;
    Label nomLabel, datetLabel, lieuLabel, imageLabel, prixLabel, descriptionLabel, promotionLabel;
    TextField
            nomTF,
            lieuTF,
            imageTF,
            prixTF,
            descriptionTF,
            promotionTF;

    boolean selectedStart = false;
    Date selectedStartDate = null;
    Date selectedEndDate = null;

    Label selectedPromotionLabel;
    Button selectPromotionButton;

    ImageViewer imageIV;
    Button selectImageButton;

    Button manageButton;

    Form previous;

    public Manage(Form previous) {
        super(new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;


        selectedPromotion = null;

        currentEvenement = DisplayAll.currentEvenement;

        addGUIs();
        addActions();

        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }


    public void refreshPromotion() {
        selectedPromotionLabel.setText(String.valueOf(selectedPromotion.getTauxPromotion()));
        selectPromotionButton.setText("Modifier le promotion");
        this.refreshTheme();
    }


    private void addGUIs() {


        nomLabel = new Label("Nom : ");
        nomLabel.setUIID("labelDefault");
        nomTF = new TextField();
        nomTF.setHint("Tapez le nom");

        lieuLabel = new Label("Lieu : ");
        lieuLabel.setUIID("labelDefault");
        lieuTF = new TextField();
        lieuTF.setHint("Tapez le lieu");


        prixLabel = new Label("Prix : ");
        prixLabel.setUIID("labelDefault");
        prixTF = new TextField();
        prixTF.setHint("Tapez le prix");

        descriptionLabel = new Label("Description : ");
        descriptionLabel.setUIID("labelDefault");
        descriptionTF = new TextField();
        descriptionTF.setHint("Tapez le description");

        promotionLabel = new Label("Promotion : ");
        promotionLabel.setUIID("labelDefault");
        promotionTF = new TextField();
        promotionTF.setHint("Tapez le promotion");


        promotionLabel = new Label("promotion : ");
        promotionLabel.setUIID("labelDefault");
        selectedPromotionLabel = new Label("vide");
        selectPromotionButton = new Button("Choisir promotion");
        selectPromotionButton.addActionListener(l -> new ChoosePromotion(this).show());

        datetLabel = new Label("Date de debut et fin : ");
        datetLabel.setUIID("labelDefault");

        calendar = new Calendar();

        imageLabel = new Label("Image : ");
        imageLabel.setUIID("labelDefault");
        selectImageButton = new Button("Ajouter une image");

        if (currentEvenement == null) {

            imageIV = new ImageViewer(theme.getImage("default.jpg").fill(1100, 500));


            manageButton = new Button("Ajouter");
        } else {


            nomTF.setText(currentEvenement.getNom());
            selectedStartDate = currentEvenement.getDateDebut();
            selectedEndDate = currentEvenement.getDateFin();
            lieuTF.setText(currentEvenement.getLieu());

            prixTF.setText(String.valueOf(currentEvenement.getPrix()));
            descriptionTF.setText(currentEvenement.getDescription());

            selectedPromotion = currentEvenement.getPromotion();

            promotionLabel = new Label("promotion : ");
            promotionLabel.setUIID("labelDefault");
            selectedPromotionLabel = new Label("null");
            selectedPromotionLabel.setText(String.valueOf(selectedPromotion.getTauxPromotion()));
            selectPromotionButton.setText("Choisir promotion");

            if (selectedStartDate != null && selectedEndDate != null) {
                calendar.highlightDate(selectedStartDate, "dateStart");
                calendar.highlightDate(selectedEndDate, "dateEnd");
            }

            if (currentEvenement.getImage() != null) {
                selectedImage = currentEvenement.getImage();
                String url = Statics.EVENEMENT_IMAGE_URL + currentEvenement.getImage();
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


            manageButton = new Button("Modifier");
        }

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(
                imageLabel, imageIV,
                selectImageButton,
                nomLabel, nomTF,
                datetLabel, calendar,
                lieuLabel, lieuTF,
                prixLabel, prixTF,
                descriptionLabel, descriptionTF,
                promotionLabel,
                selectedPromotionLabel, selectPromotionButton,
                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {
        calendar.addDayActionListener((i) -> {
            if (!selectedStart) {
                if (selectedStartDate != null) {
                    calendar.unHighlightDate(selectedStartDate);
                    calendar.unHighlightDate(selectedEndDate);
                }

                selectedStartDate = calendar.getDate();
                selectedEndDate = null;

                calendar.highlightDate(calendar.getDate(), "dateStart");
            } else {
                selectedEndDate = calendar.getDate();

                calendar.highlightDate(calendar.getDate(), "dateEnd");
            }

            selectedStart = !selectedStart;
        });
        selectImageButton.addActionListener(a -> {
            selectedImage = Capture.capturePhoto(900, -1);
            try {
                imageEdited = true;
                imageIV.setImage(Image.createImage(selectedImage));
            } catch (IOException e) {
                e.printStackTrace();
            }
            selectImageButton.setText("Modifier l'image");
        });

        if (currentEvenement == null) {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = EvenementService.getInstance().add(
                            new Evenement(
                                    nomTF.getText(),
                                    selectedStartDate,
                                    selectedEndDate,
                                    lieuTF.getText(),
                                    selectedImage,
                                    Float.parseFloat(prixTF.getText()),
                                    descriptionTF.getText(),
                                    selectedPromotion
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Evenement ajouté avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur d'ajout de evenement. Code d'erreur : " + responseCode, new Command("Ok"));
                    }
                }
            });
        } else {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = EvenementService.getInstance().edit(
                            new Evenement(
                                    currentEvenement.getId(),
                                    nomTF.getText(),
                                    selectedStartDate,
                                    selectedEndDate,
                                    lieuTF.getText(),
                                    selectedImage,
                                    Float.parseFloat(prixTF.getText()),
                                    descriptionTF.getText(),
                                    selectedPromotion
                            ), imageEdited
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Evenement modifié avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur de modification de evenement. Code d'erreur : " + responseCode, new Command("Ok"));
                    }
                }
            });
        }
    }

    private void showBackAndRefresh() {
        AccueilBack.accueilForm.addComponentAndRefresh(
                new com.culinair.gui.back.evenement.DisplayAll().addGUIs(),
                "Evenement"
        );
        previous.showBack();
    }

    private boolean controleDeSaisie() {


        if (nomTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le nom", new Command("Ok"));
            return false;
        }

        if (selectedStartDate == null) {
            Dialog.show("Avertissement", "Veuillez saisir la date d'expo", new Command("Ok"));
            return false;
        }
        if (selectedEndDate == null) {
            Dialog.show("Avertissement", "Veuillez saisir la date de fin", new Command("Ok"));
            return false;
        }
        if (dateIsAfter(selectedEndDate, selectedStartDate)) {
            Dialog.show("Avertissement", "Date de d'expo doit etre superieure a la date de fin", new Command("Ok"));
            return false;
        }


        if (lieuTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le lieu", new Command("Ok"));
            return false;
        }


        if (prixTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le prix", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(prixTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", prixTF.getText() + " n'est pas un nombre valide", new Command("Ok"));
            return false;
        }


        if (descriptionTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le description", new Command("Ok"));
            return false;
        }


        if (selectedPromotion == null) {
            Dialog.show("Avertissement", "Veuillez choisir un promotion", new Command("Ok"));
            return false;
        }


        if (selectedImage == null) {
            Dialog.show("Avertissement", "Veuillez choisir une image", new Command("Ok"));
            return false;
        }


        return true;
    }

    boolean dateIsAfter(Date d1, Date d2) {

        int day1 = (int) Float.parseFloat(new SimpleDateFormat("dd").format(d1));
        int month1 = (int) Float.parseFloat(new SimpleDateFormat("MM").format(d1));
        int year1 = (int) Float.parseFloat(new SimpleDateFormat("yyyy").format(d1));

        int day2 = (int) Float.parseFloat(new SimpleDateFormat("dd").format(d2));
        int month2 = (int) Float.parseFloat(new SimpleDateFormat("MM").format(d2));
        int year2 = (int) Float.parseFloat(new SimpleDateFormat("yyyy").format(d2));

        if (year1 <= year2) {
            if (month1 <= month2) {
                return day1 <= day2;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}