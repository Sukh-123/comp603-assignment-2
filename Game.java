package uno;


import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import uno.UnoCard;
import uno.UnoDeck;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author sukhdevsingh
 */
public class Game {
    private int currentPlayer;
    private String[] playerIds;
    
    private UnoDeck deck;
    private ArrayList<ArrayList<UnoCard>> playerHand;
    private ArrayList<UnoCard> stockPile;
    
    private UnoCard.Colour validColour;
    private UnoCard.Value validValue;
    
    boolean gameDirection;
    
    public Game(String[] pids) {
        deck = new UnoDeck();
        deck.shuffle();
        stockPile = new ArrayList<UnoCard>();
        
        playerIds = pids;
        currentPlayer =0;
        gameDirection = false;
        
        playerHand = new ArrayList<ArrayList<UnoCard>>();
        
        for (int i = 0; i < pids.length; i++) 
        {
            ArrayList<UnoCard> hand = new ArrayList<UnoCard>(Arrays.asList(deck.drawCard(7)));
            playerHand.add(hand);
        }
    }
    
    public void start(Game game) {
        UnoCard card = deck.drawCard();
        validColour = card.getColour();
        validValue = card.getValue();
        
        if (card.getValue() == UnoCard.Value.Wild) {
            start(game);
        }
        
        if (card.getValue() == UnoCard.Value.Wild_Four || card.getValue() == UnoCard.Value.DrawTwo) {
            start(game);
        }
        
        if (card.getValue() == UnoCard.Value.Skip) {
            JLabel message = new JLabel(playerIds [currentPlayer] + " was skipped! ");
            message.setFont(new Font("Arial", Font.BOLD, 48));
            JOptionPane.showMessageDialog(null, message);
            
            if(gameDirection == false){
                currentPlayer = (currentPlayer + 1) % playerIds.length;
            }
            
            else if(gameDirection == true){
                currentPlayer = (currentPlayer - 1) % playerIds.length;
                if (currentPlayer == -1){
                    currentPlayer = playerIds.length - 1;
                }
            }
        }
        if (card.getValue() == UnoCard.Value.Reverse){
            JLabel message = new JLabel(playerIds [currentPlayer] + " The game direction changed! ");
            message.setFont(new Font("Arial", Font.BOLD, 48));
            JOptionPane.showMessageDialog(null, message);
            gameDirection ^= true;
            currentPlayer = playerIds.length -1;
        }
        
        stockPile.add(card);
    }
    
    public UnoCard getTopCard(){
        return new UnoCard(validColour, validValue);
    }
    
    public ImageIcon getTopCardImage() {
        return new ImageIcon(validColour + "_" + validValue + ".png");
    }
    
    public boolean isGameOver() {
        for (String player : this.playerIds) {
            if (hasEmptyHand(player)) {
                return true;
            }
        }
        return false;
    }
    
    public String getCurrentPlayer() {
        return this.playerIds[this.currentPlayer];
    }
    
    public String getPreviousPlayer(int i) {
        int index = this.currentPlayer - i;
        if (index == -1) {
            index = playerIds.length - 1;
        }
        return this.playerIds[index];
    }
    
    public String[] getPlayers(){
        return playerIds;
    }
    
    public ArrayList<UnoCard> getPlayerHand(String pid) {
        int index = Arrays.asList(playerIds).indexOf(pid);
        return playerHand.get(index);
    }
    
    
    public int getPlayerHandSize(String pid) {
        return getPlayerHand(pid).size();
    }
    
    public UnoCard getPlayerCard(String pid, int choice){
        ArrayList<UnoCard> hand = getPlayerHand(pid);
        return hand.get(choice);
    }
    
    public boolean hasEmptyHand(String pid) {
        return getPlayerHand(pid).isEmpty();
    }
    
    public boolean validCardPlay(UnoCard card) {
        return card.getColour() == validColour || card.getValue() == validValue;
    }
    
    public void checkPlayerTurn(String pid) throws InvalidPlayerTurnException {
        if (this.playerIds[this.currentPlayer] != pid){
            throw new InvalidPlayerTurnException("it is not " + pid +  " 's turn", pid);
        }
    }
    
    public void submitDraw(String pid) throws InvalidPlayerTurnException {
        checkPlayerTurn(pid);
        
        if (deck.isEmpty()) {
            deck.replaceDeckWith(stockPile);
            deck.shuffle();
        }
        
        getPlayerHand(pid).add(deck.drawCard());
        if (gameDirection == false) {
            currentPlayer = (currentPlayer + 1) % playerIds.length;
        }
        
        else if(gameDirection == true) {
            currentPlayer = (currentPlayer - 1) % playerIds.length;
            if (currentPlayer == -1) {
                currentPlayer = playerIds.length - 1;
            }
        }
    }
    
    public void setCardColour(UnoCard.Colour colour) {
        validColour = colour;
    }
    
