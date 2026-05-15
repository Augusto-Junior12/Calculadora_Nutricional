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
        setLayout(new BorderLayout(40, 0));
        setBackground(Color.WHITE);

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);
        left.setPreferredSize(new Dimension(380, 0));

        JLabel title = new JLabel("Admissão de Paciente");
        title.setFont(NutrixTheme.FONT_H2);
        title.setBorder(new EmptyBorder(0, 0, 30, 0));
        left.add(title);

        fNome = addField(left, "Nome Completo");
        fRH = addField(left, "Registro Hospitalar (RH)");
        fIdade = addField(left, "Idade");
        cGenero = addCombo(left, "Gênero", new String[]{"Masculino", "Feminino"});

        left.add(Box.createVerticalStrut(25));
        JButton btn = NutrixTheme.createButton("CADASTRAR NO SISTEMA", true);
        btn.addActionListener(e -> admitir());
        left.add(btn);

        add(left, BorderLayout.WEST);

        JPanel right = NutrixTheme.createRoundedPanel(25, NutrixTheme.ACCENT_SOFT);
        right.setLayout(new BorderLayout());
        right.setBorder(new EmptyBorder(35, 35, 35, 35));

        resArea = new JTextArea();
        resArea.setFont(NutrixTheme.FONT_BODY);
        resArea.setEditable(false);
        resArea.setOpaque(false);
        
        JLabel resTitle = new JLabel("Status da Admissão");
        resTitle.setFont(NutrixTheme.FONT_H3);
        resTitle.setForeground(NutrixTheme.ACCENT);
        resTitle.setBorder(new EmptyBorder(0, 0, 20, 0));

        right.add(resTitle, BorderLayout.NORTH);
        right.add(new JScrollPane(resArea), BorderLayout.CENTER);

        add(right, BorderLayout.CENTER);
    }

    private void admitir() {
        try {
            Paciente p = new Paciente(fNome.getText(), fRH.getText(), 
                Integer.parseInt(fIdade.getText()), 
                cGenero.getSelectedIndex() == 0 ? Genero.MASCULINO : Genero.FEMININO,
                Etnia.BRANCO, LocalDate.now(), "UTI");
            PacienteRepository.getInstance().adicionar(p);
            resArea.setText("✅ PACIENTE ADMITIDO COM SUCESSO\n\nID: " + p.getCodigo() + "\nNome: " + p.getNome());
        } catch (Exception ex) {
            resArea.setText("Erro ao admitir. Verifique os dados.");
        }
    }

    private JTextField addField(JPanel p, String label) {
        JLabel l = new JLabel(label); l.setFont(NutrixTheme.FONT_SMALL); l.setForeground(NutrixTheme.TEXT_MUTED);
        l.setBorder(new EmptyBorder(0, 5, 5, 0)); p.add(l);
        JTextField f = NutrixTheme.createTextField(); f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42)); p.add(f);
        p.add(Box.createVerticalStrut(15)); return f;
    }

    private JComboBox<String> addCombo(JPanel p, String label, String[] items) {
        JLabel l = new JLabel(label); l.setFont(NutrixTheme.FONT_SMALL); l.setForeground(NutrixTheme.TEXT_MUTED);
        l.setBorder(new EmptyBorder(0, 5, 5, 0)); p.add(l);
        JComboBox<String> c = new JComboBox<>(items); c.setFont(NutrixTheme.FONT_BODY);
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42)); p.add(c);
        p.add(Box.createVerticalStrut(15)); return c;
    }
}
