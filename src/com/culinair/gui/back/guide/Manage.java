package com.culinair.gui.back.guide;

import com.codename1.capture.Capture;
import com.codename1.components.ImageViewer;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.culinair.entities.Guide;
import com.culinair.gui.back.AccueilBack;
import com.culinair.services.GuideService;
import com.culinair.utils.Statics;

import java.io.IOException;

public class Manage extends Form {

    Resources theme = UIManager.initFirstTheme("/theme");

    boolean imageEdited = false;
       boolean isChoose;
    Guide currentGuide;
    String selectedImage;

    Label nomLabel, prenomLabel, descriptionLabel, imageLabel;
    TextField nomTF, prenomTF;
    TextArea descriptionTF;
    ImageViewer imageIV;
    Button selectImageButton, manageButton;

    Form previous;

    public Manage(Form previous, boolean isChoos) {
        super(DisplayAll.currentGuide == null ? "Ajouter un guide" : "Modifier le guide", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;
this.isChoose = isChoose;
        currentGuide = DisplayAll.currentGuide;

        addGUIs();
        addActions();

        super.setToolbar(new Toolbar());
        super.setTitle(DisplayAll.currentGuide == null ? "Ajouter un Guide" : "Modifier le Guide");
        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }



    private void addGUIs() {

        nomLabel = new Label("Nom : ");
        nomLabel.setUIID("labelDefault");
        nomTF = new TextField();
        nomTF.setHint("Tapez le nom du guide");

        prenomLabel = new Label("Prenom : ");
        prenomLabel.setUIID("labelDefault");
        prenomTF = new TextField();
        prenomTF.setHint("Tapez le prenom du guide");

        descriptionLabel = new Label("Description : ");
        descriptionLabel.setUIID("labelDefault");
        descriptionTF = new TextArea();
        descriptionTF.setHint("Tapez la description du guide");

       

        imageLabel = new Label("Image : ");
        imageLabel.setUIID("labelDefault");
        selectImageButton = new Button("Ajouter une image");

        if (currentGuide == null) {
            imageIV = new ImageViewer(theme.getImage("default.jpg").fill(1100, 500));
            manageButton = new Button("Ajouter");
        } else {
            nomTF.setText(currentGuide.getNom());
            prenomTF.setText(currentGuide.getPrenom());
            descriptionTF.setText(currentGuide.getDescription());


            if (currentGuide.getImage() != null) {
                String url = Statics.Guide_IMAGE_URL + currentGuide.getImage();
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
            selectedImage = currentGuide.getImage();

            manageButton = new Button("Modifier");
        }

     
  

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(
                nomLabel, nomTF,
                prenomLabel, prenomTF,
                descriptionLabel, descriptionTF,
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


        if (currentGuide == null) {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = GuideService.getInstance().add(
                            new Guide(
                                    nomTF.getText(),
                                    prenomTF.getText(),
                                    descriptionTF.getText(),
                                    selectedImage
                                    
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succes", "guide ajoute avec succes", new Command("Ok"));
                    } else {
                        Dialog.show("Erreur", "Erreur d'ajout du guide. Code d'erreur : " + responseCode, new Command("Ok"));
                    }

                    showBackAndRefresh();
                }
            });
        } else {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = GuideService.getInstance().edit(
                            new Guide(
                                    currentGuide.getId(),
                                    nomTF.getText(),
                                    prenomTF.getText(),
                                    descriptionTF.getText(),
                                    selectedImage
                                    
                            ),
                            imageEdited
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succes", "guide modifie avec succes", new Command("Ok"));
                    } else {
                        Dialog.show("Erreur", "Erreur de modification du guide. Code d'erreur : " + responseCode, new Command("Ok"));
                    }

                    showBackAndRefresh();
                }
            });
        }
    }

    private void showBackAndRefresh() {
        AccueilBack.accueilForm.addComponentAndRefresh(
                new com.culinair.gui.back.guide.DisplayAll().addGUIs(),
                "Guide"
        );
        previous.showBack();
    }

    private boolean controleDeSaisie() {

        if (nomTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le nom", new Command("Ok"));
            return false;
        }

        if (prenomTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le type", new Command("Ok"));
            return false;
        }

        if (descriptionTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir la description", new Command("Ok"));
            return false;
        }
        if (selectedImage == null) {
            Dialog.show("Avertissement", "Veuillez choisir une image", new Command("Ok"));
            return false;
        }

  

        return true;
    }
}
