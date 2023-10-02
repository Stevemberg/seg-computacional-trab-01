import java.util.ArrayList;

public class Charts {
    private static final int SCATTERING_RATIO = 5;

    public static String drawChart(ArrayList<Double> data, ArrayList<String> labels) {
        StringBuilder dataStream = new StringBuilder("");

        for (Double entry : data) {
            for (int i = 0; i < SCATTERING_RATIO; i++) {
                dataStream.append(entry + " ");
            }
        }
        FileManipulation.writeFile("data.txt", dataStream.toString());
        String chart = ShellWrapper.runScript(null);

        StringBuilder labelsLine = new StringBuilder("");
        String padding = "";

        for (int i = 0; i < (int) SCATTERING_RATIO / 2; i++)
            padding = padding + " ";

        for (String label : labels) {
            labelsLine.append(padding + label + padding);
        }

        return chart + labelsLine.toString();
    }
}
