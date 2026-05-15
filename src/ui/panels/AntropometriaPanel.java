package ui.panels;

import model.AlertaClinico;
import model.enums.ClassificacaoCB;
import model.enums.ClassificacaoIMC;
import model.enums.Etnia;
import model.enums.Genero;
import service.AntropometriaService;
import ui.theme.HospitalTheme;
import util.Formatador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Painel de avaliação antropométrica.
 * Chumlea, Jung, Rabito, peso ideal, peso ajustado, IMC, %CB.
 */
public class AntropometriaPanel extends JPanel {

    private final AntropometriaService service = new AntropometriaService();
    private JTextField ajField, cbField, cpField, caField, ccField, idadeField;
    private JTextField pesoAtualField, alturaField, pesoUsualField;
    private JComboBox<String> generoBox, etniaBox;
    private JTextArea resultadoArea;

    public AntropometriaPanel() {
        setLayout(new BorderLayout(0, 10));
        setBackground(HospitalTheme.BACKGROUND);
        setBorder(new EmptyBorder(20, 25, 20, 25));

        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setOpaque(false);
        header.add(HospitalTheme.createTitle("Avaliação Antropométrica"));
        header.add(HospitalTheme.createLabel("Estimativas indiretas — Chumlea (1985/1988), Jung (2004), Rabito (2008)"));
        add(header, BorderLayout.NORTH);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setOpaque(false);

        // Dados de entrada
        JPanel card = HospitalTheme.createCard();
        card.setLayout(new BorderLayout(0, 10));
        card.add(HospitalTheme.createSubtitle("Dados do Paciente"), BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(0, 4, 10, 8));
        grid.setOpaque(false);
        idadeField = addCampo(grid, "Idade (anos):");
        generoBox = addCombo(grid, "Gênero:", new String[]{"Masculino", "Feminino"});
        etniaBox = addCombo(grid, "Etnia:", new String[]{"Branco", "Negro"});
        pesoAtualField = addCampo(grid, "Peso Atual (kg):");
        alturaField = addCampo(grid, "Altura (m):");
        ajField = addCampo(grid, "AJ (cm):");
        cbField = addCampo(grid, "CB (cm):");
        cpField = addCampo(grid, "CP (cm):");
        caField = addCampo(grid, "CA (cm):");
        ccField = addCampo(grid, "CC (cm):");
        pesoUsualField = addCampo(grid, "Peso Usual (kg):");
        addCampo(grid, ""); // espaçador
        card.add(grid, BorderLayout.CENTER);
        form.add(card);
        form.add(Box.createVerticalStrut(10));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnPanel.setOpaque(false);
        JButton calcBtn = HospitalTheme.createPrimaryButton("Calcular Tudo");
        calcBtn.addActionListener(e -> calcular());
        btnPanel.add(calcBtn);
        form.add(btnPanel);
        form.add(Box.createVerticalStrut(10));

        resultadoArea = new JTextArea(18, 60);
        resultadoArea.setFont(HospitalTheme.FONT_MONO);
        resultadoArea.setEditable(false);
        resultadoArea.setBackground(HospitalTheme.SURFACE_ALT);
        resultadoArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        JScrollPane sp = new JScrollPane(resultadoArea);
        sp.setBorder(BorderFactory.createTitledBorder("Resultados"));
        form.add(sp);

        JScrollPane mainScroll = new JScrollPane(form);
        mainScroll.setBorder(null);
        mainScroll.getVerticalScrollBar().setUnitIncrement(16);
        add(mainScroll, BorderLayout.CENTER);
    }

