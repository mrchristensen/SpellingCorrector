package spell;

import javax.swing.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Trie implements ITrie {
    Node rootNode = new Node();

    @Override
    public void add(String myWord) {
        char[] myLetters = myWord.toCharArray();
        int index = 0;
        populateTrie(rootNode, myLetters, index);
    }

    @Override
    public INode find(String myWord) {
        char[] myLetters = myWord.toCharArray();
        Node myNode;
        //If the word is in the dictionary
        myNode = findWord(rootNode, myLetters, 0);
        if(myNode != null){
            return myNode;
        }

        //If the word isn't in the dictionary try to find a similar word
        Set<String> candidateWords = new WordTransforms().generateSuggestions(myWord);

        System.out.println("Candidate words: " + candidateWords);

        //Check to see if the candidate words are valid (in the dictionary)
        //Find potential words (see how many of the generated words are found in the dictionary)
        Set<String> potentialWords = new HashSet<String>();
        potentialWords.addAll(findPotentialWords(candidateWords));

        System.out.println("Potential words: " + potentialWords);

        //If none of the generated words is in the dictionary then run the transforms on the potential words to get a distance of two
        if(potentialWords.size() == 0){
            System.out.println("Need to look for candidate words with a distance of two.");
            Set<String> oldCandidateWords = new HashSet<String>();
            oldCandidateWords.addAll(candidateWords);
            candidateWords.clear();

            for (String word : oldCandidateWords) {
                candidateWords.addAll(new WordTransforms().generateSuggestions(word));
            }
            System.out.println("New candidate words (dis=2): " + candidateWords);

            potentialWords.addAll(findPotentialWords(candidateWords));
            System.out.println("New potential words: " + potentialWords);

            if (potentialWords.size() == 0){
                return null; //There are no words with dist of 2 that are valid suggestions
            }
        }
        if(potentialWords.size() == 1){
            return findWord(rootNode, potentialWords.iterator().next().toCharArray(), 0);  //TODO:potentialWords.iterator().next().toCharArray() is SOOOOOOO bad
        }
        else{ //If there are more than one potential word
            int frequencyRecord = 0;
            Set<String> mostFrequentWords = new HashSet<String>();

            for (String word : potentialWords) {
                int frequency = findWord(rootNode, word.toCharArray(), 0).getValue();
                if(frequency > frequencyRecord){
                    frequencyRecord = frequency;
                    mostFrequentWords.clear();
                    mostFrequentWords.add(word);
                }
                else if(frequency == frequencyRecord){
                    mostFrequentWords.add(word);
                }
            }

            System.out.println("Most frequent words: " + mostFrequentWords);

            if(mostFrequentWords.size() == 0){
                return null; //There is no suggestion
            }
            else if(mostFrequentWords.size() == 1){
                return findWord(rootNode, mostFrequentWords.iterator().next().toCharArray(), 0);
            }
            else{ //There are most than two words with equal frequency
                return findWord(rootNode, mostFrequentWords.iterator().next().toCharArray(), 0); //TODO: Get this to be alphabetical
            }
        }
    }

    private Set<String> findPotentialWords(Set<String> candidateWordSet) {
        Set<String> potentialWordSet = new HashSet<String>();

        for (String word : candidateWordSet) {
            Node tempNode = findWord(rootNode, word.toCharArray(), 0);
            if(tempNode != null && tempNode.getValue() > 0){ //TODO: Clean up the args of findWord()
                potentialWordSet.add(word);
            }
        }

        return potentialWordSet;
    }

    @Override
    public int getWordCount() {
        return countWords(rootNode);
    }

    @Override
    public int getNodeCount() {
        return countNodes(rootNode) + 1; //Plus one to count the root node
    }

    public void populateTrie(Node myNode, char[] myWord, int letterIndex) {
        int letterInt = charToInt(myWord[letterIndex]); //Convert the char to an int
        if (myNode.children[letterInt] == null) { //If the node doesn't exist, generate it
            //System.out.print("(" + myWord[letterIndex] + ")");
            myNode.children[letterInt] = new Node(myWord[letterIndex]); //Give it the current char
        }
        else{
            //System.out.print(myWord[letterIndex]);
        }
        letterIndex++;

        if (letterIndex < myWord.length) { //If we're not to the end of a word yet
            populateTrie(myNode.children[letterInt], myWord, letterIndex); //Recursive call
        } else { //We've reached the end of the word
            myNode.children[letterInt].increaseCount(); //Increase the count
            //System.out.printf("\n%d time(s)\n - \n", myNode.children[letterInt].getValue());
        }

    }

    private Node findWord(Node myNode, char[] myWord, int letterIndex){ //TODO: Fix this so that there is no letterIndex, just pass a substring of a string (and cut off the first letter
        int letterInt = charToInt(myWord[letterIndex]);
        Node finalNode;
        if(myNode.children[letterInt] == null){
            return null;
        }
        letterIndex++;

        if(letterIndex < myWord.length) { //If we're not to the end of the word yet
            finalNode = findWord(myNode.children[letterInt], myWord, letterIndex);
        }
        else{ //We've successfully found the end of the word
            return myNode.children[letterInt];
        }

        return finalNode;
    }

    private int charToInt(char myLetter) {
        return myLetter - 'a';
    }

    private int countWords(Node myNode){
        int wordCount = 0;

        for (int i = 0; i < 26; i++) {
            if(myNode.children[i] != null){
                wordCount += countWords(myNode.children[i]);
                if (myNode.children[i].getValue() > 0){
                    wordCount++;
                }
            }
        }

        return wordCount;
    }

    private int countNodes(Node myNode){
        int nodeCount = 0;

        for (int i = 0; i < 26; i++) {
            if(myNode.children[i] != null){
                nodeCount += countNodes(myNode.children[i]);
                nodeCount++;
                }
            }
        return nodeCount;
    }


}