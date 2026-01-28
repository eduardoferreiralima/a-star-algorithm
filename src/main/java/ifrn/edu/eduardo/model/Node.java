package ifrn.edu.eduardo.model;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Node extends JPanel {
    public int r, c, g = 9999, h, f;
    public String type = "EMPTY";
    public Node parent;

    private static Map<String, Image> sprites = new HashMap<>();

    public Node(int r, int c) {
        this.r = r;
        this.c = c;
        setOpaque(false);
        loadSprites();
    }

    private void loadSprites() {
        if (sprites.isEmpty()) {
            sprites.put("WALL", getImage("/parede.png"));
            sprites.put("START", getImage("/heroi.png"));
            sprites.put("END", getImage("/criminoso.png"));
            sprites.put("EMPTY", getImage("/grama.png"));
            sprites.put("CAPTURED_MSG", getImage("/capturado.png"));
            sprites.put("ESCAPED_MSG", getImage("/escapou.png"));
        }
    }

    private Image getImage(String path) {
        URL url = getClass().getResource(path);
        if (url == null) return null;
        return new ImageIcon(url).getImage();
    }
    public static Image getSprite(String key) {
        if (sprites.isEmpty()) {
            new Node(0,0).loadSprites();
        }
        return sprites.get(key);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(new Color(40, 44, 52));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        Image grama = sprites.get("EMPTY");
        if (grama != null) {
            g2d.drawImage(grama, 0, 0, getWidth(), getHeight(), this);
        }

        Image objImg = sprites.get(type);
        if (objImg != null && !type.equals("EMPTY")) {
            g2d.drawImage(objImg, 0, 0, getWidth(), getHeight(), this);
        } else {
            drawFallback(g2d);
        }

        drawOverlay(g2d);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(45, 45);
    }

    private void drawFallback(Graphics2D g2d) {
        if (type.equals("WALL")) {
            g2d.setColor(new Color(240, 240, 240, 200));
            g2d.fillRoundRect(4, 4, getWidth()-8, getHeight()-8, 8, 8);
        } else if (type.equals("START")) {
            g2d.setColor(new Color(0, 255, 100));
            g2d.fillOval(6, 6, getWidth()-12, getHeight()-12);
        } else if (type.equals("END")) {
            g2d.setColor(new Color(255, 0, 80));
            g2d.fillRect(8, 8, getWidth()-16, getHeight()-16);
        }
    }

    private void drawOverlay(Graphics2D g2d) {
        if (type.equals("OPEN")) {
            g2d.setColor(new Color(0, 200, 255, 100));
            g2d.fillRect(0, 0, getWidth(), getHeight());
        } else if (type.equals("CLOSED")) {
            g2d.setColor(new Color(150, 0, 255, 60));
            g2d.fillRect(0, 0, getWidth(), getHeight());
        } else if (type.equals("PATH")) {
            g2d.setColor(new Color(255, 255, 0, 140));
            g2d.fillOval(getWidth()/3, getHeight()/3, getWidth()/3, getHeight()/3);
        }
    }

    public void reset() {
        type = "EMPTY"; g = 9999; h = 0; f = 0; parent = null;
    }
}