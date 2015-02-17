package bs.utils.db;

import bs.utils.file.SplitByLine;

import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by shahb on 2/13/15.
 */
public class ThreadedDataLoader {
    public static String INSERT_SQL = ResourceBundle.getBundle("db").getString("insert.sql");

    private ExecutorService asyncDataLoaderService = null;
    private int threadCount;
    private String inputCSVFile;

    public ThreadedDataLoader(String inputCSVFile, int threadCount) {
        this.threadCount = threadCount;
        this.inputCSVFile = inputCSVFile;
    }

    public void execute() {
        asyncDataLoaderService = Executors.newFixedThreadPool(threadCount);
        SplitByLine lineSplitter = new SplitByLine(inputCSVFile,threadCount);
        try {
            String[] splitFiles = lineSplitter.execute(inputCSVFile);

            for (int i = 0; i < splitFiles.length; i++) {
                BatchInserter batchInserter = new BatchInserter(splitFiles[i]);
                asyncDataLoaderService.submit(batchInserter);
            }
            asyncDataLoaderService.awaitTermination(90, TimeUnit.MINUTES);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }


}
