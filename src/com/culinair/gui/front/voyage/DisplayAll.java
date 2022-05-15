package com.culinair.gui.front.voyage;

import com.codename1.components.ToastBar;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.culinair.entities.Voyage;
import com.culinair.services.VoyageService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;


public class DisplayAll extends Form {

    public static String voyageCompareVar;
    ArrayList<Component> componentModels;
    Label dateDepartLabel, dateRetourLabel, nbrVoyageursLabel, prixLabel, villeLabel;
    Container btnsContainer;
    PickerComponent sortPicker;
    ArrayList<Voyage> listVoyages;

    public Component addGUIs() {
        Container compo = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        compo.setScrollableY(true);

        listVoyages = VoyageService.getInstance().getAll();
        componentModels = new ArrayList<>();

        sortPicker = PickerComponent.createStrings("dateDepart", "dateRetour", "nbrVoyageurs", "prix", "ville").label("Trier par");
        Button sortButton = new Button("Trier");
        sortButton.addActionListener((l) -> {
            if (componentModels.size() > 0) {
                for (Component componentModel : componentModels) {
                    compo.removeComponent(componentModel);
                }
            }
            componentModels = new ArrayList<>();
            ArrayList<Voyage> sortedList = listVoyages;
            voyageCompareVar = sortPicker.getPicker().getSelectedString();
            Collections.sort(sortedList);
            for (Voyage voyage : sortedList) {
                Component model = makeVoyageModel(voyage);
                compo.add(model);
                componentModels.add(model);
            }
            compo.revalidate();

            ToastBar.getInstance().setPosition(TOP);
            ToastBar.Status status = ToastBar.getInstance().createStatus();
            status.setShowProgressIndicator(false);
            status.setMessage("Trié par " + sortPicker.getPicker().getSelectedString());
            status.setExpires(5000);
            status.show();
        });
        Container sortContainer = new Container(new BorderLayout());

        sortContainer.add(BorderLayout.WEST, sortPicker);
        sortContainer.add(BorderLayout.EAST, sortButton);

        compo.add(sortContainer);

        if (listVoyages.size() > 0) {
            for (Voyage voyage : listVoyages) {
                Component model = makeVoyageModel(voyage);
                compo.add(model);
                componentModels.add(model);
            }
        } else {
            compo.add(new Label("Aucune donnée"));
        }

        return compo;
    }

    private Component makeVoyageModel(Voyage voyage) {
        Container voyageModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        voyageModel.setUIID("containerRounded");


        dateDepartLabel = new Label("DateDepart : " + new SimpleDateFormat("dd-MM-yyyy").format(voyage.getDateDepart()));
        dateDepartLabel.setUIID("labelDefault");

        dateRetourLabel = new Label("DateRetour : " + new SimpleDateFormat("dd-MM-yyyy").format(voyage.getDateRetour()));
        dateRetourLabel.setUIID("labelDefault");

        nbrVoyageursLabel = new Label("NbrVoyageurs : " + voyage.getNbrVoyageurs());
        nbrVoyageursLabel.setUIID("labelDefault");

        prixLabel = new Label("Prix : " + voyage.getPrix());
        prixLabel.setUIID("labelDefault");

        villeLabel = new Label("Ville : " + voyage.getVille().getVille());
        villeLabel.setUIID("labelDefault");

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        voyageModel.addAll(
                dateDepartLabel, dateRetourLabel, nbrVoyageursLabel, prixLabel, villeLabel,
                btnsContainer
        );

        return voyageModel;
    }
}