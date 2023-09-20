import java.io.*;
import java.util.*;

public class PlagiarismChecker {
    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.out.println("Usage: java PlagiarismChecker <orig> <plagiarized> <output>");
            return;
        }

        String origPath = args[0];
        String plagiarizedPath = args[1];
        String outputPath = args[2];

        Map<String, Integer> origWordCount = getCharacterCount(origPath);
        Map<String, Integer> plagiarizedWordCount = getCharacterCount(plagiarizedPath);

        double similarity = cosineSimilarity(origWordCount, plagiarizedWordCount);

        writeToFile(similarity, outputPath);
    }

    private static Map<String, Integer> getCharacterCount(String path) throws IOException {
        Map<String, Integer> characterCount = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.replaceAll("[^\\u4e00-\\u9fa5]", ""); // 去除非汉字字符
            for (int i = 0; i < line.length(); i++) {
                String character = line.substring(i, i + 1);
                characterCount.put(character, characterCount.getOrDefault(character, 0) + 1);
            }
        }
        reader.close();
        return characterCount;
    }

    private static double cosineSimilarity(Map<String, Integer> vectorA, Map<String, Integer> vectorB) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (String character : vectorA.keySet()) {
            dotProduct += vectorA.get(character) * vectorB.getOrDefault(character, 0);
            normA += Math.pow(vectorA.get(character), 2);
        }
        for (int count : vectorB.values()) {
            normB += Math.pow(count, 2);
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    private static void writeToFile(double similarity, String path) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        writer.write(String.format("%.2f", similarity));
        writer.close();
    }
}
