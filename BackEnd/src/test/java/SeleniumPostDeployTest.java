package org.example.e2e;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@Tag("e2e") // Tag para permitir filtrar a execução (ex: ./gradlew test --tests *Selenium*)
public class SeleniumPostDeployTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private String baseUrl;

    @BeforeEach
    void setUp() {
        // Pega URL do ambiente ou usa padrão
        baseUrl = System.getenv("APP_URL");
        if (baseUrl == null) baseUrl = "http://localhost:8080";

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        // Se estiver rodando no GitHub Actions (CI), usa modo Headless (sem janela)
        if (System.getenv("CI") != null) {
            options.addArguments("--headless");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
        }

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    void deveAcessarPaginaPrincipalECarregarTabela() {
        driver.get(baseUrl + "/index.html");

        // 1. Verifica Título
        String titulo = driver.getTitle();
        Assertions.assertTrue(titulo.contains("Func."), "Título da página incorreto: " + titulo);

        // 2. Aguarda a tabela aparecer (garante que JS executou)
        // Isso falharia se o backend estivesse fora do ar (API error)
        try {
            // Espera até que o corpo da tabela tenha conteúdo ou que o botão 'Novo' esteja clicável
            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("table")));

            WebElement botaoNovo = driver.findElement(By.linkText("Novo Funcionário"));
            Assertions.assertTrue(botaoNovo.isDisplayed());

        } catch (Exception e) {
            Assertions.fail("A interface não carregou corretamente. Backend pode estar offline.");
        }
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}