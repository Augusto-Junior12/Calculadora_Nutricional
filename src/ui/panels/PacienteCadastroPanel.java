package ui.panels;

import model.Paciente;
import model.enums.Genero;
import model.enums.Etnia;
import repository.PacienteRepository;
import ui.theme.NutrixTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;

public class PacienteCadastroPanel extends JPanel {

    private JTextField fNome, fRH, fIdade;
    private JComboBox<String> cGenero;
    private JTextArea resArea;

    public PacienteCadastroPanel() {
        setLayout(new BorderLayout(48, 0));
        setBackground(Color.WHITE);

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);
        left.setPreferredSize(new Dimension(400, 0));

        JLabel title = new JLabel("Admissão de Paciente");
        title.setFont(NutrixTheme.H2);
        title.setBorder(new EmptyBorder(0, 0, 32, 0));
        left.add(title);

        fNome = addInput(left, "Nome Completo");
        fRH = addInput(left, "Registro Hospitalar (RH)");
        fIdade = addInput(left, "Idade");
        cGenero = addCombo(left, "Gênero", new String[]{"Masculino", "Feminino"});

        left.add(Box.createVerticalStrut(24));
        JButton btn = NutrixTheme.createPrimaryButton("ADMITIR");
        btn.addActionListener(e -> admitir());
        left.add(btn);

        add(left, BorderLayout.WEST);

        resArea = new JTextArea();
        resArea.setFont(NutrixTheme.BODY);
        resArea.setEditable(false);
        resArea.setBackground(NutrixTheme.BG_SURFACE);
        resArea.setBorder(new EmptyBorder(32, 32, 32, 32));
        add(new JScrollPane(resArea), BorderLayout.CENTER);
    }

    private void admitir() {
        try {
            Paciente p = new Paciente(fNome.getText(), fRH.getText(), 
                Integer.parseInt(fIdade.getText()), 
                cGenero.getSelectedIndex() == 0 ? Genero.MASCULINO : Genero.FEMININO,
                Etnia.BRANCO, LocalDate.now(), "UTI");
            PacienteRepository.getInstance().adicionar(p);
            resArea.setText("✅ PACIENTE ADMITIDO\n\nNome: " + p.getNome() + "\nRH: " + p.getCodigo());
        } catch (Exception ex) {
            resArea.setText("Erro ao admitir. Verifique os dados.");
        }
    }

    private JTextField addInput(JPanel p, String label) {
        JLabel l = new JLabel(label);
        l.setFont(NutrixTheme.H3);
        l.setBorder(new EmptyBorder(0, 0, 8, 0));
        p.add(l);
        JTextField f = NutrixTheme.createInput();
        p.add(f);
        p.add(Box.createVerticalStrut(16));
        return f;
    }

    private JComboBox<String> addCombo(JPanel p, String label, String[] items) {
        JLabel l = new JLabel(label);
        l.setFont(NutrixTheme.H3);
        l.setBorder(new EmptyBorder(0, 0, 8, 0));
        p.add(l);
        JComboBox<String> c = new JComboBox<>(items);
        c.setFont(NutrixTheme.BODY);
        p.add(c);
        p.add(Box.createVerticalStrut(16));
        return c;
    }
}
