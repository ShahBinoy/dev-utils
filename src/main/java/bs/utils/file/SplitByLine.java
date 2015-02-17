package bs.utils.file;

import java.io.*;

/**
 * Created by shahb on 2/14/15.
 */
public class SplitByLine {

    String fileName;
    private int numOfParts;
    String prefix ;
    String fileExt;
    File originalFile;

    public SplitByLine( String fileName, int numOfParts) {
        this.fileName = fileName;
        originalFile = new File(fileName);
        int lastIndexPathSep = fileName.lastIndexOf(File.separator);
        int extStartPos = fileName.lastIndexOf(".");
        prefix = fileName.substring(lastIndexPathSep+1,extStartPos);
        fileExt = "."+fileName.substring(extStartPos+1);
        System.out.println("Prefix value = [" + prefix+ "] using index lI and extPos "+lastIndexPathSep + ", "+extStartPos);
        this.numOfParts = numOfParts;
    }


    public String[] execute(String fileName) throws IOException {
        originalFile = new File(fileName);
        long lineCount = FileUtils.countLines(originalFile);
        System.out.println("Line count for file [" + fileName + "] :- "+lineCount);
        long maxLinesPerFile = lineCount / numOfParts;
        System.out.println("Total lines per file :- "+maxLinesPerFile);
        long fileLineCount = 1;
        String splitFileNames[] = new String[numOfParts];
        int splitCounter = 1;

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(originalFile)));
        File splitCsv = null;
        File parentFolder = originalFile.getParentFile();
        String newFileName = prefix +"-part-"+  splitCounter +  fileExt;
        String parentFolderLoc = parentFolder.getAbsolutePath();
        System.out.println("Creating new file "+ newFileName+" in folder [" + parentFolderLoc + "]");

        splitCsv =  new File(parentFolderLoc,newFileName);
        PrintWriter bufferedWriter = new PrintWriter(splitCsv);

        String newLine = br.readLine();
        while (newLine != null) {
            if (fileLineCount > maxLinesPerFile) {
                splitFileNames[splitCounter-1] = splitCsv.getAbsolutePath();
                bufferedWriter.flush();
                bufferedWriter.close();

                System.out.println("Wrote "+(fileLineCount-1)+" lines to file ["+splitFileNames[splitCounter-1]+"]");

                splitCounter++;

                newFileName = prefix +"-part-"+ splitCounter + fileExt;
                splitCsv = new File(parentFolderLoc,newFileName);
                bufferedWriter = new PrintWriter(splitCsv);
                fileLineCount = 1;
            }
            bufferedWriter.println(newLine.replace(";",""));
            fileLineCount++;
            newLine = br.readLine();

        }
        splitFileNames[splitCounter-1] = splitCsv.getAbsolutePath();
        System.out.println("Wrote "+(fileLineCount-1)+" lines to file ["+splitFileNames[splitCounter-1]+"]");
        bufferedWriter.flush();
        bufferedWriter.close();

        return splitFileNames;
    }
}
