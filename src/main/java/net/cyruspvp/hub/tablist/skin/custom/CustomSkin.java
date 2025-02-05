package net.cyruspvp.hub.tablist.skin.custom;

import net.cyruspvp.hub.tablist.util.Skin;
import lombok.Getter;

@Getter
public class CustomSkin extends Skin {

    private final String name;

    public CustomSkin(String name, String value, String signature) {
        super(value, signature);
        this.name = name;
    }
}
