package ui.panels;

import repository.FormulaEnteralRepository;
import ui.theme.NutrixUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class ConsultaFormulasPanel extends JPanel {

    public ConsultaFormulasPanel() {
        setOpaque(false);
        setLayout(new BorderLayout(0, 20));

        JLabel title = new JLabel("Catálogo de Fórmulas Enterais");
        title.setFont(NutrixUI.H1);
        title.setForeground(NutrixUI.TEXT_PRIMARY);
        add(title, BorderLayout.NORTH);

        String[] cols = {"Fórmula", "Densidade (kcal/ml)", "PTN (g/L)", "CHO (g/L)", "LIP (g/L)"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        FormulaEnteralRepository.getInstance().listarTodas().forEach(f ->
            model.addRow(new Object[]{
                f.getNome(), f.getDensidade(),
                f.getPtnPorLitro(), "—", "—"
            })
        );

        JTable table = new JTable(model);
        table.setFont(NutrixUI.BODY);
        table.setRowHeight(40);
        table.setShowHorizontalLines(true);
        table.setGridColor(NutrixUI.BORDER);
        table.setSelectionBackground(NutrixUI.ACCENT_LIGHT);
        table.setSelectionForeground(NutrixUI.TEXT_PRIMARY);
        table.setOpaque(false);
        ((DefaultTableCellRenderer)table.getDefaultRenderer(Object.class)).setHorizontalAlignment(JLabel.CENTER);

        JTableHeader header = table.getTableHeader();
        header.setFont(NutrixUI.LABEL);
        header.setBackground(NutrixUI.BG_PAGE);
        header.setForeground(NutrixUI.TEXT_SECONDARY);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(NutrixUI.BORDER, 1));
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);

        add(scroll, BorderLayout.CENTER);
    }
}
