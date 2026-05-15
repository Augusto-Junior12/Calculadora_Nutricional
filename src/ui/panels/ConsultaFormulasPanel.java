package ui.panels;

import model.FormulaEnteral;
import repository.FormulaEnteralRepository;
import ui.theme.HospitalTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

/**
 * Painel de consulta de fórmulas enterais.
 * Tabela comparativa das 51 fórmulas com filtros por densidade.
 */
public class ConsultaFormulasPanel extends JPanel {

    private JTable tabela;
    private DefaultTableModel tableModel;
    private JComboBox<String> filtroBox;
    private final FormulaEnteralRepository repo = FormulaEnteralRepository.getInstance();

    public ConsultaFormulasPanel() {
        setLayout(new BorderLayout(0, 10));
        setBackground(HospitalTheme.BACKGROUND);
        setBorder(new EmptyBorder(20, 25, 20, 25));

        JPanel h = new JPanel(new GridLayout(2, 1)); h.setOpaque(false);
        h.add(HospitalTheme.createTitle("Consulta de Fórmulas Enterais"));
        h.add(HospitalTheme.createLabel(repo.getTotal() + " fórmulas cadastradas — clique nas colunas para ordenar"));
        add(h, BorderLayout.NORTH);

        // Filtros
        JPanel filtroPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filtroPanel.setOpaque(false);
        filtroPanel.add(HospitalTheme.createLabel("Filtrar por densidade:"));
        filtroBox = HospitalTheme.createComboBox(new String[]{
            "Todas", "1.0 kcal/ml", "1.2 kcal/ml", "1.5 kcal/ml", "2.0 kcal/ml",
            "Sem Soja (R11)", "Sem Fibra (R09)"
        });
        filtroBox.addActionListener(e -> filtrar());
        filtroPanel.add(filtroBox);
        add(filtroPanel, BorderLayout.NORTH);

        // Tabela
        String[] cols = {"Fórmula", "Dens.", "PTN/L", "CHO/L", "LIP/L", "Fibra", "K", "Osmol.", "Soja", "Fibra?"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
            public Class<?> getColumnClass(int c) {
                if (c >= 1 && c <= 7) return Double.class;
                return String.class;
            }
        };
        tabela = new JTable(tableModel);
        tabela.setFont(HospitalTheme.FONT_BODY);
        tabela.setRowHeight(26);
        tabela.getTableHeader().setFont(HospitalTheme.FONT_BODY_BOLD);
        tabela.setAutoCreateRowSorter(true);
        tabela.setSelectionBackground(HospitalTheme.PRIMARY_LIGHT);
        tabela.setSelectionForeground(Color.WHITE);
        tabela.setGridColor(HospitalTheme.BORDER);

        // Alternating row colors
        tabela.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int r, int c) {
                Component comp = super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                if (!sel) comp.setBackground(r % 2 == 0 ? HospitalTheme.SURFACE : HospitalTheme.SURFACE_ALT);
                return comp;
            }
        });
        tabela.setDefaultRenderer(Double.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int r, int c) {
                Component comp = super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                if (!sel) comp.setBackground(r % 2 == 0 ? HospitalTheme.SURFACE : HospitalTheme.SURFACE_ALT);
                setHorizontalAlignment(RIGHT);
                return comp;
            }
        });

        carregarDados(repo.listarTodas());

        JScrollPane sp = new JScrollPane(tabela);
        sp.setBorder(BorderFactory.createLineBorder(HospitalTheme.BORDER));
        add(sp, BorderLayout.CENTER);
    }

    private void carregarDados(List<FormulaEnteral> formulas) {
        tableModel.setRowCount(0);
        for (FormulaEnteral f : formulas) {
            tableModel.addRow(new Object[]{
                f.getNome(), f.getDensidade(), f.getPtnPorLitro(),
                f.getChoPorLitro(), f.getLipPorLitro(), f.getFibrasPorLitro(),
                f.getPotassio(), f.getOsmolaridade(),
                f.isContemSoja() ? "Sim" : "Não",
                f.isContemFibra() ? "Sim" : "Não"
            });
        }
    }

    private void filtrar() {
        int sel = filtroBox.getSelectedIndex();
        List<FormulaEnteral> lista;
        switch (sel) {
            case 1: lista = repo.filtrarPorDensidade(0.9, 1.05); break;
            case 2: lista = repo.filtrarPorDensidade(1.1, 1.25); break;
            case 3: lista = repo.filtrarPorDensidade(1.4, 1.55); break;
            case 4: lista = repo.filtrarPorDensidade(1.9, 2.1); break;
            case 5: lista = repo.filtrarSemSoja(); break;
            case 6: lista = repo.filtrarSemFibra(); break;
            default: lista = repo.listarTodas();
        }
        carregarDados(lista);
    }
}
