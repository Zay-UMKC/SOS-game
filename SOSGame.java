import javax.swing.*;
import java.awt.*;


public class SOSGame extends JFrame {
    private static final int DEFAULT_SIZE = 8;
    private JButton[][] boardButtons;
    private int boardSize = DEFAULT_SIZE;
    private boolean isBlueTurn = true; // true for Blue, false for Red
    private JRadioButton simpleGameButton;
    private JRadioButton generalGameButton;
    private JTextField boardSizeInput;
    private JLabel currentTurnLabel;
    private String currentSelection = "S";


    public SOSGame() {
        setTitle("SOS Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());


        JPanel topPanel = new JPanel(new BorderLayout());


        //Game mode selection
        JPanel gameModePanel = new JPanel();
        simpleGameButton = new JRadioButton("Simple game", true);
        generalGameButton = new JRadioButton("General game");
        ButtonGroup gameModeGroup = new ButtonGroup();
        gameModeGroup.add(simpleGameButton);
        gameModeGroup.add(generalGameButton);
        gameModePanel.add(simpleGameButton);
        gameModePanel.add(generalGameButton);
        topPanel.add(gameModePanel, BorderLayout.WEST);


        //Board size
        JPanel BoardSize = new JPanel();
        BoardSize.add(new JLabel("Board size:"));
        boardSizeInput = new JTextField(Integer.toString(DEFAULT_SIZE), 5);
        BoardSize.add(boardSizeInput);
        topPanel.add(BoardSize, BorderLayout.EAST);


        add(topPanel, BorderLayout.NORTH);


        //Game board
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(DEFAULT_SIZE, DEFAULT_SIZE));
        boardButtons = new JButton[DEFAULT_SIZE][DEFAULT_SIZE];
        initializeBoard(boardPanel);
        add(boardPanel, BorderLayout.CENTER);


        //Color turn display
        JPanel bottomPanel = new JPanel();
        currentTurnLabel = new JLabel("Current turn: Blue");
        bottomPanel.add(currentTurnLabel);
        add(bottomPanel, BorderLayout.SOUTH);


        //Board size
        boardSizeInput.addActionListener(e -> UpdateBoardSize(boardPanel));


        setVisible(true);
    }


    private void initializeBoard(JPanel boardPanel) {
        boardPanel.removeAll();
        boardPanel.setLayout(new GridLayout(boardSize, boardSize)); // Set grid to n x n
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                boardButtons[i][j] = new JButton("");
                boardButtons[i][j].setFont(new Font("Arial", Font.PLAIN, 20));
                boardButtons[i][j].setFocusPainted(false);
                int row = i, col = j;
                boardButtons[i][j].addActionListener(e -> handleButtonClick(row, col));
                boardPanel.add(boardButtons[i][j]);
            }
        }
        boardPanel.revalidate();
        boardPanel.repaint();
    }


    private void handleButtonClick(int row, int col) {
        if (!boardButtons[row][col].getText().isEmpty()) {
            return; // Do not allow overwriting
        }
        boardButtons[row][col].setText(currentSelection);
        if (isBlueTurn) {
            boardButtons[row][col].setForeground(Color.BLUE);
            currentTurnLabel.setText("Current turn: Red");
        } else {
            boardButtons[row][col].setForeground(Color.RED);
            currentTurnLabel.setText("Current turn: Blue");
        }
        isBlueTurn = !isBlueTurn;


        //Swap turns
        currentSelection = currentSelection.equals("S") ? "O" : "S";
    }


    private void UpdateBoardSize(JPanel boardPanel) {
        try {
            int newSize = Integer.parseInt(boardSizeInput.getText());
            if (newSize < 3|| newSize > 30) {
                return;
            }
            boardSize = newSize;
            boardButtons = new JButton[boardSize][boardSize];
            initializeBoard(boardPanel);
        } catch (NumberFormatException e) {
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(SOSGame::new);
    }
}