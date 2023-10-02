
public class Viginere {
    private String key = "";
    private String plainText = "";
    private String cipherText = "";
    private String deCipherText = "";

    public String getDeCipherText() {
        return deCipherText;
    }

    public void setDeCipherText(String deCipherText) {
        this.deCipherText = deCipherText;
    }

    public String getCipherText() {
        return cipherText;
    }

    public void setCipherText(String cipherText) {
        this.cipherText = cipherText;
    }

    public String getPlainText() {
        return plainText;
    }

    public void setPlainText(String plainText) {
        this.plainText = plainText;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = FileManipulation.removeSpecialCharacters(key);
    }

    public char cipher(char key, char letter) {
        return (char) (((int) letter + (int) key) % 26 + 65);
    }

    public char decipher(char key, char letter) {
        return (char) ((((int) letter - (int) key + 26) % 26) + 65);
    }

    public String viginereAlgorithm(boolean decipher) {
        StringBuilder result = new StringBuilder("");
        char c = ' ';
        String text = getPlainText();
        String methodName = "cipher";
        if (decipher){
            methodName = "decipher";
            text = getCipherText();
        }

        for (int i = 0; i < text.length(); i++) {
            try {
                c = Viginere.class.getMethod(methodName, char.class, char.class).invoke(this, key.charAt(i % key.length()), text.charAt(i)).toString().charAt(0);
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
            result.append(c);
        }
        return result.toString();
    }

    public void printAlphabet() {
        for (int i = 65; i < 91; i++) {
            char c = decipher('A', (char) i);
            System.out.printf("%d %c \n", (int) c, c);
        }
    }
}
