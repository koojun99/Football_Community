package honajun.football_community.global.security.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;

@Converter
public class AESAttributeConverter implements AttributeConverter<String, String> {

    private static final String AES = "AES";

    @Value("${aes.key}")
    private String SECRET_KEY; // 16-byte key for AES-128

    @Override
    public String convertToDatabaseColumn(String attribute) {
        try {
            if (attribute == null) {
                return null;
            }
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(SECRET_KEY.getBytes(), AES)); // 키 설정
            return Base64.getEncoder().encodeToString(cipher.doFinal(attribute.getBytes())); // 암호화 이후 Base64로 인코딩
        } catch (Exception e) {
            throw new IllegalStateException("Error encrypting data", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null) {
                return null;
            }
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(SECRET_KEY.getBytes(), AES));
            return new String(cipher.doFinal(Base64.getDecoder().decode(dbData))); // 복호화 이후 문자열로 변환
        } catch (Exception e) {
            throw new IllegalStateException("Error decrypting data", e);
        }
    }
}
