package spell;

import java.util.HashSet;
import java.util.Set;

public class WordTransforms {
    public Set<String> generateSuggestions(String myWord){
        Set<String> candidateWords = new HashSet<String>();

        candidateWords.addAll(deletionDistance(myWord));
        candidateWords.addAll(transpositionDistance(myWord));
        candidateWords.addAll(alterationDistance(myWord));
        candidateWords.addAll(insertionDistance(myWord));
        return candidateWords;
    }

    private Set<String> deletionDistance(String myWord){
        Set<String> newCandidates = new HashSet<String>();
        for (int i = 0; i < myWord.length(); i++) {
            String newWord = myWord.substring(0, i) + myWord.substring(i + 1, myWord.length());
            //%%%System.out.println(newWord);
            newCandidates.add(newWord);
        }

        return newCandidates;
    }

    private Set<String> transpositionDistance(String myWord){
        Set<String> newCandidates = new HashSet<String>();
        for (int i = 0; i < myWord.length() - 1; i++) {
            char[] newWord = myWord.toCharArray();
            char temp = newWord[i];
            newWord[i] = newWord[i+1];
            newWord[i+1] = temp;
            System.out.println(newWord);
            newCandidates.add(String.valueOf(newWord));
        }

        return newCandidates;
    }

    private Set<String> alterationDistance(String myWord){
        Set<String> newCandidates = new HashSet<String>();
        for (int i = 0; i < myWord.length(); i++) {
            for (int j = 0; j < 26; j++) {
                char[] newWord = myWord.toCharArray();
                newWord[i] = (char) (j + 'a');
                newCandidates.add(String.valueOf(newWord));
            }
        }
        return newCandidates;
    }

    private Set<String> insertionDistance(String myWord){
        Set<String> newCandidates = new HashSet<String>();

        for (int i = 0; i < myWord.length() + 1; i++) {
            for (int j = 0; j < 26; j++) {
                String newWord = myWord.substring(0,i) + (char) (j + 'a') + myWord.substring(i, myWord.length());
                newCandidates.add(newWord);
            }
        }
        return newCandidates;
    }
}
