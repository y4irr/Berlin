package net.cyruspvp.hub.tablist.extra;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TablistEntry {

    private String text;
    private TablistSkin skin;
    private int ping;

    public TablistEntry(String text, TablistSkin skin, int ping) {
        this.text = text;
        this.skin = skin;
        this.ping = ping;
    }
}