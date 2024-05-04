package DBMS.MajorAssignment1.Auth;

import java.util.Random;

public class CaptchaManager {
    private static final int CAPTCHA_LENGTH = 8;
    private final Random random = new Random();
    private String generatedCaptcha;

    public CaptchaManager() {
        generatedCaptcha = generateRandomNumericCaptcha();
    }

    public String getGeneratedCaptcha() {
        return generatedCaptcha;
    }

    public boolean validateCaptcha(String userInput) {
        return generatedCaptcha.equals(userInput);
    }

    private String generateRandomNumericCaptcha() {
        StringBuilder captcha = new StringBuilder();

        for (int i = 0; i < CAPTCHA_LENGTH; i++) {
            int digit = random.nextInt(10);
            captcha.append(digit);
        }

        return captcha.toString();
    }
}
