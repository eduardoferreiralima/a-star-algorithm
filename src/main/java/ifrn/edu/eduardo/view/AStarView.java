package ifrn.edu.eduardo.view;

import ifrn.edu.eduardo.model.Node;
import ifrn.edu.eduardo.algorithm.AStarAlgorithm;
import ifrn.edu.eduardo.algorithm.GreedyAlgorithm; // Certifique-se de que a classe existe
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class AStarView extends JFrame {
    private int ROWS = 16, COLS = 24;
    private JPanel board;
    private Node[][] grid;
    private Node startNode, endNode;
    private boolean isRunning = false;
    private String editMode = "WALL";
    private JComboBox<String> algoSelector;
    private long lastExecutionTime = 0;
    private final int WIN_WIDTH = 1300;
    private final int WIN_HEIGHT = 850;
    private final int SIDE_WIDTH = 300;
    private final Color COLOR_SELECTED = new Color(255, 193, 7);
    private final Color COLOR_BG_SIDE = new Color(25, 25, 35);

    public AStarView() {
        setTitle("A* vs Guloso - Performance Analyzer");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WIN_WIDTH, WIN_HEIGHT);
        setResizable(false);
        setLayout(new BorderLayout());

        board = new JPanel();
        board.setBackground(new Color(40, 40, 40));
        board.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        grid = new Node[ROWS][COLS];
        inicializarGrid();

        add(board, BorderLayout.CENTER);
        add(createSidePanel(), BorderLayout.EAST);
        setLocationRelativeTo(null);
    }

    private void inicializarGrid() {
        board.removeAll();
        board.setLayout(new GridLayout(ROWS, COLS, 0, 0));

        int dispW = WIN_WIDTH - SIDE_WIDTH - 60;
        int dispH = WIN_HEIGHT - 60;
        int nodeSize = Math.min(dispW / COLS, dispH / ROWS);

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                grid[r][c] = new Node(r, c);
                grid[r][c].setPreferredSize(new Dimension(nodeSize, nodeSize));
                addMouseEvents(grid[r][c]);
                board.add(grid[r][c]);
            }
        }

        startNode = grid[ROWS/2][2];
        startNode.type = "START";
        endNode = grid[ROWS/2][COLS-3];
        endNode.type = "END";

        board.revalidate();
        board.repaint();
    }

    private JPanel createSidePanel() {
        JPanel side = new JPanel(new GridBagLayout());
        side.setPreferredSize(new Dimension(SIDE_WIDTH, 0));
        side.setBackground(COLOR_BG_SIDE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;

        JLabel lblAlgo = new JLabel("ALGORITMO DE BUSCA", SwingConstants.CENTER);
        lblAlgo.setForeground(Color.GRAY);
        lblAlgo.setFont(new Font("Arial", Font.BOLD, 11));
        side.add(lblAlgo, gbc); gbc.gridy++;

        algoSelector = new JComboBox<>(new String[]{"Algoritmo A*", "Algoritmo Guloso"});
        algoSelector.setBackground(new Color(40, 40, 50));
        algoSelector.setForeground(Color.WHITE);
        side.add(algoSelector, gbc); gbc.gridy++;

        side.add(Box.createVerticalStrut(10), gbc); gbc.gridy++;

        JButton btnRun = createBtn("INICIAR OPERAÇÃO", new Color(0, 255, 100), 240, 45);
        JButton btnMaze = createBtn("GERAR LABIRINTO", Color.CYAN, 240, 45);
        JButton btnClear = createBtn("LIMPAR TUDO", Color.WHITE, 240, 45);

        side.add(btnRun, gbc); gbc.gridy++;
        side.add(btnMaze, gbc); gbc.gridy++;
        side.add(btnClear, gbc); gbc.gridy++;

        side.add(Box.createVerticalStrut(15), gbc); gbc.gridy++;
        side.add(new JSeparator(), gbc); gbc.gridy++;

        JLabel lblEdit = new JLabel("FERRAMENTAS DE EDIÇÃO", SwingConstants.CENTER);
        lblEdit.setForeground(Color.GRAY);
        lblEdit.setFont(new Font("Arial", Font.BOLD, 11));
        side.add(lblEdit, gbc); gbc.gridy++;

        JToggleButton tbWall = createToggle("DESENHAR PAREDES", true);
        JToggleButton tbHero = createToggle("MOVER HERÓI", false);
        JToggleButton tbTarget = createToggle("MOVER CRIMINOSO", false);

        ButtonGroup group = new ButtonGroup();
        group.add(tbWall); group.add(tbHero); group.add(tbTarget);

        ActionListener toggleListener = e -> {
            updateToggleStyles(tbWall, tbHero, tbTarget);
            if (tbWall.isSelected()) editMode = "WALL";
            else if (tbHero.isSelected()) editMode = "START";
            else editMode = "END";
        };

        tbWall.addActionListener(toggleListener);
        tbHero.addActionListener(toggleListener);
        tbTarget.addActionListener(toggleListener);
        updateToggleStyles(tbWall, tbHero, tbTarget);

        side.add(tbWall, gbc); gbc.gridy++;
        side.add(tbHero, gbc); gbc.gridy++;
        side.add(tbTarget, gbc); gbc.gridy++;

        side.add(new JSeparator(), gbc); gbc.gridy++;

        JLabel lblRes = new JLabel(ROWS + " x " + COLS, SwingConstants.CENTER);
        lblRes.setForeground(Color.LIGHT_GRAY);
        lblRes.setFont(new Font("Monospaced", Font.BOLD, 18));
        side.add(lblRes, gbc); gbc.gridy++;

        JSlider scaleSlider = new JSlider(1, 5, 1);
        scaleSlider.setBackground(COLOR_BG_SIDE);
        scaleSlider.addChangeListener(e -> {
            int f = scaleSlider.getValue();
            lblRes.setText((16 + (f-1)*15) + " x " + (24 + (f-1)*20));
        });
        side.add(scaleSlider, gbc); gbc.gridy++;

        JButton btnApply = createBtn("APLICAR ESCALA", Color.ORANGE, 240, 45);
        btnApply.addActionListener(e -> ajustarTamanhoMapa(16 + (scaleSlider.getValue()-1)*15, 24 + (scaleSlider.getValue()-1)*20));
        side.add(btnApply, gbc); gbc.gridy++;

        btnRun.addActionListener(e -> { if(!isRunning) solve(); });
        btnMaze.addActionListener(e -> generateMaze());
        btnClear.addActionListener(e -> resetAll());

        return side;
    }

    private void updateToggleStyles(JToggleButton... buttons) {
        for (JToggleButton b : buttons) {
            b.setBackground(b.isSelected() ? COLOR_SELECTED : new Color(45, 45, 60));
            b.setForeground(b.isSelected() ? Color.BLACK : Color.LIGHT_GRAY);
        }
    }

    private void addMouseEvents(Node node) {
        node.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(isRunning) return;
                if (editMode.equals("START") && node != endNode) {
                    startNode.type = "EMPTY"; startNode = node; startNode.type = "START";
                } else if (editMode.equals("END") && node != startNode) {
                    endNode.type = "EMPTY"; endNode = node; endNode.type = "END";
                } else if (editMode.equals("WALL") && node != startNode && node != endNode) {
                    node.type = node.type.equals("WALL") ? "EMPTY" : "WALL";
                }
                repaint();
            }
        });
    }

    private void solve() {
        for(int r=0; r<ROWS; r++) {
            for(int c=0; c<COLS; c++) {
                if(grid[r][c].type.equals("OPEN") || grid[r][c].type.equals("CLOSED") || grid[r][c].type.equals("PATH"))
                    grid[r][c].type = "EMPTY";
                grid[r][c].g = 99999; grid[r][c].parent = null;
            }
        }
        isRunning = true;

        boolean useAStar = algoSelector.getSelectedIndex() == 0;
        if (useAStar) {
            lastExecutionTime = AStarAlgorithm.solve(startNode, endNode, grid, ROWS, COLS);
        } else {
            lastExecutionTime = GreedyAlgorithm.solve(startNode, endNode, grid, ROWS, COLS);
        }
        // Limpa custos da medição para a animação visual não bugar
        for(int r=0; r<ROWS; r++) for(int c=0; c<COLS; c++) { grid[r][c].g = 99999; grid[r][c].parent = null; }

        new SwingWorker<Boolean, Node>() {
            @Override protected Boolean doInBackground() throws Exception {
                PriorityQueue<Node> open = new PriorityQueue<>(Comparator.comparingInt(n -> useAStar ? n.f : n.h));
                Set<Node> closed = new HashSet<>();
                startNode.g = 0; startNode.h = AStarAlgorithm.calcH(startNode, endNode);
                startNode.f = startNode.g + startNode.h;
                open.add(startNode);
                while (!open.isEmpty()) {
                    Node curr = open.poll();
                    if (curr == endNode) return true;
                    closed.add(curr);
                    if (curr != startNode && curr != endNode) curr.type = "CLOSED";
                    for (Node n : AStarAlgorithm.getNeighbors(curr, grid, ROWS, COLS)) {
                        if (closed.contains(n) || n.type.equals("WALL")) continue;
                        int cost = (n.r != curr.r && n.c != curr.c) ? 14 : 10;
                        int tG = curr.g + cost;
                        if (tG < n.g) {
                            n.parent = curr; n.g = tG;
                            n.h = AStarAlgorithm.calcH(n, endNode);
                            n.f = useAStar ? (n.g + n.h) : n.h;
                            if (!open.contains(n)) { if(n != endNode) n.type = "OPEN"; open.add(n); }
                        }
                    }
                    publish(curr); Thread.sleep(Math.max(1, 10 - (ROWS/15)));
                }
                return false;
            }
            @Override protected void process(List<Node> c) { repaint(); }
            @Override protected void done() {
                try { if (get()) animarCaminhada(); else { isRunning = false; exibirPopUpResultado(false); } } catch (Exception e) {}
            }
        }.execute();
    }

    private void animarCaminhada() {
        List<Node> caminho = new ArrayList<>();
        Node temp = endNode;
        while (temp != null) { caminho.add(0, temp); temp = temp.parent; }

        new SwingWorker<Void, Node>() {
            @Override protected Void doInBackground() throws Exception {
                for(int r=0; r<ROWS; r++) for(int c=0; c<COLS; c++)
                    if(grid[r][c].type.equals("OPEN") || grid[r][c].type.equals("CLOSED")) grid[r][c].type = "EMPTY";
                Node anterior = startNode;
                for (Node passo : caminho) {
                    if (anterior != startNode) anterior.type = "PATH";
                    else if (anterior == startNode && passo != startNode) anterior.type = "EMPTY";
                    if (passo != endNode) passo.type = "START";
                    anterior = passo; publish(passo); Thread.sleep(35);
                }
                return null;
            }
            @Override protected void process(List<Node> chunks) { repaint(); }
            @Override protected void done() {
                startNode = endNode; startNode.type = "START";
                isRunning = false; exibirPopUpResultado(true);
            }
        }.execute();
    }

    private void exibirPopUpResultado(boolean found) {
        String titulo = found ? "MISSÃO CUMPRIDA" : "ALVO PERDIDO";
        String msg = found ? "O criminoso foi capturado! Herói aguarda ordens." : "O criminoso escapou do cerco.";
        String corHex = found ? "#00FF64" : "#FF0050";

        Image img = Node.getSprite(found ? "CAPTURED_MSG" : "ESCAPED_MSG");
        ImageIcon icon = (img != null) ? new ImageIcon(img.getScaledInstance(120, 120, Image.SCALE_SMOOTH)) : null;

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(400, 300)); // Aumentei um pouco para caber a imagem + tempo
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        if (icon != null) {
            panel.add(new JLabel(icon), gbc);
        }

        JLabel label = new JLabel("<html><div style='text-align: center; width: 250px; font-family: Arial;'>" +
                "<h1 style='color: " + corHex + "; margin: 0;'>" + titulo + "</h1>" +
                "<p style='color: #BBBBBB; font-size: 13px; margin-top: 5px;'>" + msg + "</p>" +
                "<p style='color: #FFC107; font-size: 14px; margin-top: 10px;'>Tempo de Resposta: <b>" + lastExecutionTime + " µs</b></p>" +
                "</div></html>");
        panel.add(label, gbc);

        UIManager.put("OptionPane.background", new Color(25, 25, 35));
        UIManager.put("Panel.background", new Color(25, 25, 35));
        UIManager.put("Button.background", new Color(40, 40, 60));
        UIManager.put("Button.foreground", Color.WHITE);

        JOptionPane.showMessageDialog(this, panel, "Relatório Tático", JOptionPane.PLAIN_MESSAGE, null);
    }

    private void ajustarTamanhoMapa(int r, int c) {
        if(isRunning) return;
        this.ROWS = r; this.COLS = c;
        this.grid = new Node[ROWS][COLS];
        inicializarGrid();
    }

    private JButton createBtn(String t, Color c, int w, int h) {
        JButton b = new JButton(t);
        b.setPreferredSize(new Dimension(w, h));
        b.setFocusPainted(false);
        b.setBackground(new Color(20, 20, 30));
        b.setForeground(c);
        b.setFont(new Font("Arial", Font.BOLD, 12));
        b.setBorder(BorderFactory.createLineBorder(c, 2));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JToggleButton createToggle(String t, boolean sel) {
        JToggleButton b = new JToggleButton(t, sel);
        b.setPreferredSize(new Dimension(240, 35));
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder());
        b.setFont(new Font("Arial", Font.BOLD, 10));
        return b;
    }

    private void resetAll() {
        if(isRunning) return;
        for(int r=0; r<ROWS; r++) for(int c=0; c<COLS; c++) grid[r][c].reset();
        startNode = grid[ROWS/2][2]; startNode.type = "START";
        endNode = grid[ROWS/2][COLS-3]; endNode.type = "END";
        repaint();
    }

    private void generateMaze() {
        if(isRunning) return;
        for(int r=0; r<ROWS; r++) for(int c=0; c<COLS; c++) {
            if(!grid[r][c].type.equals("START") && !grid[r][c].type.equals("END")) {
                grid[r][c].reset();
                if(new Random().nextDouble() < 0.3) grid[r][c].type = "WALL";
            }
        }
        repaint();
    }
}