    private void calcular() {
        try {
            List<AlertaClinico> alertas = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            int idade = Integer.parseInt(idadeField.getText().trim());
            Genero genero = generoBox.getSelectedIndex() == 0 ? Genero.MASCULINO : Genero.FEMININO;
            Etnia etnia = etniaBox.getSelectedIndex() == 0 ? Etnia.BRANCO : Etnia.NEGRO;

            double aj = parseD(ajField), cb = parseD(cbField), cp = parseD(cpField);
            double ca = parseD(caField), cc = parseD(ccField);
            double pesoAtual = parseD(pesoAtualField), altura = parseD(alturaField);
            double pesoUsual = parseD(pesoUsualField);

            sb.append("═══════════════════════════════════════════\n");
            sb.append("  AVALIAÇÃO ANTROPOMÉTRICA COMPLETA\n");
            sb.append("═══════════════════════════════════════════\n\n");

            // Altura estimada
            if (aj > 0) {
                double altEst = service.calcularAlturaEstimada(genero, aj, idade, alertas);
                sb.append("▸ ALTURA ESTIMADA (Chumlea, 1985)\n");
                sb.append("  ").append(genero.getDescricao()).append(": ").append(Formatador.metros(altEst)).append("\n\n");
                if (altura <= 0) altura = altEst;
            }

            // Peso estimado
            if (aj > 0 && cb > 0) {
                double pesoEst = service.calcularPesoEstimado(genero, etnia, idade, aj, cb, alertas);
                sb.append("▸ PESO ESTIMADO (Chumlea, 1988)\n");
                sb.append("  ").append(genero.getDescricao()).append(" ").append(etnia.getDescricao());
                sb.append(idade <= 59 ? " 19-59 anos" : " >60 anos").append(": ");
                sb.append(Formatador.kg(pesoEst)).append("\n");

                if (ca > 0) {
                    double pesoJung = service.calcularPesoJung(genero, aj, cb, ca);
                    sb.append("  Jung (2004): ").append(Formatador.kg(pesoJung)).append("\n");
                }
                if (ca > 0 && cc > 0) {
                    double pesoRab = service.calcularPesoRabito(genero, aj, cb, ca, cc);
                    sb.append("  Rabito (2008): ").append(Formatador.kg(pesoRab)).append("\n");
                }
                sb.append("\n");
            }

            // Peso ideal e IMC
            if (altura > 0) {
                double pi = service.calcularPesoIdeal(altura, genero);
                double piIMC25 = service.calcularPesoIdealIMC25(altura);
                sb.append("▸ PESO IDEAL\n");
                sb.append("  ").append(genero.getDescricao()).append(" (IMC ").append(Formatador.decimal1(genero.getImcReferencia())).append("): ");
                sb.append(Formatador.kg(pi)).append("\n");
                sb.append("  IMC 25 (neutro): ").append(Formatador.kg(piIMC25)).append("\n\n");

                if (pesoAtual > 0) {
                    double imc = service.calcularIMC(pesoAtual, altura);
                    ClassificacaoIMC classIMC = service.classificarIMC(imc, idade);
                    sb.append("▸ IMC: ").append(Formatador.decimal2(imc));
                    sb.append(" — ").append(classIMC.getDescricao());
                    sb.append(idade >= 60 ? " (OPAS, 2002)" : " (OMS, 1997)").append("\n");

                    if (imc > 30) {
                        double paj = service.calcularPesoAjustado(pesoAtual, pi);
                        sb.append("  Peso Ajustado (obeso): ").append(Formatador.kg(paj)).append("\n");
                    }
                    sb.append("\n");
                }
            }

            // % Adequação CB
            if (cb > 0) {
                double adequacao = service.calcularAdequacaoCB(cb, genero, idade);
                ClassificacaoCB classCB = service.classificarCB(adequacao);
                sb.append("▸ ADEQUAÇÃO CB: ").append(Formatador.percentual(adequacao));
                sb.append(" — ").append(classCB.getDescricao()).append("\n\n");
            }

            // CP — depleção muscular
            if (cp > 0) {
                boolean deplecao = service.verificarDepleçaoMuscular(cp, genero);
                sb.append("▸ CP: ").append(Formatador.cm(cp));
                sb.append(deplecao ? " — INDICATIVO DE DEPLEÇÃO MUSCULAR" : " — Sem depleção");
                sb.append(" (Barbosa-Silva, 2016)\n\n");
            }

            // % Perda de peso
            if (pesoAtual > 0 && pesoUsual > 0) {
                double pp = service.calcularPerdaPeso(pesoAtual, pesoUsual);
                sb.append("▸ % PERDA DE PESO: ").append(Formatador.percentual(pp)).append("\n\n");
            }

            // Alertas
            if (!alertas.isEmpty()) {
                sb.append("═══ ALERTAS ═══\n");
                for (AlertaClinico a : alertas) {
                    sb.append("  ⚠ ").append(a).append("\n");
                }
            }

            resultadoArea.setText(sb.toString());
            resultadoArea.setCaretPosition(0);

        } catch (NumberFormatException e) {
            resultadoArea.setText("⚠ Erro: verifique os campos numéricos.");
        }
    }

    private double parseD(JTextField f) {
        String t = f.getText().trim().replace(",", ".");
        return t.isEmpty() ? 0 : Double.parseDouble(t);
    }

    private JTextField addCampo(JPanel grid, String label) {
        grid.add(HospitalTheme.createLabel(label));
        JTextField f = HospitalTheme.createTextField();
        grid.add(f);
        return f;
    }

    private JComboBox<String> addCombo(JPanel grid, String label, String[] items) {
        grid.add(HospitalTheme.createLabel(label));
        JComboBox<String> cb = HospitalTheme.createComboBox(items);
        grid.add(cb);
        return cb;
    }
}
