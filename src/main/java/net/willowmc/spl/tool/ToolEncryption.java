package net.willowmc.spl.tool;

import lombok.experimental.UtilityClass;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Encryption Tool for item data.
 */
@UtilityClass
public class ToolEncryption {
    /**
     * Salt, set this to a unique value if you want item values to be encrypted securely.
     * Note that this is somewhat useless if your plugin is open source.
     */
    public String TOOL_SALT = "SPLToolSalt";
    private final Charset CHARSET = StandardCharsets.UTF_8;

    /**
     * Encrypt string data with given salt.
     *
     * @param data data
     * @return encrypted data
     */
    public String encrypt(String data) {
        return b64encode(xor(data.getBytes(CHARSET), TOOL_SALT.getBytes(CHARSET)));
    }

    /**
     * Decrypt string data with given salt.
     *
     * @param data data
     * @return decrypted data
     */
    public String decrypt(String data) {
        return new String(xor(b64decode(data), TOOL_SALT.getBytes(CHARSET)), CHARSET);
    }

    private byte[] xor(byte[] data, byte[] key) {
        byte[] out = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            out[i] = (byte) (data[i] ^ key[i % key.length]);
        }
        return out;
    }

    private String b64encode(byte[] data) {
        return new String(Base64.getEncoder().encode(data), CHARSET);
    }

    private byte[] b64decode(String data) {
        return Base64.getDecoder().decode(data.getBytes(CHARSET));
    }
}
