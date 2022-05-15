package com.culinair.gui.back.restaurant;

import com.codename1.capture.Capture;
import com.codename1.components.ImageViewer;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.culinair.entities.Restaurant;
import com.culinair.gui.back.AccueilBack;
import com.culinair.gui.back.plat.ChooseRestaurant;
import com.culinair.services.RestaurantService;
import com.culinair.utils.Statics;

import java.io.IOException;

public class Manage extends Form {

    Resources theme = UIManager.initFirstTheme("/theme");

    boolean imageEdited = false;
    boolean isChoose;

    Restaurant currentRestaurant;
    String selectedImage;
    Label nomLabel, typeLabel, descriptionLabel, emplacementLabel, longitudeLabel, lattitudeLabel, imageLabel;
    TextField nomTF, typeTF, emplacementTF, longitudeTF, lattitudeTF;
    TextArea descriptionTF;
    ImageViewer imageIV;
    Button selectImageButton, manageButton;

    Form previous;

    public Manage(Form previous, boolean isChoose) {
        super(new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;
        this.isChoose = isChoose;
        currentRestaurant = DisplayAll.currentRestaurant;

        addGUIs();
        addActions();

        super.setToolbar(new Toolbar());
        super.setTitle(com.culinair.gui.back.plat.DisplayAll.currentPlat == null ? "Ajouter un restaurant" : "Modifier le restaurant");
        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    private void addGUIs() {

        nomLabel = new Label("Nom : ");
        nomLabel.setUIID("labelDefault");
        nomTF = new TextField();
        nomTF.setHint("Tapez le nom du restaurant");

        typeLabel = new Label("Type : ");
        typeLabel.setUIID("labelDefault");
        typeTF = new TextField();
        typeTF.setHint("Tapez le type du restaurant");

        descriptionLabel = new Label("Description : ");
        descriptionLabel.setUIID("labelDefault");
        descriptionTF = new TextArea();
        descriptionTF.setHint("Decrivez le restaurant");

        emplacementLabel = new Label("Emplacement : ");
        emplacementLabel.setUIID("labelDefault");
        emplacementTF = new TextField();
        emplacementTF.setHint("Tapez l'emplacement du restaurant");

        longitudeLabel = new Label("Longitude : ");
        longitudeLabel.setUIID("labelDefault");
        longitudeTF = new TextField();

        lattitudeLabel = new Label("lattitude : ");
        lattitudeLabel.setUIID("labelDefault");
        lattitudeTF = new TextField();

        imageLabel = new Label("Image : ");
        imageLabel.setUIID("labelDefault");
        selectImageButton = new Button("Ajouter une image");

        if (currentRestaurant == null) {
            imageIV = new ImageViewer(theme.getImage("default.jpg").fill(1100, 500));
            manageButton = new Button("Ajouter");
        } else {

            nomTF.setText(currentRestaurant.getNom());
            typeTF.setText(currentRestaurant.getType());
            descriptionTF.setText(currentRestaurant.getDescription());
            emplacementTF.setText(currentRestaurant.getEmplacement());
            longitudeTF.setText(currentRestaurant.getLongitude());
            lattitudeTF.setText(currentRestaurant.getLattitude());

            if (currentRestaurant.getImage() != null) {
                String url = Statics.RESTAURANT_IMAGE_URL + currentRestaurant.getImage();
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
            selectedImage = currentRestaurant.getImage();

            manageButton = new Button("Modifier");
        }

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(
                nomLabel, nomTF,
                typeLabel, typeTF,
                descriptionLabel, descriptionTF,
                emplacementLabel, emplacementTF,
                longitudeLabel, longitudeTF,
                lattitudeLabel, lattitudeTF,
                imageLabel, imageIV, selectImageButton,
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

        if (currentRestaurant == null) {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = RestaurantService.getInstance().add(
                            new Restaurant(
                                    nomTF.getText(),
                                    typeTF.getText(),
                                    descriptionTF.getText(),
                                    emplacementTF.getText(),
                                    selectedImage,
                                    longitudeTF.getText(),
                                    lattitudeTF.getText()
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succes", "Restaurant ajoute avec succes", new Command("Ok"));
                    } else {
                        Dialog.show("Erreur", "Erreur d'ajout de restaurant. Code d'erreur : " + responseCode, new Command("Ok"));
                    }

                    showBackAndRefresh();
                }
            });
        } else {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = RestaurantService.getInstance().edit(
                            new Restaurant(
                                    currentRestaurant.getId(),
                                    nomTF.getText(),
                                    typeTF.getText(),
                                    descriptionTF.getText(),
                                    emplacementTF.getText(),
                                    selectedImage,
                                    longitudeTF.getText(),
                                    lattitudeTF.getText()
                            ),
                            imageEdited
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succes", "Restaurant modifie avec succes", new Command("Ok"));
                    } else {
                        Dialog.show("Erreur", "Erreur de modification de restaurant. Code d'erreur : " + responseCode, new Command("Ok"));
                    }

                    showBackAndRefresh();
                }
            });
        }
    }

    private void showBackAndRefresh() {
        if (isChoose) {
            ((ChooseRestaurant) previous).refresh();
        } else {
            AccueilBack.accueilForm.addComponentAndRefresh(
                    new com.culinair.gui.back.restaurant.DisplayAll().addGUIs(),
                    "Restaurant"
            );
        }
        previous.showBack();
    }

    private boolean controleDeSaisie() {

        if (nomTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le nom", new Command("Ok"));
            return false;
        }

        if (typeTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le type", new Command("Ok"));
            return false;
        }

        if (descriptionTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir la description", new Command("Ok"));
            return false;
        }

        if (emplacementTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir l'eplacement", new Command("Ok"));
            return false;
        }

        if (longitudeTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir la longitude", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(longitudeTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", longitudeTF.getText() + " n'est pas un nombre valide", new Command("Ok"));
            return false;
        }

        if (lattitudeTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir la lattitude", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(lattitudeTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", lattitudeTF.getText() + " n'est pas un nombre valide", new Command("Ok"));
            return false;
        }

        if (selectedImage == null) {
            Dialog.show("Avertissement", "Veuillez choisir une image", new Command("Ok"));
            return false;
        }
        return true;
    }
}
