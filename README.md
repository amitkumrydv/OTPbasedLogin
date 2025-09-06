-- Flipkart OTP Login Automation – Code Explanation

📁 Package Declaration
package com.flipkart.login;


Defines the package name. Helps organize code and avoid name conflicts.

## 📦 Imports
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;


Imports Java libraries for:

Email reading (javax.mail, Properties)

Regex matching (Pattern, Matcher)

Imports Selenium for browser automation

Uses WebDriverManager to manage the ChromeDriver automatically

🔐 Gmail IMAP Credentials Setup
static String host = "imap.gmail.com";
static String user = System.getenv("EMAIL");      // Your Gmail address from environment variable
static String password = System.getenv("PASSWORD"); // 16-character Gmail App Password


Sets the Gmail server (IMAP)

Loads credentials securely using environment variables

🚀 Main Method Execution Starts
public static void main(String[] args) throws Exception {


This is the entry point of the program. throws Exception is used for simplicity to handle any unexpected errors.

🧭 WebDriver Setup
WebDriverManager.chromedriver().setup();
WebDriver driver = new ChromeDriver();
driver.manage().window().maximize();


Sets up ChromeDriver using WebDriverManager (auto-handles driver binaries)

Initializes Chrome browser and maximizes window

🌐 Open Flipkart Login Page
driver.get("https://www.flipkart.com/account/login");


Navigates to Flipkart login page.

🔐 Log Credentials (for Debugging Only)
System.out.println("EMAIL  "+ user);
System.out.println("password  "+ password);


Prints email and password for debugging – ⚠️ Don’t use in production!

📲 Enter Email/Mobile and Request OTP
driver.findElement(By.xpath("//form[@autocomplete='on']//input[@type='text']")).sendKeys(user);
driver.findElement(By.xpath("//button[text()='Request OTP']")).click();


Enters the user's email/mobile number

Clicks the Request OTP button to trigger OTP generation

⏳ Wait for OTP Email (Fixed Sleep)
Thread.sleep(15000);


Waits for 15 seconds to allow OTP email to arrive. Replace this with dynamic wait or polling in production.

📧 Fetch OTP from Gmail
String otp = getOtpFromEmail();


Calls a custom method to read the OTP from Gmail using IMAP.

❌ Handle Missing OTP
if (otp == null) {
    throw new RuntimeException("OTP not found in email!");
}


If OTP is not found in the email inbox, throw an error and stop the program.

🔢 Enter OTP Digit by Digit
char[] otpDigits = otp.toCharArray();
for (int i = 0; i < otpDigits.length; i++) {
    Thread.sleep(1000);
    String xpath = "//form//div[@class='XDRRi5']//div[" + (i + 1) + "]//input[@class='r4vIwl IX3CMV']";
    WebElement otpBox = driver.findElement(By.xpath(xpath));
    otpBox.sendKeys(Character.toString(otpDigits[i]));
}


Splits the 6-digit OTP into individual characters

Sends each digit to the corresponding input box using a dynamic XPath

Waits 1 second between each digit entry (helps prevent errors due to page lag)

✅ Click "Verify" Button
driver.findElement(By.xpath("//button[text()='Verify']")).click();
Thread.sleep(5000);
driver.quit();


Clicks the Verify button to submit OTP

Waits for 5 seconds before closing the browser

📩 Email OTP Extraction (Helper Method)
getOtpFromEmail()
public static String getOtpFromEmail() throws Exception {


Connects to the Gmail inbox, searches for the most recent OTP email from Flipkart, and extracts the 6-digit OTP using regex.

Logic inside:

Connects using IMAP

Opens the inbox in READ_ONLY mode

Loops over the last 5 messages to find one from Flipkart

Extracts OTP using a regex: \\b\\d{6}\\b (matches a 6-digit number)

📄 Extract Text from Email Body
private static String getTextFromMessage(Message message) throws Exception {


Extracts plain text or HTML from the email body:

If text/plain, return as is

If text/html, strip tags using regex

Supports multi-part emails

🔐 Security Note

Always use environment variables for credentials (never hardcode)

Gmail App Passwords must be generated via Google Security

🛠️ Improvements (Optional for README)

Replace Thread.sleep() with WebDriverWait

Add logging instead of System.out.println()

Improve error handling (e.g. email not found, site layout change)

Convert hardcoded XPaths to more maintainable locators

Implement email polling instead of fixed delay

📎 Dependencies

Ensure the following libraries are in your project:

Selenium Java

WebDriverManager

JavaMail API (javax.mail)

Java 8+

📌 How to Run

Set the environment variables EMAIL and PASSWORD

Run the program using an IDE or from the command line

It will:

Open Flipkart

Request OTP

Fetch OTP from Gmail

Enter OTP and log in
