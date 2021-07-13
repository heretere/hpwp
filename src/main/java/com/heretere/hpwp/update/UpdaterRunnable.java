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

package com.heretere.hpwp.update;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.AbstractMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.gson.JsonParser;
import com.heretere.hpwp.config.pojos.GlobalVariables;
import com.heretere.hpwp.tasks.Chain;
import com.heretere.hpwp.util.ChatUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdaterRunnable extends BukkitRunnable {
    private static final long PLUGIN_ID = 88018;
    private static final List<AbstractMap.SimpleEntry<Pattern, Function<Long, Long>>> PATTERN_TO_TICKS = Lists
        .newArrayList(
            new AbstractMap.SimpleEntry<>(
                    Pattern.compile("(\\d+)h", Pattern.CASE_INSENSITIVE),
                    l -> Duration.ofHours(l).toMillis() / 50L
            ),
            new AbstractMap.SimpleEntry<>(
                    Pattern.compile("(\\d+)m", Pattern.CASE_INSENSITIVE),
                    l -> Duration.ofMinutes(l).toMillis() / 50L
            ),
            new AbstractMap.SimpleEntry<>(
                    Pattern.compile("(\\d+)s", Pattern.CASE_INSENSITIVE),
                    l -> Duration.ofSeconds(l).toMillis() / 50L
            )
        );

    private final Plugin parent;
    private final GlobalVariables globalVariables;

    private static long getTicks(String input) {
        AtomicLong ticks = new AtomicLong();

        PATTERN_TO_TICKS.forEach(pair -> {
            final Matcher m = pair.getKey().matcher(input);

            while (m.find()) {
                ticks.addAndGet(pair.getValue().apply(Long.parseLong(m.group(1))));
            }
        });

        return ticks.get();
    }

    public void load() {
        this.runTaskTimer(
            this.parent,
            0L,
            getTicks(this.globalVariables.getUpdateNotificationInterval())
        );
    }

    @Override
    public void run() {
        if (!globalVariables.isUpdateNotification()) {
            return;
        }

        Chain.UPDATE.newChain()
            .asyncFirst(() -> {
                String output;

                try {
                    URL url = new URL("https://api.spiget.org/v2/resources/" + PLUGIN_ID + "/versions/latest");
                    final HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    connection.setInstanceFollowRedirects(true);
                    connection.setRequestProperty("User-Agent", "Per World Plugins");

                    if (
                        connection.getResponseCode() == HttpURLConnection.HTTP_ACCEPTED
                            || connection.getResponseCode() == HttpURLConnection.HTTP_OK
                    ) {
                        output = new JsonParser().parse(new InputStreamReader(connection.getInputStream()))
                            .getAsJsonObject()
                            .get("name")
                            .getAsString();
                    } else {
                        output = null;
                    }
                } catch (Exception ignored) {
                    output = null;
                }

                return output;
            })
            .abortIfNull()
            .syncLast(version -> {
                Version current = new Version(this.parent.getDescription().getVersion());
                Version latest = new Version(version.replaceFirst("v", ""));

                if (current.compareTo(latest) < 0) {
                    String m1 = ChatUtils.translate(
                        "&aAn update is available for &ePer World Plugins&a. (&e"
                            + version.replaceFirst("v", "")
                            + "&a)."
                    );
                    String m2 = ChatUtils.translate(
                        "&aYou can get the latest version from: &ehttps://spigotmc.org/resources/" + PLUGIN_ID
                    );
                    String m3 = ChatUtils.translate(
                        "&aYou can disable this message by editing the global.yml."
                    );

                    Bukkit.broadcast(m1, "hpwp.notify");
                    Bukkit.broadcast(m2, "hpwp.notify");
                    Bukkit.broadcast(m3, "hpwp.notify");
                }
            })
            .execute();
    }
}
