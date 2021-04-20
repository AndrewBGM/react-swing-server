package io.github.andrewbgm.reactswingserver.message

import com.google.gson.annotations.*

data class CreateInstanceMessage(
  @Expose val instanceId: Int,
  @Expose val type: String,
  @Expose val props: Map<String, Any?>,
) : IMessage