package org.jabref.gui.preftabs;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import org.jabref.gui.DialogService;
import org.jabref.gui.GUIGlobals;
import org.jabref.logic.l10n.Localization;
import org.jabref.preferences.JabRefPreferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

class AppearancePrefsTab extends JPanel implements PrefsTab {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppearancePrefsTab.class);

    private final JabRefPreferences prefs;

    private final Font usedFont = GUIGlobals.currentFont;
    private final JCheckBox fxFontTweaksLAF;
    private final JCheckBox useDarkTheme;

    private final DialogService dialogService;

    /**
     * Customization of appearance parameters.
     *
     * @param prefs a <code>JabRefPreferences</code> value
     */
    public AppearancePrefsTab(DialogService dialogService, JabRefPreferences prefs) {
        this.dialogService = dialogService;
        this.prefs = prefs;
        setLayout(new BorderLayout());

        FormLayout layout = new FormLayout("8dlu, 1dlu, left:pref:grow, 4dlu, fill:pref, 4dlu, fill:pref, 4dlu, left:pref, 1dlu, left:pref, 4dlu, left:pref", "");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        builder.appendSeparator(Localization.lang("Appearance"));

        fxFontTweaksLAF = new JCheckBox(Localization.lang("Tweak font rendering for entry editor on Linux"));
        builder.append(fxFontTweaksLAF,  13);
        builder.nextLine();

        useDarkTheme = new JCheckBox("Use dark theme");
        builder.append(useDarkTheme, 13);
        builder.nextLine();

        JPanel pan = builder.getPanel();
        pan.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(pan, BorderLayout.CENTER);
    }

    @Override
    public void setValues() {
        fxFontTweaksLAF.setSelected(prefs.getBoolean(JabRefPreferences.FX_FONT_RENDERING_TWEAK));
        useDarkTheme.setSelected(prefs.getBoolean(JabRefPreferences.DARK_THEME));
    }

    @Override
    public void storeSettings() {
        boolean isRestartRequired = false;

        // Java FX font rendering tweak
        final boolean oldFxTweakValue = prefs.getBoolean(JabRefPreferences.FX_FONT_RENDERING_TWEAK);
        prefs.putBoolean(JabRefPreferences.FX_FONT_RENDERING_TWEAK, fxFontTweaksLAF.isSelected());
        isRestartRequired |= oldFxTweakValue != fxFontTweaksLAF.isSelected();

        prefs.put(JabRefPreferences.FONT_FAMILY, usedFont.getFamily());
        prefs.putInt(JabRefPreferences.FONT_STYLE, usedFont.getStyle());
        prefs.putInt(JabRefPreferences.FONT_SIZE, usedFont.getSize());
        GUIGlobals.currentFont = usedFont;

        final boolean oldDarkThemeValue = prefs.getBoolean(JabRefPreferences.DARK_THEME);
        prefs.putBoolean(JabRefPreferences.DARK_THEME, useDarkTheme.isSelected());
        isRestartRequired |= oldDarkThemeValue != useDarkTheme.isSelected();

        if (isRestartRequired) {
            dialogService.showWarningDialogAndWait(Localization.lang("Settings"),
                    Localization.lang("Some appearance settings you changed require to restart JabRef to come into effect."));
        }
    }

    @Override
    public boolean validateSettings() {
        return true;
    }

    @Override
    public String getTabName() {
        return Localization.lang("Appearance");
    }
}
