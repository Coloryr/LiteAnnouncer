package coloryr.liteannouncer.obj;

import coloryr.liteannouncer.Main;

import java.util.ArrayList;
import java.util.List;

public class ConfigObj {
    public String version;
    public List<AItem> messages;
    public int time;

    public ConfigObj() {
        messages = new ArrayList<>();
        time = 10;
        version = Main.version;
        messages.add(new AItem("这是一条消息"));
    }

    public boolean check() {
        boolean res = false;
        if (messages == null) {
            messages = new ArrayList<>();
            res = true;
        }
        return res;
    }
}
