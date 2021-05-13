package io.github.andrewbgm.reactswingruntime.impl.component.adapters

import io.github.andrewbgm.reactswingruntime.api.*
import javax.swing.*

class JMenuItemRemoteComponentAdapter : IRemoteComponentAdapter<JMenuItem> {
  override fun create(
    view: IRemoteComponentView,
    props: Map<String, Any?>,
    ctx: IRemoteComponentContext,
  ): JMenuItem = JMenuItem().apply {
    addActionListener {
      ctx.invokeCallback("onAction")
    }

    update(view, this, props, ctx)
  }

  override fun update(
    view: IRemoteComponentView,
    obj: JMenuItem,
    changedProps: Map<String, Any?>,
    ctx: IRemoteComponentContext,
  ) = with(obj) {
    text = changedProps.getOrDefault("text", text) as String?
  }
}
