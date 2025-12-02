package Graphism;

import Utilitary.V2D;

public abstract class Sprite {

    private final String atlasName;
    private final V2D size;

    public Sprite(String atlasName, V2D size) {
        this.atlasName = atlasName;
        this.size = size;
    }

    public String getAtlasName() {
        return atlasName;
    }

    public V2D getSize() {
        return size;
    }

    
}
