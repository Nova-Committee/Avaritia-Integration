package committee.nova.mods.avaritia_integration.api.load;

import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LoadRuleStoreTest {

    @TempDir
    Path directory;

    @Test
    void changingEnabledPreservesAuthorRulesAndUnknownFields() throws IOException {
        Path path = directory.resolve("rules.json");
        String original = """
                {"module":{"dependencies":[{"modid":"upstream","minVersion":"2"}],"author":{"note":"keep"}},
                "other":{"enabled":false,"custom":[1,2]}}
                """;
        Files.writeString(path, original);
        new LoadRuleStore(path).saveEnabled(original, Map.of("module", false));
        var expected = JsonParser.parseString(original).getAsJsonObject();
        expected.getAsJsonObject("module").addProperty("enabled", false);
        assertEquals(expected, JsonParser.parseString(Files.readString(path)));
        assertFalse(Files.exists(directory.resolve("rules.json.tmp")));
    }

    @Test
    void externalEditRejectsStaleSaveWithoutChangingEitherRule() throws IOException {
        Path path = directory.resolve("rules.json");
        String original = "{\"module\":{}}";
        String external = "{\"module\":{\"enabled\":true,\"note\":\"external\"}}";
        Files.writeString(path, external);
        assertThrows(IOException.class, () -> new LoadRuleStore(path).saveEnabled(original, Map.of("module", false)));
        assertEquals(external, Files.readString(path));
        assertFalse(Files.exists(directory.resolve("rules.json.tmp")));
    }

    @Test
    void recoveryKeepsCommittedFileAndDiscardsInterruptedTemporaryWrite() throws IOException {
        Path path = directory.resolve("rules.json");
        String committed = "{\"module\":{\"enabled\":true}}";
        Files.writeString(path, committed);
        Files.writeString(directory.resolve("rules.json.tmp"), "{unfinished");
        assertEquals(committed, new LoadRuleStore(path).read());
        assertFalse(Files.exists(directory.resolve("rules.json.tmp")));
    }

    @Test
    void failedTemporaryCleanupDoesNotReplaceCommittedConfiguration() throws IOException {
        Path path = directory.resolve("rules.json");
        String committed = "{\"module\":{}}";
        Files.writeString(path, committed);
        Path temporary = Files.createDirectory(directory.resolve("rules.json.tmp"));
        Files.writeString(temporary.resolve("obstruction"), "occupied");
        assertThrows(IOException.class, () -> new LoadRuleStore(path).saveEnabled(committed, Map.of("module", false)));
        assertEquals(committed, Files.readString(path));
    }
}
