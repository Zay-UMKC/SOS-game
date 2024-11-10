import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class SOSGame extends JFrame {
  private static final int DEFAULT_SIZE = 8;
  private JButton[][] Buttons;
  private int boardSize = DEFAULT_SIZE;
  private boolean isBlueTurn = true;
  private JRadioButton simpleGameButton;
  private JRadioButton generalGameButton;
  private JRadioButton newGameButton;
  private JTextField boardSizeInput;
  private JLabel currentTurnLabel;
  private String currentSelection = "S";
  private int blueScore = 0;
  private int redScore = 0;
  private List<int[]> sosPositions = new ArrayList<>();
   //displays score on general mode
  private void generalTurnLabel() {
      String turnText = (isBlueTurn ? "Blue's turn" : "Red's turn") + " | Blue Score: " + blueScore + " | Red Score: " + redScore;
      currentTurnLabel.setText(turnText);
  }
       //chooses rules
  private void checkGameEnd() {
      if (simpleGameButton.isSelected()) {
          checkSimpleGameEnd();
      } else if (generalGameButton.isSelected()) {
          checkGeneralGameEnd();
      }
  }
  private void detectSOSAndScore(int row, int col) {
    // Check for "SOS" sequences in all four directions: horizontal, vertical, and two diagonals.
    boolean sosDetected = false;


    // Horizontal check
    if (col >= 1 && col < boardSize - 1 &&
        Buttons[row][col - 1].getText().equals("S") &&
        Buttons[row][col].getText().equals("O") &&
        Buttons[row][col + 1].getText().equals("S") &&
        addSOSPosition(row, col)) {
        sosDetected = true;
    }


    // Vertical check
    if (row >= 1 && row < boardSize - 1 &&
        Buttons[row - 1][col].getText().equals("S") &&
        Buttons[row][col].getText().equals("O") &&
        Buttons[row + 1][col].getText().equals("S") &&
        addSOSPosition(row, col)) {
        sosDetected = true;
    }


    // Diagonal checks
    // Top-left to bottom-right
    if (row >= 1 && row < boardSize - 1 && col >= 1 && col < boardSize - 1 &&
        Buttons[row - 1][col - 1].getText().equals("S") &&
        Buttons[row][col].getText().equals("O") &&
        Buttons[row + 1][col + 1].getText().equals("S") &&
        addSOSPosition(row, col)) {
        sosDetected = true;
    }


    // Top-right to bottom-left
    if (row >= 1 && row < boardSize - 1 && col >= 1 && col < boardSize - 1 &&
        Buttons[row - 1][col + 1].getText().equals("S") &&
        Buttons[row][col].getText().equals("O") &&
        Buttons[row + 1][col - 1].getText().equals("S") &&
        addSOSPosition(row, col)) {
        sosDetected = true;
    }


    // Award point if an SOS was detected
    if (sosDetected) {
        if (isBlueTurn) {
            blueScore++;
        } else {
            redScore++;
        }
    }
  }
  private boolean addSOSPosition(int[][] sosSequence) {
    for (int[][] pos : sosPositions) {
        if (pos[0][0] == sosSequence[0][0] && pos[0][1] == sosSequence[0][1] &&
            pos[1][0] == sosSequence[1][0] && pos[1][1] == sosSequence[1][1]) {
            return false; // Sequence already tracked, don't add
        }
    }
    sosPositions.add(sosSequence); // Add new SOS sequence
    return true;
  }


        //gives and checks endgame requirements for simple game
  private void checkSimpleGameEnd() {
      if (isSOSFormed()) {
          String winner = !isBlueTurn ? "Blue" : "Red";
          JOptionPane.showMessageDialog(this, winner + " wins by forming SOS!");
      } else if (isBoardFull()) {
          JOptionPane.showMessageDialog(this, "The game is a draw!");
      }
  }
   //checks if sos if formed
  private boolean isSOSFormed() {
      for (int i = 0; i < boardSize; i++) {
          for (int j = 0; j < boardSize; j++) {
              if (checkHorizontalSOS(i, j) || checkVerticalSOS(i, j) || checkDiagonalSOS(i, j)) {
                  return true;
              }
          }
      }
      return false;
  }
   //horizontal
  private boolean checkHorizontalSOS(int row, int col) {
      return col <= boardSize - 3 &&
             Buttons[row][col].getText().equals("S") &&
             Buttons[row][col + 1].getText().equals("O") &&
             Buttons[row][col + 2].getText().equals("S");
  }
   //vertical
  private boolean checkVerticalSOS(int row, int col) {
      return row <= boardSize - 3 &&
             Buttons[row][col].getText().equals("S") &&
             Buttons[row + 1][col].getText().equals("O") &&
             Buttons[row + 2][col].getText().equals("S");
  }
   //diagonal
  private boolean checkDiagonalSOS(int row, int col) {
      boolean diagonalDown = row <= boardSize - 3 && col <= boardSize - 3 &&
                             Buttons[row][col].getText().equals("S") &&
                             Buttons[row + 1][col + 1].getText().equals("O") &&
                             Buttons[row + 2][col + 2].getText().equals("S");
       boolean diagonalUp = row >= 2 && col <= boardSize - 3 &&
                           Buttons[row][col].getText().equals("S") &&
                           Buttons[row - 1][col + 1].getText().equals("O") &&
                           Buttons[row - 2][col + 2].getText().equals("S");
       return diagonalDown || diagonalUp;
  }


  //gives and checks endgame requirements for general game
  private void checkGeneralGameEnd() {
      generalTurnLabel();
      if (isSOSFormed()){
       if(isBlueTurn){
           blueScore++;
       }
       if(!isBlueTurn){
           redScore++;
       }
      }
      if (isBoardFull()) {
          String winnerMessage;
          if (blueScore > redScore) {
              winnerMessage = "Blue wins with " + blueScore + " points!";
          } else if (redScore > blueScore) {
              winnerMessage = "Red wins with " + redScore + " points!";
          } else {
              winnerMessage = "The game is a draw with both players scoring " + blueScore + " points!";
          }
          JOptionPane.showMessageDialog(this, winnerMessage);
      }
  }


  //Checks if board is full
  private boolean isBoardFull() {
      for (int i = 0; i < boardSize; i++) {
          for (int j = 0; j < boardSize; j++) {
              if (Buttons[i][j].getText().isEmpty()) {
                  return false;
              }
          }
      }
      return true;
  }


  private void newGame(JPanel boardPanel) {
    isBlueTurn = true;
    blueScore = 0;
    redScore = 0;
    currentSelection = "S";
    generalTurnLabel();
    initializeBoard(boardPanel); //Clears board
    }


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
      newGameButton = new JRadioButton( "New Game");
      ButtonGroup gameModeGroup = new ButtonGroup();
      gameModeGroup.add(simpleGameButton);
      gameModeGroup.add(generalGameButton);
      gameModePanel.add(simpleGameButton);
      gameModePanel.add(generalGameButton);
      gameModePanel.add(newGameButton);
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
      Buttons = new JButton[DEFAULT_SIZE][DEFAULT_SIZE];
      initializeBoard(boardPanel);
      add(boardPanel, BorderLayout.CENTER);
      if (newGameButton.isSelected()){


      }


      //Players turn
      JPanel bottomPanel = new JPanel();
      currentTurnLabel = new JLabel("Current turn: Blue");
      bottomPanel.add(currentTurnLabel);
      add(bottomPanel, BorderLayout.SOUTH);


      //Board size
      boardSizeInput.addActionListener(e -> UpdateBoardSize(boardPanel));
      newGameButton.addActionListener(e -> newGame(boardPanel));


      setVisible(true);
  }


  private void initializeBoard(JPanel boardPanel) {
      boardPanel.removeAll();
      boardPanel.setLayout(new GridLayout(boardSize, boardSize)); //Set grid to n x n
      for (int i = 0; i < boardSize; i++) {
          for (int j = 0; j < boardSize; j++) {
              Buttons[i][j] = new JButton("");
              Buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 20));
              Buttons[i][j].setFocusPainted(false);
              int row = i, col = j;
              Buttons[i][j].addActionListener(e -> ButtonClick(row, col));
              boardPanel.add(Buttons[i][j]);
          }
      }
      boardPanel.revalidate();
      boardPanel.repaint();
  }
 
  private void ButtonClick(int row, int col) {
   if (!Buttons[row][col].getText().isEmpty()) {
       return; //prevents overwriting
   }
   Buttons[row][col].setText(currentSelection);


   if (isBlueTurn) {
       Buttons[row][col].setForeground(Color.BLUE);
       
       currentTurnLabel.setText("Current turn: Red");
   } else {
       Buttons[row][col].setForeground(Color.RED);
       currentTurnLabel.setText("Current turn: Blue");
   }
   
   isBlueTurn = !isBlueTurn;  


   currentSelection = currentSelection.equals("S") ? "O" : "S";
   checkGameEnd();
   detectSOSAndScore(row,col);
}


  private void UpdateBoardSize(JPanel boardPanel) {
      try {
          int newSize = Integer.parseInt(boardSizeInput.getText());
          if (newSize < 3|| newSize > 30) {
              return;
          }
          boardSize = newSize;
          Buttons = new JButton[boardSize][boardSize];
          initializeBoard(boardPanel);
      } catch (NumberFormatException e) {
      }
  }
  public static void main(String[] args) {
      SwingUtilities.invokeLater(SOSGame::new);
  }
}
