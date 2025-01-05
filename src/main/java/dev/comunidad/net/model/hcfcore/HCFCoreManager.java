package dev.comunidad.net.model.hcfcore;

import dev.comunidad.net.BerlinPlugin;
import dev.comunidad.net.model.hcfcore.vapor.VaporHCF;
import dev.comunidad.net.model.hcfcore.vapor.VaporKitMap;
import dev.comunidad.net.utilities.Berlin;
import dev.comunidad.net.database.mongo.MongoManager;
import dev.comunidad.net.model.hcfcore.azurite.AzuriteHCF;
import dev.comunidad.net.model.hcfcore.azurite.AzuriteKitMap;
import dev.comunidad.net.model.hcfcore.lazarus.LazarusHCF;
import dev.comunidad.net.model.hcfcore.lazarus.LazarusKitMap;
import dev.comunidad.net.utilities.ChatUtil;
import lombok.Getter;

@Getter
public class HCFCoreManager {

    private IHCFCore kitmap, hcf;

    public HCFCoreManager(Berlin plugin) {
        MongoManager mongoManager = plugin.getMongoManager();
        HCFCoreType hcfCoreType;

        try {
            hcfCoreType = HCFCoreType.valueOf(plugin.getConfigFile().getString("hcfcore-type").toUpperCase());
        }
        catch (Exception ex) {
            ChatUtil.logger("&c[" + BerlinPlugin.getPlugin().getName() + "] HCFCore failed to connect.");
            return;
        }

        if (hcfCoreType == HCFCoreType.VAPOR) {
            this.kitmap = new VaporKitMap(mongoManager.getMongo("kitmap"));
            this.hcf = new VaporHCF(mongoManager.getMongo("hcf"));
        }
        else if (hcfCoreType == HCFCoreType.AZURITE) {
            this.kitmap = new AzuriteKitMap(mongoManager.getMongo("kitmap"));
            this.hcf = new AzuriteHCF(mongoManager.getMongo("hcf"));
        }
        else if (hcfCoreType == HCFCoreType.LAZARUS) {
            this.kitmap = new LazarusKitMap(mongoManager.getMongo("kitmap"));
            this.hcf = new LazarusHCF(mongoManager.getMongo("hcf"));
        }


        ChatUtil.logger("&a[" + BerlinPlugin.getPlugin().getName() + "] HCFCore " + hcfCoreType.name() + " successfully connected.");
    }
}
