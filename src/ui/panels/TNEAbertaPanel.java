package ui.panels;

import ui.theme.HospitalTheme;
import util.Formatador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Painel de TNE em sistema aberto (nutrição modular artesanal).
 * Trophic Basic, Carbodex, Albumix Power, Óleo de soja.
 */
public class TNEAbertaPanel extends JPanel {

    private JTextField vetField, pesoField;
    private JTextField trophicDosesField, carbodexDosesField, albumixDosesField, oleoDosesField;
    private JComboBox<String> ptnPercentBox;
    private JTextArea resultadoArea;

    // Módulos artesanais (valores fixos)
    private static final double TROPHIC_KCAL = 33.93, TROPHIC_CHO = 4.68, TROPHIC_PTN = 1.24, TROPHIC_LIP = 1.09;
    private static final double CARBODEX_KCAL = 40, CARBODEX_CHO = 10;
    private static final double ALBUMIX_KCAL = 70, ALBUMIX_PTN = 16, ALBUMIX_CHO = 1.5;
    private static final double OLEO_KCAL = 108, OLEO_LIP = 12;

    public TNEAbertaPanel() {
        setLayout(new BorderLayout(0, 10));
        setBackground(HospitalTheme.BACKGROUND);
        setBorder(new EmptyBorder(20, 25, 20, 25));

        JPanel h = new JPanel(new GridLayout(2, 1)); h.setOpaque(false);
        h.add(HospitalTheme.createTitle("TNE — Sistema Aberto"));
        h.add(HospitalTheme.createLabel("Nutrição modular artesanal — Trophic, Carbodex, Albumix, Óleo"));
        add(h, BorderLayout.NORTH);

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setOpaque(false);

        // Parâmetros
        JPanel card = HospitalTheme.createCard();
        card.setLayout(new BorderLayout(0, 10));
        card.add(HospitalTheme.createSubtitle("Parâmetros"), BorderLayout.NORTH);
        JPanel grid = new JPanel(new GridLayout(0, 4, 10, 8));
        grid.setOpaque(false);
        vetField = addCampo(grid, "VET alvo (kcal):");
        pesoField = addCampo(grid, "Peso (kg):");
        grid.add(HospitalTheme.createLabel("% PTN desejado:"));
        ptnPercentBox = HospitalTheme.createComboBox(new String[]{"15%", "20%"});
        grid.add(ptnPercentBox);
        addCampo(grid, ""); // spacer
        card.add(grid, BorderLayout.CENTER);
        main.add(card);
        main.add(Box.createVerticalStrut(10));

        // Doses
        JPanel dosesCard = HospitalTheme.createCard();
        dosesCard.setLayout(new BorderLayout(0, 10));
        dosesCard.add(HospitalTheme.createSubtitle("Doses dos Módulos"), BorderLayout.NORTH);
        JPanel dGrid = new JPanel(new GridLayout(0, 4, 10, 8));
        dGrid.setOpaque(false);
        trophicDosesField = addCampo(dGrid, "Trophic Basic (medidas 7.8g):");
        carbodexDosesField = addCampo(dGrid, "Carbodex (medidas 10g):");
        albumixDosesField = addCampo(dGrid, "Albumix (medidores 20g):");
        oleoDosesField = addCampo(dGrid, "Óleo soja (colheres 13ml):");
        dosesCard.add(dGrid, BorderLayout.CENTER);
        main.add(dosesCard);
        main.add(Box.createVerticalStrut(10));

        JPanel bp = new JPanel(new FlowLayout(FlowLayout.LEFT)); bp.setOpaque(false);
        JButton calcBtn = HospitalTheme.createPrimaryButton("Calcular Composição");
        JButton autoBtn = HospitalTheme.createSecondaryButton("Calcular Doses Auto");
        calcBtn.addActionListener(e -> calcularManual());
        autoBtn.addActionListener(e -> calcularAuto());
        bp.add(calcBtn); bp.add(autoBtn);
        main.add(bp);
        main.add(Box.createVerticalStrut(10));

        resultadoArea = new JTextArea(18, 60);
        resultadoArea.setFont(HospitalTheme.FONT_MONO);
        resultadoArea.setEditable(false);
        resultadoArea.setBackground(HospitalTheme.SURFACE_ALT);
        resultadoArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        main.add(new JScrollPane(resultadoArea));

        JScrollPane ms = new JScrollPane(main); ms.setBorder(null);
        ms.getVerticalScrollBar().setUnitIncrement(16);
        add(ms, BorderLayout.CENTER);
    }

