/*
 * Project hpwp, 2021-07-13T19:01-0400
 *
 * Copyright 2021 Justin Heflin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.heretere.hpwp.gui.util.items;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Texture {
    BLACK_MONITOR(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0d"
                + "XJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzUxYTEwYzc3ZGJlOThjMzFhODJkZGQ5"
                + "YTM2MmE1YThiMTJmNDYyMGY4ZjIwYjg1MDBiNDFjYzE3NTUwN2I4MSJ9fX0="
    ),
    MAILBOX(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5"
                + "lY3JhZnQubmV0L3RleHR1cmUvMTE4MjQ3NmI1ZWZiN2Q2ZjczMzk2NmY0ZmNi"
                + "ZTRiYzMxNDYzNzEyMGI4N2E0NmQ3NzZlZmZmY2QwZjhiMjY1NSJ9fX0="
    ),
    GLOBE(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90"
                + "ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWY1MjQ5ZmQxZDdmZjI5YmF"
                + "mYzZjMzdlMDNjNmU5NDE1ODU1OGZkNmNlZDdiMjk2ZGEyMDEwNmE2MjFjNTkwNSJ9fX0="
    ),
    PLUS_ICON(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90Z"
                + "Xh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2VkZDIwYm"
                + "U5MzUyMDk0OWU2Y2U3ODlkYzRmNDNlZmFlYjI4YzcxN2VlNmJmY2JiZTAyNzgwMTQyZjcxNiJ9fX0="
    ),
    NEXT(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5t"
                + "aW5lY3JhZnQubmV0L3RleHR1cmUvMmEzYjhmNjgxZGFhZDhiZjQzNmNhZThkYTNmZT"
                + "gxMzFmNjJhMTYyYWI4MWFmNjM5YzNlMDY0NGFhNmFiYWMyZiJ9fX0="
    ),
    NEXT_2(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5t"
                + "aW5lY3JhZnQubmV0L3RleHR1cmUvOWM5ZWM3MWMxMDY4ZWM2ZTAzZDJjOTI4N2Y5ZGE5MTkzNjM5Zj"
                + "NhNjM1ZTJmYmQ1ZDg3YzJmYWJlNjQ5OSJ9fX0="
    ),
    BACK(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQub"
                + "mV0L3RleHR1cmUvODY1MmUyYjkzNmNhODAyNmJkMjg2NTFkN2M5ZjI4MTlkMmU5MjM2OT"
                + "c3MzRkMThkZmRiMTM1NTBmOGZkYWQ1ZiJ9fX0="
    ),
    BACK_2(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0"
                + "L3RleHR1cmUvODE2ZWEzNGE2YTZlYzVjMDUxZTY5MzJmMWM0NzFiNzAxMmIyOThkMz"
                + "hkMTc5ZjFiNDg3YzQxM2Y1MTk1OWNkNCJ9fX0="
    ),
    INFORMATION(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L"
                + "3RleHR1cmUvMTY0MzlkMmUzMDZiMjI1NTE2YWE5YTZkMDA3YTdlNzVlZGQyZDUwMTVkM"
                + "TEzYjQyZjQ0YmU2MmE1MTdlNTc0ZiJ9fX0="
    );

    @Getter
    private final String textureValue;
}
