package mod.syconn.accuratemaps.platform.forge;

//? if forge {

import mod.syconn.accuratemaps.platform.forge.networking.ForgeNetwork;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import static mod.syconn.accuratemaps.platform.forge.ForgeAccurateMaps.MOD;

@Mod(MOD)
public class ForgeAccurateMaps {
    public static final String MOD = "accuratemaps";

    public ForgeAccurateMaps(FMLJavaModLoadingContext context) {
        ForgeNetwork.register();
    }
}
//? }