    private void calcularManual() {
        try {
            double peso = parseD(pesoField);
            double tDoses = parseD(trophicDosesField);
            double cDoses = parseD(carbodexDosesField);
            double aDoses = parseD(albumixDosesField);
            double oDoses = parseD(oleoDosesField);

            double kcal = tDoses * TROPHIC_KCAL + cDoses * CARBODEX_KCAL + aDoses * ALBUMIX_KCAL + oDoses * OLEO_KCAL;
            double cho = tDoses * TROPHIC_CHO + cDoses * CARBODEX_CHO + aDoses * ALBUMIX_CHO;
            double ptn = tDoses * TROPHIC_PTN + aDoses * ALBUMIX_PTN;
            double lip = tDoses * TROPHIC_LIP + oDoses * OLEO_LIP;

            double kcalCHO = cho * 4, kcalPTN = ptn * 4, kcalLIP = lip * 9;
            double pctCHO = (kcalCHO / kcal) * 100, pctPTN = (kcalPTN / kcal) * 100, pctLIP = (kcalLIP / kcal) * 100;

            StringBuilder sb = new StringBuilder();
            sb.append("═══════════════════════════════\n");
            sb.append("  TNE SISTEMA ABERTO\n");
            sb.append("═══════════════════════════════\n\n");
            sb.append("▸ COMPOSIÇÃO TOTAL\n");
            sb.append("  Kcal: ").append(Formatador.kcal(kcal)).append("\n");
            sb.append("  CHO:  ").append(Formatador.gramas(cho)).append(" (").append(Formatador.percentual(pctCHO)).append(")\n");
            sb.append("  PTN:  ").append(Formatador.gramas(ptn)).append(" (").append(Formatador.percentual(pctPTN)).append(")\n");
            sb.append("  LIP:  ").append(Formatador.gramas(lip)).append(" (").append(Formatador.percentual(pctLIP)).append(")\n\n");
            if (peso > 0) {
                sb.append("  Kcal/kg: ").append(Formatador.decimal2(kcal / peso)).append("\n");
                sb.append("  PTN/kg:  ").append(Formatador.decimal2(ptn / peso)).append("\n\n");
            }

            // Alertas macro
            if (pctCHO < 45 || pctCHO > 65) sb.append("  ⚠ CHO fora de 45-65%\n");
            if (pctPTN < 15 || pctPTN > 20) sb.append("  ⚠ PTN fora de 15-20%\n");
            if (pctLIP < 20 || pctLIP > 35) sb.append("  ⚠ LIP fora de 20-35%\n");

            // Volume e latas
            double aguaMl = kcal * 1.0; // ~1 ml/kcal
            sb.append("\n▸ VOLUME E RENDIMENTO\n");
            sb.append("  Volume água aprox: ").append(Formatador.ml(aguaMl)).append("\n");
            sb.append("  4 doses de: ").append(Formatador.ml(aguaMl / 4)).append(" cada\n");
            sb.append("  Lata Trophic (258g): ").append(Formatador.decimal2((tDoses * 7.8) / 258)).append(" latas/dia\n");
            sb.append("  Lata Carbodex (400g): ").append(Formatador.decimal2((cDoses * 10) / 400)).append(" latas/dia\n");
            sb.append("  Lata Albumix (250g): ").append(Formatador.decimal2((aDoses * 20) / 250)).append(" latas/dia\n");

            resultadoArea.setText(sb.toString());
            resultadoArea.setCaretPosition(0);
        } catch (Exception e) {
            resultadoArea.setText("⚠ Erro: " + e.getMessage());
        }
    }

    private void calcularAuto() {
        try {
            double vet = parseD(vetField);
            double peso = parseD(pesoField);
            double pctPTN = ptnPercentBox.getSelectedIndex() == 0 ? 15 : 20;

            double ptnAlvo = (vet * pctPTN / 100.0) / 4;
            double albumixDoses = Math.max(1, Math.ceil((ptnAlvo - 10) / ALBUMIX_PTN));
            double carbodexDoses = 3;
            double oleoDoses = 0.5;

            double kcalFixas = albumixDoses * ALBUMIX_KCAL + carbodexDoses * CARBODEX_KCAL + oleoDoses * OLEO_KCAL;
            double kcalTrophic = vet - kcalFixas;
            double trophicDoses = Math.max(0, kcalTrophic / TROPHIC_KCAL);

            trophicDosesField.setText(Formatador.decimal1(trophicDoses));
            carbodexDosesField.setText(String.valueOf((int) carbodexDoses));
            albumixDosesField.setText(String.valueOf((int) albumixDoses));
            oleoDosesField.setText(Formatador.decimal1(oleoDoses));

            calcularManual();
        } catch (Exception e) {
            resultadoArea.setText("⚠ Erro: " + e.getMessage());
        }
    }

    private double parseD(JTextField f) {
        String t = f.getText().trim().replace(",", "."); return t.isEmpty() ? 0 : Double.parseDouble(t);
    }
    private JTextField addCampo(JPanel g, String l) {
        g.add(HospitalTheme.createLabel(l)); JTextField f = HospitalTheme.createTextField(); g.add(f); return f;
    }
}
