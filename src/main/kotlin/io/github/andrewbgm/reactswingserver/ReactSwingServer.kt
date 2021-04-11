package io.github.andrewbgm.reactswingserver

import com.google.gson.*
import io.github.andrewbgm.reactswingserver.bridge.*
import io.github.andrewbgm.reactswingserver.gson.*
import io.github.andrewbgm.reactswingserver.messages.*
import io.javalin.*
import io.javalin.plugin.json.*
import io.javalin.websocket.*

class ReactSwingServer {
  private val app: Javalin by lazy { configureApp() }

  private val bridge: ReactSwingServerBridge by lazy { ReactSwingServerBridge() }

  fun start(
    port: Int
  ) {
    app.start(port)
  }

  fun stop() {
    app.stop()
  }

  private fun handleClose(
    ctx: WsCloseContext
  ) {
    println("Session #${ctx.sessionId} closed.")
  }

  private fun handleConnect(
    ctx: WsConnectContext
  ) {
    println("Session #${ctx.sessionId} opened.")
  }

  private fun handleError(
    ctx: WsErrorContext
  ) {
    println("Connection error: ${ctx.error()}")
  }

  private fun handleMessage(
    ws: WsMessageContext
  ) {
    when (val message = ws.message<IMessage>()) {
      is AppendChildMessage -> bridge.appendChild(
        ws,
        message.parentId,
        message.childId
      )
      is AppendChildToContainerMessage -> bridge.appendChildToContainer(
        ws,
        message.containerId,
        message.childId
      )
      is AppendInitialChildMessage -> bridge.appendInitialChild(
        ws,
        message.parentId,
        message.childId
      )
      is ClearContainerMessage -> bridge.clearContainer(ws, message.containerId)
      is CommitTextUpdateMessage -> bridge.commitTextUpdate(
        ws,
        message.instanceId,
        message.oldText,
        message.newText
      )
      is CommitUpdateMessage -> bridge.commitUpdate(
        ws,
        message.type,
        message.instanceId,
        message.changedProps
      )
      is CreateInstanceMessage -> bridge.createInstance(
        ws,
        message.instanceId,
        message.type,
        message.props
      )
      is CreateTextInstanceMessage -> bridge.createTextInstance(
        ws,
        message.instanceId,
        message.text
      )
      is FreeCallbackMessage -> bridge.freeCallback(ws, message.callbackId)
      is InsertBeforeMessage -> bridge.insertBefore(
        ws,
        message.parentId,
        message.childId,
        message.beforeChildId
      )
      is InsertInContainerBeforeMessage -> bridge.insertInContainerBefore(
        ws,
        message.containerId,
        message.childId,
        message.beforeChildId
      )
      is InvokeCallbackMessage -> bridge.invokeCallback(
        ws,
        message.callbackId,
        message.args
      )
      is RemoveChildFromContainerMessage -> bridge.removeChildFromContainer(
        ws,
        message.containerId,
        message.childId
      )
      is RemoveChildMessage -> bridge.removeChild(
        ws,
        message.parentId,
        message.childId
      )
      is StartApplicationMessage -> bridge.startApplication(
        ws,
        message.containerId
      )
      else -> println(message)
    }
  }

  private fun handleServerStarting() {
    println("Server starting...")
  }

  private fun handleServerStarted() {
    println("Server started!")
  }

  private fun handleServerStartFailed() {
    println("Server failed to start!")
  }

  private fun handleServerStopping() {
    println("Server stopping...")
  }

  private fun handleServerStopped() {
    println("Server stopped!")
  }

  private fun configureApp(): Javalin {
    configureGson()

    return Javalin.create()
      .events {
        it.serverStarting(::handleServerStarting)
        it.serverStarted(::handleServerStarted)
        it.serverStartFailed(::handleServerStartFailed)

        it.serverStopping(::handleServerStopping)
        it.serverStopped(::handleServerStopped)
      }
      .ws("/") { ws ->
        ws.onClose(::handleClose)
        ws.onConnect(::handleConnect)
        ws.onError(::handleError)
        ws.onMessage(::handleMessage)
      }
  }

  private fun configureGson() {
    val gson = GsonBuilder()
      .excludeFieldsWithoutExposeAnnotation()
      .registerTypeAdapter(
        IMessage::class.java, MessageAdapter(
          AppendChildMessage::class,
          AppendChildToContainerMessage::class,
          AppendInitialChildMessage::class,
          ClearContainerMessage::class,
          CommitTextUpdateMessage::class,
          CommitUpdateMessage::class,
          CreateInstanceMessage::class,
          CreateTextInstanceMessage::class,
          FreeCallbackMessage::class,
          InsertBeforeMessage::class,
          InsertInContainerBeforeMessage::class,
          InvokeCallbackMessage::class,
          RemoveChildFromContainerMessage::class,
          RemoveChildMessage::class,
          StartApplicationMessage::class,
        )
      )
      .create()

    JavalinJson.fromJsonMapper = object : FromJsonMapper {
      override fun <T> map(
        json: String,
        targetClass: Class<T>,
      ) = gson.fromJson(json, targetClass)
    }

    JavalinJson.toJsonMapper = object : ToJsonMapper {
      override fun map(
        obj: Any,
      ): String = if (obj is IMessage) gson.toJson(
        obj,
        IMessage::class.java
      ) else gson.toJson(obj)
    }
  }
}
