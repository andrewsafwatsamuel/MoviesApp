package com.example

import io.ktor.utils.io.errors.IOException

class NotConnectedException : IOException("Not connected")