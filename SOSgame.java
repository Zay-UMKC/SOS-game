package SOS;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SOSgame extends JFrame {
 private static final int DEFAULT_SIZE = 8;
 protected JButton[][] Buttons;
 protected int boardSize = DEFAULT_SIZE;
 protected boolean isBlueTurn = true;
 protected JRadioButton simpleGameButton;
 protected JRadioButton generalGameButton;
 private JRadioButton newGameButton;
 private JTextField boardSizeInput;
 protected JLabel currentTurnLabel;
 protected String currentSelection = " ";
 protected JRadioButton sButton;
 protected JRadioButton oButton;
 private ButtonGroup selectionGroup;
 protected int blueScore = 0;
 protected int redScore = 0;
 private List<int[]> sosPositions = new ArrayList<>();
 protected JCheckBox redIsComputer;
 protected JCheckBox blueIsComputer;
  //displays score on general mode
  protected void generalTurnLabel() {
     String turnText = (isBlueTurn ? "Blue's turn" : "Red's turn") + " | Blue Score: " + blueScore + " | Red Score: " + redScore;
     currentTurnLabel.setText(turnText);
 }
      //chooses rules
      protected void checkGameEnd() {
     if (simpleGameButton.isSelected()) {
         checkSimpleGameEnd();
     } else if (generalGameButton.isSelected()) {
         checkGeneralGameEnd();
     }
 }
       //gives and checks endgame requirements for simple game
       protected void checkSimpleGameEnd() {
     if (isSOSFormed()) {
         String winner = !isBlueTurn ? "Blue" : "Red";
         JOptionPane.showMessageDialog(this, winner + " wins by forming SOS!");
     } else if (isBoardFull()) {
         JOptionPane.showMessageDialog(this, "The game is a draw!");
     }
 }
  //checks if sos if formed
  protected boolean isSOSFormed() {
   boolean sosFound = false;
   for (int i = 0; i < boardSize; i++) {
       for (int j = 0; j < boardSize; j++) {
           sosFound |= checkHorizontalSOS(i, j);
           sosFound |= checkVerticalSOS(i, j);
           sosFound |= checkDiagonalSOS(i, j);
       }
   }
   return sosFound;
 }
// Check horizontal SOS
private boolean checkHorizontalSOS(int row, int col) {
   if (col <= boardSize - 3 && Buttons[row][col].getText().equals("S") &&
           Buttons[row][col + 1].getText().equals("O") &&
           Buttons[row][col + 2].getText().equals("S")) {
       return addSOSPosition(new int[]{row, col}, new int[]{row, col + 1}, new int[]{row, col + 2});
   }
   return false;
}




// Check vertical SOS
private boolean checkVerticalSOS(int row, int col) {
   if (row <= boardSize - 3 && Buttons[row][col].getText().equals("S") &&
           Buttons[row + 1][col].getText().equals("O") &&
           Buttons[row + 2][col].getText().equals("S")) {
       return addSOSPosition(new int[]{row, col}, new int[]{row + 1, col}, new int[]{row + 2, col});
   }
   return false;
}




// Check diagonal SOS
private boolean checkDiagonalSOS(int row, int col) {
   // Check down-right diagonal
   if (row <= boardSize - 3 && col <= boardSize - 3 &&
           Buttons[row][col].getText().equals("S") &&
           Buttons[row + 1][col + 1].getText().equals("O") &&
           Buttons[row + 2][col + 2].getText().equals("S")) {
       return addSOSPosition(new int[]{row, col}, new int[]{row + 1, col + 1}, new int[]{row + 2, col + 2});
   }




   // Check down-left diagonal
   if (row <= boardSize - 3 && col >= 2 &&
           Buttons[row][col].getText().equals("S") &&
           Buttons[row + 1][col - 1].getText().equals("O") &&
           Buttons[row + 2][col - 2].getText().equals("S")) {
       return addSOSPosition(new int[]{row, col}, new int[]{row + 1, col - 1}, new int[]{row + 2, col - 2});
   }




   return false;
}




// Adds SOS position if not already present
private boolean addSOSPosition(int[] pos1, int[] pos2, int[] pos3) {
   int[][] newSOS = {pos1, pos2, pos3};
   for (int[] pos : newSOS) {
       for (int[] existingPos : sosPositions) {
           if (pos[0] == existingPos[0] && pos[1] == existingPos[1]) {
               return false;
           }
       }
   }
   sosPositions.add(pos1);
   sosPositions.add(pos2);
   sosPositions.add(pos3);
   return true;
}




 //gives and checks endgame requirements for general game
 private void checkGeneralGameEnd() {
     if (isSOSFormed()){
       if(isBlueTurn){
           redScore++;
       } else{
           blueScore++;
       }
       generalTurnLabel();
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

 public void makeComputerMove() {
    List<int[]> availableMoves = new ArrayList<>();
    for (int i = 0; i < boardSize; i++) {
        for (int j = 0; j < boardSize; j++) {
            if (Buttons[i][j].getText().isEmpty()) {
                availableMoves.add(new int[]{i, j});
            }
        }
    }
    if (!availableMoves.isEmpty()) {
        int[] move = availableMoves.get((int) (Math.random() * availableMoves.size()));
        currentSelection = isBlueTurn ? "S" : "O"; // Default selection for computer
        Buttons[move[0]][move[1]].setText(currentSelection);
        Buttons[move[0]][move[1]].setForeground(isBlueTurn ? Color.BLUE : Color.RED);

        boolean sosFormed = isSOSFormed();
        if (sosFormed) {
            if (simpleGameButton.isSelected()) {
                JOptionPane.showMessageDialog(this, (isBlueTurn ? "Blue" : "Red") + " wins by forming SOS!");
                if (isBlueTurn) {
                    blueScore++;
                } else {
                    redScore++;
                }
                return;
            } else if (generalGameButton.isSelected()) {
                if (isBlueTurn) {
                    blueScore++;
                } else {
                    redScore++;
                }
                if (!isSOSFormed()) {
                    isBlueTurn = !isBlueTurn;
                }
            }
        } else {
            isBlueTurn = !isBlueTurn;
        }

        generalTurnLabel();
        checkGameEnd();

        if ((isBlueTurn && blueIsComputer.isSelected()) || (!isBlueTurn && redIsComputer.isSelected())) {
            SwingUtilities.invokeLater(this::makeComputerMove);
        }
    }
}


 private void newGame(JPanel boardPanel) {
   isBlueTurn = true;
   blueScore = 0;
   redScore = 0;
   currentSelection = " ";
   sosPositions.clear();
   initializeBoard(boardPanel); //Clears board
   }
   


  public SOSgame() {
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
     redIsComputer = new JCheckBox("Computer as Red");
     blueIsComputer = new JCheckBox("Computer as Blue");
     ButtonGroup gameModeGroup = new ButtonGroup();
     gameModeGroup.add(simpleGameButton);
     gameModeGroup.add(generalGameButton);
     gameModePanel.add(simpleGameButton);
     gameModePanel.add(generalGameButton);
     gameModePanel.add(newGameButton);
     gameModePanel.add(redIsComputer);
     gameModePanel.add(blueIsComputer);
     topPanel.add(gameModePanel, BorderLayout.WEST);




     //Board size
     JPanel BoardSize = new JPanel();
     BoardSize.add(new JLabel("Board size:"));
     boardSizeInput = new JTextField(Integer.toString(DEFAULT_SIZE), 5);
     BoardSize.add(boardSizeInput);
     topPanel.add(BoardSize, BorderLayout.EAST);




     add(topPanel, BorderLayout.NORTH);
     //S and O choice
     sButton = new JRadioButton("S");
     oButton = new JRadioButton("O");
     selectionGroup = new ButtonGroup();
     selectionGroup.add(sButton);
     selectionGroup.add(oButton);
    
     // Add action listeners to update the current selection
     sButton.addActionListener(e -> currentSelection = "S");
     oButton.addActionListener(e -> currentSelection = "O");
    
     // Create a panel to hold the buttons
     JPanel selectionPanel = new JPanel();
     selectionPanel.setLayout(new BoxLayout(selectionPanel, BoxLayout.Y_AXIS));
     selectionPanel.add(new JLabel("Choose Letter:"));
     selectionPanel.add(sButton);
     selectionPanel.add(oButton);
      // Add the selection panel to the left side of the main frame
     getContentPane().add(selectionPanel, BorderLayout.WEST);
     //Game board
     JPanel boardPanel = new JPanel();
     boardPanel.setLayout(new GridLayout(DEFAULT_SIZE, DEFAULT_SIZE));
     Buttons = new JButton[DEFAULT_SIZE][DEFAULT_SIZE];
     initializeBoard(boardPanel);
     add(boardPanel, BorderLayout.CENTER);
     if (oButton.isSelected()){
       isBlueTurn = !isBlueTurn;
     }
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
    boardPanel.setLayout(new GridLayout(boardSize, boardSize));
    Buttons = new JButton[boardSize][boardSize];
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

    if ((blueIsComputer.isSelected() && isBlueTurn) || (redIsComputer.isSelected() && !isBlueTurn)) {
        SwingUtilities.invokeLater(this::makeComputerMove);
    }
}

private void ButtonClick(int row, int col) {
    if (!Buttons[row][col].getText().isEmpty()) {
        return; // Prevents overwriting
    }

    // Set currentSelection based on the selected button
    if (oButton.isSelected()) {
        currentSelection = "O";
    } else if (sButton.isSelected()) {
        currentSelection = "S";
    } else {
        return;
    }

    Buttons[row][col].setText(currentSelection);
    Buttons[row][col].setForeground(isBlueTurn ? Color.BLUE : Color.RED);

    boolean sosFormed = isSOSFormed();
    if (sosFormed) {
        if (simpleGameButton.isSelected()) {
            JOptionPane.showMessageDialog(this, (isBlueTurn ? "Blue" : "Red") + " wins by forming SOS!");
            if (isBlueTurn) {
                blueScore++;
            } else {
                redScore++;
            }
            return;
        } else if (generalGameButton.isSelected()) {
            if (isBlueTurn) {
                blueScore++;
            } else {
                redScore++;
            }
            if (!isSOSFormed()) {
                isBlueTurn = !isBlueTurn;
            }
        }
    } else {
        isBlueTurn = !isBlueTurn;
    }

    generalTurnLabel();
    checkGameEnd();

    if ((isBlueTurn && blueIsComputer.isSelected()) || (!isBlueTurn && redIsComputer.isSelected())) {
        SwingUtilities.invokeLater(this::makeComputerMove);
    }
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
    SwingUtilities.invokeLater(() -> {
        SOSgame game = new SOSgame();
        if (game.blueIsComputer.isSelected() || game.redIsComputer.isSelected()) {
            game.makeComputerMove();
        }
    });
}
}