package com.culinair.gui.front.activite;

import com.codename1.components.ImageViewer;
import com.codename1.components.ShareButton;
import com.codename1.io.FileSystemStorage;
import com.codename1.io.Log;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.util.ImageIO;
import com.culinair.entities.Activite;
import com.culinair.gui.front.AccueilFront;
import com.culinair.services.ActiviteService;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class DisplayAll extends Form {

    public static Activite currentActivite = null;
    TextField searchTF;
    ArrayList<Component> componentModels;
    Label nomLabel, typeActiviteLabel, descriptionLabel, tempsLabel, prixActiviteLabel, nombreParticipantLabel, transportLabel;
    Container btnsContainer;

    public Component addGUIs() {
        Container compo = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        compo.setScrollableY(true);

        ArrayList<Activite> listActivites = ActiviteService.getInstance().getAll();
        componentModels = new ArrayList<>();

        // recherche
        searchTF = new TextField("", "Chercher un activite");
        searchTF.addDataChangedListener((d, t) -> {
            if (componentModels.size() > 0) {
                for (Component componentModel : componentModels) {
                    compo.removeComponent(componentModel);
                }
            }
            componentModels = new ArrayList<>();
            for (Activite activite : listActivites) {
                if (activite.getNom().startsWith(searchTF.getText())) {
                    Component model = makeActiviteModel(activite);
                    compo.add(model);
                    componentModels.add(model);
                }
            }
            compo.revalidate();
        });
        compo.add(searchTF);

        if (listActivites.size() > 0) {
            for (Activite activite : listActivites) {
                Component model = makeActiviteModel(activite);
                compo.add(model);
                componentModels.add(model);
            }
        } else {
            compo.add(new Label("Aucune donnee"));
        }

        return compo;
    }

    private Component makeActiviteModel(Activite activite) {
        Container activiteModel = makeModelWithoutButtons(activite);
        Button btnAfficherScreenshot = new Button("Partager");
        btnAfficherScreenshot.addActionListener(listener -> share(activite));

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");
        btnsContainer.add(BorderLayout.CENTER, btnAfficherScreenshot);

        activiteModel.add(
                btnsContainer
        );

        return activiteModel;
    }

    private Container makeModelWithoutButtons(Activite activite) {
        Container activiteModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        activiteModel.setUIID("containerRounded");

        nomLabel = new Label(activite.getNom());
        nomLabel.setUIID("labelCenter");

        typeActiviteLabel = new Label("Type : " + activite.getTypeActivite());
        typeActiviteLabel.setUIID("labelDefault");

        descriptionLabel = new Label("Description : " + activite.getDescription());
        descriptionLabel.setUIID("labelDefault");

        tempsLabel = new Label("Temps : " + activite.getTemps());
        tempsLabel.setUIID("labelDefault");

        prixActiviteLabel = new Label("Prix : " + activite.getPrixActivite());
        prixActiviteLabel.setUIID("labelDefault");

        nombreParticipantLabel = new Label("Nb de participants : " + activite.getNombreParticipant());
        nombreParticipantLabel.setUIID("labelDefault");

        if (activite.getTransport() != null) {
            transportLabel = new Label("Transport : " + activite.getTransport().getTypeTransport());
            transportLabel.setUIID("labelDefault");
        } else {
            transportLabel = new Label("Transport : null");
        }

        activiteModel.addAll(
                nomLabel, typeActiviteLabel, descriptionLabel, tempsLabel, prixActiviteLabel, nombreParticipantLabel, transportLabel
        );

        return activiteModel;
    }

    private void share(Activite activite) {
        Form form = new Form();
        form.add(new Label("Activite " + activite.getNom()));
        form.add(makeModelWithoutButtons(activite));
        String imageFile = FileSystemStorage.getInstance().getAppHomePath() + "screenshot.png";
        Image screenshot = Image.createImage(
                com.codename1.ui.Display.getInstance().getDisplayWidth(),
                com.codename1.ui.Display.getInstance().getDisplayHeight()
        );
        form.revalidate();
        form.setVisible(true);
        form.paintComponent(screenshot.getGraphics(), true);
        form.removeAll();
        try (OutputStream os = FileSystemStorage.getInstance().openOutputStream(imageFile)) {
            ImageIO.getImageIO().save(screenshot, os, ImageIO.FORMAT_PNG, 1);
        } catch (IOException err) {
            Log.e(err);
        }
        Form screenShotForm = new Form("Partager l'activite", new BoxLayout(BoxLayout.Y_AXIS));
        ImageViewer screenshotViewer = new ImageViewer(screenshot.fill(1000, 2000));
        screenshotViewer.setFocusable(false);
        screenshotViewer.setUIID("screenshot");
        ShareButton btnPartager = new ShareButton();
        btnPartager.setText("Partager ");
        btnPartager.setTextPosition(LEFT);
        btnPartager.setImageToShare(imageFile, "image/png");
        btnPartager.setTextToShare(activite.toString());
        screenShotForm.addAll(screenshotViewer, btnPartager);
        Toolbar toolbar = new Toolbar();
        screenShotForm.setToolbar(toolbar);
        screenShotForm.setTitle("Partager l'activite");
        screenShotForm.getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> AccueilFront.accueilForm.showBack());
        screenShotForm.show();
        // FIN API PARTAGE
    }
}
