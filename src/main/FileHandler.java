package main;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileHandler {

    GamePanel gp;
    private final String fileName = "GameData.txt";

    public FileHandler(GamePanel gp) {
        this.gp = gp;
    }

    // 🔥 LOAD DATA
    public void loadData() {

        File file = new File(fileName);

        if (!file.exists()) {
            saveData();
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            Map<String, String> data = new HashMap<>();
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    data.put(parts[0], parts[1]);
                }
            }

            gp.player.hasHearts = Integer.parseInt(data.getOrDefault("health", "0"));
            gp.player.hasKey = Integer.parseInt(data.getOrDefault("keys", "0"));
            gp.player.worldx = Integer.parseInt(data.getOrDefault("x", "150"));
            gp.player.worldy = Integer.parseInt(data.getOrDefault("y", "150"));

            // 🔥 LOAD REMOVED OBJECTS
            String removed = data.getOrDefault("removed", "");
            if (!removed.equals("")) {
                String[] arr = removed.split(",");
                for (String s : arr) {
                    int index = Integer.parseInt(s);
                    if (index >= 0 && index < gp.obj.length) {
                        gp.obj[index] = null;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🔥 SAVE DATA
    public void saveData() {

        try (PrintWriter pw = new PrintWriter(new FileWriter(fileName))) {

            pw.println("health=" + gp.player.hasHearts);
            pw.println("keys=" + gp.player.hasKey);
            pw.println("x=" + gp.player.worldx);
            pw.println("y=" + gp.player.worldy);

            // 🔥 SAVE REMOVED OBJECTS
            StringBuilder removed = new StringBuilder();

            for (int i = 0; i < gp.obj.length; i++) {
                if (gp.obj[i] == null) {
                    removed.append(i).append(",");
                }
            }

            pw.println("removed=" + removed.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void resetData() {
    try (PrintWriter pw = new PrintWriter(new FileWriter(fileName))) {
        pw.println("health=0");
        pw.println("keys=0");
        pw.println("x=150");
        pw.println("y=150");
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    // 🔥 CLEAR SAVE (FOR NEW GAME)
    public void clearData() {
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
    }
}