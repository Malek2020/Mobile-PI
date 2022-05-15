package com.culinair.gui.back.plat;

import com.codename1.capture.Capture;
import com.codename1.components.ImageViewer;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.culinair.entities.Plat;
import com.culinair.entities.Restaurant;
import com.culinair.gui.back.AccueilBack;
import com.culinair.services.PlatService;
import com.culinair.utils.Statics;

import java.io.IOException;

public class Manage extends Form {

    public static Restaurant selectedRestaurant;
    Resources theme = UIManager.initFirstTheme("/theme");
    boolean imageEdited = false;
    Plat currentPlat;
    String selectedImage;

    Label nomLabel, typeCuisineLabel, descriptionLabel, prixLabel, imageLabel, restaurantLabel, selectedRestaurantLabel;
    TextField nomTF, typeCuisineTF, prixTF;
    TextArea descriptionTF;
    ImageViewer imageIV;
    Button selectRestaurantButton, selectImageButton, manageButton;

    Form previous;

    public Manage(Form previous) {
        super(DisplayAll.currentPlat == null ? "Ajouter un plat" : "Modifier le plat", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        selectedRestaurant = null;
        currentPlat = DisplayAll.currentPlat;

        addGUIs();
        addActions();

        super.setToolbar(new Toolbar());
        super.setTitle(DisplayAll.currentPlat == null ? "Ajouter un plat" : "Modifier le plat");
        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    public void refreshRestaurant() {
        selectedRestaurantLabel.setText(selectedRestaurant.getNom());
        selectRestaurantButton.setText("Modifier le restaurant");
        this.refreshTheme();
    }

    private void addGUIs() {

        nomLabel = new Label("Nom : ");
        nomLabel.setUIID("labelDefault");
        nomTF = new TextField();
        nomTF.setHint("Tapez le nom du plat");

        typeCuisineLabel = new Label("Type : ");
        typeCuisineLabel.setUIID("labelDefault");
        typeCuisineTF = new TextField();
        typeCuisineTF.setHint("Tapez le type du plat");

        descriptionLabel = new Label("Description : ");
        descriptionLabel.setUIID("labelDefault");
        descriptionTF = new TextArea();
        descriptionTF.setHint("Tapez le description du plat");

        prixLabel = new Label("Prix : ");
        prixLabel.setUIID("labelDefault");
        prixTF = new TextField();
        prixTF.setHint("Tapez le prix du plat");

        imageLabel = new Label("Image : ");
        imageLabel.setUIID("labelDefault");
        selectImageButton = new Button("Ajouter une image");

        if (currentPlat == null) {
            imageIV = new ImageViewer(theme.getImage("default.jpg").fill(1100, 500));
            manageButton = new Button("Ajouter");
        } else {
            nomTF.setText(currentPlat.getNom());
            typeCuisineTF.setText(currentPlat.getTypeCuisine());
            descriptionTF.setText(currentPlat.getDescription());
            prixTF.setText(String.valueOf(currentPlat.getPrix()));

            selectedRestaurant = currentPlat.getRestaurant();

            if (currentPlat.getImage() != null) {
                String url = Statics.PLAT_IMAGE_URL + currentPlat.getImage();
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

            selectImageButton.setText("Modifier l'image");
            selectedImage = currentPlat.getImage();

            manageButton = new Button("Modifier");
        }

        restaurantLabel = new Label("Restaurant : ");
        restaurantLabel.setUIID("labelDefault");
        if (selectedRestaurant != null) {
            selectedRestaurantLabel = new Label(selectedRestaurant.getNom());
            selectRestaurantButton = new Button("Modifier le restaurant");
        } else {
            selectedRestaurantLabel = new Label("Aucun restaurant selectionne");
            selectRestaurantButton = new Button("Choisir une restaurant");
        }

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(
                nomLabel, nomTF,
                typeCuisineLabel, typeCuisineTF,
                descriptionLabel, descriptionTF,
                prixLabel, prixTF,
                imageLabel, imageIV, selectImageButton,
                restaurantLabel, selectedRestaurantLabel, selectRestaurantButton,
                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {
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

        selectRestaurantButton.addActionListener(l -> new ChooseRestaurant(this).show());

        if (currentPlat == null) {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = PlatService.getInstance().add(
                            new Plat(
                                    nomTF.getText(),
                                    typeCuisineTF.getText(),
                                    descriptionTF.getText(),
                                    Float.parseFloat(prixTF.getText()),
                                    selectedImage,
                                    selectedRestaurant
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succes", "Plat ajoute avec succes", new Command("Ok"));
                    } else {
                        Dialog.show("Erreur", "Erreur d'ajout de plat. Code d'erreur : " + responseCode, new Command("Ok"));
                    }

                    showBackAndRefresh();
                }
            });
        } else {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = PlatService.getInstance().edit(
                            new Plat(
                                    currentPlat.getId(),
                                    nomTF.getText(),
                                    typeCuisineTF.getText(),
                                    descriptionTF.getText(),
                                    Float.parseFloat(prixTF.getText()),
                                    selectedImage,
                                    selectedRestaurant
                            ),
                            imageEdited
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succes", "Plat modifie avec succes", new Command("Ok"));
                    } else {
                        Dialog.show("Erreur", "Erreur de modification de plat. Code d'erreur : " + responseCode, new Command("Ok"));
                    }

                    showBackAndRefresh();
                }
            });
        }
    }

    private void showBackAndRefresh() {
        AccueilBack.accueilForm.addComponentAndRefresh(
                new com.culinair.gui.back.plat.DisplayAll().addGUIs(),
                "Plat"
        );
        previous.showBack();
    }

    private boolean controleDeSaisie() {

        if (nomTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le nom", new Command("Ok"));
            return false;
        }

        if (typeCuisineTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le type", new Command("Ok"));
            return false;
        }

        if (descriptionTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir la description", new Command("Ok"));
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

        if (selectedImage == null) {
            Dialog.show("Avertissement", "Veuillez choisir une image", new Command("Ok"));
            return false;
        }

        if (selectedRestaurant == null) {
            Dialog.show("Avertissement", "Veuillez choisir une restaurant", new Command("Ok"));
            return false;
        }

        return true;
    }
}
