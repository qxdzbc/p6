package com.qxdzbc.p6.ui.format2

import com.qxdzbc.p6.app.document.range.address.RangeAddress
import io.kotest.matchers.collections.shouldContainOnly
import io.kotest.matchers.shouldBe
import kotlin.test.Test

internal class RangeAddressSetImpTest {
    val r1 = RangeAddressSetImp(listOf("B3:D4", "B5:B6", "C5:D6", "B7:D12").map { RangeAddress(it) }.toSet())
    val bigRange = RangeAddress("B5:G8")

    @Test
    fun getAllIntersectionWith() {
        r1.getAllIntersectionWith(bigRange)
            .shouldBe(
                RangeAddressSetImp(listOf("B5:B6", "C5:D6", "B7:D8").map { RangeAddress(it) }.toSet())
            )
    }

    @Test
    fun getNotIn() {
        RangeAddressSetImp(bigRange).getNotIn(r1) shouldBe RangeAddressSetImp(RangeAddress("E5:G8"))
        RangeAddressSetImp(RangeAddress("D11:F16")).getNotIn(r1) shouldBe RangeAddressSetImp(listOf(
            "E11:F12", "D13:F16"
        ).map { RangeAddress(it) }.toSet()
        )
    }

    @Test
    fun `getNotIn against range`() {
        val ras = RangeAddressSetImp(
            listOf(
                "B1:C12", "E1:E12", "G1:G12"
            ).map { RangeAddress(it) }.toSet()
        )
        val out = (ras.getNotIn(RangeAddress("A10:J18")))
        out.ranges.shouldContainOnly(
            listOf(
                "B1:C9", "E1:E9", "G1:G9"
            ).map { RangeAddress(it) }
        )
    }

    @Test
    fun `getNotIn against range set`() {
        val ras = RangeAddressSetImp(
            listOf(
                "B1:C12",
                // "E1:E12", "G1:G12"
            ).map { RangeAddress(it) }.toSet()
        )
        val rtg = RangeAddressSetImp(
            listOf(
                "A4:B6", "A10:J18"
            ).map { RangeAddress(it) }.toSet()
        )
        val out = ras.getNotIn(rtg)
        println(out)
//        out.ranges.shouldContainOnly(
//            listOf(
//                "B1:C9", "E1:E9", "G1:G9"
//            ).map { RangeAddress(it) }
//        )
    }
}
