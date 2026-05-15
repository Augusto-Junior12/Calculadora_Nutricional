package ui.panels;

import ui.theme.NutrixTheme;
import util.Formatador;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class IngestaoOralPanel extends JPanel {
    public IngestaoOralPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        JLabel l = new JLabel("Módulo de Ingestão Oral - Nutrix v3", JLabel.CENTER);
        l.setFont(NutrixTheme.H2);
        add(l, BorderLayout.CENTER);
    }
}
