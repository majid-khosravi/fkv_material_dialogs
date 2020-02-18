/**
 * Designed and developed by Aidan Follestad (@afollestad)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:Suppress("unused")

package com.afollestad.materialdialogs.checkbox

import android.view.View
import android.widget.CheckBox
import androidx.annotation.CheckResult
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.widget.CompoundButtonCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.R
import com.afollestad.materialdialogs.utils.MDUtil.assertOneSet
import com.afollestad.materialdialogs.utils.MDUtil.createColorSelector
import com.afollestad.materialdialogs.utils.MDUtil.maybeSetTextColor
import com.afollestad.materialdialogs.utils.MDUtil.resolveString
import com.afollestad.materialdialogs.utils.resolveColors

typealias BooleanCallback = ((Boolean) -> Unit)?

@CheckResult
fun MaterialDialog.getCheckBoxPrompt(): CheckBox {
  return view.buttonsLayout?.checkBoxPrompt ?: throw IllegalStateException(
      "The dialog does not have an attached buttons layout."
  )
}

@CheckResult
fun MaterialDialog.isCheckPromptChecked() = getCheckBoxPrompt().isChecked

/**
 * @param res The string resource to display for the checkbox label.
 * @param text The literal string to display for the checkbox label.
 * @param isCheckedDefault Whether or not the checkbox is initially checked.
 * @param onToggle A listener invoked when the checkbox is checked or unchecked.
 */
fun MaterialDialog.checkBoxPrompt(
  @StringRes res: Int = 0,
  text: String? = null,
  isCheckedDefault: Boolean = false,
  @ColorRes textColorRes: Int? = null,
  onToggle: BooleanCallback
): MaterialDialog {
  assertOneSet("checkBoxPrompt", text, res)
  view.buttonsLayout?.checkBoxPrompt?.run {
    this.visibility = View.VISIBLE
    this.text = text ?: resolveString(this@checkBoxPrompt, res)
    this.isChecked = isCheckedDefault
    this.setOnCheckedChangeListener { _, checked ->
      onToggle?.invoke(checked)
    }
    maybeSetTextColor(windowContext, colorRes = textColorRes, attrRes = R.attr.md_color_content)
    bodyFont?.let(this::setTypeface)

    val widgetAttrs = intArrayOf(R.attr.md_color_widget, R.attr.md_color_widget_unchecked)
    resolveColors(attrs = widgetAttrs).let {
      CompoundButtonCompat.setButtonTintList(
          this,
          createColorSelector(windowContext, checked = it[0], unchecked = it[1])
      )
    }
  }
  return this
}
