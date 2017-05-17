package core;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import userInterface.BattleshipUI;
import userInterface.Player;
import userInterface.PlayerOptionDialog;
import userInterface.GameOptionDialog;
import userInterface.*;



public class BattleshipClient 
{
    Player[]players;
    BattleshipUI ui;
    public BattleshipClient(Player[] p, BattleshipUI u) 
    {
        players = p;
        ui = u;
        
    }
    
    Player currentPlayer;
    //method that decides who goes first and updates the status panel
    public void play()
    {
        changeListener();
        
        if(players[Constants.PLAYER_ONE].isIsFirst())
        {
            ui.updateTextArea("Player 1, your turn");
            currentPlayer = players[Constants.PLAYER_ONE];            
        }
        else if(players[Constants.PLAYER_TWO].isIsFirst())
        {
            ui.updateTextArea("Player 2, your turn");
            currentPlayer = players[Constants.PLAYER_TWO];
            computerPick();
        }
        else
        {
          ui.updateTextArea("Player 1, your turn");
          currentPlayer = players[Constants.PLAYER_ONE];  
        }
    }
    
    private void changeListener()
    {
        for(Player player: players)
        {
            for(int row = 0; row < player.getRows(); row++)
            {
                for(int col = 0; col < player.getCols(); col++)
                {
                    player.removeListener(player.getBoard()[row][col], player.getBoardListener());
                }
            }
        }
        for(int row = 0; row < players[Constants.PLAYER_TWO].getRows(); row++)
            {
                for(int col = 0; col < players[Constants.PLAYER_TWO].getCols(); col++)
                {    
                 players[Constants.PLAYER_TWO].setListener(players[Constants.PLAYER_TWO].getBoard()[row][col], new playListener());                    
                }
            }
    }
    
    //action listener that changes the block color based on a hit, miss, or already selected square
    //then calls switch players
    public class playListener implements ActionListener
    {
        @Override
        public void actionPerformed (ActionEvent e)
        {
            if(e.getSource() instanceof JButton)
            {
                JButton button = (JButton)e.getSource();
                int row = (int)button.getClientProperty("row");
                int col = (int)button.getClientProperty("col");
                
                if(players[Constants.PLAYER_TWO].getBoard()[row][col].getBackground() == Color.RED || players[Constants.PLAYER_TWO].getBoard()[row][col].getBackground() == Color.BLUE)
                    {
                    BattleshipUI.updateTextArea("Square already selected, lose turn");  
                    }
                else if (players[Constants.PLAYER_TWO].isHit(row,col))
                   {
                      players[Constants.PLAYER_TWO].getBoard()[row][col].setBackground(Color.RED);
                   }
                else
                        {
                        players[Constants.PLAYER_TWO].getBoard()[row][col].setBackground(Color.BLUE);
                        }
                switchPlayers();
            }
        }
                
    }

    //method that will switch the players if one player is already selected
    private void switchPlayers() 
    {
        if(checkForWinner())
        {
            endgame();
        }
        if(currentPlayer == players[Constants.PLAYER_ONE])
        {
          currentPlayer = players[Constants.PLAYER_TWO];
          BattleshipUI.updateTextArea("Player 2, your turn");
          computerPick();
        }
        else
        {
            currentPlayer = players[Constants.PLAYER_ONE];
            BattleshipUI.updateTextArea("Player 1, your turn");
            
        }
        
    }
    //checks for the winner by checking if all the ships have been sunk
    private boolean checkForWinner()
    {
        int ships = 0;
        for(Player player : players)
        {
            for(Ship ship: player.getShips())
            {
                if(ship.isShipSunk())
                    ships++;
            }
        if(ships == player.getShips().size())
        {
            JOptionPane.showMessageDialog(ui, player.getUserName() + " has lost.");
            return true;
        }
        }
        return false;
    }
    //if a winner is checked true then it disables the board
    private void endgame()
    {
        for(Player player : players){
            for(int row = 0; row < player.getRows(); row++) 
            {
                for(int col = 0; col < player.getCols(); col++)
                {
                    player.getBoard()[row][col].setEnabled(false);
                }
            }
        }
           
    }
    //method that randomly selects a block and checks the computers selected block and if it is a hit, miss,or already selected
    private void computerPick()
    {
        Random random = new Random();
        int row = random.nextInt(10);
        int col = random.nextInt(10);
        
        if(players[Constants.PLAYER_ONE].getBoard()[row][col].getBackground()==Color.RED || players[Constants.PLAYER_ONE].getBoard()[row][col].getBackground()==Color.BLUE)
        {
            ui.updateTextArea("Square already selected, lose turn");            
        }
        else if(players[Constants.PLAYER_ONE].isHit(row, col))
        {            
        players[Constants.PLAYER_ONE].getBoard()[row][col].setBackground(Color.RED);
        }
        else
        {
        players[Constants.PLAYER_ONE].getBoard()[row][col].setBackground(Color.BLUE);    
        }
        switchPlayers();
    }
  
}

