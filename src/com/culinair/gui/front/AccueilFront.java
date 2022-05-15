package com.culinair.gui.front;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.util.Resources;
import com.culinair.MainApp;
import com.culinair.gui.LoginForm;

public class AccueilFront extends Form {

    public static AccueilFront accueilForm;

    public AccueilFront(Resources theme) {
        super("Accueil");
        accueilForm = this;

        Toolbar t = new Toolbar();
        setToolbar(t);
        t.setScrollOffUponContentPane(true);

        Label rat = new Label(theme.getImage("round_logo.png"));
        rat.setTextPosition(Label.BOTTOM);
        rat.setText("Culinair");
        rat.setUIID("SideMenuLogo");
        t.addComponentToSideMenu(rat);
        setLayout(new BorderLayout());

        addComponentAndRefresh(
                new com.culinair.gui.front.user.Profil().addGUIs(),
                "Mon profil"
        );

        t.addMaterialCommandToSideMenu("Mon profil  ", FontImage.MATERIAL_PERSON, (l) -> {
            addComponentAndRefresh(
                    new com.culinair.gui.front.user.Profil().addGUIs(),
                    "Mon profil"
            );
            t.closeSideMenu();
        });

        t.addMaterialCommandToSideMenu("Restaurants  ", FontImage.MATERIAL_LOCAL_RESTAURANT, (l) -> {
            addComponentAndRefresh(
                    new com.culinair.gui.front.restaurant.DisplayAll().addGUIs(),
                    "Restaurants"
            );
            t.closeSideMenu();
        });

        t.addMaterialCommandToSideMenu("Plats  ", FontImage.MATERIAL_FOOD_BANK, (l) -> {
            addComponentAndRefresh(
                    new com.culinair.gui.front.plat.DisplayAll().addGUIs(),
                    "Plats"
            );
            t.closeSideMenu();
        });

        t.addMaterialCommandToSideMenu("Voyage  ", FontImage.MATERIAL_AIRPLANEMODE_ON, (l) -> {
            addComponentAndRefresh(
                    new com.culinair.gui.front.voyage.DisplayAll().addGUIs(),
                    "Voyage"
            );
            t.closeSideMenu();
        });

        t.addMaterialCommandToSideMenu("Pays  ", FontImage.MATERIAL_MAP, (l) -> {
            addComponentAndRefresh(
                    new com.culinair.gui.front.pays.DisplayAll().addGUIs(),
                    "Pays"
            );
            t.closeSideMenu();
        });

        t.addMaterialCommandToSideMenu("Villes  ", FontImage.MATERIAL_LOCATION_CITY, (l) -> {
            addComponentAndRefresh(
                    new com.culinair.gui.front.ville.DisplayAll().addGUIs(),
                    "Villes"
            );
            t.closeSideMenu();
        });

        t.addMaterialCommandToSideMenu("Reservation  ", FontImage.MATERIAL_BOOKMARK, (l) -> {
            addComponentAndRefresh(
                    new com.culinair.gui.front.reservation.Manage().addGUIs(),
                    "Reservation"
            );
            t.closeSideMenu();
        });

        t.addMaterialCommandToSideMenu("Guide  ", FontImage.MATERIAL_BOOK, (l) -> {
            addComponentAndRefresh(
                    new com.culinair.gui.front.guide.DisplayAll().addGUIs(),
                    "Guide"
            );
            t.closeSideMenu();
        });

        t.addMaterialCommandToSideMenu("Evenement  ", FontImage.MATERIAL_CALENDAR_TODAY, (l) -> {
            addComponentAndRefresh(
                    new com.culinair.gui.front.evenement.DisplayAll().addGUIs(),
                    "Evenement"
            );
            t.closeSideMenu();
        });

        t.addMaterialCommandToSideMenu("Promotion  ", FontImage.MATERIAL_MONEY_OFF, (l) -> {
            addComponentAndRefresh(
                    new com.culinair.gui.front.promotion.DisplayAll().addGUIs(),
                    "Promotion"
            );
            t.closeSideMenu();
        });

        t.addMaterialCommandToSideMenu("Activite  ", FontImage.MATERIAL_HIKING, (l) -> {
            addComponentAndRefresh(
                    new com.culinair.gui.front.activite.DisplayAll().addGUIs(),
                    "Activite"
            );
            t.closeSideMenu();
        });

        t.addMaterialCommandToSideMenu("Transport  ", FontImage.MATERIAL_CAR_RENTAL, (l) -> {
            addComponentAndRefresh(
                    new com.culinair.gui.front.transport.DisplayAll().addGUIs(),
                    "Transport"
            );
            t.closeSideMenu();
        });

        t.addMaterialCommandToSideMenu("Deconnexion  ", FontImage.MATERIAL_LOGOUT, (l) -> {
            MainApp.setSession(null);
            LoginForm.loginForm.showBack();
            t.closeSideMenu();
        });
    }

    public void addComponentAndRefresh(Component component, String title) {
        super.removeAll();
        addComponent(BorderLayout.CENTER, component);
        super.getToolbar().setTitle(title);
        super.revalidate();
    }
}
