package ui.panels;

import ui.theme.HospitalTheme;
import util.Formatador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Painel Prescrito × Infundido.
 * R12: volume recebido ≤ prescrito. Meta institucional ≥ 80%.
 */
public class PrescritoInfundidoPanel extends JPanel {

    private JTextField pacienteField, codigoField;
    private JTextField volPrescField, volRecebField;
    private DefaultTableModel tableModel;
    private JTable tabela;
    private JTextArea resultadoArea;
    private final List<double[]> registros = new ArrayList<>(); // [prescrito, recebido]

    public PrescritoInfundidoPanel() {
        setLayout(new BorderLayout(0, 10));
        setBackground(HospitalTheme.BACKGROUND);
        setBorder(new EmptyBorder(20, 25, 20, 25));

        JPanel h = new JPanel(new GridLayout(2, 1)); h.setOpaque(false);
        h.add(HospitalTheme.createTitle("Prescrito × Infundido"));
        h.add(HospitalTheme.createLabel("Indicador de qualidade — Meta institucional ≥ 80%"));
        add(h, BorderLayout.NORTH);

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setOpaque(false);

        // Formulário de entrada
        JPanel card = HospitalTheme.createCard();
        card.setLayout(new BorderLayout(0, 10));
        card.add(HospitalTheme.createSubtitle("Novo Registro"), BorderLayout.NORTH);
        JPanel grid = new JPanel(new GridLayout(0, 4, 10, 8));
        grid.setOpaque(false);
        pacienteField = addCampo(grid, "Paciente:");
        codigoField = addCampo(grid, "Código:");
        volPrescField = addCampo(grid, "Vol. Prescrito (ml):");
        volRecebField = addCampo(grid, "Vol. Recebido (ml):");
        card.add(grid, BorderLayout.CENTER);

        JPanel bp = new JPanel(new FlowLayout(FlowLayout.LEFT)); bp.setOpaque(false);
        JButton addBtn = HospitalTheme.createPrimaryButton("Adicionar Registro");
        JButton calcBtn = HospitalTheme.createSecondaryButton("Calcular Média");
        addBtn.addActionListener(e -> adicionarRegistro());
        calcBtn.addActionListener(e -> calcularMedia());
        bp.add(addBtn); bp.add(calcBtn);
        card.add(bp, BorderLayout.SOUTH);
        main.add(card);
        main.add(Box.createVerticalStrut(10));

        // Tabela de registros
        String[] cols = {"Paciente", "Código", "Vol. Prescrito", "Vol. Recebido", "% Recebido", "Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = new JTable(tableModel);
        tabela.setFont(HospitalTheme.FONT_BODY);
        tabela.setRowHeight(28);
        tabela.getTableHeader().setFont(HospitalTheme.FONT_BODY_BOLD);

        // Colorir coluna de status
        tabela.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int r, int c) {
                Component comp = super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                String status = v != null ? v.toString() : "";
                if (status.contains("ADEQUADO")) comp.setForeground(HospitalTheme.SUCCESS);
                else if (status.contains("INADEQUADO")) comp.setForeground(HospitalTheme.DANGER);
                else comp.setForeground(HospitalTheme.TEXT_PRIMARY);
                return comp;
            }
        });

        JScrollPane sp = new JScrollPane(tabela);
        sp.setPreferredSize(new Dimension(0, 200));
        sp.setBorder(BorderFactory.createTitledBorder("Registros"));
        main.add(sp);
        main.add(Box.createVerticalStrut(10));

        resultadoArea = new JTextArea(5, 50);
        resultadoArea.setFont(HospitalTheme.FONT_MONO);
        resultadoArea.setEditable(false);
        resultadoArea.setBackground(HospitalTheme.SURFACE_ALT);
        resultadoArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        main.add(new JScrollPane(resultadoArea));

        JScrollPane ms = new JScrollPane(main); ms.setBorder(null);
        ms.getVerticalScrollBar().setUnitIncrement(16);
        add(ms, BorderLayout.CENTER);
    }

    private void adicionarRegistro() {
        try {
            String pac = pacienteField.getText().trim();
            String cod = codigoField.getText().trim();
            double presc = parseD(volPrescField);
            double receb = parseD(volRecebField);

            if (presc <= 0) {
                resultadoArea.setText("⚠ Volume prescrito deve ser > 0");
                return;
            }
            if (receb > presc) {
                resultadoArea.setText("⚠ Volume recebido não pode exceder prescrito (R12)");
                return;
            }

            double pct = (receb / presc) * 100.0;
            String status = pct >= 80 ? "✅ ADEQUADO" : "⚠ INADEQUADO";

            tableModel.addRow(new Object[]{
                pac, cod,
                Formatador.ml(presc), Formatador.ml(receb),
                Formatador.percentual(pct), status
            });
            registros.add(new double[]{presc, receb});

            resultadoArea.setText("Registro adicionado: " + Formatador.percentual(pct));
            volPrescField.setText(""); volRecebField.setText("");

        } catch (Exception e) {
            resultadoArea.setText("⚠ Erro: " + e.getMessage());
        }
    }

    private void calcularMedia() {
        if (registros.isEmpty()) {
            resultadoArea.setText("⚠ Nenhum registro para calcular.");
            return;
        }

        double somaPct = 0;
        int count = 0;
        for (double[] r : registros) {
            if (r[0] > 0) {
                somaPct += (r[1] / r[0]) * 100.0;
                count++;
            }
        }

        double media = somaPct / count;
        StringBuilder sb = new StringBuilder();
        sb.append("▸ MÉDIA DO PERÍODO\n");
        sb.append("  Registros: ").append(count).append("\n");
        sb.append("  Média % Recebido: ").append(Formatador.percentual(media)).append("\n\n");
        if (media >= 80) {
            sb.append("✅ Meta institucional atingida (≥ 80%)\n");
        } else {
            sb.append("⚠ ABAIXO da meta institucional (< 80%)\n");
            sb.append("  Investigar causas: jejum, intolerância, posicionamento\n");
        }
        resultadoArea.setText(sb.toString());
    }

    private double parseD(JTextField f) {
        String t = f.getText().trim().replace(",", "."); return t.isEmpty() ? 0 : Double.parseDouble(t);
    }
    private JTextField addCampo(JPanel g, String l) {
        g.add(HospitalTheme.createLabel(l)); JTextField f = HospitalTheme.createTextField(); g.add(f); return f;
    }
}
