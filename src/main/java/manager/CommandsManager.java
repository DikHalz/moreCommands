package manager;

import arc.Core;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.CommandHandler;


public class CommandsManager {
  private static ObjectMap<String, Boolean> commands = new ObjectMap<>(), temp = new ObjectMap<>();
  private static String clientPrefix = mindustry.Vars.netServer.clientCommands.getPrefix();
  private static volatile boolean canLoad = false;
  
  public static Commands get(String name) {
    Boolean result = commands.get(name);
    return result == null ? null : new Commands(name, result);
  }

  public static void add(String name, boolean value) {
    commands.put(name, value);
  }
  
  public static Seq<Commands> getCommands() {
    Seq<Commands> result = new Seq<>();
    commands.each((k, v) -> result.add(new Commands(k, v)));
    
    return result;
  }
  
  public static void save() {
    Core.settings.put("handlerManager", commands.toString(" | "));
    Core.settings.forceSave();
  }
  
  public static void update(CommandHandler handler) {
    commands.forEach(command -> {
      if (command.value != temp.get(command.key)) {
        handler.removeCommand("host");
        
        handler.register("host", "[mapname] [mode]", "Abre el servidor. Se establecerá de forma predeterminada en supervivencia y un mapa aleatorio si no se especifica.", arg -> 
          arc.util.Log.warn("Se han realizado cambios. Reinicie el servidor para que surtan efecto.")
        );
        return;
      }
    });
  }

  public static void load(CommandHandler handler) {
    while (!canLoad) {}
    
    handler.getCommandList().forEach(command -> {
      if (!commands.containsKey(handler.getPrefix() + command.text)) 
        commands.put(handler.getPrefix() + command.text, true);
    });
    
    Seq<String> list = (clientPrefix.equals(handler.getPrefix()) ? commands.keys().toSeq().filter(c -> c.startsWith(handler.getPrefix())) : 
          commands.keys().toSeq().filter(c -> !c.startsWith(clientPrefix))),
        comparator = handler.getCommandList().map(command -> command.text);
    
    commands.forEach(command -> { 
      if (!command.value) handler.removeCommand(command.key.substring(handler.getPrefix().length()));
    });
    
    comparator.each(c -> list.remove(handler.getPrefix() + c));
    list.each(c -> commands.remove(c));
    
    temp.putAll(commands);
    save();
  }
  
  public static void init() {
    if (Core.settings.has("handlerManager")) {
      try {
        String[] temp;
        for (String line : Core.settings.getString("handlerManager").split(" \\| ")) {
          temp = line.split("\\=");
          commands.put(temp[0], Boolean.parseBoolean(temp[1]));
        }
      } catch (Exception e) { save(); }
      
    } else save();
    
    canLoad = true;
  }
  
  
  public static class Commands {
    public final String name;
    public boolean isActivate;

    private Commands(String name, boolean isActivate) {
      this.name = name;
      this.isActivate = isActivate;
    }
    
    public void set(boolean value) {
      commands.put(name, value);
      this.isActivate = value;
    }
  }
}
