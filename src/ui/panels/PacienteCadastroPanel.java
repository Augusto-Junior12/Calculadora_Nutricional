package ui.panels;

import model.Paciente;
import model.enums.Etnia;
import model.enums.Genero;
import repository.PacienteRepository;
import ui.theme.NutrixTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Cadastro de Pacientes — Nutrix Hospital OS.
 */
public class PacienteCadastroPanel extends JPanel {

    private final JTextField nomeField, codigoField, idadeField;
    private final JTextField pesoField, alturaField, cbField;
    private final JComboBox<String> generoBox, etniaBox;
    private final JTextArea resultadoArea;
    private final PacienteRepository repo;

    public PacienteCadastroPanel() {
        repo = PacienteRepository.getInstance();
        setLayout(new BorderLayout(0, 25));
        setBackground(NutrixTheme.BG_MAIN);
        setBorder(new EmptyBorder(30, 45, 30, 45));

        JPanel formScroll = new JPanel();
        formScroll.setLayout(new BoxLayout(formScroll, BoxLayout.Y_AXIS));
        formScroll.setOpaque(false);

        // --- Seção de Identificação ---
        JPanel idCard = NutrixTheme.createCard();
        idCard.setLayout(new BorderLayout(0, 20));
        
        JLabel idTitle = new JLabel("DADOS DE IDENTIFICAÇÃO");
        idTitle.setFont(NutrixTheme.FONT_H3);
        idTitle.setForeground(NutrixTheme.ACCENT);
        idCard.add(idTitle, BorderLayout.NORTH);

        JPanel idGrid = new JPanel(new GridLayout(0, 3, 20, 15));
        idGrid.setOpaque(false);
        nomeField = addCampoModerno(idGrid, "Nome Completo:");
        codigoField = addCampoModerno(idGrid, "Registro Hospitalar (RH):");
        idadeField = addCampoModerno(idGrid, "Idade:");
        generoBox = addComboModerno(idGrid, "Gênero:", new String[]{"Masculino", "Feminino"});
        etniaBox = addComboModerno(idGrid, "Etnia:", new String[]{"Branco", "Negro"});
        addCampoModerno(idGrid, "Setor UTI:");
        idCard.add(idGrid, BorderLayout.CENTER);
        formScroll.add(idCard);
        formScroll.add(Box.createVerticalStrut(25));

        // --- Seção de Antropometria ---
        JPanel antCard = NutrixTheme.createCard();
        antCard.setLayout(new BorderLayout(0, 20));
        JLabel antTitle = new JLabel("ANTROPOMETRIA INICIAL");
        antTitle.setFont(NutrixTheme.FONT_H3);
        antTitle.setForeground(NutrixTheme.ACCENT);
        antCard.add(antTitle, BorderLayout.NORTH);

        JPanel antGrid = new JPanel(new GridLayout(0, 3, 20, 15));
        antGrid.setOpaque(false);
        pesoField = addCampoModerno(antGrid, "Peso Atual (kg):");
        alturaField = addCampoModerno(antGrid, "Altura (m):");
        cbField = addCampoModerno(antGrid, "Circunferência Braço (cm):");
        antCard.add(antGrid, BorderLayout.CENTER);
        formScroll.add(antCard);
        formScroll.add(Box.createVerticalStrut(25));

        // --- Botões ---
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        btnPanel.setOpaque(false);
        JButton salvarBtn = NutrixTheme.createButton("CADASTRAR PACIENTE", true);
        JButton limparBtn = NutrixTheme.createButton("LIMPAR", false);
        
        salvarBtn.addActionListener(e -> salvar());
        btnPanel.add(salvarBtn);
        btnPanel.add(limparBtn);
        formScroll.add(btnPanel);
        formScroll.add(Box.createVerticalStrut(25));

        // --- Feedback ---
        resultadoArea = new JTextArea(4, 50);
        resultadoArea.setFont(NutrixTheme.FONT_BODY);
        resultadoArea.setEditable(false);
        resultadoArea.setBackground(NutrixTheme.BG_INPUT);
        resultadoArea.setBorder(new EmptyBorder(15, 15, 15, 15));
        formScroll.add(resultadoArea);

        JScrollPane scroll = new JScrollPane(formScroll);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        add(scroll, BorderLayout.CENTER);
    }

    private JTextField addCampoModerno(JPanel grid, String label) {
        JPanel p = new JPanel(new BorderLayout(0, 5));
        p.setOpaque(false);
        JLabel l = new JLabel(label);
        l.setFont(NutrixTheme.FONT_SMALL);
        l.setForeground(NutrixTheme.TEXT_MUTED);
        JTextField f = NutrixTheme.createTextField();
        p.add(l, BorderLayout.NORTH);
        p.add(f, BorderLayout.CENTER);
        grid.add(p);
        return f;
    }

    private JComboBox<String> addComboModerno(JPanel grid, String label, String[] items) {
        JPanel p = new JPanel(new BorderLayout(0, 5));
        p.setOpaque(false);
        JLabel l = new JLabel(label);
        l.setFont(NutrixTheme.FONT_SMALL);
        l.setForeground(NutrixTheme.TEXT_MUTED);
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setFont(NutrixTheme.FONT_BODY);
        p.add(l, BorderLayout.NORTH);
        p.add(cb, BorderLayout.CENTER);
        grid.add(p);
        return cb;
    }

    private void salvar() {
        try {
            Paciente p = new Paciente(nomeField.getText(), codigoField.getText(), 
                Integer.parseInt(idadeField.getText()), 
                generoBox.getSelectedIndex() == 0 ? Genero.MASCULINO : Genero.FEMININO,
                etniaBox.getSelectedIndex() == 0 ? Etnia.BRANCO : Etnia.NEGRO,
                LocalDate.now(), "UTI");
            repo.adicionar(p);
            resultadoArea.setText("✅ Paciente admitido com sucesso: " + p.getNome());
            resultadoArea.setForeground(NutrixTheme.SUCCESS);
        } catch (Exception e) {
            resultadoArea.setText("⚠ Erro no cadastro: " + e.getMessage());
            resultadoArea.setForeground(NutrixTheme.DANGER);
        }
    }
}
