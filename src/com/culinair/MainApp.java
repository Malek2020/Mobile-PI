package com.culinair;

import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Toolbar;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.culinair.entities.Evenement;
import com.culinair.entities.Plat;
import com.culinair.entities.User;
import com.culinair.gui.LoginForm;

import java.util.ArrayList;

import static com.codename1.ui.CN.updateNetworkThreadCount;

public class MainApp {

    private static User session;
    private Form current;
    private Resources theme;
    public static ArrayList<Evenement> listEvenementsParticipe;
    public static ArrayList<Plat> listPlatFavoris;

    public static User getSession() {
        return session;
    }

    public static void setSession(User session) {
        MainApp.session = session;
    }

    public void init(Object context) {
        theme = UIManager.initFirstTheme("/theme");
        updateNetworkThreadCount(3);
        Toolbar.setGlobalToolbar(true);
    }

    public void start() {
        if (current != null) {
            current.show();
            return;
        }
        listEvenementsParticipe = new ArrayList<>();
        listPlatFavoris = new ArrayList<>();
        new LoginForm(theme).show();
    }

    public void stop() {
        current = Display.getInstance().getCurrent();
    }

    public void destroy() {
    }

}
