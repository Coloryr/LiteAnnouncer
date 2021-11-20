package coloryr.liteannouncer;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import net.kyori.adventure.text.Component;

public class Command implements SimpleCommand {
    @Override
    public void execute(final Invocation invocation) {
        String[] arg = invocation.arguments();
        CommandSource sender = invocation.source();
        if (!sender.hasPermission("liteannouncer.admin")) {
            sender.sendMessage(Component.text("你没有使用该命令的权限"));
            return;
        }
        if (arg[0].equalsIgnoreCase("reload")) {
            Main.main.load();
            sender.sendMessage(Component.text("已重载"));
        }
    }
}
