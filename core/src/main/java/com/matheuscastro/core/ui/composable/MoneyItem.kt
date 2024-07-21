package com.matheuscastro.core.ui.composable

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.matheuscastro.core.domain.model.Money

@Composable
fun MoneyItem(
    money: Money,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current
) {
    Text(
        text = "${money.amount / 100} ${money.currency.name}",
        modifier = modifier,
        style = style
    )
}