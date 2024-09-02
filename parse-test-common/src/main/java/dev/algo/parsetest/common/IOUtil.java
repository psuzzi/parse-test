package dev.algo.parsetest.common;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class to handle threa-safe IO operations on files and classpath resources.
 * Note: resources can be loaded both when packaged in a jar and when unpacked in the classpath
 */
public class IOUtil {

    // Prevent instantiation
    private IOUtil() {}

    /**
     * Loads resource files from a specified folder in the classpath.
     * This method is thread-safe.
     *
     * @param classLoader The ClassLoader to use for loading resources
     * @param resourceFolderName The name of the folder containing the resources, which must exists.
     * @param extension The file extension to filter for (e.g., ".txt")
     * @return A Map where keys are file names and values are file contents
     * @throws IOException If there's an error reading the resources
     */
    public static synchronized Map<String, String> loadContentsFromResource(ClassLoader classLoader, String resourceFolderName, String extension) throws IOException {
        URL resourceUrl = classLoader.getResource(resourceFolderName);
        if(resourceUrl==null)
            throw new IOException("Resource folder not found " + resourceFolderName);

        URI uri;
        try {
            uri = resourceUrl.toURI();
        } catch (URISyntaxException e) {
            throw new IOException("Error resolving URI from resource folder " + resourceFolderName, e);
        }

        Map<String, String> fileContents = new HashMap<>();

        // Determine if we're dealing with a JAR file or a regular filesystem
        Path basePath;
        if (uri.getScheme().equals("jar")) {
            // If it's a JAR, create a new FileSystem to access its contents
            try(FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap())){
                basePath = fileSystem.getPath(resourceFolderName);
                internalLoadFilesFromPath(basePath, extension, fileContents);
            }
        } else {
            // For regular filesystem, create a Path directly
            basePath = Paths.get(uri);
            internalLoadFilesFromPath(basePath, extension, fileContents);
        }

        return fileContents;
    }

    /**
     * Loads files from a specified folder on the file system.
     * This method is thread-safe.
     *
     * @param folderPath The Path object representing the folder containing the files
     * @param extension The file extension to filter for (e.g., ".txt")
     * @return A Map where keys are file names and values are file contents
     * @throws IOException If there's an error reading the files
     */
    public static synchronized Map<String, String> loadContentsFromPath(Path folderPath, String extension) throws IOException {
        Map<String, String> fileContents = new HashMap<>();
        internalLoadFilesFromPath(folderPath, extension, fileContents);
        return fileContents;
    }

    /**
     * Internal, not thread-safe, to read files from a path into a map of file-names:file-contents
     *
     * @param folderPath path to the containing folder
     * @param extension extension to filter the relevant files
     * @param fileContents map with filenames:contents
     * @throws IOException in case of read error
     */
    private static void internalLoadFilesFromPath(Path folderPath, String extension, Map<String, String> fileContents) throws IOException {
        try (var paths = Files.walk(folderPath, 1)) {
            paths.filter(path -> !Files.isDirectory(path))
                    .filter(path -> path.toString().endsWith(extension))
                    .forEach(path -> {
                        try {
                            String fileName = path.getFileName().toString();
                            String content = Files.readString(path);
                            fileContents.put(fileName, content);
                        } catch (IOException e) {
                            throw new RuntimeException("Error reading file: " + path, e);
                        }
                    });
        }
    }
}