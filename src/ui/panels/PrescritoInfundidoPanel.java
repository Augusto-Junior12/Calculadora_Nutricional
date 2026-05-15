package ui.panels;

import ui.theme.NutrixTheme;
import javax.swing.*;
import java.awt.*;

public class PrescritoInfundidoPanel extends JPanel {
    public PrescritoInfundidoPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        JLabel l = new JLabel("Qualidade (P×I) - Nutrix v3", JLabel.CENTER);
        l.setFont(NutrixTheme.H2);
        add(l, BorderLayout.CENTER);
    }
}
