import ui.LoginFrame;
import javax.swing.*;

/**
 * Ponto de entrada do Nutrix Hospital OS.
 * Inicia sempre pela tela de Login.
 */
public class Main {
    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}