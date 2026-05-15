package ui.panels;

import ui.theme.NutrixUI;
import javax.swing.*;
import java.awt.*;

public class PrescritoInfundidoPanel extends JPanel {
    public PrescritoInfundidoPanel() {
        setOpaque(false);
        setLayout(new BorderLayout());
        JLabel l = new JLabel("Módulo em desenvolvimento", JLabel.CENTER);
        l.setFont(NutrixUI.BODY);
        l.setForeground(NutrixUI.TEXT_MUTED);
        add(l);
    }
}
