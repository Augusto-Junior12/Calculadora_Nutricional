package ui.components;

import ui.theme.NutrixIcons;
import ui.theme.NutrixUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * ClinicalFormPanel — Layout base para todos os painéis clínicos.
 *
 * Layout:
 *   WEST  — form card (inputs)
 *   CENTER — result card (outputs com ícones)
 */
public abstract class ClinicalFormPanel extends JPanel {

    protected final JPanel formCard;
    protected final JPanel resultCard;
    protected final JPanel resultBody;

    protected ClinicalFormPanel(String formTitle, NutrixIcons.Icon icon, Color accent, Color accentLight) {
        setOpaque(false);

        // ── Form Card ──
        formCard = NutrixUI.card(28);
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setPreferredSize(new Dimension(370, 0));

        // Form header
        JPanel fHeader = new JPanel(new BorderLayout(14, 0));
        fHeader.setOpaque(false);
        fHeader.setBorder(new EmptyBorder(0, 0, 24, 0));
        JPanel iconBadge = NutrixIcons.iconBadge(icon, 44, accentLight, accent);
        JPanel headerText = new JPanel(new GridLayout(2, 1));
        headerText.setOpaque(false);
        JLabel fTitle = new JLabel(formTitle);
        fTitle.setFont(NutrixUI.H1);
        fTitle.setForeground(NutrixUI.TEXT_PRIMARY);
        JLabel fSub = new JLabel("Preencha os dados abaixo");
        fSub.setFont(NutrixUI.SMALL);
        fSub.setForeground(NutrixUI.TEXT_MUTED);
        headerText.add(fTitle);
        headerText.add(fSub);
        fHeader.add(iconBadge, BorderLayout.WEST);
        fHeader.add(headerText, BorderLayout.CENTER);
        formCard.add(fHeader);

        // ── Result Card ──
        resultCard = NutrixUI.card(28);
        resultCard.setLayout(new BorderLayout(0, 16));

        JPanel rHeader = new JPanel(new BorderLayout());
        rHeader.setOpaque(false);
        rHeader.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel rTitle = new JLabel("Resultado");
        rTitle.setFont(NutrixUI.H2);
        rTitle.setForeground(NutrixUI.TEXT_SECONDARY);

        JLabel rBadge = NutrixUI.badge("aguardando cálculo", NutrixUI.BG_PAGE, NutrixUI.TEXT_MUTED);
        JPanel rBadgeRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rBadgeRow.setOpaque(false);
        rBadgeRow.add(rBadge);

        rHeader.add(rTitle, BorderLayout.WEST);
        rHeader.add(rBadgeRow, BorderLayout.EAST);
        resultCard.add(rHeader, BorderLayout.NORTH);

        resultBody = new JPanel();
        resultBody.setLayout(new BoxLayout(resultBody, BoxLayout.Y_AXIS));
        resultBody.setOpaque(false);
        resultCard.add(resultBody, BorderLayout.CENTER);

        // ── Centered Layout Wrapper ──
        setLayout(new GridBagLayout());
        
        JPanel contentRow = new JPanel(new GridBagLayout());
        contentRow.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 14, 0, 14);
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1.0;

        c.gridx = 0; c.weightx = 0.42;
        contentRow.add(formCard, c);

        c.gridx = 1; c.weightx = 0.58;
        contentRow.add(resultCard, c);

        GridBagConstraints rootGbc = new GridBagConstraints();
        rootGbc.weightx = 1.0; rootGbc.weighty = 1.0;
        rootGbc.anchor = GridBagConstraints.CENTER;
        rootGbc.insets = new Insets(0, 0, 0, 0);
        
        add(contentRow, rootGbc);
    }

    /** Adiciona um campo de texto ao formCard */
    protected JTextField addField(String label) {
        JLabel lbl = NutrixUI.fieldLabel(label);
        lbl.setBorder(new EmptyBorder(0, 2, 6, 0));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(lbl);

        JTextField f = NutrixUI.input(label);
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        formCard.add(f);
        formCard.add(Box.createVerticalStrut(14));
        return f;
    }

    /** Adiciona combo ao formCard */
    protected JComboBox<String> addCombo(String label, String[] items) {
        JLabel lbl = NutrixUI.fieldLabel(label);
        lbl.setBorder(new EmptyBorder(0, 2, 6, 0));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(lbl);

        JComboBox<String> c = NutrixUI.combo(items);
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        formCard.add(c);
        formCard.add(Box.createVerticalStrut(14));
        return c;
    }

    /** Adiciona botão primário ao formCard */
    protected JButton addPrimaryButton(String text) {
        formCard.add(Box.createVerticalStrut(10));
        JButton btn = NutrixUI.btnPrimary(text);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        formCard.add(btn);
        return btn;
    }

    /** Exibe linhas de resultado no resultBody */
    protected void showResults(String[][] rows, Color accent) {
        resultBody.removeAll();
        for (String[] row : rows) {
            resultBody.add(NutrixUI.resultRow(row[0], row[1], accent));
        }
        resultBody.revalidate();
        resultBody.repaint();
    }

    protected void showError(String msg) {
        resultBody.removeAll();
        JLabel err = new JLabel("⚠ " + msg);
        err.setFont(NutrixUI.BODY);
        err.setForeground(NutrixUI.DANGER);
        resultBody.add(err);
        resultBody.revalidate();
        resultBody.repaint();
    }

    protected double parseField(JTextField f) {
        return Double.parseDouble(f.getText().trim().replace(",", "."));
    }
}
