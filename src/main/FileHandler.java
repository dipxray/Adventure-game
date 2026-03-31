package main;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FileHandler {

    GamePanel gp;
    private final String fileName = "GameData.txt";
    
    // 🔥 PERSISTENT REMOVAL TRACKER (Format: "mapName:index")
    public Set<String> removedItems = new HashSet<>();
    public Set<String> removedNPCs = new HashSet<>();

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
            gp.player.worldx = Integer.parseInt(data.getOrDefault("x", "1294"));
            gp.player.worldy = Integer.parseInt(data.getOrDefault("y", "1294"));

            gp.player.hasApple = Boolean.parseBoolean(data.getOrDefault("hasApple", "false"));
            gp.player.hasCherry = Boolean.parseBoolean(data.getOrDefault("hasCherry", "false"));
            gp.player.hasCat = Boolean.parseBoolean(data.getOrDefault("hasCat", "false"));
            gp.player.hasSword = Boolean.parseBoolean(data.getOrDefault("hasSword", "false"));
            gp.player.hasDiamond = Boolean.parseBoolean(data.getOrDefault("hasDiamond", "false"));
            gp.player.questStage = Integer.parseInt(data.getOrDefault("questStage", "0"));

            // 🔥 LOAD MAP
            gp.tileM.currentMap = data.getOrDefault("currentMap", "mymap.txt");
            gp.tileM.loadMap("/res/tiles/" + gp.tileM.currentMap);

            // 🔥 LOAD REMOVED OBJECTS (Enhanced)
            removedItems.clear();
            String removed = data.getOrDefault("removed", "");
            if (!removed.equals("")) {
                String[] arr = removed.split(",");
                for (String s : arr) {
                    if (!s.isEmpty()) {
                        removedItems.add(s);
                    }
                }
            }

            // 🔥 LOAD REMOVED NPCs (Enhanced)
            removedNPCs.clear();
            String removedNPCsData = data.getOrDefault("removedNPCs", "");
            if (!removedNPCsData.equals("")) {
                String[] arr = removedNPCsData.split(",");
                for (String s : arr) {
                    if (!s.isEmpty()) {
                        removedNPCs.add(s);
                    }
                }
            }

            // Apply removals to the current map
            applyRemovals();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🔥 SAVE DATA
    public synchronized void saveData() {

        try (PrintWriter pw = new PrintWriter(new FileWriter(fileName))) {

            pw.println("health=" + gp.player.hasHearts);
            pw.println("keys=" + gp.player.hasKey);
            pw.println("x=" + gp.player.worldx);
            pw.println("y=" + gp.player.worldy);
            pw.println("hasApple=" + gp.player.hasApple);
            pw.println("hasCherry=" + gp.player.hasCherry);
            pw.println("hasCat=" + gp.player.hasCat);
            pw.println("hasSword=" + gp.player.hasSword);
            pw.println("hasDiamond=" + gp.player.hasDiamond);
            pw.println("questStage=" + gp.player.questStage);
            pw.println("currentMap=" + gp.tileM.currentMap);

            // 🔥 SAVE REMOVED OBJECTS (Enhanced - Cumulative)
            StringBuilder removed = new StringBuilder();
            for (String id : removedItems) {
                removed.append(id).append(",");
            }
            pw.println("removed=" + removed.toString());

            // 🔥 SAVE REMOVED NPCs (Enhanced - Cumulative)
            StringBuilder removedNPCsSB = new StringBuilder();
            for (String id : removedNPCs) {
                removedNPCsSB.append(id).append(",");
            }
            pw.println("removedNPCs=" + removedNPCsSB.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void resetData() {
    try (PrintWriter pw = new PrintWriter(new FileWriter(fileName))) {
        pw.println("health=0");
        pw.println("keys=0");
        pw.println("x=1294");
        pw.println("y=1294");
        pw.println("hasApple=false");
        pw.println("hasCherry=false");
        pw.println("hasCat=false");
        pw.println("hasSword=false");
        pw.println("hasDiamond=false");
        pw.println("questStage=0");
        pw.println("currentMap=mymap.txt");
        pw.println("removed=");
        pw.println("removedNPCs=");
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    // 🔥 CLEAR SAVE (FOR NEW GAME)
    public synchronized void clearData() {
        removedItems.clear();
        removedNPCs.clear();
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
    }

    // 🔥 APPLY REMOVALS TO CURRENT MAP
    public void applyRemovals() {
        if (gp.tileM.currentMap == null) return;
        String currentMap = gp.tileM.currentMap.trim();

        // Apply object removals
        for (int i = 0; i < gp.obj.length; i++) {
            if (removedItems.contains(currentMap + ":" + i)) {
                gp.obj[i] = null;
            }
        }

        // Apply NPC removals
        for (int i = 0; i < gp.npc.length; i++) {
            if (removedNPCs.contains(currentMap + ":" + i)) {
                gp.npc[i] = null;
            }
        }
    }
}