package bs.utils.file;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.stream.Stream;

/**
 * Created by shahb on 2/14/15.
 */
public class FileUtils {
    public static long countLines(File file) {
        try (Stream<String> lines = Files.lines(file.toPath(), Charset.defaultCharset())) {
            return lines.count();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
