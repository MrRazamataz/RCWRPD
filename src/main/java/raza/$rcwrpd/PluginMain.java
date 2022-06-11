package raza.$rcwrpd;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.event.*;
import org.bukkit.plugin.java.*;

public class PluginMain extends JavaPlugin implements Listener {

	private static PluginMain instance;

	@Override
	public void onEnable() {
		instance = this;
		getServer().getPluginManager().registerEvents(this, this);
		saveDefaultConfig();
		try {
			new Metrics(PluginMain.getInstance(), ((int) (15427d)));
			PluginMain.getInstance().getLogger().info(ChatColor.translateAlternateColorCodes('&',
					ChatColor.translateAlternateColorCodes('&', "&aPlugin has started. Connected to bstats.")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		getServer().getPluginManager().registerEvents(PlayerDataManager.getInstance(), this);
	}

	@Override
	public void onDisable() {
		PlayerDataManager.getInstance().saveAllData();
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] commandArgs) {
		return true;
	}

	public static void procedure(String procedure, List procedureArgs) throws Exception {
	}

	public static Object function(String function, List functionArgs) throws Exception {
		return null;
	}

	public static List createList(Object obj) {
		if (obj instanceof List) {
			return (List) obj;
		}
		List list = new ArrayList<>();
		if (obj.getClass().isArray()) {
			int length = java.lang.reflect.Array.getLength(obj);
			for (int i = 0; i < length; i++) {
				list.add(java.lang.reflect.Array.get(obj, i));
			}
		} else if (obj instanceof Collection<?>) {
			list.addAll((Collection<?>) obj);
		} else if (obj instanceof Iterator) {
			((Iterator<?>) obj).forEachRemaining(list::add);
		} else {
			list.add(obj);
		}
		return list;
	}

	public static void createResourceFile(String path) {
		Path file = getInstance().getDataFolder().toPath().resolve(path);
		if (Files.notExists(file)) {
			try (InputStream inputStream = PluginMain.class.getResourceAsStream("/" + path)) {
				Files.createDirectories(file.getParent());
				Files.copy(inputStream, file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static PluginMain getInstance() {
		return instance;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void event1(org.bukkit.event.player.PlayerResourcePackStatusEvent event) throws Exception {
		if ((PlayerDataManager.getInstance().getData(((org.bukkit.entity.Player) event.getPlayer()),
				"joined") == null)) {
			if (PluginMain.checkEquals(
					((org.bukkit.event.player.PlayerResourcePackStatusEvent.Status) event.getStatus()),
					((org.bukkit.event.player.PlayerResourcePackStatusEvent.Status) org.bukkit.event.player.PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED))) {
				PluginMain.getInstance().getLogger().info(
						(((java.lang.String) ((org.bukkit.command.CommandSender) (Object) ((org.bukkit.entity.Player) event
								.getPlayer())).getName())
								+ ChatColor.translateAlternateColorCodes('&', ChatColor.translateAlternateColorCodes(
										'&', "&ahas downloade the resource pack. Command running..."))));
				org.bukkit.Bukkit.dispatchCommand(
						((org.bukkit.command.CommandSender) (Object) ((org.bukkit.command.ConsoleCommandSender) org.bukkit.Bukkit
								.getConsoleSender())),
						((java.lang.String) String.valueOf(PluginMain.getInstance().getConfig().get("command"))
								.replace(String.valueOf("%player%"), String.valueOf(
										((java.lang.String) ((org.bukkit.command.CommandSender) (Object) ((org.bukkit.entity.Player) event
												.getPlayer())).getName())))));
				PlayerDataManager.getInstance().setData(((org.bukkit.entity.Player) event.getPlayer()), "joined",
						((java.lang.Object) (Object) true));
			} else if (PluginMain.checkEquals(
					((org.bukkit.event.player.PlayerResourcePackStatusEvent.Status) event.getStatus()),
					((org.bukkit.event.player.PlayerResourcePackStatusEvent.Status) org.bukkit.event.player.PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD))) {
				PluginMain.getInstance().getLogger().info(
						(((java.lang.String) ((org.bukkit.command.CommandSender) (Object) ((org.bukkit.entity.Player) event
								.getPlayer())).getName())
								+ ChatColor.translateAlternateColorCodes('&', ChatColor
										.translateAlternateColorCodes('&', "&c resource pack download failed!"))));
				((org.bukkit.command.CommandSender) (Object) ((org.bukkit.entity.Player) event.getPlayer()))
						.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.translateAlternateColorCodes(
								'&', "&aThe resource pack failed to download. Please leave and rejoin.")));
			}
		} else {
			PluginMain.getInstance().getLogger().info("Player is not a new player. Skipping command.");
		}
	}

	public static boolean checkEquals(Object o1, Object o2) {
		if (o1 == null || o2 == null) {
			return false;
		}
		return o1 instanceof Number && o2 instanceof Number
				? ((Number) o1).doubleValue() == ((Number) o2).doubleValue()
				: o1.equals(o2);
	}
}
