package Graphism;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class Atlas {

    private static final HashMap<String, BufferedImage> atlases = new HashMap<>();

    static {
        try
        {
            atlases.put("TileSet", ImageIO.read(Atlas.class.getResourceAsStream("/assets/tiles/TileSet.png")));

            atlases.put("Archer", ImageIO.read(Atlas.class.getResourceAsStream("/assets/sprites/Archer.png")));
            atlases.put("Warrior", ImageIO.read(Atlas.class.getResourceAsStream("/assets/sprites/Warrior.png")));
            atlases.put("Mage", ImageIO.read(Atlas.class.getResourceAsStream("/assets/sprites/Mage.png")));

            atlases.put("Gobelin", ImageIO.read(Atlas.class.getResourceAsStream("/assets/sprites/Gobelin.png")));
            atlases.put("Orc", ImageIO.read(Atlas.class.getResourceAsStream("/assets/sprites/Orc.png")));
            atlases.put("Skeleton", ImageIO.read(Atlas.class.getResourceAsStream("/assets/sprites/Skeletons.png")));

            atlases.put("Minotaurus", ImageIO.read(Atlas.class.getResourceAsStream("/assets/sprites/Minotaur.png")));
            atlases.put("Jack", ImageIO.read(Atlas.class.getResourceAsStream("/assets/sprites/Jack.png")));
            atlases.put("OrcBoss", ImageIO.read(Atlas.class.getResourceAsStream("/assets/sprites/OrcBoss.png")));
            atlases.put("Necromancer", ImageIO.read(Atlas.class.getResourceAsStream("/assets/sprites/Necromancer.png")));

            atlases.put("Merchant", ImageIO.read(Atlas.class.getResourceAsStream("/assets/sprites/Merchant.png")));

            atlases.put("Effects", ImageIO.read(Atlas.class.getResourceAsStream("/assets/sprites/Effects.png")));

            //Load des sprites UI
            atlases.put("UI", ImageIO.read(Atlas.class.getResourceAsStream("/assets/ui/UI.png")));
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public static BufferedImage get(String name) {
        return atlases.get(name);
    }
}
