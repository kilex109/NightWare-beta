package nightware.main;

import lombok.Setter;
import nightware.main.utility.misc.CryptorUtility;
import org.json.JSONException;
import org.json.JSONObject;
import ru.crashdami.internalprotection.nativeobfuscator.Native;
import ru.crashdami.internalprotection.nativeobfuscator.NotNative;
import lombok.Getter;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Native
public class AvtorizaciyaHAHA {
    @Getter @Setter
    public static String hwid;
    @Getter
    private static AvtorizaciyaHAHA instance;
    @Getter @Setter
    private static boolean NBwh = false;

    @Native
    public static void setInstance(AvtorizaciyaHAHA instance) {
        AvtorizaciyaHAHA.instance = instance;
    }
    static {
        try {
            AvtorizaciyaHAHA.hwid = generateHwid();
        } catch (IOException e) {
            System.out.println(e);
            //System.out.println("?");
        }
    }

    @Native
    public static void Server() throws IOException {
        try {
            URL url = new URL(CryptorUtility.decrypt("3L/qi7EhonVEXOCXlAWu9cPR1LVUBYCL3rIN2TKpuMf3FHh+mcKGCUkTRQUttBqf/sob1VcGndFPBLUofwLpQssqTu3AXvYnZX/jouGBmm4LPDuv01v6VXP2880P81Pr", "qweqweqweqwe1234") + hwid);
            //System.out.println(hwid);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.addRequestProperty("User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
            String domain = url.getHost();
            InetAddress addresses = InetAddress.getByName(domain);

            if (!addresses.getHostAddress().equalsIgnoreCase("127.0.0.1")) {
                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    if (!addresses.getHostAddress().equalsIgnoreCase("127.0.0.1")) {
                            JSONObject jsonResponse = new JSONObject(response.toString());
                            if (jsonResponse != null) {

                                String base64Key = jsonResponse.getString("key");
                                if (base64Key == null) {
                                    System.out.println("JVM Error: 0x99, 0xDE don't verify!");
                                    System.exit(-1);
                                }
                                String base64EncryptedResponse = jsonResponse.getString("data");
                                if (base64EncryptedResponse == null) {
                                    System.out.println("JVM Error: 0x99, 0xDE don't verify!");
                                    System.exit(-1);
                                }
                                String keysDec = CryptorUtility.decrypt("fN5yDE9XhyMTfVpwgzZRxM9RMGIQklv2Bo+yevIlN8ktvjoPEH2vxGiKzighoEic", "wadswadasdawdasd");
                                String dec = decrypt(base64Key, keysDec);
                                String decryptedResponse = decrypt(base64EncryptedResponse, dec);
                                //  System.out.println(decryptedResponse);

                                if(decryptedResponse.contains(":")) {
                                    System.out.println("[InternalProtect] Auth Succeffull");
                                } else if (decryptedResponse.equalsIgnoreCase("fuck")) {
                                    System.out.println("[InternalProtect] HWID Not found! Your HWID: " + hwid);
                                    for(; ;);
                                } else {
                                    System.out.println("Suck fat cock cracker, unluck");
                                    for(; ;);
                                }
                               // System.out.println(decryptedResponse);
                                if (decryptedResponse == null) {
                                    System.out.println("JVM Error: 0x99, 0xDE don't verify!");
                                    System.exit(-1);
                                }
                                if (!addresses.getHostAddress().equalsIgnoreCase("127.0.0.1")) {

                                    if (decryptedResponse.contains(":")) {
                                        String[] parts = decryptedResponse.split(":");
                                        if (parts[0].equalsIgnoreCase("null")) {
                                            System.exit(-1);
                                        }
                                        if (parts[4].equalsIgnoreCase("null")) {
                                            System.exit(-1);
                                        }
                                        NightWare.username = parts[0];
                                        if (NightWare.username.equalsIgnoreCase("null")) {
                                            System.exit(-1);
                                        }
                                        if (NightWare.username.equalsIgnoreCase("")) {
                                            System.out.println("JVM Error: 0x99, 0xDE don't verify!");
                                            System.exit(-1);
                                        }
                                        NightWare.uid = Integer.parseInt(parts[1]);
                                        NightWare.till = PodpiskoyUTILITA.setTill(parts[3]);
                                        NightWare.role = parts[4];
                                        NBwh = true;
                                    }
                                } else {

                                    for (; ;) ;
                                }
                            }
                            //}
                    }
                }
            }
        } catch (IOException e) {
            for (;;);
        } catch (JSONException e) {
            for (;;);
        } catch (NumberFormatException e) {
            for (;;);
        }
    }
    private static String decrypt(String encryptedData, String key) {
        StringBuilder result = new StringBuilder();
        int dataLength = encryptedData.length();
        int keyLength = key.length();

        for (int i = 0; i < dataLength; ++i) {
            result.append((char) (encryptedData.charAt(i) ^ key.charAt(i % keyLength)));
        }

        return result.toString();
    }

    private static String decoder(String input) {
        byte[] decodedBytes = Base64.getDecoder().decode(input);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }

    @Native
    private static String generateHwid()
            throws IOException {
        System.out.println("hwid");
        return DigestUtils.md5Hex(DigestUtils.md5Hex(
                System.getenv("os") +
                        System.getProperty("os.arch") +
                        System.getenv("HOMEDRIVE") +
                        System.getenv("PROCESSOR_ARCHITEW6432") +
                        System.getenv("PROCESSOR_LEVEL") +
                        System.getProperty("os.version") +
                        System.getProperty("os.name") +
                        System.getenv("PROCESSOR_REVISION") +
                        System.getenv("PROCESSOR_IDENTIFIER") +
                        System.getenv("PROCESSOR_ARCHITECTURE") +
                        System.getenv("COMPUTERNAME") +
                        System.getenv("user.name") +
                        System.getenv("PHYSICAL_MEMORY_SIZE")

        ));
    }
}