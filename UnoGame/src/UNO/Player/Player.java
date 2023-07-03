package UNO.Player;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import UNO.UnoCard;

public class Player { // implements Runnable

    private String name;
    private Boolean isAlive;
    private ArrayList<UnoCard> handCards = new ArrayList<UnoCard>();


    public Player(String name, ArrayList<UnoCard> handCards) throws Exception {
        this.name = name;
        this.isAlive = true;
        try{
            if (handCards.size() > 7) {
                throw new Exception("The player can't have more than 7 cards");
            }

            this.handCards = handCards;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
    }
    
    public String getName() {
        return name;
    }

    public Boolean getIsAlive() {
        return isAlive;
    }

    public ArrayList<UnoCard> getHandCards() {
        return handCards;
    }

    public void printHandCards() {
        System.out.println("Card # » Description/Type (Color, Value) ");
        handCards.forEach(      (k) -> {
            System.out.println(handCards.indexOf(k) + " » " + k);
        });
        
    }

    public UnoCard[] playCard(UnoCard cardOnGame) {
        printHandCards();
        System.out.println(
                "Choose the number of the card to play \n (if more than one, please use the numbers separated by commas): ");
        
        /* 
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        UnoCard[] cardToPlay = Arrays.stream(in.readLine().split(","))
                                                .mapToInt(Integer::parseInt)
                                                .forEach(handCards::get);
        */


        Scanner in = new Scanner(System.in);

        UnoCard cardToPlay[] = new UnoCard[handCards.size()];
        
        int i = 0;
        while (in.hasNext()) {

            if (in.hasNextInt())
                cardToPlay[i++] = handCards.get(in.nextInt());
            else
                in.next();
        }
        
        in.close();
        
        return cardToPlay;
    }


    public void drawCard(UnoCard card) {
        
        handCards.add(card);
    }

    public void sortHandCards(){

        // Based on: https://www.bezkoder.com/java-sort-arraylist-of-objects/#Sort_ArrayList_of_Objects_by_field_in_Java 

        Comparator<UnoCard> unoCardComparator = Comparator.comparing(UnoCard::getColor)
                                                          .thenComparing(UnoCard::getValue);

        
        ArrayList<UnoCard> sortedCards = (ArrayList<UnoCard>) handCards
				.stream().sorted(unoCardComparator)
                .collect(Collectors.toList());
        
        this.handCards = sortedCards;
    }


    private enum PlayerMenu {
        SHOW_HAND_CARDS,
        SHOW_PLAYABLE_CARDS,


    }





    //method drawcard from deck

    //Show available cards

    //Show possible cards to play  given the card in game

    //method playCard
    // Should include the message "uno"
    
}
