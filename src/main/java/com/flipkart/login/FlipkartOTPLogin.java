package com.flipkart.login;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class FlipkartOTPLogin {

    static String host = "imap.gmail.com";
    
    static String user = System.getenv("EMAIL");  // your Gmail
    static String password = System.getenv("PASSWORD");  // Gmail app password (16-char)

    public static void main(String[] args) throws Exception {
        // Setup WebDriver
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();

        // Open Flipkart login page
        driver.get("https://www.flipkart.com/account/login");
        
        
        System.out.println("EMAIL  "+ user);
        System.out.println("password  "+ password);

        // Enter email/mobile
        driver.findElement(By.xpath("//form[@autocomplete='on']//input[@type='text']")).sendKeys(user);
        driver.findElement(By.xpath("//button[text()='Request OTP']")).click();

        // Wait for OTP
        Thread.sleep(15000);

        // Fetch OTP from Gmail
        String otp = getOtpFromEmail();
        System.out.println("OTP Fetched: " + otp);

        if (otp == null) {
            throw new RuntimeException("OTP not found in email!");
        }

        // Enter OTP digit by digit
        char[] otpDigits = otp.toCharArray();
        System.out.println("otpDigits lenth " +otpDigits.length);
        for (int i = 0; i < otpDigits.length; i++) {
            // Build dynamic xpath for each digit input
        	  Thread.sleep(1000);
            String xpath = "//form//div[@class='XDRRi5']//div[" + (i + 1) + "]//input[@class='r4vIwl IX3CMV']";
            WebElement otpBox = driver.findElement(By.xpath(xpath));

            otpBox.sendKeys(Character.toString(otpDigits[i]));
        }

        // Click Verify button
        driver.findElement(By.xpath("//button[text()='Verify']")).click();

        Thread.sleep(5000);
        driver.quit();
    }

    public static String getOtpFromEmail() throws Exception {
        Properties props = new Properties();
        props.put("mail.store.protocol", "imaps");

        Session session = Session.getInstance(props);
        Store store = session.getStore("imaps");
        store.connect(host, user, password);

        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);

        int count = inbox.getMessageCount();
        for (int i = count; i > count - 5 && i > 0; i--) {
            Message message = inbox.getMessage(i);
            String subject = message.getSubject();

            if (subject != null && subject.contains("Flipkart Account")) {
                Matcher subjectMatcher = Pattern.compile("\\b\\d{6}\\b").matcher(subject);
                if (subjectMatcher.find()) {
                    return subjectMatcher.group(0);
                }

                String body = getTextFromMessage(message);
                Matcher bodyMatcher = Pattern.compile("\\b\\d{6}\\b").matcher(body);
                if (bodyMatcher.find()) {
                    return bodyMatcher.group(0);
                }
            }
        }
        return null;
    }

    private static String getTextFromMessage(Message message) throws Exception {
        if (message.isMimeType("text/plain")) {
            return message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) message.getContent();
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart part = multipart.getBodyPart(i);
                if (part.isMimeType("text/plain")) {
                    return part.getContent().toString();
                } else if (part.isMimeType("text/html")) {
                    String html = (String) part.getContent();
                    return html.replaceAll("<[^>]*>", "");
                }
            }
        }
        return "";
    }
}