    public void submitPlayerCard(String pid, UnoCard card, UnoCard.Colour declaredColour) 
        throws InvalidColourSubmissionException, InvalidValueSubmissionException, InvalidPlayerTurnException {
        checkPlayerTurn(pid);
        
        ArrayList<UnoCard> pHand = getPlayerHand(pid);
        
        if (!validCardPlay(card)) {
            if (card.getColour() == UnoCard.Colour.Wild) {
                validColour = card.getColour();
                validValue = card.getValue();
            }
            
            if (card.getColour() != validColour) {
                JLabel message = new JLabel("Invalid player move, expected colour: "+ validColour +" but got colour " + card.getColour());
                message.setFont(new Font("Arial", Font.BOLD, 48));
                JOptionPane.showMessageDialog(null, message);
                throw new InvalidColourSubmissionException("Invalid player move, expected colour: "+ validColour +" but got colour " + card.getColour(), card.getColour(), validColour);
            }
            
            else if (card.getValue() != validValue) {
               JLabel message2 = new JLabel("Invalid player move, expected value: "+ validValue +" but got colour " + card.getValue());
               message2.setFont(new Font("Arial", Font.BOLD, 48));
               JOptionPane.showMessageDialog(null, message2);
               throw new InvalidValueSubmissionException("Invalid player move, expected value: "+ validValue +" but got colour " + card.getValue(), card.getValue(), validValue);
            }
    }
        
        pHand.remove(card);
        
        if (hasEmptyHand(this.playerIds[currentPlayer])) {
            JLabel message2 = new JLabel(this.playerIds[currentPlayer] + " won the game! Thank you for playing!");
            message2.setFont(new Font("Arial", Font.BOLD, 48));
            JOptionPane.showMessageDialog(null, message2);
            System.exit(0);
        }
        
        validColour = card.getColour();
        validValue = card.getValue();
        stockPile.add(card);
        
        if (gameDirection == false) {
            currentPlayer = (currentPlayer + 1) % playerIds.length;
        }
        else if(gameDirection == true){
            currentPlayer = (currentPlayer - 1) % playerIds.length;
            if (currentPlayer == -1) {
                currentPlayer = playerIds.length - 1;
            }
        }
        
        if (card.getColour() == UnoCard.Colour.Wild) {
            validColour = declaredColour;
        }
        
        if (card.getValue() == UnoCard.Value.DrawTwo) {
            pid = playerIds[currentPlayer];
            getPlayerHand(pid).add(deck.drawCard());
            getPlayerHand(pid).add(deck.drawCard());
            JLabel message = new JLabel(pid + " drew 2 cards!");
        }
            if (card.getValue() == UnoCard.Value.Wild_Four) {
            pid = playerIds[currentPlayer];
            getPlayerHand(pid).add(deck.drawCard());
            getPlayerHand(pid).add(deck.drawCard());
            getPlayerHand(pid).add(deck.drawCard());
            getPlayerHand(pid).add(deck.drawCard());
            JLabel message = new JLabel(pid + " drew 4 cards!");
        }
          
            if (card.getValue() == UnoCard.Value.Skip) {
                JLabel message = new JLabel(playerIds[currentPlayer] + " was skipped!");
                message.setFont(new Font("Arial", Font.BOLD, 48));
                JOptionPane.showMessageDialog(null, message);
                if (gameDirection == false) {
                    currentPlayer = (currentPlayer + 1) % playerIds.length;
                }
                
                else if(gameDirection == true) {
                    currentPlayer = (currentPlayer - 1) % playerIds.length;
                    if (currentPlayer == -1) {
                        currentPlayer = playerIds.length -1;
                    }
                }
            }
            
            if (card.getValue() == UnoCard.Value.Reverse) {
               JLabel message = new JLabel(pid + " changed the game direction!");
                message.setFont(new Font("Arial", Font.BOLD, 48));
                JOptionPane.showMessageDialog(null, message);
                
                gameDirection ^= true;
                if (gameDirection == true) {
                    currentPlayer = (currentPlayer - 2) % playerIds.length;
                    if (currentPlayer == -1) {
                        currentPlayer = playerIds.length - 1;
                    }
                    
                    if (currentPlayer == -2) {
                        currentPlayer = playerIds.length - 2;
                }
            }
                else if (gameDirection == false) {
                    currentPlayer = (currentPlayer + 2) % playerIds.length;
                }
        }
    }

    
}

class InvalidPlayerTurnException extends Exception {
    String playerId;
    
    public InvalidPlayerTurnException(String message, String pid) {
        super(message);
        playerId = pid;
    }
    
    public String getPid() {
        return playerId;
    }
}

class InvalidColourSubmissionException extends Exception {
    private UnoCard.Colour expected;
    private UnoCard.Colour actual;
    
    public InvalidColourSubmissionException(String message, UnoCard.Colour actual, UnoCard.Colour expected){
        this.actual = actual;
        this.expected = expected;
    }
}

class InvalidValueSubmissionException extends Exception {
    private UnoCard.Value expected;
    private UnoCard.Value actual;
    
    public InvalidValueSubmissionException(String message, UnoCard.Value actual, UnoCard.Value expected){
        this.actual = actual;
        this.expected = expected;
    }
}
