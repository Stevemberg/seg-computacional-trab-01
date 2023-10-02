import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Cracker {
    public static final Integer MAX_SEQUENCE_LENGTH = 3;
    public static final Integer MAX_KEY_LENGTH = 20;

    private String language;

    private String candidatePlainText;

    private String candidateKey;

    private ArrayList<HashMap<String, Double>> letterFrequencies;

    public Cracker() {
        setLanguage("pt");
        setCandidatePlainText("");
        setLetterFrequencies(new ArrayList<HashMap<String, Double>>());
        setCandidateKey("__");
    }

    public ArrayList<Integer> calculateDividers(Integer num) {
        ArrayList<Integer> result = new ArrayList<>();

        for (int i = 2; (i <= num / 2) && (i <= MAX_KEY_LENGTH); i++) {
            if (num % i == 0)
                result.add(i);
        }
        if (num <= MAX_KEY_LENGTH)
            result.add(num);
        return result;
    }

    public HashMap<String, Integer> getFrequencies(String cypherText, Integer quantity) {
        HashMap<String, Integer> result = new HashMap<>();

        for (int searchPosition = 0; (result.size() < quantity) && (searchPosition + MAX_SEQUENCE_LENGTH < cypherText.length()); searchPosition++) {
            String sequence = cypherText.substring(searchPosition, searchPosition + MAX_SEQUENCE_LENGTH);
            int foundPosition = cypherText.indexOf(sequence, searchPosition + MAX_SEQUENCE_LENGTH);
            int distance = foundPosition - searchPosition;
            ArrayList<Integer> dividers = calculateDividers(distance);

            if (foundPosition != -1 && result.get(sequence) == null && dividers.size() > 0)
                result.put(sequence, distance);

        }
        return result;
    }

    public String drawLangFreqChart() {
        if (!getLanguage().equals("en") || getLanguage().trim().isEmpty())
            setLanguage("pt");

        HashMap<String, Double> csvFile = FileManipulation.readCSV("freq-" + getLanguage() + ".csv");

        return drawFreqChart(csvFile);
    }

    public String drawFreqChart(HashMap<String, Double> alphabetFreq) {
        ArrayList<Double> data = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        for (Map.Entry<String, Double> entry : alphabetFreq.entrySet()) {
            data.add(entry.getValue());
            labels.add(entry.getKey().toString());
        }

        return drawFreqChart(data, labels);
    }

    public String drawFreqChart(ArrayList<Double> data, ArrayList<String> labels) {
        return Charts.drawChart(data, labels);
    }

    public void calculateFrequencies(String cipherText) {
        Double estimateNumberKeyRepetitions = Double.valueOf(cipherText.length() / getCandidateKey().length());
        initializeLetterFrequencies();

        for (int i = 0; i < cipherText.length(); i++) {
            String letter = cipherText.substring(i, i + 1);
            int arrayPosition = i % getCandidateKey().length();
            Double quantity = getLetterFrequencies().get(arrayPosition).get(letter);
            quantity++;
            getLetterFrequencies().get(arrayPosition).replace(letter, quantity);
        }

        for (HashMap<String, Double> hashMap : getLetterFrequencies()) {
            for (String key : hashMap.keySet()) {
                Double value = hashMap.get(key);
                hashMap.replace(key, (value / estimateNumberKeyRepetitions) * 100);
            }
        }

    }

    private void initializeLetterFrequencies() {
        HashMap<String, Double> hashMap = initializeHMWithLetters();
        for (int index = 0; index < getCandidateKey().length(); index++) {
            getLetterFrequencies().add(new HashMap<String, Double>(hashMap));
        }
    }

    private HashMap<String, Double> initializeHMWithLetters() {
        HashMap<String, Double> result = new HashMap<>();
        for (int i = 65; i < 91; i++) {
            result.put(Character.toString(i), 0.0);
        }
        return result;
    }

    public String drawShiftChart(int shiftQuant, HashMap<String, Double> alphabetFreq) {
        ArrayList<Double> data = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        for (Map.Entry<String, Double> entry : alphabetFreq.entrySet()) {
            data.add(entry.getValue());
            labels.add(entry.getKey().toString());
        }
        Double lastValue;
        String lastKey;

        for (int i = 0; i < shiftQuant; i++) {
            lastValue = data.get(0);
            lastKey = labels.get(0);

            data.remove(0);
            labels.remove(0);

            data.add(lastValue);
            labels.add(lastKey);
        }

        return drawFreqChart(data, labels);

    }

    public void setCandidateKey(Integer index, Integer shift) {
        String candidateKey = "";
        for (int i = 0; i < getCandidateKey().length(); i++) {
            if (i == index){
                candidateKey = candidateKey + ((char)(shift + 65));
            }
            else
                candidateKey = candidateKey + getCandidateKey().charAt(i);
        }
        setCandidateKey(candidateKey);
    }

    public void initBlankKey(int n) {
        String result = "";
        for (int i = 0; i < n; i++) {
            result = result + "_";
        }
        setCandidateKey(result);
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language.trim().toLowerCase();
    }

    public String getCandidatePlainText() {
        return candidatePlainText;
    }

    public void setCandidatePlainText(String candidatePlainText) {
        this.candidatePlainText = candidatePlainText;
    }

    public ArrayList<HashMap<String, Double>> getLetterFrequencies() {
        return letterFrequencies;
    }

    public void setLetterFrequencies(ArrayList<HashMap<String, Double>> letterFrequencies) {
        this.letterFrequencies = letterFrequencies;
    }

    public String getCandidateKey() {
        return candidateKey;
    }

    public void setCandidateKey(String candidateKey) {
        this.candidateKey = candidateKey;
    }
}