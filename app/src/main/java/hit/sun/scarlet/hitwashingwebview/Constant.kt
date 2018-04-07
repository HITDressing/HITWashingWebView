package hit.sun.scarlet.hitwashingwebview

import com.gprinter.command.EscCommand
import com.gprinter.command.LabelCommand

object Constant {
    val BLUETOOTH_REQUEST_CODE = 0x001

    fun sendReceiptWithResponse(id: Int, title: String) {
        val esc = EscCommand()
        esc.addInitializePrinter()
        esc.addPrintAndFeedLines(3.toByte())
        // 设置打印居中
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER)
        // 设置为倍高倍宽
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF)
        // 打印文字
        esc.addText("博鑫顺清洁服务\n")
        esc.addPrintAndLineFeed()

        /* 打印文字 */
        // 取消倍高倍宽
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF)
        // 设置打印左对齐
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT)
        // 打印文字
        esc.addText(title)
        // 打印文字
        esc.addPrintAndLineFeed()
        for (i in 0..31) esc.addText("-")
        esc.addPrintAndLineFeed()

        esc.addText("物品名称")
        esc.addSetHorAndVerMotionUnits(7.toByte(), 0.toByte())
        esc.addSetAbsolutePrintPosition(6.toShort())
        esc.addText("数量")
        esc.addSetAbsolutePrintPosition(10.toShort())
        esc.addText("价格")
        esc.addPrintAndLineFeed()

        for (i in 0..2) {
            esc.addText("ABB")
            esc.addSetHorAndVerMotionUnits(7.toByte(), 0.toByte())
            esc.addSetAbsolutePrintPosition(6.toShort())
            esc.addText("100")
            esc.addSetAbsolutePrintPosition(10.toShort())
            esc.addText("65.65")
            esc.addPrintAndLineFeed()
        }

        esc.addPrintAndLineFeed()
        for (i in 0..31) esc.addText("-")
        esc.addPrintAndLineFeed()

        esc.addText("合计：\n")

        // 设置打印左对齐
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER)

        esc.addGeneratePlus(LabelCommand.FOOT.F5, 255.toByte(), 255.toByte())
        esc.addPrintAndFeedLines(8.toByte())

        esc.addQueryPrinterStatus()
        val datas = esc.command
        // 发送数据
        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].sendDataImmediately(datas)
    }
}