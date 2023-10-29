/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uno;

import java.util.ArrayList;
import java.util.Random;
import javax.swing.ImageIcon;

/**
 *
 * @author sukhdevsingh
 */
public class UnoDeck 
{
    private UnoCard[] cards;
    private int cardsInDeck;
    
    public UnoDeck()
    {
        cards = new UnoCard[108];
        
        UnoCard.Colour[] colours = UnoCard.Colour.values();
        cardsInDeck = 0;
        
        for(int i = 0; i < colours.length-1; i++)
        {
            UnoCard.Colour colour = colours[i];
            
            cards[cardsInDeck++] = new UnoCard(colour, UnoCard.Value.getValue(0));
            
            for(int j = i; j < 10; j++)
            {
                cards[cardsInDeck++] = new UnoCard(colour, UnoCard.Value.getValue(j));
                cards[cardsInDeck++] = new UnoCard(colour, UnoCard.Value.getValue(j));
            }
            
            UnoCard.Value[] values = new UnoCard.Value[] {UnoCard.Value.DrawTwo, UnoCard.Value.Skip,UnoCard.Value.Reverse};
            for(UnoCard.Value value : values)
            {
                cards[cardsInDeck++] = new UnoCard(colour, value);
                cards[cardsInDeck++] = new UnoCard(colour, value);
            }
        }
        
            UnoCard.Value[] values = new UnoCard.Value[] {UnoCard.Value.Wild, UnoCard.Value.Wild_Four};
            for (UnoCard.Value value :values)
            {
                    for(int i = 0; i < 4; i++)
                    {
                        cards[cardsInDeck++] = new UnoCard(UnoCard.Colour.Wild, value);
                    }
            }
    }
    
            public void replaceDeckWith(ArrayList<UnoCard> cards) {
                this.cards = cards.toArray(new UnoCard[cards.size()]);
                this.cardsInDeck = this.cards.length;
            }
            
            public boolean isEmpty(){
                return cardsInDeck ==0;
            }
            
            public void shuffle() {
                int n = cards.length;
                Random random = new Random();
                
                for (int i = 0; i < cards.length; i++) {
                    
                    int randomValue = i + random.nextInt(n - i);
                    UnoCard randomCard = cards[randomValue];
                    cards[randomValue] = cards[i];
                    cards[i] = randomCard;
                }
            }
            
            public UnoCard drawCard() throws IllegalArgumentException {
                if (isEmpty()) {
                    throw new IllegalArgumentException("Cannot draw a card since there is no cards in the Deck");
            }
            return cards[--cardsInDeck];
        }
 
            public ImageIcon drawCardImage() throws IllegalArgumentException {
                if(isEmpty()) {
                    throw new IllegalArgumentException("Cannot draw a card since the deck is empty");
            }
                return new ImageIcon(cards[--cardsInDeck].toString() + ".png");
        }
            
            public UnoCard[] drawCard(int n) {
                if (n < 0){
                    throw new IllegalArgumentException("Must draw positive cards but tried to draw " + n + " cards");
                }
                
                if (n > cardsInDeck) {
                    throw new IllegalArgumentException("Cannt draw " + n + " cards since there are only " + cardsInDeck);
                }
                
                UnoCard[] ret = new UnoCard[n];
                
                for (int i = 0; i < n; i++) {
                    ret[i] = cards[--cardsInDeck];
                }
                return ret;
            }
}
