package de.uulm.automotive.cds

import de.uulm.automotive.cds.services.SchedulingService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

@SpringBootTest
class CdsApplicationTests {

    @TestConfiguration
    class TestConfig {

        @MockBean
        @Autowired
        protected lateinit var schedulingService: SchedulingService
    }


    @Test
    fun contextLoads() {
    }

}
