package com.heretere.hpwp.tasks;

import co.aikar.taskchain.TaskChain;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Chain {
    IO("io");

    private final String name;

    public TaskChain<Object> newChain() {
        return TaskRegister.getFactory().newSharedChain(this.name);
    }
}
