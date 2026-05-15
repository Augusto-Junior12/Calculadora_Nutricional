package ui.theme;

import java.awt.*;
import java.awt.geom.*;

/**
 * NutrixIcons — Gerador de ícones vetoriais via Java2D.
 * Estilo Minimalista / Moderno.
 */
public class NutrixIcons {

    public enum IconType {
        DASHBOARD, PATIENT, SCALE, TARGET, PILL, WATER, MONITOR, FOOD, CHART, SEARCH, BOX, SETTINGS
    }

    public static void drawIcon(Graphics2D g2, IconType type, int size, Color color) {
        g2 = (Graphics2D) g2.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);
        g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        double s = size;
        switch (type) {
            case DASHBOARD:
                g2.draw(new RoundRectangle2D.Double(s*0.1, s*0.1, s*0.35, s*0.35, 3, 3));
                g2.draw(new RoundRectangle2D.Double(s*0.55, s*0.1, s*0.35, s*0.35, 3, 3));
                g2.draw(new RoundRectangle2D.Double(s*0.1, s*0.55, s*0.35, s*0.35, 3, 3));
                g2.fill(new RoundRectangle2D.Double(s*0.55, s*0.55, s*0.35, s*0.35, 3, 3));
                break;
            case PATIENT:
                g2.draw(new Ellipse2D.Double(s*0.3, s*0.15, s*0.4, s*0.4));
                Path2D body = new Path2D.Double();
                body.moveTo(s*0.15, s*0.85);
                body.curveTo(s*0.15, s*0.6, s*0.85, s*0.6, s*0.85, s*0.85);
                g2.draw(body);
                break;
            case SCALE:
                g2.draw(new Line2D.Double(s*0.1, s*0.8, s*0.9, s*0.8));
                g2.draw(new Path2D.Double() {{
                    moveTo(s*0.2, s*0.8); lineTo(s*0.3, s*0.3); lineTo(s*0.7, s*0.3); lineTo(s*0.8, s*0.8);
                }});
                break;
            case TARGET:
                g2.draw(new Ellipse2D.Double(s*0.1, s*0.1, s*0.8, s*0.8));
                g2.draw(new Ellipse2D.Double(s*0.3, s*0.3, s*0.4, s*0.4));
                g2.fill(new Ellipse2D.Double(s*0.45, s*0.45, s*0.1, s*0.1));
                break;
            case PILL:
                g2.draw(new RoundRectangle2D.Double(s*0.2, s*0.4, s*0.6, s*0.3, s*0.3, s*0.3));
                g2.draw(new Line2D.Double(s*0.5, s*0.4, s*0.5, s*0.7));
                break;
            case WATER:
                Path2D drop = new Path2D.Double();
                drop.moveTo(s*0.5, s*0.1);
                drop.curveTo(s*0.8, s*0.5, s*0.8, s*0.9, s*0.5, s*0.9);
                drop.curveTo(s*0.2, s*0.9, s*0.2, s*0.5, s*0.5, s*0.1);
                g2.draw(drop);
                break;
            case MONITOR:
                g2.draw(new RoundRectangle2D.Double(s*0.1, s*0.1, s*0.8, s*0.6, 5, 5));
                Path2D pulse = new Path2D.Double();
                pulse.moveTo(s*0.2, s*0.4); pulse.lineTo(s*0.35, s*0.4); 
                pulse.lineTo(s*0.45, s*0.2); pulse.lineTo(s*0.55, s*0.6); 
                pulse.lineTo(s*0.65, s*0.4); pulse.lineTo(s*0.8, s*0.4);
                g2.draw(pulse);
                break;
            case FOOD:
                g2.draw(new Ellipse2D.Double(s*0.1, s*0.2, s*0.8, s*0.6));
                g2.draw(new Line2D.Double(s*0.3, s*0.1, s*0.3, s*0.3));
                g2.draw(new Line2D.Double(s*0.7, s*0.1, s*0.7, s*0.3));
                break;
            case CHART:
                g2.draw(new Line2D.Double(s*0.1, s*0.1, s*0.1, s*0.9));
                g2.draw(new Line2D.Double(s*0.1, s*0.9, s*0.9, s*0.9));
                Path2D line = new Path2D.Double();
                line.moveTo(s*0.2, s*0.7); line.lineTo(s*0.4, s*0.4);
                line.lineTo(s*0.6, s*0.6); line.lineTo(s*0.8, s*0.2);
                g2.draw(line);
                break;
            case SEARCH:
                g2.draw(new Ellipse2D.Double(s*0.1, s*0.1, s*0.5, s*0.5));
                g2.draw(new Line2D.Double(s*0.5, s*0.5, s*0.85, s*0.85));
                break;
            case BOX:
                g2.draw(new Rectangle2D.Double(s*0.1, s*0.3, s*0.8, s*0.6));
                g2.draw(new Path2D.Double() {{ moveTo(s*0.1, s*0.3); lineTo(s*0.3, s*0.1); lineTo(s*0.7, s*0.1); lineTo(s*0.9, s*0.3); }});
                break;
        }
        g2.dispose();
    }
}
