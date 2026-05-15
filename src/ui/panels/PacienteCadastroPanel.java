package ui.panels;

import model.Paciente;
import model.enums.Etnia;
import model.enums.Genero;
import repository.PacienteRepository;
import ui.theme.HospitalTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Painel de cadastro de pacientes.
 */
public class PacienteCadastroPanel extends JPanel {

    private final JTextField nomeField, codigoField, idadeField, setorField;
    private final JTextField pesoField, alturaField, cbField, cpField, ajField;
    private final JTextField caField, ccField, dctField;
    private final JComboBox<String> generoBox, etniaBox;
    private final JTextField dataIntField;
    private final JCheckBox obesoCheck, hdInterCheck, hdContCheck;
    private final JCheckBox alergiaSojaCheck, ileoCheck, posOpCheck, restHidricaCheck;
    private final JTextArea resultadoArea;
    private final PacienteRepository repo;

    public PacienteCadastroPanel() {
        repo = PacienteRepository.getInstance();
        setLayout(new BorderLayout(0, 10));
        setBackground(HospitalTheme.BACKGROUND);
        setBorder(new EmptyBorder(20, 25, 20, 25));

        // Título
        JLabel titulo = HospitalTheme.createTitle("Cadastro de Paciente");
        JLabel subtitulo = HospitalTheme.createLabel("Admissão em UTI — dados de identificação e medidas");
        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        headerPanel.setOpaque(false);
        headerPanel.add(titulo);
        headerPanel.add(subtitulo);
        add(headerPanel, BorderLayout.NORTH);

        // Formulário principal
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setOpaque(false);

        // --- Identificação ---
        JPanel idCard = criarSecao("Identificação");
        JPanel idGrid = new JPanel(new GridLayout(0, 4, 10, 8));
        idGrid.setOpaque(false);
        nomeField = addCampo(idGrid, "Nome:");
        codigoField = addCampo(idGrid, "Código:");
        idadeField = addCampo(idGrid, "Idade:");
        generoBox = addCombo(idGrid, "Gênero:", new String[]{"Masculino", "Feminino"});
        etniaBox = addCombo(idGrid, "Etnia:", new String[]{"Branco", "Negro"});
        setorField = addCampo(idGrid, "Setor UTI:");
        dataIntField = addCampo(idGrid, "Data Internação:");
        dataIntField.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        idCard.add(idGrid, BorderLayout.CENTER);
        form.add(idCard);
        form.add(Box.createVerticalStrut(10));

        // --- Medidas Antropométricas ---
        JPanel medCard = criarSecao("Medidas Antropométricas");
        JPanel medGrid = new JPanel(new GridLayout(0, 4, 10, 8));
        medGrid.setOpaque(false);
        pesoField = addCampo(medGrid, "Peso (kg):");
        alturaField = addCampo(medGrid, "Altura (m):");
        cbField = addCampo(medGrid, "CB (cm):");
        cpField = addCampo(medGrid, "CP (cm):");
        ajField = addCampo(medGrid, "AJ (cm):");
        caField = addCampo(medGrid, "CA (cm):");
        ccField = addCampo(medGrid, "CC (cm):");
        dctField = addCampo(medGrid, "DCT (mm):");
        medCard.add(medGrid, BorderLayout.CENTER);
        form.add(medCard);
        form.add(Box.createVerticalStrut(10));

        // --- Condições Especiais ---
        JPanel condCard = criarSecao("Condições Especiais");
        JPanel condGrid = new JPanel(new GridLayout(0, 4, 10, 6));
        condGrid.setOpaque(false);
        obesoCheck = addCheck(condGrid, "Obeso (IMC > 30)");
        hdInterCheck = addCheck(condGrid, "HD Intermitente");
        hdContCheck = addCheck(condGrid, "HD Contínua (CRRT)");
        alergiaSojaCheck = addCheck(condGrid, "Alergia à Soja");
        ileoCheck = addCheck(condGrid, "Íleo Paralítico");
        posOpCheck = addCheck(condGrid, "Pós-Op Abdominal");
        restHidricaCheck = addCheck(condGrid, "Restrição Hídrica");
        condCard.add(condGrid, BorderLayout.CENTER);
        form.add(condCard);
        form.add(Box.createVerticalStrut(10));

        // --- Botões ---
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnPanel.setOpaque(false);
        JButton salvarBtn = HospitalTheme.createPrimaryButton("Cadastrar Paciente");
        JButton limparBtn = HospitalTheme.createSecondaryButton("Limpar Campos");
        salvarBtn.addActionListener(e -> salvarPaciente());
        limparBtn.addActionListener(e -> limparCampos());
        btnPanel.add(salvarBtn);
        btnPanel.add(limparBtn);
        form.add(btnPanel);
        form.add(Box.createVerticalStrut(10));

        // --- Resultado ---
        resultadoArea = new JTextArea(5, 50);
        resultadoArea.setFont(HospitalTheme.FONT_MONO);
        resultadoArea.setEditable(false);
        resultadoArea.setBackground(HospitalTheme.SURFACE_ALT);
        resultadoArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        JScrollPane sp = new JScrollPane(resultadoArea);
        sp.setBorder(BorderFactory.createTitledBorder("Resultado"));
        form.add(sp);

        JScrollPane mainScroll = new JScrollPane(form);
        mainScroll.setBorder(null);
        mainScroll.getVerticalScrollBar().setUnitIncrement(16);
        add(mainScroll, BorderLayout.CENTER);
    }

