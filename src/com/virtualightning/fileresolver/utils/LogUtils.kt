package com.virtualightning.fileresolver.utils

import java.util.logging.Logger

val logger = Logger.getLogger("FileResolverHelper")

fun Warn(content : Any?) {
    logger.warning(content.toString())
}

fun Info(content : Any?) {
    logger.info(content.toString())
}
