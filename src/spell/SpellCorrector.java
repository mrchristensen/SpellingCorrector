package spell;

        import java.io.File;
        import java.io.IOException;
        import java.util.HashSet;
        import java.util.Scanner;
        import java.util.Set;

public class SpellCorrector implements ISpellCorrector {

    Trie dictionaryTree = new Trie();

    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        File myFile = new File(dictionaryFileName);
        Scanner scanner = new Scanner(myFile);

        while(scanner.hasNext()){
            //dictionaryWords.add(scanner.next());
            dictionaryTree.add(scanner.next());
        }
        System.out.println("-Just for fun-\nWord count: " + dictionaryTree.getWordCount());
        System.out.println("Node count: " + dictionaryTree.getNodeCount());
        //System.out.println("\nToString(): \n" + dictionaryTree.toString());
        System.out.println("-");
        //System.out.println(dictionaryWords);
    }

    @Override
    public String suggestSimilarWord(String inputWord) {
        INode myNode;

        //If the input word is in the dictionary return it
        myNode = dictionaryTree.find(inputWord);
        if(myNode != null){
            return inputWord;
        }

        //If the word isn't in the dictionary try to find a similar word
        Set<String> candidateWords = new WordTransforms().generateSuggestions(inputWord);
        System.out.println("Candidate words: " + candidateWords);
        Set<String> potentialWords = findPotentialWords(candidateWords); //Check to see if the candidate words are valid (in the dictionary)
        //potentialWords.addAll(findPotentialWords(candidateWords));
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
            return potentialWords.iterator().next();
        }
        else{ //If there are more than one potential word
            int frequencyRecord = 0;
            Set<String> mostFrequentWords = new HashSet<String>();

            for (String word : potentialWords) {
                int frequency = dictionaryTree.find(word).getValue();
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

            if(mostFrequentWords.size() == 1){
                return mostFrequentWords.iterator().next();
            }
            else{ //There are more than two words with equal frequency, pick the most alphabetical one
                return mostFrequentWords.iterator().next(); //TODO: Get this to be alphabetical
            }
        }

    }

    private Set<String> findPotentialWords(Set<String> candidateWordSet) {
        Set<String> potentialWordSet = new HashSet<String>();

        for (String word : candidateWordSet) {
            INode tempNode = dictionaryTree.find(word);
            if(tempNode != null && tempNode.getValue() > 0){
                potentialWordSet.add(word);
            }
        }

        return potentialWordSet;
    }

}
