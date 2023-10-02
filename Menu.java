import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Menu {
    private static final HashMap<String, String> GRID = new HashMap<>() {
        {
            put("V", "│");
            put("H", "─");
            put("M", "┼");
            put("E", "├");
            put("D", "┤");
            put("B", "┬");
            put("C", "┴");
            put("SE", "┌");
            put("SD", "┐");
            put("IE", "└");
            put("ID", "┘");
        }
    };

    Scanner scan;

    private Cracker cracker;

    private Viginere viginere;

    public Menu() {
        viginere = new Viginere();
        cracker = new Cracker();
        scan = new Scanner(System.in);
    }

    public static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    public static String padLeft(String s, int n) {
        return String.format("%" + n + "s", s);
    }

    public boolean isNumeric(String strNum) {
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }

    public ArrayList<Map.Entry<String, Integer>> sortHashMap(HashMap<String, Integer> sequenceHashMap) {
        ArrayList<Map.Entry<String, Integer>> result = new ArrayList<Map.Entry<String, Integer>>(sequenceHashMap.entrySet());
        result.sort((entry1, entry2) -> entry1.getValue() - entry2.getValue());
        return result;
    }

    public void printSequencesTable(Integer linesQuantity) {
        HashMap<String, Integer> sequenceHashMap = cracker.getFrequencies(viginere.getCipherText(), linesQuantity);
        Integer cellSize = 4;
        for (Map.Entry<String, Integer> item : sequenceHashMap.entrySet()) {
            cellSize = item.getValue().toString().length() > cellSize ? item.getValue().toString().length() : cellSize;
        }

        System.out.println("┌──────┬──────┬────┬────┬────┬────┬────┬────┬────┬────┬────┬────┬────┬────┬────┬────┬────┬────┬────┬────┬────┐");
        System.out.println("│ sequ │ dist │  2 │  3 │  4 │  5 │  6 │  7 │  8 │  9 │ 10 │ 11 │ 12 │ 13 │ 14 │ 15 │ 16 │ 17 │ 18 │ 19 │ 20 │");
        System.out.println("├──────┼──────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┤");
        for (Map.Entry<String, Integer> item : sequenceHashMap.entrySet()) {
            ArrayList<Integer> dividers = new Cracker().calculateDividers(item.getValue());
            System.out.println(tableLineString(item, cellSize, dividers));
        }
        System.out.println("└──────┴──────┴────┴────┴────┴────┴────┴────┴────┴────┴────┴────┴────┴────┴────┴────┴────┴────┴────┴────┴────┘");
    }

    private String tableLineString(Map.Entry<String, Integer> item, Integer cellSize, ArrayList<Integer> dividers) {
        StringBuilder result = new StringBuilder("");
        result.append(GRID.get("V") + " ");
        result.append(padLeft(item.getKey(), 4) + " ");
        result.append(GRID.get("V") + " ");
        result.append(padLeft(item.getValue().toString(), cellSize));
        result.append(" " + GRID.get("V"));

        for (int index = 0, divider = 2; divider <= Cracker.MAX_KEY_LENGTH; divider++) {

            if (dividers.size() > index && dividers.get(index) % divider == 0) {
                result.append("████" + GRID.get("V"));
                index++;
            } else {
                result.append("    " + GRID.get("V"));
            }
        }
        return result.toString();
    }

    private void menuInicial() {
        StringBuilder result = new StringBuilder("");
        result.append("Escolha uma opção:\n");
        result.append("1 - Encriptar\n");
        result.append("2 - Decriptar\n");
        result.append("3 - Criptoanálise\n");
        result.append("0 - Sair\n");
        Integer input = -1;

        while (input != 0) {
            ShellWrapper.clear();
            System.out.println(result);
            input = Integer.parseInt(scan.nextLine());

            switch (input) {
            case 1:
                menuEncriptar();
                break;
            case 2:
                menuDecriptar();
                break;
            case 3:
                menuCriptoAnalise();
                break;
            }

        }
        scan.close();
    }

    private void menuEncriptar() {
        System.out.println("\nCole o texto a ser encriptado em \"plaintext.txt\" antes de continuar.");
        scan.nextLine();
        String plainText = FileManipulation.readFile("plaintext.txt", true);
        String key;
        String cipherText;

        if (plainText.length() > 0)
            System.out.println("Texto lido com sucesso.\n");
        viginere.setPlainText(plainText);

        System.out.println("Digite a chave de encriptação (key.txt)");
        key = scan.nextLine().trim().toUpperCase();
        if (key.isBlank())
            key = FileManipulation.readFile("key.txt", true);
        viginere.setKey(key);

        cipherText = viginere.viginereAlgorithm(false);
        viginere.setCipherText(cipherText);
        FileManipulation.writeFile("ciphertext.txt", cipherText);

        System.out.println("\nChave:" + viginere.getKey());
        System.out.println("Texto cifrado (ciphertext.txt):\n" + viginere.getCipherText());
        scan.nextLine();
    }

    private void menuDecriptar() {
        String fileName;
        String cipherText;
        String deCipherText;
        boolean newKey = true;

        System.out.println("\nNome do arquivo que possui o texto cifrado. (ciphertext.txt)");
        fileName = scan.nextLine();
        if (fileName == null || fileName.isBlank())
            fileName = "ciphertext.txt";

        cipherText = FileManipulation.readFile(fileName);
        viginere.setCipherText(cipherText);

        if (viginere.getKey() != null && viginere.getKey().length() > 0) {
            System.out.println("\nDeseja usar a chave(" + viginere.getKey() + ") já cadastrada? (S/n)");
            String str = scan.nextLine().trim();
            if (str == null || str.equals("S") || str.isBlank())
                newKey = false;
        }
        if (newKey) {
            System.out.println("Digite a nova chave");
            String key = scan.nextLine();
            viginere.setKey(key);
        }

        deCipherText = viginere.viginereAlgorithm(true);
        viginere.setDeCipherText(deCipherText);
        FileManipulation.writeFile("deciphertext.txt", deCipherText);

        System.out.println("\nChave:" + viginere.getKey());
        System.out.println("Texto decifrado (deciphertext.txt):\n" + viginere.getDeCipherText());
        scan.nextLine();

    }

    private void menuCriptoAnalise() {
        StringBuilder stringMenu = new StringBuilder("");
        stringMenu.append("Escolha uma opção:\n");
        stringMenu.append("1 - Tabela de repetição\n");
        stringMenu.append("2 - Descoberta de chave\n");
        stringMenu.append("3 - Mostrar resultado\n");
        stringMenu.append("0 - Sair\n");
        Integer input = -1;

        while (input != 0) {
            ShellWrapper.clear();
            System.out.println(stringMenu);
            input = Integer.parseInt(scan.nextLine());

            switch (input) {
            case 1:
                menuTabelaDeRepeticao();
                break;
            case 2:
                menuDecobertaDeChave();
                break;
            case 3:
                viginere.setKey(cracker.getCandidateKey());
                System.out.println("Chave candidata:" + cracker.getCandidateKey());
                System.out.println("Texto decifrado:");
                System.out.println(viginere.viginereAlgorithm(true));
                scan.nextLine();
                break;
            }
        }

    }

    private void menuTabelaDeRepeticao() {
        Integer linesQuantity = 10;
        if (viginere.getCipherText() == null || viginere.getCipherText().isBlank()) {
            ShellWrapper.clear();
            System.out.println("Nenhum texto crifrado foi carregado.");
            System.out.println("Texto do arquivo ciphertext.txt considerado para utilização.");
            viginere.setCipherText(FileManipulation.readFile("ciphertext.txt"));
        }
        System.out.println("\nQuantas linhas da tabela devem ser elaboradas?  (10)");
        String nextLine = scan.nextLine().trim();
        if (!nextLine.isBlank())
            linesQuantity = Integer.parseInt(nextLine);

        printSequencesTable(linesQuantity);
        scan.nextLine();
    }

    private void menuDecobertaDeChave() {
        System.out.println("Qual o tamanho da chave?");
        cracker.initBlankKey(Integer.valueOf(scan.nextLine()));

        System.out.println("Deseja frequência de letras de qual língua? (PT/en)");
        String language = scan.nextLine();
        if (language != null && !language.isBlank())
            cracker.setLanguage(language);

        cracker.calculateFrequencies(viginere.getCipherText());
        cracker.setCandidatePlainText(viginere.getCipherText());

        String languageFreqChart = cracker.drawLangFreqChart();
        StringBuilder menuString = new StringBuilder("");
        menuString.append("Escolha uma opção:\n");
        menuString.append("1 - Deslocar p/ Esquerda     ");
        menuString.append("2 - Deslocar p/ Direita      ");
        menuString.append("Chave candidata:%s\n");
        menuString.append("3 - Letra antecessora        ");
        menuString.append("4 - Letra seguinte");
        menuString.append("\t\t                  %s\n");
        menuString.append("0 - Sair                     ");
        menuString.append("<letra> - Definir uma letra\n");
        menuString.append(languageFreqChart.toString() + "\n");
        String input = "";
        String candidateChart = "";
        String candidateKey = "";
        String arrowPosition = "";
        Integer option = -1;
        Integer keyIndex = 0;

        ArrayList<Integer> shifts = new ArrayList<Integer>();
        for (int i = 0; i < cracker.getCandidateKey().length(); i++)
            shifts.add(0);

        while (option != 0) {
            candidateChart = cracker.drawShiftChart(shifts.get(keyIndex), cracker.getLetterFrequencies().get(keyIndex));
            candidateKey = cracker.getCandidateKey();
            arrowPosition = drawArrowKeyPosition(keyIndex);
            ShellWrapper.clear();

            System.out.printf(menuString.toString(), candidateKey, arrowPosition);
            System.out.println(candidateChart);
            input = scan.nextLine().trim();
            if (isNumeric(input))
                option = Integer.valueOf(input);
            else
                option = -1;

            switch (option) {
            case 0:
                break;
            case 1:
                shifts.set(keyIndex, (shifts.get(keyIndex) + 1) % 26);
                break;
            case 2:
                shifts.set(keyIndex, (shifts.get(keyIndex) - 1 + 26) % 26);
                break;
            case 3:
                keyIndex = (keyIndex - 1 + cracker.getCandidateKey().length()) % cracker.getCandidateKey().length();
                break;
            case 4:
                keyIndex = (keyIndex + 1) % cracker.getCandidateKey().length();
                break;
            default:
                int letter = ((int) input.trim().toUpperCase().charAt(0)) - 65;
                shifts.set(keyIndex, letter);
                cracker.setCandidateKey(keyIndex, shifts.get(keyIndex));
                keyIndex = (keyIndex + 1) % cracker.getCandidateKey().length();
                break;
            }
            cracker.setCandidateKey(keyIndex, shifts.get(keyIndex));
        }
    }

    private String drawArrowKeyPosition(int index) {
        String result = "⬆";
        for (int i = 0; i < index; i++) {
            result = " " + result;
        }
        return result;
    }

    public void initMenu() {
        menuInicial();
    }

}
