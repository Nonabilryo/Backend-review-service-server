package nonabili.reviewserviceserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients(basePackages = ["nonabili.reviewserviceserver.client"])
class ReviewServiceServerApplication

fun main(args: Array<String>) {
    runApplication<ReviewServiceServerApplication>(*args)
}
