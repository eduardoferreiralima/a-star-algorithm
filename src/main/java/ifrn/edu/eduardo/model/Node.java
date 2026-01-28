package ifrn.edu.eduardo.model;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Node extends JPanel {
    public int r, c;
    public int g = 99999, h, f;
    public Node parent;
    public String type = "EMPTY";

    private static final Map<String, Image> sprites = new HashMap<>();

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

    /**
     * Limpa apenas os dados da busca anterior (g, h, f, parent e cores de busca).
     * Mantém Paredes, Herói e Criminoso intactos.
     */
    public void clearSearchData() {
        g = 99999;
        h = 0;
        f = 0;
        parent = null;
        if (type.equals("OPEN") || type.equals("CLOSED") || type.equals("PATH")) {
            type = "EMPTY";
        }
    }

    /**
     * Reseta o nó completamente para o estado inicial (Grama).
     */
    public void reset() {
        clearSearchData();
        type = "EMPTY";
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(new Color(40, 44, 52));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        Image grama = sprites.get("EMPTY");
        if (grama != null) {
            g2d.drawImage(grama, 0, 0, getWidth(), getHeight(), this);
        }

        Image objImg = sprites.get(type);
        if (objImg != null && (type.equals("WALL") || type.equals("START") || type.equals("END"))) {
            g2d.drawImage(objImg, 0, 0, getWidth(), getHeight(), this);
        } else if (!type.equals("EMPTY") && !isSearchType()) {
            drawFallback(g2d);
        }

        drawOverlay(g2d);
    }

    private boolean isSearchType() {
        return type.equals("OPEN") || type.equals("CLOSED") || type.equals("PATH");
    }

    private void drawFallback(Graphics2D g2d) {
        if (type.equals("WALL")) {
            g2d.setColor(new Color(200, 200, 200, 180));
            g2d.fillRoundRect(4, 4, getWidth()-8, getHeight()-8, 5, 5);
        }
    }

    private void drawOverlay(Graphics2D g2d) {
        switch (type) {
            case "OPEN" -> {
                g2d.setColor(new Color(0, 200, 255, 80));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
            case "CLOSED" -> {
                g2d.setColor(new Color(150, 0, 255, 40));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
            case "PATH" -> {
                // Glow amarelo para o caminho final
                g2d.setColor(new Color(255, 255, 0, 160));
                int size = getWidth() / 2;
                g2d.fillOval(getWidth()/4, getHeight()/4, size, size);
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(45, 45);
    }
}