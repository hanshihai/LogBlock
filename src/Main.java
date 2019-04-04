import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Main {

    private boolean defaultOutput = true;

    private StringBuilder buffer = new StringBuilder();
    private String pattern = "^\\d{4}-\\d{2}-\\d{2}";
    private List<String> keyWords = new ArrayList<String>();
    private List<String> badWords = new ArrayList<String>();

    private String filePath;

    private Pattern p;

    public boolean isMatch(String line) {
        if(p == null) {
            p = Pattern.compile(pattern);
        }
        return p.matcher(line).find();
    }

    private void print() {
        System.out.println(buffer.toString());
    }

    private void cut(String line) {
            boolean started = isMatch(line);
            if(started) {
                if(isValid()) {
                    print();
                }
                buffer = new StringBuilder();
                buffer.append(line);
                buffer.append("\n");
            }else{
                if(line != null && line.trim().length() > 0) {
                    buffer.append(line);
                    buffer.append("\n");
                }
            }
    }

    private boolean isValid() {
        String text = buffer.toString();
        boolean hasKeyWord = keyWords.stream().anyMatch(key -> text.contains(key));
        if(hasKeyWord) {
            return true;
        }
        boolean hasBadWord = badWords.stream().anyMatch(key -> text.contains(key));
        if(hasBadWord) {
            return false;
        }
        return defaultOutput;
    }

    public void filter() {
        File file = new File(filePath);
        try(InputStream stream = new FileInputStream(file)){
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            while((line = reader.readLine()) != null) {
                cut(line);
            }
            if(isValid()) {
                print();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void usage() {
        System.out.println(" ---                  usage with parameters              ---");
        System.out.println(" -f: log file path, such as -f=/va/log/log.1.log");
        System.out.println(" -p: pattern, such as -p=^\\d{4}-\\d{2}-\\d{2}");
        System.out.println(" -k: key words for searching, such as -k=ERROR,WARNING");
        System.out.println(" -b: filtered out words, such as -b=CRON,METRIC");
        System.out.println(" -o: default output, such as -o=true");
        System.out.println(" ---                         End                         ---");
    }

    public Main parseParameters(String args[]) {
        for(String arg: args) {
            if(arg.startsWith("-f")) {
                filePath = arg.split("=",2)[1];
            }
            if(arg.startsWith("-p")) {
                p = Pattern.compile(arg.split("=",2)[1]);
            }
            if(arg.startsWith("-k")) {
                String keywords = arg.split("=",2)[1];
                String[] keyword = keywords.split(",");
                for(String k: keyword){
                    keyWords.add(k);
                }
            }
            if(arg.startsWith("-b")) {
                String badwords = arg.split("=",2)[1];
                String[] badword = badwords.split(",");
                for(String b: badword){
                    badWords.add(b);
                }
            }
            if(arg.startsWith("-o")) {
                defaultOutput = Boolean.valueOf(arg.split("=", 2)[1]);
            }
        }
        if(args == null || args.length < 2) {
            usage();
        }
        return this;
    }
    public static void main(String[] args) {
        Main main = new Main();
        main.parseParameters(args).filter();
    }
}
