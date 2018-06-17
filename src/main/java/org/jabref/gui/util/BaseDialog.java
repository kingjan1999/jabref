package org.jabref.gui.util;

import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.jabref.Globals;
import org.jabref.gui.icon.IconTheme;
import org.jabref.gui.keyboard.KeyBinding;
import org.jabref.gui.keyboard.KeyBindingRepository;
import org.jabref.preferences.JabRefPreferences;

public class BaseDialog<T> extends Dialog<T> {

    protected BaseDialog() {
        getDialogPane().getScene().setOnKeyPressed(event -> {
            KeyBindingRepository keyBindingRepository = Globals.getKeyPrefs();
            if (keyBindingRepository.checkKeyCombinationEquality(KeyBinding.CLOSE, event)) {
                close();
            }
        });

        setDialogIcon(IconTheme.getJabRefImageFX());
        if(Globals.prefs.getBoolean(JabRefPreferences.DARK_THEME)) {
            Globals.getThemeLoader().installDarkCss(getDialogPane().getScene());
        } else {
            Globals.getThemeLoader().installBaseCss(getDialogPane().getScene());
        }
    }

    private void setDialogIcon(Image image) {
        Stage dialogWindow = (Stage) getDialogPane().getScene().getWindow();
        dialogWindow.getIcons().add(image);
    }
}
