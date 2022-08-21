package io.github.wckds.wckdhax;

import io.github.wckds.wckdhax.automationhax.*;
import io.github.wckds.wckdhax.clienthax.*;
import io.github.wckds.wckdhax.renderhax.*;
import io.netty.channel.Channel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Settings {
    public static HashMap<Integer, Hax<?>> toggles;
    public static List<Hax<?>> categories;
    public static ClientPlayerEntity player;
    public static ClientWorld world;
    public static Channel channel;
    public static MinecraftClient client;

    static File file = new File(MinecraftClient.getInstance().runDirectory, "mods");
    static File config = new File(file, "wckdhax.cfg");

    public Settings() {
    }

    public static void keyDown(int key) {
        if (toggles.containsKey(key)) {
            toggles.get(key).toggle();
        }
    }

    public static void saveToggles() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(config, false));
            writer.write(toggles.size() + "\n");

            toggles.values().forEach(module -> {
                try {
                    HashMap<String, String> args = module.getArgs();
                    writer.write(module.getModuleName() + " " + args.size() + "\n");
                    args.forEach((k, v) -> {
                        char[] key = k.toCharArray();
                        char[] val = v.toCharArray();
                        try {
                            writer.write(key.length); // this isn't in plaintext
                            writer.write(key);
                            writer.write(val.length);
                            writer.write(val);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    writer.write("\n");
                } catch (IOException ignored) {
                }
            });
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (Settings.player != null) player.sendMessage(Text.of("Error while saving toggles to file. Resetting Toggles."), true);
        }

    }

    public static void loadToggles() {
        if (!config.exists()) saveToggles();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(config));

            int s = Integer.parseInt(reader.readLine());
            for (int i = 0; i < s; i++) {
                String module;
                int size;
                HashMap<String, String> args = new HashMap<>();

                String[] line = reader.readLine().split(" ");
                module = line[0];
                size = Integer.parseInt(line[1]);

                for (int j = 0; j < size; j++) {
                    int a = reader.read();
                    char[] key = new char[a];
                    reader.read(key);
                    int b = reader.read();
                    char[] val = new char[b];
                    reader.read(val);

                    args.put(String.copyValueOf(key), String.copyValueOf(val));
                }

                toggles.values().stream()
                        .filter(m -> m.getModuleName().equals(module))
                        .findAny()
                        .ifPresentOrElse(h -> h.setArgs(args), () -> {});

                reader.read();
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (Settings.player != null) player.sendMessage(Text.of("Error while loading toggles from file. Resetting Toggles."), true);
        }
    }

    static {
        toggles = new HashMap<>();
        toggles.put((int)'W', Hax.of(Walker.class));
        toggles.put((int)'R', Hax.of(GUI.class));
        toggles.put(-1, Hax.of(AutoHotbar.class));
        toggles.put((int)'Z', Hax.of(FreeCam.class));
        toggles.put(-5, Hax.of(FullBright.class));
        toggles.put(-9, Hax.of(FarmPlanter.class));
        toggles.put(-10, Hax.of(Harvester.class));

        categories = new LinkedList<>();
        categories.add(Hax.of(AutomationBase.class));
        categories.add(Hax.of(RenderBase.class));
        categories.add(Hax.of(ClientBase.class));
    }
}
