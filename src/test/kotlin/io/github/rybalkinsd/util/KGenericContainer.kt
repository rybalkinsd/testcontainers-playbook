package io.github.rybalkinsd.util

import org.testcontainers.containers.GenericContainer
import java.util.concurrent.Future

/**
 * Created by Sergey Rybalkin on 25/11/2018.
 */
class   KGenericContainer : GenericContainer<KGenericContainer> {
    constructor(dockerImageName: String) : super(dockerImageName)
    constructor(image: Future<String>) : super(image)
}