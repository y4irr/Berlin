package net.cyruspvp.hub.nametags.extra;

import lombok.Getter;

import java.util.Objects;


@Getter
public class NameInfo {

    private final String name;
    private final String color;
    private final String prefix;
    private final String suffix;
    private final NameVisibility visibility;

    public NameInfo(String name, String color, String prefix, String suffix, NameVisibility visibility, boolean friendlyInvis) {
        this.name = name;
        this.color = color;
        this.prefix = prefix;
        this.suffix = suffix;
        this.visibility = visibility;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NameInfo)) return false;
        NameInfo that = (NameInfo) o;
        return Objects.equals(name, that.name) && Objects.equals(color, that.color) && Objects.equals(prefix, that.prefix) && Objects.equals(suffix, that.suffix);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, color, prefix, suffix);
    }
}