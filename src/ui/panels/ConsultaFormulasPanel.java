package ui.panels;

import repository.FormulaEnteralRepository;
import ui.theme.NutrixTheme;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ConsultaFormulasPanel extends JPanel {
    public ConsultaFormulasPanel() {
        setLayout(new BorderLayout(0, 24));
        setBackground(Color.WHITE);
        
        JLabel t = new JLabel("Catálogo de Fórmulas");
        t.setFont(NutrixTheme.H2);
        add(t, BorderLayout.NORTH);

        String[] cols = {"Fórmula", "Densidade", "PTN/L"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        FormulaEnteralRepository.getInstance().listarTodas().forEach(f -> {
            model.addRow(new Object[]{f.getNome(), f.getDensidade(), f.getPtnPorLitro()});
        });
        
        JTable table = new JTable(model);
        table.setFont(NutrixTheme.BODY);
        table.setRowHeight(32);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }
}
