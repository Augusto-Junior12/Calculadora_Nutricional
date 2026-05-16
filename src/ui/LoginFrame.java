package ui;

import auth.AuthUser;
import auth.UserSession;
import firebase.AuthService;
import firebase.FirebaseConfig;
import ui.theme.NutrixUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * LoginFrame — Tela de Login Nutrix Hospital OS.
 * Layout: barra de controles no topo + dois painéis side-by-side centralizados.
 */
public class LoginFrame extends JFrame {

    private final AuthService authService = new AuthService();
    private JTextField emailField;
    private JPasswordField passwordField;
    private JLabel errorLabel;
    private JButton loginButton;

    public LoginFrame() {
        super("Nutrix Hospital OS — Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(820, 520));
        setSize(960, 600);
        setLocationRelativeTo(null);
        setUndecorated(true);   // sem barra de título nativa

        // ── Root: barra de controles em cima + conteúdo abaixo ──
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(15, 23, 42));
        setContentPane(root);

        root.add(buildTitleBar(), BorderLayout.NORTH);
        root.add(buildContent(), BorderLayout.CENTER);

        installDrag(root);
    }

    // ────────────────────────────────────────────────
    // Barra de controles (Minimizar / Maximizar / Fechar)
    // ────────────────────────────────────────────────
    private JPanel buildTitleBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);
        bar.setBorder(new EmptyBorder(10, 20, 0, 14));
        bar.setPreferredSize(new Dimension(0, 40));

        // título discreto à esquerda
        JLabel title = new JLabel("Nutrix Hospital OS");
        title.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        title.setForeground(new Color(100, 116, 139));
        bar.add(title, BorderLayout.WEST);

        // botões à direita
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        btnRow.setOpaque(false);

        JButton btnMin = winCtrlBtn("—", new Color(234, 179, 8));
        JButton btnMax = winCtrlBtn("□", new Color(34, 197, 94));
        JButton btnClose = winCtrlBtn("✕", new Color(239, 68, 68));

        btnMin.addActionListener(e -> setState(Frame.ICONIFIED));
        btnMax.addActionListener(e -> {
            if (getExtendedState() == Frame.MAXIMIZED_BOTH) setExtendedState(Frame.NORMAL);
            else setExtendedState(Frame.MAXIMIZED_BOTH);
        });
        btnClose.addActionListener(e -> System.exit(0));

        btnRow.add(btnMin);
        btnRow.add(btnMax);
        btnRow.add(btnClose);

        bar.add(btnRow, BorderLayout.EAST);
        return bar;
    }

    private JButton winCtrlBtn(String symbol, Color baseColor) {
        JButton b = new JButton() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Circle background
                g2.setColor(getModel().isRollover() ? baseColor : new Color(
                    baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), 160));
                g2.fillOval(2, 2, getWidth()-4, getHeight()-4);
                // Symbol (only visible on hover)
                if (getModel().isRollover()) {
                    g2.setColor(new Color(0, 0, 0, 150));
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 9));
                    FontMetrics fm = g2.getFontMetrics();
                    int x = (getWidth()  - fm.stringWidth(symbol)) / 2;
                    int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                    g2.drawString(symbol, x, y);
                }
                g2.dispose();
            }
        };
        b.setPreferredSize(new Dimension(16, 16));
        b.setMaximumSize(new Dimension(16, 16));
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setToolTipText(symbol.equals("—") ? "Minimizar" : symbol.equals("□") ? "Maximizar" : "Fechar");
        return b;
    }

    // ────────────────────────────────────────────────
    // Conteúdo principal — dois painéis centralizados
    // ────────────────────────────────────────────────
    private JPanel buildContent() {
        // Wrapper com gradiente de fundo
        JPanel bg = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(0, 0, new Color(109, 40, 217),
                        getWidth(), getHeight(), new Color(15, 23, 42)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };

        // Card branco centralizado com os dois painéis
        JPanel card = new JPanel(new GridLayout(1, 2));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(820, 500));
        card.setMaximumSize(new Dimension(820, 500));

        card.add(buildBrandPanel());
        card.add(buildFormPanel());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        bg.add(card, gbc);

        return bg;
    }

    // ── LEFT: Brand ──
    private JPanel buildBrandPanel() {
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, new Color(109, 40, 217),
                        getWidth(), getHeight(), new Color(49, 10, 100)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(50, 40, 40, 40));

        // Logo badge
        JPanel badge = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 40));
                g2.fillRoundRect(0, 0, 56, 56, 16, 16);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 26));
                g2.drawString("N", 16, 40);
                g2.dispose();
            }
        };
        badge.setOpaque(false);
        badge.setPreferredSize(new Dimension(56, 56));
        badge.setMaximumSize(new Dimension(56, 56));
        badge.setAlignmentX(0f);
        p.add(badge);
        p.add(Box.createVerticalStrut(32));

        brandLabel(p, "Nutrix", 28, Font.BOLD, Color.WHITE);
        brandLabel(p, "Hospital OS", 15, Font.PLAIN, new Color(196, 181, 253));
        p.add(Box.createVerticalStrut(24));
        brandLabel(p, "Sistema de Gestão de", 13, Font.PLAIN, new Color(196, 181, 253));
        brandLabel(p, "Terapia Nutricional Enteral", 13, Font.PLAIN, new Color(196, 181, 253));
        p.add(Box.createVerticalStrut(36));

        String[] features = {
            "• Avaliação Antropométrica",
            "• Prescrição Enteral Avançada",
            "• Monitoramento de UTI",
            "• Evolução Clínica e Relatórios"
        };
        for (String f : features) {
            JLabel l = new JLabel(f);
            l.setFont(NutrixUI.BODY);
            l.setForeground(new Color(221, 214, 254));
            l.setAlignmentX(0f);
            p.add(l);
            p.add(Box.createVerticalStrut(7));
        }
        p.add(Box.createVerticalGlue());
        return p;
    }

    // ── RIGHT: Form ──
    private JPanel buildFormPanel() {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(Color.WHITE);

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setPreferredSize(new Dimension(300, 380));

        // Título
        JLabel title = new JLabel("Bem-vindo");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(NutrixUI.TEXT_PRIMARY);
        title.setAlignmentX(0f);
        form.add(title);
        form.add(Box.createVerticalStrut(6));

        JLabel sub = new JLabel("Faça login para continuar");
        sub.setFont(NutrixUI.BODY);
        sub.setForeground(NutrixUI.TEXT_MUTED);
        sub.setAlignmentX(0f);
        form.add(sub);
        form.add(Box.createVerticalStrut(30));

        // Email
        fieldLabel(form, "E-MAIL INSTITUCIONAL");
        emailField = NutrixUI.input("exemplo@hospital.com");
        emailField.setAlignmentX(0f);
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        form.add(emailField);
        form.add(Box.createVerticalStrut(16));

        // Senha
        fieldLabel(form, "SENHA");
        passwordField = NutrixUI.passwordInput("••••••••");
        passwordField.setAlignmentX(0f);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        passwordField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) doLogin();
            }
        });
        form.add(passwordField);
        form.add(Box.createVerticalStrut(8));

        // Mensagem de erro
        errorLabel = new JLabel(" ");
        errorLabel.setFont(NutrixUI.SMALL);
        errorLabel.setForeground(NutrixUI.DANGER);
        errorLabel.setAlignmentX(0f);
        form.add(errorLabel);
        form.add(Box.createVerticalStrut(16));

        // Botão ENTRAR
        loginButton = createLoginButton();
        loginButton.setAlignmentX(0f);
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        form.add(loginButton);

        outer.add(form, new GridBagConstraints());
        return outer;
    }

    private JButton createLoginButton() {
        JButton btn = new JButton("ENTRAR") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (!isEnabled()) {
                    g2.setColor(new Color(167, 139, 250));
                } else if (getModel().isPressed()) {
                    g2.setColor(NutrixUI.ACCENT_HOVER);
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(124, 58, 237));
                } else {
                    g2.setColor(NutrixUI.ACCENT);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 15));
                FontMetrics fm = g2.getFontMetrics();
                String text = getText();
                int tx = (getWidth() - fm.stringWidth(text)) / 2;
                int ty = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(text, tx, ty);
                g2.dispose();
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setForeground(Color.WHITE);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> doLogin());
        return btn;
    }

    private void brandLabel(JPanel p, String text, int size, int style, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", style, size));
        l.setForeground(color);
        l.setAlignmentX(0f);
        p.add(l);
    }

    private void fieldLabel(JPanel p, String text) {
        JLabel l = new JLabel(text);
        l.setFont(NutrixUI.LABEL);
        l.setForeground(NutrixUI.TEXT_SECONDARY);
        l.setAlignmentX(0f);
        l.setBorder(new EmptyBorder(0, 2, 6, 0));
        p.add(l);
    }

    // ────────────────────────────────────────────────
    // Login Logic
    // ────────────────────────────────────────────────
    private void doLogin() {
        String email = emailField.getText().trim();
        String pass  = new String(passwordField.getPassword());

        if (email.isEmpty() || pass.isEmpty()) {
            errorLabel.setText("Preencha e-mail e senha.");
            return;
        }
        if (!email.contains("@") || !email.contains(".")) {
            errorLabel.setText("E-mail institucional inválido.");
            return;
        }
        if (pass.length() < 6) {
            errorLabel.setText("Senha deve ter no mínimo 6 caracteres.");
            return;
        }

        loginButton.setEnabled(false);
        loginButton.setText("Autenticando...");
        errorLabel.setText(" ");

        new Thread(() -> {
            AuthUser user;
            if (FirebaseConfig.isConfigured()) {
                AuthService.AuthResult result = authService.signIn(email, pass);
                if (!result.isSuccess()) {
                    SwingUtilities.invokeLater(() -> {
                        errorLabel.setText(result.errorMessage());
                        loginButton.setEnabled(true);
                        loginButton.setText("ENTRAR");
                    });
                    return;
                }
                user = result.user();
            } else {
                auth.UserRole role = email.contains("admin") ? auth.UserRole.ADMIN : auth.UserRole.MEDICO;
                String name = email.split("@")[0];
                user = new AuthUser("demo-uid", email, name, role, "demo-token");
            }

            final AuthUser finalUser = user;
            SwingUtilities.invokeLater(() -> {
                UserSession.get().login(finalUser);
                dispose();
                new MainFrame().setVisible(true);
            });
        }).start();
    }

    // ────────────────────────────────────────────────
    // Drag de janela
    // ────────────────────────────────────────────────
    private void installDrag(Component comp) {
        final Point[] start = {null};
        comp.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { start[0] = e.getPoint(); }
        });
        comp.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (start[0] == null) return;
                Point loc = getLocation();
                setLocation(loc.x + e.getX() - start[0].x,
                            loc.y + e.getY() - start[0].y);
            }
        });
    }
}
