package coloryr.liteannouncer;

import coloryr.liteannouncer.obj.AItem;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;

import java.util.Collection;

public class MyLoop {
    private static Thread thread = new Thread(MyLoop::run);
    private static int index;
    private static int loop;
    private static boolean isRun;

    public static void start() {
        isRun = true;
        thread.start();
    }

    public static void stop() {
        isRun = false;
    }

    private static String replace(String data)
    {
        return data.replace("%onlineCount%", Main.main.server.getPlayerCount() + "").replace("&", "§");
    }

    private static void run() {
        while (isRun) {
            try {
                if (loop != 0) {
                    loop--;
                } else {
                    Collection<Player> players = Main.main.server.getAllPlayers();
                    if (players.size() != 0) {
                        AItem data = Main.main.config.messages.get(index);
                        players.forEach((player -> {
                            player.sendMessage(Component.text(replace(data.message)));
                        }));
                    }
                    index++;
                    if (index == Main.main.config.messages.size()) {
                        index = 0;
                    }
                    loop = Main.main.config.time;
                }
                Thread.sleep(1000);
            } catch (Exception e) {
                Main.main.logError(e);
            }
        }
    }
}
