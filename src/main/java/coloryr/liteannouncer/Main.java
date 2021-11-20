package coloryr.liteannouncer;

import coloryr.liteannouncer.obj.AItem;
import coloryr.liteannouncer.obj.ConfigObj;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Plugin(
        id = "liteannouncer",
        name = "LiteAnnouncer",
        version = Main.version,
        url = "https://github.com/HeartAge/LiteAnnouncer",
        description = "LiteAnnouncer",
        authors = {"Color_yr"}
)
public class Main {
    public static final String version = "1.0.0";

    public final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;
    private final Gson gson = new Gson();
    private File file;
    public ConfigObj config;
    public static Main main;

    @Inject
    public Main(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    public void load() {
        try {
            if (!Files.exists(dataDirectory)) {
                Files.createDirectory(dataDirectory);
            }
            file = new File(dataDirectory.toFile(), "config.json");
            if (!file.exists()) {
                config = new ConfigObj();
                String data = gson.toJson(config);
                FileOutputStream stream = new FileOutputStream(file);
                stream.write(data.getBytes(StandardCharsets.UTF_8));
                stream.close();
            } else {
                InputStreamReader stream = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
                BufferedReader bf = new BufferedReader(stream);
                config = gson.fromJson(bf, ConfigObj.class);
                bf.close();
                stream.close();
                if (config.check()) {
                    config = new ConfigObj();
                    String data = gson.toJson(config);
                    FileOutputStream stream1 = new FileOutputStream(file);
                    stream1.write(data.getBytes(StandardCharsets.UTF_8));
                    stream1.close();
                }
            }
        } catch (Exception e) {
            logError(e);
        }
    }

    public void logError(Exception e) {
        logger.error("§d[LiteAnnouncer]§c发生错误", e);
    }

    public void log(String data) {
        logger.info("§d[LiteAnnouncer]§e" + data);
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        main = this;
        log("正在启动中，版本：" + version);
        CommandMeta meta = server.getCommandManager().metaBuilder("la")
                .aliases("liteannouncer")
                .build();
        server.getCommandManager().register(meta, new Command());
        load();
        MyLoop.start();
        log("已启动");
    }

    @Subscribe
    public void onStop(ProxyShutdownEvent event) {
        log("正在停止");
        MyLoop.stop();
    }
}
