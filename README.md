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



# Project setup

## Install Git in your system
## open CMD command line window
<img width="865" height="646" alt="image" src="https://github.com/user-attachments/assets/34d85ae8-44c9-4616-8fa1-344d5ee62aec" />

and enter the command : git clone https://github.com/amitkumrydv/OTPbasedLogin.git
<img width="984" height="517" alt="image" src="https://github.com/user-attachments/assets/ed229be4-e3f0-43a9-a57b-5b3176d8d956" />

## After clone import in your eclips ide as maven.

## And after success full import project

* Right click on the project to set the environment variable for email or password

  <img width="1365" height="723" alt="image" src="https://github.com/user-attachments/assets/761880f2-f15c-4742-96fc-4467c1beb103" />

  <img width="865" height="646" alt="image" src="https://github.com/user-attachments/assets/1cd39e7a-9b66-4e4d-8757-c83012c3a61c" />

  <img width="1355" height="635" alt="image" src="https://github.com/user-attachments/assets/969e8e40-1435-4cfd-81ad-f7eb0ac2d21a" />

  <img width="1356" height="583" alt="image" src="https://github.com/user-attachments/assets/a418d59a-450f-4131-8735-5314087eace3" />

 <img width="1346" height="681" alt="image" src="https://github.com/user-attachments/assets/f4fd9a49-fb5d-42fd-a990-ceca4127dd0f" />

 <img width="1365" height="701" alt="image" src="https://github.com/user-attachments/assets/594ac4f4-3735-4ef5-aa8a-34807e3dea50" />

 ## Now you see in the screen short there have mention the passord so we generate password using SMPT.

 * Login you email in the chrome browser ( it only for gmail user not any other such as yahoo, outlook etc.. )
 * search the url in same browser or tab https://myaccount.google.com/apppasswords

   <img width="1360" height="691" alt="image" src="https://github.com/user-attachments/assets/d9af0c10-fe66-4a83-9170-b9c1bb3b5df4" />

   <img width="1365" height="571" alt="image" src="https://github.com/user-attachments/assets/bf58dca0-718e-4421-8cb1-f966e216ce9f" />

   <img width="1326" height="653" alt="image" src="https://github.com/user-attachments/assets/89a66e4a-4bee-439c-b92a-0ce022dd09ea" />

   ## use this passwored over here
   
  <img width="1359" height="681" alt="image" src="https://github.com/user-attachments/assets/c0575253-21c6-4e00-a71c-dce1e57d9230" />

 ## Use any java version it should 1.8 or above.

 ## Once complete all above step you can run the project.
  <img width="1365" height="714" alt="image" src="https://github.com/user-attachments/assets/57568717-70bc-4e49-b226-f2ac3d60c9bd" />

  if not execute you can comment i wiil connect wit you guys...






 














