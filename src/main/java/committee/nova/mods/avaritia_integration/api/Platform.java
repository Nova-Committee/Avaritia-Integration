package committee.nova.mods.avaritia_integration.api;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.data.loading.DatagenModLoader;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.LoadingModList;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.apache.maven.artifact.versioning.ArtifactVersion;

import java.nio.file.Path;
import java.util.List;

/**
 * @author: cnlimiter
 */
public class Platform {
    private Platform() {
    }

    public static boolean isModLoaded(String id) {
        ModList modList = ModList.get();
        if (modList == null) {
            return LoadingModList.get().getModFileById(id) != null;
        }
        return modList.isLoaded(id);
    }

    public static boolean isPhysicalClient() {
        return FMLEnvironment.dist.isClient();
    }

    public static MinecraftServer getServer() {
        return ServerLifecycleHooks.getCurrentServer();
    }

    public static boolean isProduction() {
        return FMLEnvironment.production;
    }

    public static boolean isDataGen() {
        return DatagenModLoader.isRunningDataGen();
    }

    public static Path getConfigDir() {
        return FMLPaths.CONFIGDIR.get();
    }

    public static int getVersionNumber(String id) {
        ModContainer container = ModList.get().getModContainerById(id).orElseThrow();
        ArtifactVersion version = container.getModInfo().getVersion();
        List<Integer> versions = List.of(version.getMajorVersion(), version.getMinorVersion(), version.getIncrementalVersion());
        int result = 0;
        for (int i : versions) {
            int group = Math.min(i, 99);
            result = result * 100 + group;
        }
        return result;
    }

    public static Platform.Type getPlatform() {
        return Type.Forge;
    }

    public static Platform.Type getPlatformSeries() {
        return Type.Forge;
    }


    public enum Type {
        Vanilla, Fabric, Quilt, Forge, NeoForge, Unknown
    }
}
