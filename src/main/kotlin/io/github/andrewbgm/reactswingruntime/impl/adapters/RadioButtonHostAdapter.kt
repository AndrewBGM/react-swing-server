package io.github.andrewbgm.reactswingruntime.impl.adapters

import io.github.andrewbgm.reactswingruntime.api.*
import java.awt.event.*
import javax.swing.*

class RadioButtonHostAdapter : IHostAdapter<JRadioButton> {
  override fun create(
    props: Map<String, Any?>,
    ctx: IHostContext,
  ): JRadioButton = JRadioButton().apply {
    isSelected = props.getOrDefault("initialValue", false) as Boolean

    addItemListener {
      ctx.invokeCallback("onChange", listOf(it.stateChange == ItemEvent.SELECTED))
    }

    update(this, props, ctx)
  }

  override fun update(
    host: JRadioButton,
    changedProps: Map<String, Any?>,
    ctx: IHostContext,
  ) = with(host) {
    text = changedProps.getOrDefault("children", text) as String?
  }

  override fun setChildren(
    host: JRadioButton,
    children: List<Any>,
    ctx: IHostContext,
  ) = error("Cannot set children for $host")

  override fun appendChild(
    host: JRadioButton,
    child: Any,
    ctx: IHostContext,
  ) = error("Cannot append $child to $host")

  override fun appendToContainer(
    host: JRadioButton,
    ctx: IHostContext,
  ) = error("Cannot append $host to container")

  override fun removeChild(
    host: JRadioButton,
    child: Any,
    ctx: IHostContext,
  ) = error("Cannot remove $child from $host")

  override fun removeFromContainer(
    host: JRadioButton,
    ctx: IHostContext,
  ) = error("Cannot remove $host from container")

  override fun insertChild(
    host: JRadioButton,
    child: Any,
    beforeChild: Any,
    ctx: IHostContext,
  ) = error("Cannot insert $child in $host before $beforeChild")

  override fun insertInContainer(
    host: JRadioButton,
    beforeChild: Any,
    ctx: IHostContext,
  ) = error("Cannot insert $host in container before $beforeChild")
}
