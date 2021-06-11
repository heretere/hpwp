package com.heretere.hpwp.util;

import com.google.common.collect.Lists;
import com.heretere.hch.core.annotation.Comment;

import java.lang.reflect.Field;
import java.util.List;

public class ConfigUtils {
    public static List<String> generateCommentsFromField(Field field) {
        if (field == null) {
            return null;
        }

        List<String> comments = Lists.newArrayList();

        if (field.isAnnotationPresent(Comment.class)) {
            comments.add("# " + field.getAnnotation(Comment.class).value());
        }

        if (field.isAnnotationPresent(Comment.List.class)) {
            for (Comment comment : field.getAnnotation(Comment.List.class).value()) {
                comments.add("# " + comment.value());
            }
        }

        return comments;
    }
}
