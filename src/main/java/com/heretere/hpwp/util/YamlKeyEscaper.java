package com.heretere.hpwp.util;

import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

public class YamlKeyEscaper {
    public static String escape(final @NotNull String input) {
        return StringUtils.replace(input, " ", "-");
    }
}
