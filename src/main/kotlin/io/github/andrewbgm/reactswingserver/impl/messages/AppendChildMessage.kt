package io.github.andrewbgm.reactswingserver.impl.messages

import com.google.gson.annotations.*
import io.github.andrewbgm.reactswingserver.api.*

data class AppendChildMessage(
  @Expose val parentId: Number,
  @Expose val childId: Number
) : Message
