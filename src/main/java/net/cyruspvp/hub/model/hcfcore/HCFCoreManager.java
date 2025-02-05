package net.cyruspvp.hub.model.hcfcore;

import net.cyruspvp.hub.BerlinPlugin;
import net.cyruspvp.hub.model.hcfcore.vapor.VaporHCF;
import net.cyruspvp.hub.model.hcfcore.vapor.VaporKitMap;
import net.cyruspvp.hub.utilities.Berlin;
import net.cyruspvp.hub.database.mongo.MongoManager;
import net.cyruspvp.hub.model.hcfcore.azurite.AzuriteHCF;
import net.cyruspvp.hub.model.hcfcore.azurite.AzuriteKitMap;
import net.cyruspvp.hub.model.hcfcore.lazarus.LazarusHCF;
import net.cyruspvp.hub.model.hcfcore.lazarus.LazarusKitMap;
import net.cyruspvp.hub.utilities.ChatUtil;
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