    private JPanel criarSecao(String titulo) {
        JPanel card = HospitalTheme.createCard();
        card.setLayout(new BorderLayout(0, 10));
        JLabel lbl = HospitalTheme.createSubtitle(titulo);
        card.add(lbl, BorderLayout.NORTH);
        return card;
    }

    private JTextField addCampo(JPanel grid, String label) {
        grid.add(HospitalTheme.createLabel(label));
        JTextField field = HospitalTheme.createTextField();
        grid.add(field);
        return field;
    }

    private JComboBox<String> addCombo(JPanel grid, String label, String[] items) {
        grid.add(HospitalTheme.createLabel(label));
        JComboBox<String> cb = HospitalTheme.createComboBox(items);
        grid.add(cb);
        return cb;
    }

    private JCheckBox addCheck(JPanel grid, String label) {
        JCheckBox cb = new JCheckBox(label);
        cb.setFont(HospitalTheme.FONT_BODY);
        cb.setOpaque(false);
        grid.add(cb);
        return cb;
    }

    private double parseDouble(JTextField field) {
        String text = field.getText().trim().replace(",", ".");
        if (text.isEmpty()) return 0;
        return Double.parseDouble(text);
    }

    private void salvarPaciente() {
        try {
            String nome = nomeField.getText().trim();
            String codigo = codigoField.getText().trim();
            int idade = Integer.parseInt(idadeField.getText().trim());
            Genero genero = generoBox.getSelectedIndex() == 0 ? Genero.MASCULINO : Genero.FEMININO;
            Etnia etnia = etniaBox.getSelectedIndex() == 0 ? Etnia.BRANCO : Etnia.NEGRO;
            String setor = setorField.getText().trim();

            if (nome.isEmpty() || codigo.isEmpty()) {
                resultadoArea.setText("⚠ Nome e Código são obrigatórios.");
                return;
            }

            Paciente p = new Paciente(nome, codigo, idade, genero, etnia, LocalDate.now(), setor);

            // Medidas
            p.getMedidas().setPesoAtual(parseDouble(pesoField));
            p.getMedidas().setAltura(parseDouble(alturaField));
            p.getMedidas().setCb(parseDouble(cbField));
            p.getMedidas().setCp(parseDouble(cpField));
            p.getMedidas().setAj(parseDouble(ajField));
            p.getMedidas().setCa(parseDouble(caField));
            p.getMedidas().setCc(parseDouble(ccField));
            p.getMedidas().setDct(parseDouble(dctField));

            // Condições
            p.getCondicoes().setObeso(obesoCheck.isSelected());
            p.getCondicoes().setHemodialiseIntermitente(hdInterCheck.isSelected());
            p.getCondicoes().setHemodialisesContinua(hdContCheck.isSelected());
            p.getCondicoes().setAlergiaSoja(alergiaSojaCheck.isSelected());
            p.getCondicoes().setIleoParalitico(ileoCheck.isSelected());
            p.getCondicoes().setPosOperatorioAbdominal(posOpCheck.isSelected());
            p.getCondicoes().setRestricaoHidrica(restHidricaCheck.isSelected());

            repo.adicionar(p);

            resultadoArea.setText("✅ Paciente cadastrado com sucesso!\n\n" + p.toString() +
                "\nPeso: " + p.getMedidas().getPesoAtual() + " kg" +
                "\nAltura: " + p.getMedidas().getAltura() + " m" +
                "\nTotal de pacientes ativos: " + repo.getTotalAtivos());

        } catch (NumberFormatException e) {
            resultadoArea.setText("⚠ Erro: verifique os campos numéricos.");
        } catch (Exception e) {
            resultadoArea.setText("⚠ Erro: " + e.getMessage());
        }
    }

    private void limparCampos() {
        nomeField.setText(""); codigoField.setText(""); idadeField.setText("");
        pesoField.setText(""); alturaField.setText(""); cbField.setText("");
        cpField.setText(""); ajField.setText(""); caField.setText("");
        ccField.setText(""); dctField.setText("");
        obesoCheck.setSelected(false); hdInterCheck.setSelected(false);
        hdContCheck.setSelected(false); alergiaSojaCheck.setSelected(false);
        ileoCheck.setSelected(false); posOpCheck.setSelected(false);
        restHidricaCheck.setSelected(false);
        resultadoArea.setText("");
        nomeField.requestFocus();
    }
}
