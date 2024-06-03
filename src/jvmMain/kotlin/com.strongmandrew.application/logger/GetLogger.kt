package com.strongmandrew.application.logger

import java.util.logging.Logger

fun <T : Any> T.getLogger(): Logger = Logger.getLogger(this::class.simpleName)