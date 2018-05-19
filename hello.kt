﻿import kotlinx.cinterop.*
import libui.*

fun main(args: Array<String>) = memScoped {
    val options = alloc<uiInitOptions>()
    val error = uiInit(options.ptr)
//  if (error != null) throw Error("Error: '${error.toKString()}'")
//TODO: if uncomment previous line - program just silently exits.
/*TODO: should work like this:
    uiInitOptions options;
    memset(&options, 0, sizeof(options));
    if (uiInit(&options) != NULL)
        abort();
*/

    val window = uiNewWindow("Hello", 320, 240, 0)
    uiWindowSetMargined(window, 1)

    val box = uiNewVerticalBox()
    uiBoxSetPadded(box, 1)
    uiWindowSetChild(window, box?.reinterpret())

    val scroll = uiNewMultilineEntry()
    uiMultilineEntrySetReadOnly(scroll, 1)
    val button = uiNewButton("libui говорит: click me!")
    fun saySomething(box: CPointer<uiButton>?, scroll: COpaquePointer?) {
        uiMultilineEntryAppend(scroll?.reinterpret(),
            "Hello, World!  Ciao, mondo!\n" +
            "Привет, мир!  你好，世界！\n\n")
    }
    uiButtonOnClicked(button, staticCFunction(::saySomething), scroll)
    uiBoxAppend(box, button?.reinterpret(), 0)
    uiBoxAppend(box, scroll?.reinterpret(), 1)

    fun onClosing(window: CPointer<uiWindow>?, data: COpaquePointer?): Int {
        uiQuit()
        return 1
    }
    uiWindowOnClosing(window, staticCFunction(::onClosing), null)
    uiControlShow(window?.reinterpret())
    uiMain()
}