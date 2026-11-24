import gg.meza.stonecraft.mod
import me.modmuss50.mpp.ReleaseType

plugins {
    id("gg.meza.stonecraft")
}

val awFile =
    when {
        stonecutter.current.parsed <= "1.20.4" -> project.rootProject.layout.projectDirectory.file("src/main/resources/accesswideners/1.20.accesswidener")
        stonecutter.current.parsed <= "1.21.4" -> project.rootProject.layout.projectDirectory.file("src/main/resources/accesswideners/1.21.accesswidener")
        else -> project.rootProject.layout.projectDirectory.file("src/main/resources/accesswideners/26.1.accesswidener")
    }

modSettings {
    accessWidenerLocation = awFile

    clientOptions {
        fov = 90
        guiScale = 3
        narrator = false
        musicVolume = 0.0
    }
}

loom {
    if (mod.isForge) {
        forge {
            mixinConfigs("${mod.id}.mixins.json",)
        }
    }
}

stonecutter {
    replacements {
        string {
            direction = eval(current.version, ">=1.21.11")
            replace("location", "identifier")
            replace("import net.minecraft.Util;", "import net.minecraft.util.Util;")
            replace("ResourceLocation", "Identifier")
            replace("playC2S", "serverboundPlay")
            replace("playS2C", "clientboundPlay")
            replace("sendToServer", "sendToAllPlayers")
        }
    }
}

java {
    withSourcesJar()

    val javaVersion = when {
        stonecutter.eval(stonecutter.current.version, "<=1.20.4") -> 17
        stonecutter.eval(stonecutter.current.version, "<=1.21.4") -> 21
        else -> 25
    }

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }

    sourceCompatibility = JavaVersion.toVersion(javaVersion)
    targetCompatibility = JavaVersion.toVersion(javaVersion)
}

if (mod.isForge) { // FORGE FIX for Gradle
    tasks.withType<JavaExec>().configureEach {
        dependsOn("generatePackMCMetaJson")
    }
}

publishMods {
    type=ReleaseType.STABLE

    modrinth {
         if (mod.isFabric) requires("fabric-api")
    }

    curseforge {
         client = true
         server = true
         if (mod.isFabric) requires("fabric-api")
    }
}
