package ifrn.edu.eduardo;

import ifrn.edu.eduardo.view.AStarView;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AStarView().setVisible(true));
    }
}