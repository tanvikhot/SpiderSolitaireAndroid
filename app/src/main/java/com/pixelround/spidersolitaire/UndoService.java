package com.pixelround.spidersolitaire;

import com.pixelround.spidersolitaire.messages.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class UndoService {
    public static final UndoService instance = new UndoService();
    private final Stack<String> moves = new Stack<>();

    private UndoService() {

    }

    public void addCommand(String command) {
        if (moves.size() == 0)
            EventBus.getDefault().post(new MessageEvent("undoenable"));
        System.out.println(command);
        moves.push(command);
    }

    public void clearCommands() {
        this.moves.clear();
    }

    public List<String> undoCommand() {
        return undoCommand(1);
    }

    public List<String> undoCommand(int count) {
        List<String> commands = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            if (moves.isEmpty())
                break;
            commands.add(moves.pop());
        }
        if (moves.isEmpty())
            EventBus.getDefault().post(new MessageEvent("undodisable"));
        System.out.println(commands);
        return commands;
    }
}