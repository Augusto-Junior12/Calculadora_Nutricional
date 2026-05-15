package ui.panels;

import auth.UserSession;
import firebase.FirebaseConfig;
import firebase.FirestoreService;
import ui.components.ClinicalFormPanel;
import ui.theme.NutrixIcons;
import ui.theme.NutrixUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * ConfiguracoesPanel — Visível apenas para ADMIN.
 * Mostra info do sistema, Firebase status, e gestão de usuários.
 */
public class ConfiguracoesPanel extends JPanel {

    public ConfiguracoesPanel() {
        setLayout(new BorderLayout(0, 24));
        setOpaque(false);

        // Section Title
        JLabel title = new JLabel("Configurações do Sistema");
        title.setFont(NutrixUI.H1);
        title.setForeground(NutrixUI.TEXT_PRIMARY);
        add(title, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(1, 2, 24, 0));
        grid.setOpaque(false);

        // ── Card: Firebase Status ──
        JPanel firebaseCard = NutrixUI.card(24);
        firebaseCard.setLayout(new BoxLayout(firebaseCard, BoxLayout.Y_AXIS));

        JLabel fbTitle = new JLabel("Banco de Dados Firebase");
        fbTitle.setFont(NutrixUI.BODY_BOLD);
        fbTitle.setForeground(NutrixUI.TEXT_PRIMARY);
        fbTitle.setAlignmentX(0f);

        boolean configured = FirebaseConfig.isConfigured();
        JLabel fbStatus = NutrixUI.badge(
            configured ? "● Configurado" : "● Demo Mode",
            configured ? NutrixUI.SUCCESS_LIGHT : NutrixUI.WARNING_LIGHT,
            configured ? NutrixUI.SUCCESS : NutrixUI.WARNING
        );

        JLabel pid = new JLabel("Project ID: " + FirebaseConfig.PROJECT_ID);
        pid.setFont(NutrixUI.SMALL);
        pid.setForeground(NutrixUI.TEXT_MUTED);
        pid.setAlignmentX(0f);

        JLabel instructions = new JLabel("<html><body style='width: 280px; font-size: 12px; color: #64748b;'>"
            + "Para ativar o Firebase real:<br>"
            + "1. Acesse console.firebase.google.com<br>"
            + "2. Crie um projeto e ative Auth + Firestore<br>"
            + "3. Edite <b>FirebaseConfig.java</b> com sua API Key<br>"
            + "4. No Firestore, crie a coleção <b>users</b> com campo role</body></html>");
        instructions.setAlignmentX(0f);

        firebaseCard.add(fbTitle);
        firebaseCard.add(Box.createVerticalStrut(10));
        firebaseCard.add(fbStatus);
        firebaseCard.add(Box.createVerticalStrut(14));
        firebaseCard.add(pid);
        firebaseCard.add(Box.createVerticalStrut(14));
        firebaseCard.add(instructions);

        // ── Card: Sessão Atual ──
        JPanel sessionCard = NutrixUI.card(24);
        sessionCard.setLayout(new BoxLayout(sessionCard, BoxLayout.Y_AXIS));

        JLabel sessTitle = new JLabel("Sessão Atual");
        sessTitle.setFont(NutrixUI.BODY_BOLD);
        sessTitle.setForeground(NutrixUI.TEXT_PRIMARY);
        sessTitle.setAlignmentX(0f);
        sessionCard.add(sessTitle);
        sessionCard.add(Box.createVerticalStrut(14));

        auth.AuthUser user = UserSession.get().getUser();
        if (user != null) {
            addInfo(sessionCard, "Usuário", user.getDisplayName());
            addInfo(sessionCard, "E-mail", user.getEmail());
            addInfo(sessionCard, "Papel", user.getRole().getLabel());
            addInfo(sessionCard, "UID", user.getUid());
        }

        grid.add(firebaseCard);
        grid.add(sessionCard);
        add(grid, BorderLayout.CENTER);
    }

    private void addInfo(JPanel p, String label, String value) {
        JPanel row = NutrixUI.resultRow(label, value, null);
        row.setAlignmentX(0f);
        p.add(row);
    }
}
