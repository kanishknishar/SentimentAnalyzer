import java.util.Arrays;

public class SentimentAnalyzer {
    public static int[] detectProsAndCons(String review, String[][] featureSet, String[] posOpinionWords,
                                          String[] negOpinionWords) {
        int[] answer = new int[featureSet.length];
        int position = 0;
        review = review.toLowerCase();

        featureSet: for (String[] featuresArray : featureSet) {
            for (String feature : featuresArray) {
                if (review.contains(feature)) {
                    answer[position++] = getOpinionOnFeature(review, feature, posOpinionWords, negOpinionWords);
                    continue featureSet;
                }
            }
            answer[position++] = 0;
        }

        return answer;
    }

    private static int getOpinionOnFeature(String review, String feature, String[] posOpinionWords, String[] negOpinionWords) {
        int opinion = checkForWasPhrasePattern(review, feature, posOpinionWords, negOpinionWords);

        if (opinion == 0) {
            opinion = checkForOpinionFirstPattern(review, feature, posOpinionWords, negOpinionWords);
        }

        return opinion;
    }

    public static int checkForWasPhrasePattern(String review, String feature, String[] posOpinionWords, String[] negOpinionWords) {
        String[] sentences = review.split("[!\\.]\\s?");
        String pattern = feature + " was ";
        for (String sentence : sentences) {
            if (sentence.contains(pattern)) {
                for (String posOpinionWord : posOpinionWords) {
                    if (sentence.contains(posOpinionWord)) {
                        return 1;
                    }
                }

                for (String negOpinionWord : negOpinionWords) {
                    if (sentence.contains(negOpinionWord)) {
                        return -1;
                    }
                }
            }
        }

        return 0;
    }

    private static int checkForOpinionFirstPattern(String review, String feature, String[] posOpinionWords,
                                                   String[] negOpinionWords) {
        String[] sentences = review.split("[!\\.]\\s?");
        int opinion = 0;
        String pattern;

        for (String sentence : sentences) {
            for (String posOpinionWord : posOpinionWords) {
                pattern = posOpinionWord + " " + feature;
                if (sentence.contains(pattern)) {
                    return 1;
                }
            }

            for (String negOpinionWord : negOpinionWords) {
                pattern = negOpinionWord + feature;
                if (sentence.contains(pattern)) {
                    return -1;
                }
            }
        }

        return 0;
    }

    public static void main(String[] args) {
        String review = "Haven't been here in years! Fantastic service and the food was delicious! Definetly will be a frequent flyer! Francisco was very attentive";

        String[][] featureSet = {
                { "ambiance", "ambience", "atmosphere", "decor" },
                { "dessert", "ice cream", "desert" },
                { "food" },
                { "soup" },
                { "service", "management", "waiter", "waitress", "bartender", "staff", "server" } };
        String[] posOpinionWords = { "good", "fantastic", "friendly", "great", "excellent", "amazing", "awesome",
                "delicious" };
        String[] negOpinionWords = { "slow", "bad", "horrible", "awful", "unprofessional", "poor" };

        int[] featureOpinions = detectProsAndCons(review, featureSet, posOpinionWords, negOpinionWords);
        System.out.println("Opinions on Features: " + Arrays.toString(featureOpinions));
    }
}