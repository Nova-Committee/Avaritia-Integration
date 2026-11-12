package committee.nova.mods.avaritia_integration.api.load;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Objects;

/** Stores next-launch rules without replacing fields owned by pack authors. */
final class LoadRuleStore {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Path path;

    LoadRuleStore(Path path) {
        this.path = path;
    }

    synchronized String read() throws IOException {
        Files.createDirectories(path.getParent());
        try (FileChannel channel = lockChannel(); FileLock ignored = channel.lock()) {
            discardTemporaryFile();
            return readCurrent();
        }
    }

    synchronized String saveEnabled(String expected, Map<String, Boolean> changes) throws IOException {
        JsonObject document = expected == null ? new JsonObject() : JsonParser.parseString(expected).getAsJsonObject();
        changes.forEach((id, enabled) -> {
            if (!document.has(id) || !document.get(id).isJsonObject()) {
                throw new IllegalArgumentException("Missing integration rule: " + id);
            }
            document.getAsJsonObject(id).addProperty("enabled", enabled);
        });
        return save(expected, document);
    }

    synchronized String save(String expected, JsonObject document) throws IOException {
        Files.createDirectories(path.getParent());
        try (FileChannel channel = lockChannel(); FileLock ignored = channel.lock()) {
            discardTemporaryFile();
            if (!Objects.equals(expected, readCurrent())) {
                throw new IOException("Integration rules changed outside this screen. Reload before saving.");
            }
            Path temporary = temporaryPath();
            try {
                byte[] bytes = (GSON.toJson(document) + "\n").getBytes(StandardCharsets.UTF_8);
                try (FileChannel output = FileChannel.open(temporary, StandardOpenOption.CREATE_NEW,
                        StandardOpenOption.WRITE)) {
                    ByteBuffer buffer = ByteBuffer.wrap(bytes);
                    while (buffer.hasRemaining()) output.write(buffer);
                    output.force(true);
                }
                // Recheck even for writers that do not participate in our lock protocol.
                if (!Objects.equals(expected, readCurrent())) {
                    throw new IOException("Integration rules changed while saving. Reload before saving.");
                }
                Files.move(temporary, path, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } finally {
                Files.deleteIfExists(temporary);
            }
            // The rename has committed. Report a durability warning instead of pretending it did not save.
            try (FileChannel directory = FileChannel.open(path.getParent(), StandardOpenOption.READ)) {
                directory.force(true);
                return null;
            } catch (IOException | UnsupportedOperationException e) {
                return "Saved, but directory sync is unavailable: " + e.getMessage();
            }
        }
    }

    private FileChannel lockChannel() throws IOException {
        return FileChannel.open(path.resolveSibling(path.getFileName() + ".lock"),
                StandardOpenOption.CREATE, StandardOpenOption.WRITE);
    }

    private String readCurrent() throws IOException {
        return Files.exists(path) ? Files.readString(path, StandardCharsets.UTF_8) : null;
    }

    private Path temporaryPath() {
        return path.resolveSibling(path.getFileName() + ".tmp");
    }

    private void discardTemporaryFile() throws IOException {
        // Holding the sibling lock proves no cooperating writer owns this temporary file.
        if (Files.deleteIfExists(temporaryPath())) {
            com.mojang.logging.LogUtils.getLogger().warn("Removed orphan integration rules temporary file: {}",
                    temporaryPath());
        }
    }
}
