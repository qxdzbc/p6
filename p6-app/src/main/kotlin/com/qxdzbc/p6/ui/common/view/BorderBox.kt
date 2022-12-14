package com.qxdzbc.p6.ui.common.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.p6.ui.common.compose.P6TestApp
import java.util.*

/**
 * [padContent]: whether content is overlapped by the border or not. By default, this property is false to mimic the behavior the provided compose border box.
 */
@Composable
fun BorderBox(
    style: EnumSet<BorderStyle> = BorderStyle.ALL,
    borderColor: Color = Color.Black,
    borderThickness: Int = 1,
    modifier: Modifier = Modifier,
    padContent:Boolean = false,
    content: @Composable BoxScope.() -> Unit = {},
) {

    MBox(
        modifier = modifier.drawWithContent {
            drawContent()
            val xWidth = size.width
            val yHeight = size.height
            val t = borderThickness.toFloat()
            if(t >= size.width && t >= size.height){
                drawRect(
                    color = borderColor,
                )
            }else{
                val sideThickness = minOf(t,xWidth)
                val botTopThickness = minOf(t,yHeight)
                if (BorderStyle.__TOP in style) {
                    drawLine(
                        color = borderColor,
                        start = Offset(0f, 0f+botTopThickness/2), // top left
                        end = Offset(xWidth, 0f+botTopThickness/2), // top right
                        strokeWidth = botTopThickness,

                        )
                }
                if (BorderStyle.__LEFT in style) {
                    drawLine(
                        color = borderColor,
                        start = Offset(0f+sideThickness/2, 0f), //top left
                        end = Offset(0f+sideThickness/2, yHeight), //bot left
                        strokeWidth = sideThickness
                    )
                }
                if (BorderStyle.__RIGHT in style) {
                    drawLine(
                        color = borderColor,
                        start = Offset(xWidth-sideThickness/2, 0f), // top right
                        end = Offset(xWidth-sideThickness/2, yHeight), // bot right
                        strokeWidth = sideThickness
                    )
                }
                if (BorderStyle.__BOT in style) {
                    drawLine(
                        color = borderColor,
                        start = Offset(0f, yHeight-botTopThickness/2), // bot left
                        end = Offset(xWidth, yHeight-botTopThickness/2), // bot right
                        strokeWidth = botTopThickness
                    )
                }
            }
        }
    ) {
        if(padContent){
            MBox(modifier=Modifier.padding(
                top = (if(BorderStyle.__TOP in style) borderThickness else 0).dp,
                bottom = (if(BorderStyle.__BOT in style) borderThickness else 0).dp,
                start = (if(BorderStyle.__LEFT in style) borderThickness else 0).dp,
                end = (if(BorderStyle.__RIGHT in style) borderThickness else 0).dp,
            )){
                content()
            }
        }else{
            content()
        }
    }
}

fun main() = P6TestApp {
    val size = DpSize(200.dp, 100.dp)
    Column(modifier = Modifier.padding(20.dp)) {
        MBox(modifier = Modifier.size(size).border(10.dp, Color.Black)){
            Text("ABC")
        }
        Row (modifier = Modifier.padding(top=30.dp, bottom =10.dp,)) {
            BorderBox(style = BorderStyle.TOP_LEFT,
                modifier = Modifier.padding(end=10.dp).size(size).background(Color.Cyan),
                borderColor = Color.Black.copy(alpha =0.7f),
                padContent = true,
                borderThickness = 80) {
                Text("ABC")
            }
            MBox(modifier = Modifier.size(size).border(10.dp, Color.Black)){
                Text("ABC")
            }
        }

        BorderBox(style = BorderStyle.BOT_RIGHT, modifier = Modifier.size(size).background(Color.Cyan), borderThickness = 10,padContent = false,) {
            Text("ABC")
        }
    }
}
