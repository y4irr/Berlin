package net.cyruspvp.hub.user;

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Hub
 * @author Yair © 2024
 * Date: 13 - nov
 */

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocialStuff {
    private String youtubeLink;
    private String twitchLink;
    private String twitterLink;
    private String discordLink;

    public SocialStuff(String youtubeLink, String twitchLink, String twitterLink, String discordLink) {
        this.youtubeLink = youtubeLink;
        this.twitchLink = twitchLink;
        this.twitterLink = twitterLink;
        this.discordLink = discordLink;
    }
}
