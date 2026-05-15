package ui.panels;

import ui.theme.NutrixUI;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class IngestaoOralPanel extends JPanel {
    public IngestaoOralPanel() {
        setOpaque(false);
        setLayout(new BorderLayout());
        JLabel l = new JLabel("Módulo em desenvolvimento", JLabel.CENTER);
        l.setFont(NutrixUI.BODY);
        l.setForeground(NutrixUI.TEXT_MUTED);
        add(l);
    }
}
