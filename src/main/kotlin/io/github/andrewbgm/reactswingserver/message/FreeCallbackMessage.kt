package io.github.andrewbgm.reactswingserver.message

import com.google.gson.annotations.*

data class FreeCallbackMessage(
  @Expose val callbackId: Int,
) : IMessage