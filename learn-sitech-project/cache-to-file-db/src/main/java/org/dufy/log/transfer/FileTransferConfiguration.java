package org.dufy.log.transfer;

import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.Yaml;


/**
 * The configuration resolver for {@link FileTransferManager}
 *
 * @author Charlie
 */
public abstract class FileTransferConfiguration {

    public final static String CONFIG_FILE_NAME_TEST = "FileTransferTest.yaml";
    public final static String CONFIG_FILE_NAME = "FileTransfer.yaml";

    @SuppressWarnings("unchecked")
    public static List<Parameter> loadTransferTasks() {
        InputStream is = null;
        is = Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_FILE_NAME_TEST);
        if (is == null) {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_FILE_NAME);
        }
        if (is == null) {
            throw new RuntimeException("FileTransfer config file is not found!");
        }

        Yaml yaml = new Yaml();
        List<Parameter> paras = (List<Parameter>) yaml.load(is);
        return paras;
    }

    /**
     * The parameters of {@link FileTransferConfiguration}
     *
     * @author Charlie
     */
    public static class Parameter {
        /**
         * This should be configured with name of logger which recorded these files.
         * If multiple logger associated with these files, use comma (,) to join the logger names of them.
         * <b>The names will be used to send log events to the loggers, to avoid insufficient log events
         * which will cause file delay rolling.</b>
         *
         * <p>Such as: "user"
         */
        private String fileType;
        private String scanPath;

        /**
         * Scan interval in second
         */
        private Integer interval;

        private String filePattern;
        private Pattern compiledFilePattern;

        /**
         * The backup path, can be configured with captured value in {@code filePattern}
         */
        private String backupPath;
        private String compressType = "gz";

        /**
         * The url format is {@code sftp://host[:port]/location?user=&password=}
         * <br>or
         * <br>{@code ftp://host[:port]/location?user=&password=}
         */
        private String transferUrl;

        /**
         * This field is just a holder of values which are contained in {@code transferUrl},
         * so you should not configure this field, it's automatically set after the
         * {@code transferUrl} field is set.
         */
        private Server transferServer;

        /**
         * The destination file name will be the same as original name
         * when this field is null or empty.
         *
         * <p>The captured value in {@code filePattern} can be used in this field,
         * according to standard {@link Matcher#replaceAll(String)}.
         * Which work like this:
         *
         * <p><code>destinationFileName = filePattern.matcher(fileNameToTransfer).replaceAll(destFileRenamePattern)
         */
        private String destFileRenamePattern;

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }

        public String getScanPath() {
            return scanPath;
        }

        public void setScanPath(String scanPath) {
            this.scanPath = scanPath;
        }

        public Integer getInterval() {
            return interval;
        }

        public void setInterval(String interval) {
            this.interval = Integer.parseInt(interval);
        }

        public void setInterval(Integer interval) {
            this.interval = interval;
        }

        public Pattern getCompiledFilePattern() {
            if (compiledFilePattern == null) {
                compiledFilePattern = Pattern.compile(filePattern);
            }
            return compiledFilePattern;
        }

        public String getFilePattern() {
            return this.filePattern;
        }

        public void setFilePattern(String filePattern) {
            this.filePattern = filePattern;
            this.compiledFilePattern = Pattern.compile(filePattern);
        }

        public String getBackupPath() {
            return backupPath;
        }

        public void setBackupPath(String backupPath) {
            this.backupPath = backupPath;
        }

        public String getCompressType() {
            return compressType;
        }

        public void setCompressType(String compressType) {
            this.compressType = compressType;
        }

        public String getTransferUrl() {
            return transferUrl;
        }

        public void setTransferUrl(String transferUrl) {
            this.transferUrl = transferUrl;
            this.transferServer = Server.fromUrl(transferUrl);
        }

        public Server getTransferServer() {
            if (this.transferServer == null) {
                this.transferServer = Server.fromUrl(transferUrl);
            }
            return transferServer;
        }

        public void setTransferServer(Server transferServer) {
            this.transferServer = transferServer;
        }

        public String getDestFileRenamePattern() {
            return destFileRenamePattern;
        }

        public void setDestFileRenamePattern(String destFileRenamePattern) {
            this.destFileRenamePattern = destFileRenamePattern;
        }
    }

    /**
     * Resolve the standard protocol describe string to separated fields.
     *
     * <p>Example: <code>sftp://host:port/location?user=&password=</code>
     * into fields of this class
     *
     * @author Charlie
     */
    public static class Server {
        private String protocol;
        private String host;
        private Integer port;
        private String location;
        private String user;
        private String password;
        private String key;
        private String keyPhrase;

        protected static final Pattern urlPatt = Pattern.compile("(\\w+)://(.+?)(:\\d+)?/(.+)\\?(.+)$");

        /**
         * Parse the url to {@link Server}
         *
         * @param url
         * @return
         */
        public static Server fromUrl(String url) {
            Matcher mat = urlPatt.matcher(url);

            if (!mat.matches()) {
                throw new IllegalArgumentException(
                        String.format("The url [%s] is invalid!", url));
            }

            Server s = new Server();
            s.setProtocol(mat.group(1));
            s.setHost(mat.group(2));

            String sPort = mat.group(3);
            if (StringUtils.isEmpty(sPort)) {
                if ("sftp".equalsIgnoreCase(s.getProtocol())) {
                    s.setPort(22);
                } else if ("ftp".equalsIgnoreCase(s.getProtocol())) {
                    s.setPort(21);
                } else {
                    throw new IllegalArgumentException(
                            String.format("The url [%s] is invalid! Protocol is invalid.", url));
                }
            } else {
                s.setPort(Integer.parseInt(sPort.substring(1)));
            }

            //Prepend a / to path of Linux
            if (mat.group(4).lastIndexOf(':') == -1) {
                s.setLocation("/" + mat.group(4));
            } else {
                s.setLocation(mat.group(4));
            }

            String paras = mat.group(5);
            s.setUser(getFirstValueFromQueryString("user", paras));
            s.setPassword(getFirstValueFromQueryString("password", paras));
            s.setKey(getFirstValueFromQueryString("key", paras));
            s.setKeyPhrase(getFirstValueFromQueryString("keyPhrase", paras));
            return s;
        }

        private static String getFirstValueFromQueryString(String key, String queryString) {
            if (StringUtils.isEmpty(key)) {
                return null;
            }

            int start = queryString.indexOf(key + '='), end = -1;
            if (start != -1) {
                start = start + key.length() + 1;
                end = queryString.indexOf('&', start);
                if (end != -1) {
                    return queryString.substring(start, end);
                } else {
                    return queryString.substring(start);
                }
            }

            return null;
        }

        public String getProtocol() {
            return protocol;
        }

        public void setProtocol(String protocol) {
            this.protocol = protocol;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getKeyPhrase() {
            return keyPhrase;
        }

        public void setKeyPhrase(String keyPhrase) {
            this.keyPhrase = keyPhrase;
        }

    }

}
