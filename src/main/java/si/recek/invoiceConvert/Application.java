package si.recek.invoiceConvert;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import si.recek.invoiceConvert.monthlystatement.StatementMaker;
import si.recek.invoiceConverter.synchronization.InputEntry;
import si.recek.invoiceConverter.synchronization.InvoiceRepository;
import si.recek.invoiceConverter.synchronization.InvoiceSynchronizer;


public class Application {
    public static final String workingDir = Paths.get(System.getProperty("user.dir")).resolve("Izhod").toString();

    public static void main(String[] args) {
        Application app = new Application();
        System.out.println("to je š , to je è. in to je ž");
        System.out.println("File encoding is " + System.getProperty("file.encoding") + " and should be windows-1250");
        app.convert();
    }

    private void convert() {
        getInvoiceStructure();
        InvoiceRepository repository = InvoiceRepository.getInstance();
        StatementMaker sm = new StatementMaker();
        sm.createStatement(repository.getAllInvoices());

    }


    private void getInvoiceStructure() {
        Path destinationFolder = unzip();
        File entryFile = null;

        for(File f : destinationFolder.toFile().listFiles()){
            if(f.getName().toLowerCase().contains("POSTAVKE".toLowerCase())){
                entryFile = f;
            }
        }
        if(entryFile != null ){
            readEntryFileAndUpdateDB(entryFile);
        }


        for(File f : destinationFolder.toFile().listFiles()){
            f.delete();
        }
    }

    public Path unzip() {
        Path destinationFolder = Paths.get(workingDir);

        try {
            ZipFile zipFile = new ZipFile(new File(getInputZipPath()));
            zipFile.extractAll(destinationFolder.toString());
        } catch (ZipException e) {
            e.printStackTrace();
        }
        return destinationFolder;
    }

    private String getInputZipPath() {
        String zipPath = null;
        for(File f : Paths.get(System.getProperty("user.dir")).resolve("Vhod").toFile().listFiles()){
            if(f.getName().toLowerCase().contains("zip".toLowerCase())){
                zipPath = f.getAbsolutePath();
            }
        }
        return zipPath;
    }

    private void readEntryFileAndUpdateDB(File entryFile) {
        String cvsSplitBy = ";";
        convertToUTF(entryFile);
        List<String> lines = new ArrayList<>();
        List<InputEntry> entries = new ArrayList<InputEntry>();
        try {
            lines = Files.readAllLines(entryFile.toPath(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        lines.remove(0);
        lines.remove(lines.size()-1);
        for(String line : lines){
            String[] entryFromFile = line.split(cvsSplitBy);
            InputEntry row = new InputEntry();
            row.setInvoiceID(entryFromFile[5]);
            row.setDateFromString(entryFromFile[1]);
            row.setTimeFromString(entryFromFile[2]);
            row.setProductNotation(entryFromFile[8]);
            row.setAmount(Double.valueOf(entryFromFile[9].replace(",", ".")).intValue());
            row.setTotal(Double.parseDouble(entryFromFile[12].replace(",", ".")));
            entries.add(row);
        }
        InvoiceSynchronizer synchronizer = new InvoiceSynchronizer();
        synchronizer.synchronizeEntries(entries);
    }

    private void convertToUTF(File entryFile) {
        Path p = entryFile.toPath();
        ByteBuffer bb = null;
        try {
            bb = ByteBuffer.wrap(Files.readAllBytes(p));
        } catch (IOException e) {
            e.printStackTrace();
        }
        CharBuffer cb = Charset.forName("windows-1252").decode(bb);
        bb = Charset.forName("UTF-8").encode(cb);
        try {
            Files.write(p, bb.array());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

