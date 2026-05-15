package ui.panels;

import ui.theme.NutrixTheme;
import javax.swing.*;
import java.awt.*;

public class TNEAbertaPanel extends JPanel {
    public TNEAbertaPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        JLabel l = new JLabel("Sistema Aberto - Nutrix v3", JLabel.CENTER);
        l.setFont(NutrixTheme.H2);
        add(l, BorderLayout.CENTER);
    }
}
