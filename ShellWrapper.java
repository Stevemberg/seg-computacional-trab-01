import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ShellWrapper {
    public static String runScript(String[] args) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        String result = "";

        // -- Linux --

        // Run a shell command
        if (args != null && args.length > 0) {
            processBuilder.command("bash", "-c", args[0]);
        } else {
            processBuilder.command("./script.sh");
        }

        // Run a shell script

        // -- Windows --

        // Run a command
        // processBuilder.command("cmd.exe", "/c", "dir C:\\Users\\mkyong");

        // Run a bat file
        // processBuilder.command("C:\\Users\\mkyong\\hello.bat");

        try {

            Process process = processBuilder.start();

            StringBuilder output = new StringBuilder();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }

            int exitVal = process.waitFor();
            if (exitVal == 0) {
                result = output.toString();
                // System.out.println("Success!");
                // System.out.println(output);
                // System.exit(0);
            } else {
                // abnormal...
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        return result;
    }

    public static void clear() {
        String result = ShellWrapper.runScript(new String[] { "clear" });
        System.out.print(result.substring(0, result.length()-1));
    }
}
