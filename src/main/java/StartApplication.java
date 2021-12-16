import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

//разбиваем по дням в формате
//2021-11-12_android-access-error.tail.log
//где android-access-error.tail.log имя исходного файла
public class StartApplication {
    public static void main(String[] args) throws IOException {
        List<String> readFile = readFile();
        List<List<String>>  resultsTestMethod = testMthod(readFile);
        for (int i = 0; i < resultsTestMethod.size(); i++) {
            createLogFile(resultsTestMethod.get(i), i);
        }
    }

    public static List<List<String>> testMthod(List<String> list) {
        List<List<String>> resultList = new ArrayList<>();
        List<String> currentDateLogList = new ArrayList<>();
        LocalDate previousLogDate = null;
        // LocalDate currentLogDate = getDate(logString);
        for (String logString : list) {
            if (previousLogDate == null) {
                previousLogDate = getDate(logString);
                currentDateLogList.add(logString);
            } else if (previousLogDate.equals(getDate(logString))) {
                currentDateLogList.add(logString);
            } else {
                previousLogDate = getDate(logString);
                resultList.add(currentDateLogList);
                currentDateLogList = new ArrayList<>();
                currentDateLogList.add(logString);
            }
        }
        resultList.add(currentDateLogList);
        return resultList;
    }

    public static LocalDate getDate(String string) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);
        String regex = "^([\\d.]+) (\\S+) (\\S+) \\[([\\w:/]+\\s[+-]\\d{4})\\] \"(.+?)\" (\\d{3}) (\\d+) \"([^\"]+)\" ";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);
        LocalDate date = null;
        if (matcher.find()) {
            LocalDateTime dt = LocalDateTime.parse(matcher.group(4), dateTimeFormatter);
            date = dt.toLocalDate();
        }
        return date;

    }
    public static void createLogFile(List<String> bigList, int postfix) throws IOException {
        File file = new File("android-access-error.tail"+"-" + postfix + ".log");
        OutputStream os = new FileOutputStream(file);
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(os, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (String propertiesOb : bigList) {
            //sb.append(getCountryCode(propertiesOb));
            sb.append(propertiesOb);
            sb.append("\n");
        }
        writer.write(sb.toString());
        writer.close();
        os.close();
    }
    //getCountryCode(matcher.group(1)));
    public static String getCountryCode (String ipAddress) throws IOException {
        var ipApiKey = "1fIDT0dFDR4FYIu"; // var ip = "217.138.198.94";
        var url = "https://pro.ip-api.com/csv/" + ipAddress + "?fields=countryCode&key=" + ipApiKey;
        String countryCode = new Scanner(new URL(url).openStream(), StandardCharsets.UTF_8).next();
        Pattern pattern = Pattern.compile("^([\\d.]+) (\\S+) (\\S+) \\[([\\w:/]+\\s[+-]\\d{4})\\] \"(.+?)\" (\\d{3}) (\\d+) \"([^\"]+)\" ");
        Matcher matcher =  pattern.matcher(ipAddress);
        if(matcher.find()) {
            countryCode = matcher.group(1);
        }
        return countryCode;
    }

    public static List<String> readFile() throws FileNotFoundException {
        File filePath = new File("C:\\Users\\Royal computers\\IdeaProjects\\parseLog\\src\\main\\resources\\android-access-error.tail.log");
        BufferedReader reader = new BufferedReader
                (new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8));
        List<String> fileName = reader.lines().collect(Collectors.toList());
        return fileName;
    }

//    public static int forNamingFile() throws FileNotFoundException {
//        int date = 0;
//        List<List<String>> desktopFile = testMthod(readFile());
//        desktopFile.forEach(list -> list.forEach(s -> {
//            LocalDate localDate = getDate(s);
//
//        }));
//
//        date = lo.getDayOfMonth();
//        return date;
//    }

}

