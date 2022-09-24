package uk.ac.ucl.shell;

import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;

public class Test {
    @Rule public final TemporaryFolder temporaryFolder = new TemporaryFolder();
    protected final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    protected final String file1Name = "file1.txt";
    protected final String[] file1Content = new String[]{"BBB", "DDD", "AAA", "CCC", "EEE", "BBB", "DDD", "AAA", "CCC",
                                                         "EEE", "BBB", "DDD", "AAA", "CCC", "EEE"};
    protected final String file2Name = "file2.txt";
    protected final String[] file2Content = new String[]{"AAA", "BBB", "CCC"};
    protected final String file3Name = "file3.txt";
    protected final String[] file3Content = new String[]{"AAA", "AAA", "aaa", "BBB"};
    protected final String file4Name = "file4.txt";
    protected final String[] file4Content = new String[]{"ABC", "DEF", "GHI"};
    protected final String folder1Name = "folder1";
    protected final String folder2Name = "folder2";
    protected final String folder3Name = "folder3";
    protected final String nonExistentFileName = "nonExistentFile.txt";
    protected final String unsortedFileName = "unsortedFile.txt";
    protected final String[] unsortedFileContent = new String[]{"AAA", "BBB", "CCC"};
    protected final String unsortedFileName2 = "unsortedFile2.txt";
    protected final String[] unsortedFileContent2 = new String[]{"BBB", "AAA", "CCC"};
    protected final String nonExistentDirectoryName = "nonExistentDirectory";
    protected final String grepFile1Name = "grepFile1.txt";
    protected final String[] grepFile1Content = new String[]{"BBB", "DDD", "AAA", "CCC", "EEE"};
    protected final String grepFile2Name = "grepFile2.txt";
    protected final String[] grepFile2Content = new String[]{"AAA", "BBB", "CCC"};
    protected final String uniqFileName = "uniqFile.txt";
    protected final String[] uniqFileContent = new String[]{"AAA", "AAA", "aaa", "BBB"};

    public Test() {
        PrintStream printStream = new PrintStream(byteArrayOutputStream);
        System.setOut(printStream);
    }

    protected void setupTestingEnvironment() throws IOException {
        Shell.setCurrentDirectory(temporaryFolder.getRoot().toString());
        File file1 = temporaryFolder.newFile(file1Name);
        File file2 = temporaryFolder.newFile(file2Name);
        File file3 = temporaryFolder.newFile(file3Name);
        File file4 = temporaryFolder.newFile(file4Name);
        File unsortedFile = temporaryFolder.newFile(unsortedFileName);
        Arrays.sort(unsortedFileContent, Comparator.reverseOrder());
        File unsortedFile2 = temporaryFolder.newFile(unsortedFileName2);
        File grepFile1 = temporaryFolder.newFile(grepFile1Name);
        File grepFile2 = temporaryFolder.newFile(grepFile2Name);
        File uniqFile = temporaryFolder.newFile(uniqFileName);
        generateFile(file1Content, file1);
        generateFile(file2Content, file2);
        generateFile(file3Content, file3);
        generateFile(file4Content, file4);
        generateFile(unsortedFileContent, unsortedFile);
        generateFile(unsortedFileContent2, unsortedFile2);
        generateFile(grepFile1Content, grepFile1);
        generateFile(grepFile2Content, grepFile2);
        generateFile(uniqFileContent, uniqFile);
        temporaryFolder.newFolder(folder1Name, folder2Name, folder3Name);
    }

    protected void generateFile(String[] content, File file) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
        for (String line : content) {
            bufferedWriter.write(line);
            bufferedWriter.newLine();
        }
        bufferedWriter.close();
        fileOutputStream.close();
    }

    protected String getCommandOutput(String command) {
        runCommand(command);
        String result = byteArrayOutputStream.toString();
        byteArrayOutputStream.reset();
        return result;
    }

    protected void runCommand(String command) {
        Shell.main(new String[]{"-c", command});
    }

    protected String formatExpectedOutput(String[] expectedOutput) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String line : expectedOutput) {
            stringBuilder.append(line).append(System.lineSeparator());
        }
        return stringBuilder.toString();
    }

    protected String fileContents(String filename) throws IOException {
        return Files.readString(Path.of(pathToItem(filename)));
    }

    protected String pathToItem(String filename) {
        return rootPath() + File.separator + filename;
    }

    protected String rootPath() {
        return temporaryFolder.getRoot().toString();
    }

    protected boolean nonExistentFileExists() {
        File file = new File(pathToItem("nonExistentFile.txt"));
        return file.exists() && file.isFile();
    }

    protected String findPathToFile1() {
        String[] splitRootPath = rootPath().split(File.separator);
        return "." + File.separator + splitRootPath[splitRootPath.length - 1] + File.separator + "file1.txt";
    }

    protected String[] getItemsInRoot() {
        File[] files = temporaryFolder.getRoot().listFiles();
        String[] fileNames;
        if (files == null) {
            fileNames = new String[0];
        } else {
            fileNames = new String[files.length];
            for (int i = 0; i < files.length; ++i) {
                fileNames[i] = files[i].getName();
            }
        }
        return fileNames;
    }
}