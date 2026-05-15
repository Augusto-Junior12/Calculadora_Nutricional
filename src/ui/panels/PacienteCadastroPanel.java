package ui.panels;

import model.Paciente;
import model.enums.Genero;
import model.enums.Etnia;
import repository.PacienteRepository;
import ui.components.ClinicalFormPanel;
import ui.theme.NutrixIcons;
import ui.theme.NutrixUI;

import javax.swing.*;
import java.time.LocalDate;

public class PacienteCadastroPanel extends ClinicalFormPanel {

    private final JTextField fNome, fRH, fIdade;
    private final JComboBox<String> cGenero;

    public PacienteCadastroPanel() {
        super("Admissão", NutrixIcons.Icon.PATIENT, NutrixUI.ACCENT, NutrixUI.ACCENT_LIGHT);

        fNome  = addField("Nome Completo");
        fRH    = addField("Registro Hospitalar (RH)");
        fIdade = addField("Idade");
        cGenero = addCombo("Gênero", new String[]{"Masculino", "Feminino"});

        JButton btn = addPrimaryButton("ADMITIR PACIENTE");
        btn.addActionListener(e -> admitir());
    }

    private void admitir() {
        try {
            Paciente p = new Paciente(
                fNome.getText().trim(), fRH.getText().trim(),
                Integer.parseInt(fIdade.getText().trim()),
                cGenero.getSelectedIndex() == 0 ? Genero.MASCULINO : Genero.FEMININO,
                Etnia.BRANCO, LocalDate.now(), "UTI"
            );
            PacienteRepository.getInstance().adicionar(p);
            showResults(new String[][]{
                {"Status", "✅ Admitido com sucesso"},
                {"Nome", p.getNome()},
                {"Registro", p.getCodigo()},
                {"Internação", p.getDataInternacao().toString()}
            }, NutrixUI.SUCCESS);
        } catch (Exception ex) {
            showError("Verifique os dados informados.");
        }
    }
}
