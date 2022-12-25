package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static data.DataGenerator.generateDate;

class DeliveryTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        String city = "Оренбург";
        var validUser = DataGenerator.Registration.generateUser("ru");

        int addDays = 7;

        int addDaysDefault = 3;

        $("[data-test-id=city] input").setValue(city.substring(0, 2));
        $$(".menu-item__control").findBy(text(city)).click();

        $("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.DELETE);
        if (!generateDate(addDaysDefault, "MM").equals(generateDate(addDays, "MM"))) {
            $("[data-step='1']").click();
            $$(".calendar__day").findBy(text(generateDate(addDays, "d"))).click();

            $("[data-test-id=name] input").setValue(validUser.getName());
            $("[data-test-id=phone] input").setValue(validUser.getPhone());
            $("[data-test-id=agreement]").click();
            $x("//*[contains(text(),'Забронировать')]").click();
            $(".notification__content")
                    .shouldBe(Condition.visible, Duration.ofSeconds(15))
                    .shouldHave(Condition.exactText("Встреча успешно забронирована на " + generateDate(addDays, "dd.MM.yyyy")));

        }

    }

}

