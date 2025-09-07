# Flipkart OTP Login Automation – Code Explanation

## Automates the OTP-based login process on Flipkart using Selenium WebDriver and Gmail IMAP.

## Package Declaration
* package com.flipkart.login; Defines the package name. Helps organize code and avoid name conflicts.

📦 Imports
```import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;
```


## Purpose:

* javax.mail, Properties: Reading OTP emails from Gmail
* Pattern, Matcher: Regex matching to extract OTP
* Selenium: Browser automation
* WebDriverManager: Auto-manages ChromeDriver

## Gmail IMAP Credentials Setup
```
static String host = "imap.gmail.com";
static String user = System.getenv("EMAIL");      // Your Gmail address from environment variable
static String password = System.getenv("PASSWORD"); // 16-character Gmail App Password
```
* Connects to Gmail's IMAP server
* Loads credentials securely using environment variables
*** Never hardcode credentials.

## Main Method Execution Starts
```
public static void main(String[] args) throws Exception {
```
*** Entry point of the program throws Exception for simplicity in handling any errors

## WebDriver Setup
```
WebDriverManager.chromedriver().setup();
WebDriver driver = new ChromeDriver();
driver.manage().window().maximize();
```
## Sets up ChromeDriver using WebDriverManager Launch and maximizes Chrome browser

🌐 Open Flipkart Login Page
```
driver.get("https://www.flipkart.com/account/login");
```

## Navigates to Flipkart's login page

🔐 Log Credentials (for Debugging Only)
```
System.out.println("EMAIL  " + user);
System.out.println("password  " + password);
```
⚠️ Don't use this in production!

## Enter Email/Mobile and Request OTP
```
driver.findElement(By.xpath("//form[@autocomplete='on']//input[@type='text']")).sendKeys(user);
driver.findElement(By.xpath("//button[text()='Request OTP']")).click();
```
Inputs email/mobile
Clicks "Request OTP" button

## Wait for OTP Email (Fixed Sleep)
```
Thread.sleep(15000);
```
Waits 15 seconds for OTP email.

## Fetch OTP from Gmail
```
String otp = getOtpFromEmail();
```
* Calls helper method to read OTP from email using IMAP
* Handle Missing OTP
```
if (otp == null) {
    throw new RuntimeException("OTP not found in email!");
}
```
* Terminates the program if OTP is not found 

## Enter OTP Digit by Digit
```
char[] otpDigits = otp.toCharArray();
for (int i = 0; i < otpDigits.length; i++) {
    Thread.sleep(1000);
    String xpath = "//form//div[@class='XDRRi5']//div[" + (i + 1) + "]//input[@class='r4vIwl IX3CMV']";
    WebElement otpBox = driver.findElement(By.xpath(xpath));
    otpBox.sendKeys(Character.toString(otpDigits[i]));
}
```
* Splits 6-digit OTP into characters
* Sends each digit to the respective OTP input box
* Uses dynamic XPath for locating input fields

## Click "Verify" Button
```
driver.findElement(By.xpath("//button[text()='Verify']")).click();
Thread.sleep(5000);
driver.quit();
```
* Clicks "Verify" after entering OTP
* Waits and closes browser

## Email OTP Extraction – Helper Method
```
getOtpFromEmail()
public static String getOtpFromEmail() throws Exception {
    // Connect to Gmail, search for Flipkart email, extract 6-digit OTP using regex
}
```
* Connects using IMAP
* Opens inbox in READ_ONLY mode
* Loops over last 5 messages to find one from Flipkart
* Extracts OTP using:
```
Pattern.compile("\\b\\d{6}\\b")
```

## Extract Text from Email Body
```
getTextFromMessage(Message message)
```
* Handles multi-part email content
* Supports both text/plain and text/html


## Security Note
* Use environment variables for credentials
* Generate App Password from Google Account Security
